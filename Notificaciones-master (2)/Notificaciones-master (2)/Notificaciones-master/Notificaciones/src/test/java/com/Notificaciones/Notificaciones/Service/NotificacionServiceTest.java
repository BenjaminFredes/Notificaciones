package com.Notificaciones.Notificaciones.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.Notificaciones.Notificaciones.Model.Notificacion;
import com.Notificaciones.Notificaciones.Model.TipoNotificacion;
import com.Notificaciones.Notificaciones.Repository.NotificacionRepository;
import com.Notificaciones.Notificaciones.Services.NotificacionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class NotificacionServiceTest {

    @Autowired
    private NotificacionService notificacionService;

    @MockBean
    private NotificacionRepository notificacionRepository;

    private final String rutPaciente = "12345678-9";
    private final TipoNotificacion tipoNotificacion = new TipoNotificacion(1, "Correo");

    private final Notificacion notificacion = new Notificacion(
        1,
        "Mensaje test",
        rutPaciente,
        tipoNotificacion
    );

    @Test
    public void testListarNotificacion() {
        when(notificacionRepository.findAll()).thenReturn(List.of(notificacion));

        List<Notificacion> resultado = notificacionService.listarNotificacion();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacionRepository).findAll();
    }

    @Test
    public void testGuardarNotificacion() {
        when(notificacionRepository.save(notificacion)).thenReturn(notificacion);

        String mensaje = notificacionService.guardarNotificacion(notificacion);

        String esperado = "La notificacion con la siguiente ID: " + notificacion.getIdNotificacion() + " fue guardada con exito";
        assertEquals(esperado, mensaje);
        verify(notificacionRepository).save(notificacion);
    }

    @Test
    public void testGuardarNotificaciones() {
        List<Notificacion> lista = List.of(notificacion);
        when(notificacionRepository.saveAll(lista)).thenReturn(lista);

        String mensaje = notificacionService.guardarNotificaciones(lista);

        assertEquals("Hay " + lista.size() + " Notificaciones guardadas con exito.", mensaje);
        verify(notificacionRepository).saveAll(lista);
    }

    @Test
    public void testActualizarNotificacion_Existente() {
        Notificacion actualizado = new Notificacion(
            1,
            "Mensaje actualizado",
            rutPaciente,
            tipoNotificacion
        );

        when(notificacionRepository.findById(1)).thenReturn(Optional.of(notificacion));
        when(notificacionRepository.save(actualizado)).thenReturn(actualizado);

        String mensaje = notificacionService.actualizarNotificacion(actualizado);

        assertEquals("Notificacion 1 actualizada con éxito", mensaje);
        verify(notificacionRepository).findById(1);
        verify(notificacionRepository).save(actualizado);
    }

    @Test
    public void testActualizarNotificacion_NoExistente() {
        Notificacion actualizado = new Notificacion(
            99,
            "Mensaje no existente",
            rutPaciente,
            tipoNotificacion
        );

        when(notificacionRepository.findById(99)).thenReturn(Optional.empty());

        String mensaje = notificacionService.actualizarNotificacion(actualizado);

        assertEquals("Notificacion con ID 99 no encontrada", mensaje);
        verify(notificacionRepository).findById(99);
        verify(notificacionRepository, never()).save(any());
    }

    @Test
    public void testEliminarNotificacion_Existente() {
        when(notificacionRepository.findById(1)).thenReturn(Optional.of(notificacion));
        doNothing().when(notificacionRepository).deleteById(1);

        String mensaje = notificacionService.eliminarNotificacion(1);

        assertEquals("Notificacion 1 eliminada con éxito", mensaje);
        verify(notificacionRepository).findById(1);
        verify(notificacionRepository).deleteById(1);
    }

    @Test
    public void testEliminarNotificacion_NoExistente() {
        when(notificacionRepository.findById(99)).thenReturn(Optional.empty());

        String mensaje = notificacionService.eliminarNotificacion(99);

        assertEquals("Notificacion con ID 99 no encontrada", mensaje);
        verify(notificacionRepository).findById(99);
        verify(notificacionRepository, never()).deleteById(anyInt());
    }

    @Test
    public void testBuscarPorId_Existente() {
        when(notificacionRepository.findById(1)).thenReturn(Optional.of(notificacion));

        Notificacion encontrada = notificacionService.buscarPorId(1);

        assertNotNull(encontrada);
        assertEquals("Mensaje test", encontrada.getMensaje());
        verify(notificacionRepository).findById(1);
    }

    @Test
    public void testBuscarPorId_NoExistente() {
        when(notificacionRepository.findById(99)).thenReturn(Optional.empty());

        Notificacion encontrada = notificacionService.buscarPorId(99);

        assertNull(encontrada);
        verify(notificacionRepository).findById(99);
    }
}
