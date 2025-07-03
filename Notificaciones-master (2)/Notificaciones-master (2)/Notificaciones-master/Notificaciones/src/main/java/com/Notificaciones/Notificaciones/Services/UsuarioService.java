package com.Notificaciones.Notificaciones.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Notificaciones.Notificaciones.clients.UsuarioClient;
import com.Notificaciones.Notificaciones.dto.PacienteDTO;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioClient usuarioClient;

    // Obtener paciente por RUT usando FeignClient
    public PacienteDTO obtenerPacientePorRut(String rut) {
        return usuarioClient.buscarPacientePorRut(rut);
    }
}