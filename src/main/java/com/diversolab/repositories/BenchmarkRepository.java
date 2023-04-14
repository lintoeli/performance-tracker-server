package com.diversolab.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.diversolab.entities.Benchmark;

@Repository
public interface BenchmarkRepository extends JpaRepository<Benchmark, String> {

    Optional<Benchmark> findByName(String name);

    boolean existsByName(String name);

}