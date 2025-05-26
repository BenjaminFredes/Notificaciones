package com.Notificaciones.Notificaciones.Model;




import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name= "Notificacion")

public class Notificacion {
@Id
    @Column(unique = true, length = 100, nullable = false)
    private Integer IdNotificacion;
    
    @Column(nullable = false, length = 200)
    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "rut", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "idTipo", nullable = false)
    private TipoNotificacion tipoNotificacion;
}

