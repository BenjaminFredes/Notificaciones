package com.Notificaciones.Notificaciones.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "TipoNotificacion")
public class TipoNotificacion {

    @Id
    @Column(unique = true, length = 20)
    private int idTipo;

    @Column(nullable = false, length = 100)
    private String nombreNoti;
    

}
