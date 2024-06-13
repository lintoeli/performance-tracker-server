package com.diversolab.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.diversolab.entities.Benchmark;

@Repository
public interface BenchmarkRepository extends JpaRepository<Benchmark, String> {
    Optional<List<Benchmark>> findByProjectName(String projectName);
}