package com.diversolab.scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.diversolab.entities.Benchmark;
import com.diversolab.entities.Project;
import com.diversolab.servicies.BenchmarkService;
import com.diversolab.servicies.ColorRangeService;
import com.diversolab.servicies.MetricService;
import com.diversolab.servicies.ProjectService;

import reactor.util.function.Tuple2;


@Component
public class ScheduledTasks {

    private ProjectService projectService;
    private MetricService metricService;
    private BenchmarkService benchmarkService;
    private ColorRangeService colorRangeService;
    
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

    /**
     * Función para automatizar el cálculo de métricas de forma semestral
     * Descomentar el @Scheduled para que funcione automáticamente si el backend está arrancado
     */
    //@Scheduled(cron = "0 0 0 1 1,7 *")
    public void updateColorRangesAndProjects() {
        // Paso 1: Obtener las fechas de nuestro semestre y el string period

        // Crear el formateador de fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+00:00'");
        // Obtener la fecha actual
        LocalDateTime now = LocalDateTime.now();
        // Obtener la fecha de seis meses atrás
        LocalDateTime sixMonthsAgo = now.minusMonths(6);
        // Formatear ambas fechas
        String endPeriod = now.format(formatter);
        String startPeriod = sixMonthsAgo.format(formatter);
        // Obtener string del periodo
        String period = this.obtainPeriodString(startPeriod);
        // Imprimir los resultados
        System.out.println("endPeriod: " + endPeriod);
        System.out.println("startPeriod: " + startPeriod);
        System.out.println("period:" + period);
        

        // Paso 2: Recupera los proyectos guardados
        List<Project> projecList = this.projectService.findAll();

        // Paso 3: Recorremos todos los proyectos y volvemos a medir sus métricas. Además creamos un nuevo Benchmark 
        for (Project project : projecList) {
            String owner = project.getAddress().split("/")[0];
            String repo = project.getAddress().split("/")[1];

            System.out.println("Midiendo " + repo + "..."); // LOG

            Tuple2<Double,Double> releaseFrequencyMetric = this.metricService.calculateGithubDeploymentFrequency(owner, repo, startPeriod, endPeriod);
            Tuple2<Double,Double> leadTimeForReleasedChangesMetric = this.metricService.calculateGithubLeadTimeForChanges(owner, repo, startPeriod, endPeriod);
            Tuple2<Double,Double> timeToRepairCodeMetric = this.metricService.calculateGithubTimeToRestoreService(owner, repo, startPeriod, endPeriod);
            Double bugIssuesRateMetric = this.metricService.calculateGithubChangeFailureRate(owner, repo, startPeriod, endPeriod);

            project.setBugIssuesRate(bugIssuesRateMetric);
            project.setLeadTime(leadTimeForReleasedChangesMetric.getT1());
            project.setReleaseFrequency(releaseFrequencyMetric.getT1());
            project.setTimeToRepair(timeToRepairCodeMetric.getT1());

            this.projectService.save(project);
            System.out.println("Proyecto " + project.getName() + " actualizado. Creando benchmark..."); // LOG

            Benchmark benchmark = new Benchmark();
            benchmark.setProjectName(project.getName());
            benchmark.setPeriod(period);
            benchmark.setBugIssuesRate(project.getBugIssuesRate());
            benchmark.setLeadTime(project.getLeadTime());
            benchmark.setReleaseFrequency(project.getReleaseFrequency());
            benchmark.setTimeToRepair(project.getTimeToRepair());

            this.benchmarkService.save(benchmark);
            System.out.println("Benchmark del proyecto " + project.getName() + " creado"); // LOG

            try {
                System.out.println("Esperando 50 minutos para medir el siguiente periodo");
                // Thread.sleep(3600000); // Esperar 1 hora (3600000 ms)
                Thread.sleep(3000000); // Esperar 50 min
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        // Paso 4: Recalculamos los ColorRanges una vez actualizados los proyectos

        this.colorRangeService.defineRanges();
        System.out.println("=================================================================================");
        System.out.println("TODAS LAS OPERACIONES COMPLETADAS");
        System.out.println("=================================================================================");

    }

    private String obtainPeriodString(String date){
        String[] dateSplit = date.split("-");
        if(Integer.parseInt(dateSplit[1]) > 6){
            return dateSplit[0] + "-S2";
        } else{
            return dateSplit[0] + "-S1";
        }
    }
}
