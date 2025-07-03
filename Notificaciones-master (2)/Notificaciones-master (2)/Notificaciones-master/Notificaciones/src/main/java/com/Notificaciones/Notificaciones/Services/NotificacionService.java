package com.Notificaciones.Notificaciones.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Notificaciones.Notificaciones.Model.Notificacion;
import com.Notificaciones.Notificaciones.Repository.NotificacionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> listarNotificacion() {
        return notificacionRepository.findAll();
    }

    public String guardarNotificacion(Notificacion notificacion) {
        notificacionRepository.save(notificacion);
        return "La notificacion con la siguiente ID: " + notificacion.getIdNotificacion() + " fue guardada con exito";
    }

    public String guardarNotificaciones(List<Notificacion> notificaciones) {
        notificacionRepository.saveAll(notificaciones);
        return "Hay " + notificaciones.size() + " Notificaciones guardadas con exito.";
    }

    public String actualizarNotificacion(Notificacion notificacion) {
        Optional<Notificacion> existente = notificacionRepository.findById(notificacion.getIdNotificacion());
        if (existente.isPresent()) {
            notificacionRepository.save(notificacion);
            return "Notificacion " + notificacion.getIdNotificacion() + " actualizada con éxito";
        } else {
            return "Notificacion con ID " + notificacion.getIdNotificacion() + " no encontrada";
        }
    }

    public String eliminarNotificacion(Integer idNotificacion) {
        Optional<Notificacion> notificacion = notificacionRepository.findById(idNotificacion);
        if (notificacion.isPresent()) {
            notificacionRepository.deleteById(idNotificacion);
            return "Notificacion " + idNotificacion + " eliminada con éxito";
        } else {
            return "Notificacion con ID " + idNotificacion + " no encontrada";
        }
    }

    public Notificacion buscarPorId(Integer idNotificacion) {
        return notificacionRepository.findById(idNotificacion).orElse(null);
    }
}
