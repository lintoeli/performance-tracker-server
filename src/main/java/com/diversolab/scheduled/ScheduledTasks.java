package com.diversolab.scheduled;

import java.util.Calendar;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    
    // @Scheduled(fixedRate = 60000)
    // public void imprimirHolaMundo() {
    //     System.out.println("Hola Mundo");

    //      // Obtener la instancia del calendario para la fecha y hora actual
    //     Calendar calendario = Calendar.getInstance();

    //     // Extraer el año actual
    //     int año = calendario.get(Calendar.YEAR);

    //     // Extraer el mes actual y ajustar por 1, ya que Calendar.MONTH devuelve un índice basado en 0
    //     int mes = calendario.get(Calendar.MONTH) + 1;

    //     // Imprimir los resultados
    //     System.out.println("Año: " + año);
    //     System.out.println("Mes: " + mes);
    //     System.out.println("------------------------------------------------");
    // }
}
