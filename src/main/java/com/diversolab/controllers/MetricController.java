package com.diversolab.controllers;

import java.util.List;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diversolab.entities.BugTags;
import com.diversolab.entities.GithubProject;
import com.diversolab.entities.github.GithubRelease;
import com.diversolab.servicies.BugTagsService;
import com.diversolab.servicies.GitHubReleasesService;
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
    private final GitHubReleasesService gitHubReleasesService;
    private final BugTagsService bugTagsService;

    private final String[] startPeriods = {"2021-01-01T00:00:00+00:00", "2021-07-01T00:00:00+00:00", "2022-01-01T00:00:00+00:00", "2022-07-01T00:00:00+00:00", "2023-01-01T00:00:00+00:00"};
    private final String[] endPeriods = {"2021-07-01T00:00:00+00:00", "2021-12-31T00:00:00+00:00", "2022-06-30T00:00:00+00:00", "2022-12-31T00:00:00+00:00", "2023-06-30T00:00:00+00:00"};
    private final String[] periods = {"2021-S1", "2021-S2", "2022-S1", "2022-S2", "2023-S1"};
            
    @CrossOrigin
    @PostMapping("/{owner}/{repo}/measure/{period}")
    public String measure(@PathVariable String owner, @PathVariable String repo, @PathVariable Integer period){
        JSONObject res = new JSONObject();
        System.out.println("Midiendo " + repo + "...");
        if(githubProjectService.existsByAddress(owner+"/"+repo)){
            BugTags bugTags = this.bugTagsService.findByAddress(owner+"/"+repo);
            System.out.println("BugTags del repo: " + bugTags);
            String startPeriod = startPeriods[period];
            String endPeriod = endPeriods[period];
            System.out.println("Midiendo desde " + startPeriod + " hasta " + endPeriod);
            // try{
                List<GithubRelease> releases = this.metricService.getGithubReleases(owner, repo, startPeriod, endPeriod);
                Tuple2<Double,Double> releaseFrequencyMetric = this.metricService.calculateGithubDeploymentFrequency(owner, repo, startPeriod, endPeriod);
                System.out.println("Release frequency: " + releaseFrequencyMetric.getT1());
                Tuple2<Double,Double> leadTimeForReleasedChangesMetric = this.metricService.calculateGithubLeadTimeForChanges(owner, repo, startPeriod, endPeriod);
                System.out.println("Lead time: " + leadTimeForReleasedChangesMetric.getT1());
                Tuple2<Double,Double> timeToRepairCodeMetric = this.metricService.calculateGithubTimeToRestoreService(owner, repo, startPeriod, endPeriod);
                System.out.println("Time to repair: " + timeToRepairCodeMetric.getT1());
                Double bugIssuesRateMetric = this.metricService.calculateGithubChangeFailureRate(owner, repo, startPeriod, endPeriod);
                System.out.println("Bug issues rate: " + bugIssuesRateMetric);

                this.gitHubReleasesService.deleteAll();

                GithubProject githubProject = githubProjectService.findByAddress(owner+"/"+repo);
                githubProject.setReleaseFrequency(releaseFrequencyMetric.getT1());
                githubProject.setLeadTimeForReleasedChanges(leadTimeForReleasedChangesMetric.getT1());
                githubProject.setTimeToRepairCode(timeToRepairCodeMetric.getT1());
                githubProject.setBugIssuesRate(bugIssuesRateMetric);

                res.put("releaseFrequency", githubProject.getReleaseFrequency());
                res.put("leadTimeForReleasedChanges", githubProject.getLeadTimeForReleasedChanges());
                res.put("timeToRepairCode", githubProject.getTimeToRepairCode());
                res.put("bugIssuesRate", githubProject.getBugIssuesRate());
                res.put("completed", true);


                this.githubProjectService.save(githubProject);
            // }catch(Exception e){
            //     System.out.println("Error: no se ha podido medir " + repo + "/" + owner);
            //     res.put("completed", false);
            //     res.put("error", "Error: no se ha podido medir " + repo + "/" + owner);
            //     System.err.println(e.getMessage());
            //}
            
        } else {
            res.put("completed", false);
            res.put("error", "Error: no se ha podido medir " + repo + "/" + owner);
        }

        return res.toString();
    }
    
}
