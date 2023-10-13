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
            
    @CrossOrigin
    @PostMapping("/{owner}/{repo}/measure")
    public String measure(@PathVariable String owner, @PathVariable String repo){
        JSONObject res = new JSONObject();
        if(githubProjectService.existsByAddress(owner+"/"+repo)){

            Tuple2<Double,Double> releaseFrequencyMetric = this.metricService.calculateGithubDeploymentFrequency(owner, repo);
            Tuple2<Double,Double> leadTimeForReleasedChangesMetric = this.metricService.calculateGithubLeadTimeForChanges(owner, repo);
            Tuple2<Double,Double> timeToRepairCodeMetric = this.metricService.calculateGithubTimeToRestoreService(owner, repo);
            Double bugIssuesRateMetric = this.metricService.calculateGithubChangeFailureRate(owner, repo);

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
