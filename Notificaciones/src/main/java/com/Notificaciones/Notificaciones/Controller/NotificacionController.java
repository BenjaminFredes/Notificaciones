package com.Notificaciones.Notificaciones.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Notificaciones.Notificaciones.Model.Notificacion;
import com.Notificaciones.Notificaciones.Services.NotificacionService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/notificacion")
public class NotificacionController {

    

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/listar")
    public ResponseEntity<List<Notificacion>> listarNotifiacion() {
        List<Notificacion> notificaciones = notificacionService.listarNotificacion();
        if (notificaciones.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(notificaciones);
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarNotificacion(@RequestBody Notificacion notificacion) {
        if (notificacion.getIdNotificacion() == null ||
            notificacion.getMensaje().isEmpty() ||
            notificacion.getPaciente() == null ||
            notificacion.getTipoNotificacion()== null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Todos los campos deben ser obligatorios.");
        }

        Notificacion notificacionExistente = notificacionService.buscarPorId(notificacion.getIdNotificacion());
        if (notificacionExistente!= null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("La notifiacion con id "+ notificacion.getIdNotificacion() + " ya existe");
        }
        String mensaje = notificacionService.guardarNotificacion(notificacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PostMapping("/guardar-notifiacione(s)")
    public ResponseEntity<String> guardarNotificaciones(@RequestBody List<Notificacion> notificaciones) {
        if (notificaciones.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La lista de notificaciones está vacía.");
        }
        
        for(Notificacion notificacion : notificaciones){
            if (notificacion.getIdNotificacion() == null ||
            notificacion.getMensaje().isEmpty() ||
            notificacion.getPaciente() == null ||
            notificacion.getTipoNotificacion()== null){
                
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Datos incompletos de alguna notificacion en la lista.");
}
    Notificacion notificacionExistente = notificacionService.buscarPorId(notificacion.getIdNotificacion());
        if (notificacionExistente != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Ya existe una notificacion con el ID " + notificacion.getIdNotificacion());
    }     
 }
    String mensaje = notificacionService.guardarNotificaciones(notificaciones);
    return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
        }

        
    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarNotificacion(@RequestBody Notificacion notificacion) {
        Notificacion notificacionExistente = notificacionService.buscarPorId(notificacion.getIdNotificacion());
        if (notificacionExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificacion con Id " + notificacion.getIdNotificacion() + " no encontrado.");
        }
        notificacionExistente.setMensaje(notificacion.getMensaje());
        notificacionExistente.setTipoNotificacion(notificacion.getTipoNotificacion());
        String mensaje = notificacionService.actualizarNotificacion(notificacionExistente);
        return ResponseEntity.ok(mensaje);  // 200 OK con el mensaje de éxito
    }

      @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarNotificacion(@PathVariable Integer id) {
        Notificacion notificacionExistente = notificacionService.buscarPorId(id);
        if (notificacionExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificacion con ID " + id + " no encontrada.");
        }

        String mensaje = notificacionService.eliminarNotificacion(id);
        return ResponseEntity.ok(mensaje);  // 200 OK con el mensaje de éxito
    }

    



}     
    



    


