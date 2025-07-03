package com.Notificaciones.Notificaciones.DataLoader;

import com.Notificaciones.Notificaciones.Model.Notificacion;
import com.Notificaciones.Notificaciones.Model.TipoNotificacion;
import com.Notificaciones.Notificaciones.Repository.NotificacionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import net.datafaker.Faker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Faker faker = new Faker();
    private Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Verificar si existen tipos de notificación, sino insertar
        Long countTipo = (Long) entityManager.createQuery("SELECT COUNT(t) FROM TipoNotificacion t").getSingleResult();
        if (countTipo == 0) {
            TipoNotificacion[] tipos = {
                new TipoNotificacion(1, "Correo"),
                new TipoNotificacion(2, "SMS"),
                new TipoNotificacion(3, "Llamada"),
                new TipoNotificacion(4, "WhatsApp"),
                new TipoNotificacion(5, "AppPush")
            };
            for (TipoNotificacion tipo : tipos) {
                entityManager.persist(tipo);
            }
            System.out.println("Tipos de notificación insertados correctamente");
        }

        // Insertar notificaciones de prueba si no existen
        if (notificacionRepository.count() == 0) {

            List<TipoNotificacion> tiposExistentes = entityManager.createQuery("SELECT t FROM TipoNotificacion t", TipoNotificacion.class).getResultList();

            int nextId = 1; // Para IdNotificacion manual

            for (int i = 0; i < 10; i++) {
                Notificacion noti = new Notificacion();

                noti.setIdNotificacion(nextId++);
                noti.setMensaje(faker.lorem().sentence(6, 12));
                noti.setRutPaciente(faker.idNumber().valid());
                noti.setTipoNotificacion(tiposExistentes.get(random.nextInt(tiposExistentes.size())));

                notificacionRepository.save(noti);
            }
            System.out.println("Notificaciones de prueba insertadas correctamente");
        } else {
            System.out.println("Notificaciones ya existen, omitiendo inserción");
        }
    }
}
