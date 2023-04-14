package com.diversolab.controllers;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.diversolab.entities.GithubProject;
import com.diversolab.servicies.GithubProjectService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class GithubProjectController {

    private final GithubProjectService githubProjectService;

    @CrossOrigin
    @GetMapping("/{owner}/{repo}/metrics")
    public String getMetrics(@PathVariable String owner, @PathVariable String repo){
        JSONObject res = new JSONObject();
        if(githubProjectService.existsByAddress(owner+"/"+repo)){
            res.put("exists", true);

            GithubProject githubProject = githubProjectService.findByAddress(owner+"/"+repo);
            
            // Checking if benchmarking request has been completed and the data of the project is in the database
            if(githubProject.getReleaseFrequency() != null){
                res.put("releaseFrequency", githubProject.getReleaseFrequency());
                res.put("leadTimeForReleasedChanges", githubProject.getLeadTimeForReleasedChanges());
                res.put("timeToRepairCode", githubProject.getTimeToRepairCode());
                res.put("bugIssuesRate", githubProject.getBugIssuesRate());

                res.put("completed", true);
            } else {
                res.put("completed", false);
            }
            
        }else{
            res.put("exists", false);
            res.put("completed", false);
        }
        return res.toString();
    }

}