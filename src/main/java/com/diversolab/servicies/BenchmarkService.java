package com.diversolab.servicies;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import com.diversolab.entities.Benchmark;
import com.diversolab.repositories.BenchmarkRepository;

@AllArgsConstructor
@Service
public class BenchmarkService {

    private final BenchmarkRepository benchmarkRepository;

    public Benchmark findById(String id) {
        return benchmarkRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public List<Benchmark> findAll() {
        return benchmarkRepository.findAll();
    }

    public Benchmark save(Benchmark benchmark) {
        return benchmarkRepository.save(benchmark);
    }

    public List<Benchmark> findByProjectName(String projectName) {
        return benchmarkRepository.findByProjectName(projectName).orElseThrow(() -> new NoSuchElementException());
    }
    
}

    