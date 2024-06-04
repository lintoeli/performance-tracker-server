package com.diversolab.controllers;

import java.util.List;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diversolab.entities.BugTags;
import com.diversolab.entities.Benchmark;
import com.diversolab.entities.Project;
import com.diversolab.entities.github.GithubRelease;
import com.diversolab.servicies.BenchmarkService;
import com.diversolab.servicies.BugTagsService;
import com.diversolab.servicies.GitHubReleasesService;
import com.diversolab.servicies.MetricService;
import com.diversolab.servicies.ProjectService;

import lombok.RequiredArgsConstructor;
import reactor.util.function.Tuple2;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricService metricService;
    private final GitHubReleasesService gitHubReleasesService;
    private final BugTagsService bugTagsService;
    private final ProjectService projectService;
    private final BenchmarkService benchmarkService;

    private final String[] startPeriods = {"2021-01-01T00:00:00+00:00", "2021-07-01T00:00:00+00:00", "2022-01-01T00:00:00+00:00", "2022-07-01T00:00:00+00:00", "2023-01-01T00:00:00+00:00"};
    private final String[] endPeriods = {"2021-07-01T00:00:00+00:00", "2022-01-01T00:00:00+00:00", "2022-07-01T00:00:00+00:00", "2023-01-01T00:00:00+00:00", "2023-07-01T00:00:00+00:00"};
    private final String[] periods = {"2021-S1", "2021-S2", "2022-S1", "2022-S2", "2023-S1"};
            
    @CrossOrigin
    @PostMapping("/{owner}/{repo}/measure/{period}")
    public String measure(@PathVariable String owner, @PathVariable String repo, @PathVariable Integer period){
        JSONObject res = new JSONObject();
        System.out.println("Midiendo " + repo + "...");
        if(projectService.existsByAddress(owner+"/"+repo)){
            BugTags bugTags = this.bugTagsService.findByAddress(owner+"/"+repo);
            System.out.println("BugTags del repo: " + bugTags);
            String startPeriod = startPeriods[period];
            String endPeriod = endPeriods[period];
            System.out.println("Midiendo desde " + startPeriod + " hasta " + endPeriod);
            try{
                // Sacamos releases
                List<GithubRelease> releases = this.metricService.getGithubReleases(owner, repo, startPeriod, endPeriod);

                //Obtenemos métricas
                Tuple2<Double,Double> releaseFrequencyMetric = this.metricService.calculateGithubDeploymentFrequency(owner, repo, startPeriod, endPeriod);
                System.out.println("Release frequency: " + releaseFrequencyMetric.getT1());
                Tuple2<Double,Double> leadTimeForReleasedChangesMetric = this.metricService.calculateGithubLeadTimeForChanges(owner, repo, startPeriod, endPeriod);
                System.out.println("Lead time: " + leadTimeForReleasedChangesMetric.getT1());
                Tuple2<Double,Double> timeToRepairCodeMetric = this.metricService.calculateGithubTimeToRestoreService(owner, repo, startPeriod, endPeriod);
                System.out.println("Time to repair: " + timeToRepairCodeMetric.getT1());
                Double bugIssuesRateMetric = this.metricService.calculateGithubChangeFailureRate(owner, repo, startPeriod, endPeriod);
                System.out.println("Bug issues rate: " + bugIssuesRateMetric);

                // Limpiamos tabla de releases
                this.gitHubReleasesService.deleteAll();

                // Actualizamos proyectos
                Project project = projectService.findByAddress(owner+"/"+repo);
                project.setReleaseFrequency(releaseFrequencyMetric.getT1());
                project.setLeadTime(leadTimeForReleasedChangesMetric.getT1());
                project.setTimeToRepair(timeToRepairCodeMetric.getT1());
                project.setBugIssuesRate(bugIssuesRateMetric);
                this.projectService.save(project);

                // Creamos benchmark
                Benchmark benchmark = new Benchmark();
                benchmark.setPeriod(periods[period]);
                benchmark.setProjectName(project.getName());
                benchmark.setReleaseFrequency(project.getReleaseFrequency());
                benchmark.setLeadTime(project.getLeadTime());
                benchmark.setTimeToRepair(project.getTimeToRepair());
                benchmark.setBugIssuesRate(project.getBugIssuesRate());
                this.benchmarkService.save(benchmark);

                // Construimos el res 
                res.put("releaseFrequency", project.getReleaseFrequency());
                res.put("leadTimeForReleasedChanges", project.getLeadTime());
                res.put("timeToRepairCode", project.getTimeToRepair());
                res.put("bugIssuesRate", project.getBugIssuesRate());
                res.put("completed", true);
                
            }catch(Exception e){
                this.gitHubReleasesService.deleteAll();
                System.out.println("Error: no se ha podido medir " + repo + "/" + owner);
                res.put("completed", false);
                res.put("error", "Error: no se ha podido medir " + repo + "/" + owner);
                System.err.println(e.getMessage());
            }
            
        } else {
            this.gitHubReleasesService.deleteAll();
            res.put("completed", false);
            res.put("error", "Error: no se ha podido medir " + repo + "/" + owner);
        }

        return res.toString();
    }

    @CrossOrigin
    @PostMapping("/{owner}/{repo}/measure-all-periods")
    public String measureAllPeriods(@PathVariable String owner, @PathVariable String repo) {
        JSONObject res = new JSONObject();
        for (int i = 0; i < periods.length; i++) {
            String result = measure(owner, repo, i);
            res.put(periods[i], new JSONObject(result));
            try {
                System.out.println("Esperando 1 hora para medir el siguiente periodo");
                // Thread.sleep(3600000); // Esperar 1 hora (3600000 ms)
                Thread.sleep(3000000); // Esperar 50 min
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                res.put("error", "Error: la operación fue interrumpida");
                return res.toString();
            }
        }
        res.put("completed", true);
        return res.toString();
    }



    
}
