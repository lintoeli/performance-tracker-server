package com.diversolab.repositories;

import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.diversolab.entities.ColorRange;

@Repository
public interface ColorRangeRepository extends JpaRepository<ColorRange, String>{
    Optional<ColorRange> findByMetric(String metric);
}
