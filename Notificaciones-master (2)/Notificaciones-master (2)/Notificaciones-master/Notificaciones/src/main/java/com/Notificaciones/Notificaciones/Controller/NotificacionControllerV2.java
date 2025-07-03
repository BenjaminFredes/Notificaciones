package com.Notificaciones.Notificaciones.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Notificaciones.Notificaciones.Model.Notificacion;
import com.Notificaciones.Notificaciones.Services.NotificacionService;
import com.Notificaciones.Notificaciones.assemblers.NotificacionModelAssembler;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/notificaciones")
public class NotificacionControllerV2 {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private NotificacionModelAssembler notificacionAssembler;

    @Operation(summary = "Listar todas las notificaciones")
    @ApiResponse(
        responseCode = "200",
        description = "Listado completo de notificaciones",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = CollectionModel.class)
        )
    )
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Notificacion>>> listarNotificaciones() {
        List<Notificacion> notificaciones = notificacionService.listarNotificacion();
        List<EntityModel<Notificacion>> notificacionesModel = notificaciones.stream()
                .map(notificacionAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(notificacionesModel,
                        linkTo(methodOn(NotificacionControllerV2.class).listarNotificaciones()).withSelfRel()));
    }

    @Operation(summary = "Buscar notificación por ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Notificación encontrada",
            content = @Content(
                mediaType = "application/hal+json",
                schema = @Schema(implementation = EntityModel.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping(value = "/buscar/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Notificacion>> buscarPorId(@PathVariable Integer id) {
        Notificacion notificacion = notificacionService.buscarPorId(id);
        if (notificacion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notificacionAssembler.toModel(notificacion));
    }

}
