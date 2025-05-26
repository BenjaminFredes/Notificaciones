package com.Notificaciones.Notificaciones.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Notificaciones.Notificaciones.Model.Notificacion;
@Repository
//AGRUPA METODOS
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer>{

}
