package com.Notificaciones.Notificaciones.Controller;

import com.Notificaciones.Notificaciones.Model.Notificacion;
import com.Notificaciones.Notificaciones.Services.NotificacionService;
import com.Notificaciones.Notificaciones.Services.UsuarioService;

import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacion")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private UsuarioService usuarioService;  // ahora se usa el servicio

    @Operation(summary = "Listar todas las notificaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado completo de notificaciones",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron notificaciones")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Notificacion>> listarNotificaciones() {
        List<Notificacion> notificaciones = notificacionService.listarNotificacion();
        return notificaciones.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(notificaciones);
    }

    @Operation(summary = "Guardar una notificación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notificación guardada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o notificación ya existente")
    })
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarNotificacion(@RequestBody Notificacion notificacion) {
        // Validar campos obligatorios
        if (notificacion.getIdNotificacion() == null ||
            notificacion.getMensaje() == null || notificacion.getMensaje().isEmpty() ||
            notificacion.getRutPaciente() == null || notificacion.getRutPaciente().isEmpty() ||
            notificacion.getTipoNotificacion() == null) {
            return ResponseEntity.badRequest().body("Todos los campos deben ser obligatorios.");
        }

        // Validar existencia de paciente en microservicio usuarios (a través del servicio)
        try {
            if (usuarioService.obtenerPacientePorRut(notificacion.getRutPaciente()) == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Paciente con RUT " + notificacion.getRutPaciente() + " no está registrado.");
            }
        } catch (FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Paciente con RUT " + notificacion.getRutPaciente() + " no está registrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al validar paciente: " + e.getMessage());
        }

        // Verificar si notificación ya existe
        if (notificacionService.buscarPorId(notificacion.getIdNotificacion()) != null) {
            return ResponseEntity.badRequest()
                    .body("La notificación con ID " + notificacion.getIdNotificacion() + " ya existe.");
        }

        String mensaje = notificacionService.guardarNotificacion(notificacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @Operation(summary = "Actualizar una notificación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarNotificacion(@RequestBody Notificacion notificacion) {
        Notificacion existente = notificacionService.buscarPorId(notificacion.getIdNotificacion());
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Notificación con ID " + notificacion.getIdNotificacion() + " no encontrada.");
        }

        // Actualizar campos (puedes agregar más campos si quieres)
        existente.setMensaje(notificacion.getMensaje());
        existente.setTipoNotificacion(notificacion.getTipoNotificacion());

        String mensaje = notificacionService.actualizarNotificacion(existente);
        return ResponseEntity.ok(mensaje);
    }

    @Operation(summary = "Eliminar una notificación por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarNotificacion(@PathVariable Integer id) {
        Notificacion existente = notificacionService.buscarPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Notificación con ID " + id + " no encontrada.");
        }

        String mensaje = notificacionService.eliminarNotificacion(id);
        return ResponseEntity.ok(mensaje);
    }
}
