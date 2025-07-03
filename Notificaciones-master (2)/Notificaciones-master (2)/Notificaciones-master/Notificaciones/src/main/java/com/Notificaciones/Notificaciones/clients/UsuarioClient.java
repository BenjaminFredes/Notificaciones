package com.Notificaciones.Notificaciones.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.Notificaciones.Notificaciones.dto.PacienteDTO;

@FeignClient(name = "gestionycreacionuser", url = "http://localhost:8081")
public interface UsuarioClient {
       // Pacientes
    @GetMapping("/pacientes/buscar/rut/{rut}")
    PacienteDTO buscarPacientePorRut(@PathVariable("rut") String rut);

}
