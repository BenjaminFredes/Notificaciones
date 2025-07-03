package com.Notificaciones.Notificaciones.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.Notificaciones.Notificaciones.Controller.NotificacionController;
import com.Notificaciones.Notificaciones.Model.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class NotificacionModelAssembler implements RepresentationModelAssembler<Notificacion, EntityModel<Notificacion>> {

    @Override
    public EntityModel<Notificacion> toModel(Notificacion notificacion) {
        return EntityModel.of(notificacion,
            linkTo(methodOn(com.Notificaciones.Notificaciones.Controller.NotificacionController.class).listarNotificaciones()).withRel("todas"),
            linkTo(methodOn(NotificacionController.class).eliminarNotificacion(notificacion.getIdNotificacion())).withRel("eliminar"),
            linkTo(methodOn(NotificacionController.class).guardarNotificacion(notificacion)).withRel("guardar"),
            linkTo(methodOn(NotificacionController.class).actualizarNotificacion(notificacion)).withRel("actualizar")
        );
    }
}
