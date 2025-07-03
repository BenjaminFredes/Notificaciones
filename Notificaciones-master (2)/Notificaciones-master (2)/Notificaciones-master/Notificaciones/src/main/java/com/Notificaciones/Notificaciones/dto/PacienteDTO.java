package com.Notificaciones.Notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacienteDTO {

        private String rutPaciente;
        private String nombre;
        private String correo;
        private String fono;
        private String historialMedico;
}
