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
@Table (name= "Paciente")
public class Paciente {
    
    @Id
    @Column(unique = true, length = 13)
    private String rut;

    @Column(nullable = false, length = 100)
    private String nombrePaciente;

}
