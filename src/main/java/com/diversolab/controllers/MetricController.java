package com.diversolab.controllers;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diversolab.entities.GithubProject;
import com.diversolab.servicies.GithubProjectService;
import com.diversolab.servicies.MetricService;

import lombok.RequiredArgsConstructor;
import reactor.util.function.Tuple2;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricService metricService;
    private final GithubProjectService githubProjectService;

    private final String[] startPeriods = {"2021-01-01T00:00:00+00:00", "2021-07-01T00:00:00+00:00", "2022-01-01T00:00:00+00:00", "2022-07-01T00:00:00+00:00", "2023-01-01T00:00:00+00:00"};
    private final String[] endPeriods = {"2021-06-30T00:00:00+00:00", "2021-12-31T00:00:00+00:00", "2022-06-30T00:00:00+00:00", "2022-12-31T00:00:00+00:00", "2023-06-30T00:00:00+00:00"};
            
    @CrossOrigin
    @PostMapping("/{owner}/{repo}/measure/{period}")
    public String measure(@PathVariable String owner, @PathVariable String repo, @PathVariable Integer period){
        JSONObject res = new JSONObject();
        System.out.println("Midiendo " + repo + "...");
        if(githubProjectService.existsByAddress(owner+"/"+repo)){
            String startPeriod = startPeriods[period];
            String endPeriod = endPeriods[period];
            System.out.println("Midiendo desde " + startPeriod + " hasta " + endPeriod);
            Tuple2<Double,Double> releaseFrequencyMetric = this.metricService.calculateGithubDeploymentFrequency(owner, repo, startPeriod, endPeriod);
            Tuple2<Double,Double> leadTimeForReleasedChangesMetric = this.metricService.calculateGithubLeadTimeForChanges(owner, repo, startPeriod, endPeriod);
            Tuple2<Double,Double> timeToRepairCodeMetric = this.metricService.calculateGithubTimeToRestoreService(owner, repo, startPeriod, endPeriod);
            Double bugIssuesRateMetric = this.metricService.calculateGithubChangeFailureRate(owner, repo, startPeriod, endPeriod);

            GithubProject githubProject = githubProjectService.findByAddress(owner+"/"+repo);
            githubProject.setReleaseFrequency(releaseFrequencyMetric.getT1());
            githubProject.setLeadTimeForReleasedChanges(leadTimeForReleasedChangesMetric.getT1());
            githubProject.setTimeToRepairCode(timeToRepairCodeMetric.getT1());
            githubProject.setBugIssuesRate(bugIssuesRateMetric);

            System.out.println(githubProject);
            System.out.println("releaseFrequencySD: "+releaseFrequencyMetric.getT2());
            System.out.println("leadTimeForReleasedChangesSD: "+leadTimeForReleasedChangesMetric.getT2());
            System.out.println("timeToRepairCodeSD: "+timeToRepairCodeMetric.getT2());

            res.put("releaseFrequency", githubProject.getReleaseFrequency());
            res.put("leadTimeForReleasedChanges", githubProject.getLeadTimeForReleasedChanges());
            res.put("timeToRepairCode", githubProject.getTimeToRepairCode());
            res.put("bugIssuesRate", githubProject.getBugIssuesRate());
            res.put("completed", true);


            this.githubProjectService.save(githubProject);


        } else {
            res.put("completed", false);
        }

        return res.toString();
    }
    
}
