package com.Notificaciones.Notificaciones.Controller;

import com.Notificaciones.Notificaciones.Model.Notificacion;
import com.Notificaciones.Notificaciones.Model.TipoNotificacion;
import com.Notificaciones.Notificaciones.Services.NotificacionService;
import com.Notificaciones.Notificaciones.Services.UsuarioService;
import com.Notificaciones.Notificaciones.dto.PacienteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacionController.class)
public class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    @MockBean
    private UsuarioService usuarioService;  // Ahora mockeamos el servicio

    @Autowired
    private ObjectMapper objectMapper;

    private Notificacion notificacion;

    @BeforeEach
    public void setup() {
        TipoNotificacion tipoNotificacion = new TipoNotificacion();
        tipoNotificacion.setIdTipo(1);
        tipoNotificacion.setNombreNoti("Cita médica");

        notificacion = new Notificacion();
        notificacion.setIdNotificacion(1);
        notificacion.setMensaje("Recordatorio de cita médica");
        notificacion.setRutPaciente("12345678-9");
        notificacion.setTipoNotificacion(tipoNotificacion);
    }

    @Test
    public void testListarNotificaciones() throws Exception {
        Mockito.when(notificacionService.listarNotificacion()).thenReturn(List.of(notificacion));

        mockMvc.perform(get("/notificacion/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idNotificacion").value(1));
    }

    @Test
    public void testGuardarNotificacion() throws Exception {
        Mockito.when(notificacionService.buscarPorId(1)).thenReturn(null);
        Mockito.when(usuarioService.obtenerPacientePorRut("12345678-9")).thenReturn(new PacienteDTO());
        Mockito.when(notificacionService.guardarNotificacion(any(Notificacion.class)))
                .thenReturn("La notificacion con la siguiente ID: 1 fue guardada con exito");

        mockMvc.perform(post("/notificacion/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificacion)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("fue guardada con exito")));
    }

    @Test
    public void testGuardarNotificacionPacienteNoExiste() throws Exception {
        Mockito.when(notificacionService.buscarPorId(1)).thenReturn(null);

        Mockito.when(usuarioService.obtenerPacientePorRut("12345678-9"))
                .thenThrow(mock(FeignException.NotFound.class));

        mockMvc.perform(post("/notificacion/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificacion)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("no está registrado")));
    }

    @Test
    public void testActualizarNotificacion() throws Exception {
        Mockito.when(notificacionService.buscarPorId(1)).thenReturn(notificacion);
        Mockito.when(notificacionService.actualizarNotificacion(any(Notificacion.class)))
                .thenReturn("Notificacion 1 actualizada con éxito");

        notificacion.setMensaje("Mensaje actualizado");

        mockMvc.perform(put("/notificacion/actualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificacion)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("actualizada con éxito")));
    }

    @Test
    public void testEliminarNotificacion() throws Exception {
        Mockito.when(notificacionService.buscarPorId(1)).thenReturn(notificacion);
        Mockito.when(notificacionService.eliminarNotificacion(1)).thenReturn("Notificacion 1 eliminada con éxito");

        mockMvc.perform(delete("/notificacion/eliminar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("eliminada con éxito")));
    }
}
