package com.Notificaciones.Notificaciones.Services;

import java.util.List;

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
        return "La notificacion con la siguiente ID:"+notificacion.getIdNotificacion()+"fue guardada con exito";
    }

    public String guardarNotificaciones(List<Notificacion> notificaciones) {
        notificacionRepository.saveAll(notificaciones);
        return "Hay " + notificaciones.size() + " Notificaciones guardadas con exito.";
    }

    public String actualizarNotificacion(Notificacion notificacion) {
        notificacionRepository.save(notificacion); 
        return "Notificacion "+notificacion.getIdNotificacion()+" actualizada con éxito";
    }

    public String eliminarNotificacion( Integer idNotificacion) {
        notificacionRepository.deleteById(idNotificacion);
        return "Notificacion " + idNotificacion + " eliminada con éxito";
    }

    public Notificacion buscarPorId(Integer idNotificacion) {
        return notificacionRepository.findById(idNotificacion).orElse(null);
    }



}
