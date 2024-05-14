package com.diversolab.controllers;

import java.util.Comparator;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<Benchmark>> getBenchmarks() {
        List<Benchmark> allBenchmarks = benchmarkService.findAll();
        return ResponseEntity.ok(allBenchmarks);
    }

    @CrossOrigin
    @GetMapping("/by-project-name")//?name={nombre}
    public ResponseEntity<List<Benchmark>> getBenchmarksByProjectName(@RequestParam String projectName) {
    	List<Benchmark> benchmarks = benchmarkService.findByProjectName(projectName);
        if (benchmarks != null) {
            // Ordena la lista alfabéticamente por el campo 'period'
            benchmarks.sort(Comparator.comparing(Benchmark::getPeriod));
            return ResponseEntity.ok(benchmarks);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}