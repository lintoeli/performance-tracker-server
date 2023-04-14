package com.diversolab.controllers;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.diversolab.entities.Benchmark;
import com.diversolab.servicies.BenchmarkService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/benchmarks")
@RequiredArgsConstructor
public class BenchmarkController {

    private final BenchmarkService benchmarkService;

    @CrossOrigin
    @GetMapping("/all")
    public String getBenchmarks(){
        
        JSONObject res = new JSONObject();
        
        if(benchmarkService.existsByName("OSS")){
            res.put("exists", true);
            Benchmark benchmark = benchmarkService.findByName("OSS");
            res.put("OSS", new JSONObject());
            ((JSONObject) res.get("OSS")).put("releaseFrequency", benchmark.getReleaseFrequency());
            ((JSONObject) res.get("OSS")).put("leadTimeForReleasedChanges", benchmark.getLeadTimeForReleasedChanges());
            ((JSONObject) res.get("OSS")).put("timeToRepairCode", benchmark.getTimeToRepairCode());
            ((JSONObject) res.get("OSS")).put("bugIssuesRate", benchmark.getBugIssuesRate());
        }else{
            res.put("OSS", false);
        }

        if(benchmarkService.existsByName("DORA")){
            res.put("exists", true);
            Benchmark benchmark = benchmarkService.findByName("DORA");
            res.put("DORA", new JSONObject());
            ((JSONObject) res.get("DORA")).put("releaseFrequency", benchmark.getReleaseFrequency());
            ((JSONObject) res.get("DORA")).put("leadTimeForReleasedChanges", benchmark.getLeadTimeForReleasedChanges());
            ((JSONObject) res.get("DORA")).put("timeToRepairCode", benchmark.getTimeToRepairCode());
            ((JSONObject) res.get("DORA")).put("bugIssuesRate", benchmark.getBugIssuesRate());
        }else{
            res.put("DORA", false);
        }

        return res.toString();
    }

}