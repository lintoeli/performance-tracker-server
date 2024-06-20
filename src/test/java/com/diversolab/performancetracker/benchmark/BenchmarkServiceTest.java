package com.diversolab.performancetracker.benchmark;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.diversolab.entities.Benchmark;
import com.diversolab.entities.Project;
import com.diversolab.repositories.BenchmarkRepository;
import com.diversolab.servicies.BenchmarkService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BenchmarkServiceTest {

    @InjectMocks
    private BenchmarkService benchmarkService;

    @Mock
    private BenchmarkRepository benchmarkRepository;

    private Project firstProject;
    private Benchmark benchmark1;
    private Benchmark benchmark2;
    private Benchmark benchmark3;

    @BeforeEach
    public void setUp() {
        firstProject = new Project();
        firstProject.setId(1L);
        firstProject.setTitle("Project n1");
        firstProject.setName("p1");
        firstProject.setAddress("test/p1");
        firstProject.setReleaseFrequency(10.45);
        firstProject.setLeadTime(150.75);
        firstProject.setTimeToRepair(210.35);
        firstProject.setBugIssuesRate(55.25);

        benchmark1 = new Benchmark();
        benchmark1.setId(1L);
        benchmark1.setProjectName("p1");
        benchmark1.setPeriod("2022-S1");
        benchmark1.setReleaseFrequency(10.50);
        benchmark1.setLeadTime(160.00);
        benchmark1.setTimeToRepair(220.00);
        benchmark1.setBugIssuesRate(60.00);

        benchmark2 = new Benchmark();
        benchmark2.setId(2L);
        benchmark2.setProjectName("p1");
        benchmark2.setPeriod("2022-S2");
        benchmark2.setReleaseFrequency(10.75);
        benchmark2.setLeadTime(155.00);
        benchmark2.setTimeToRepair(215.00);
        benchmark2.setBugIssuesRate(58.00);

        benchmark3 = new Benchmark();
        benchmark3.setId(3L);
        benchmark3.setProjectName("p1");
        benchmark3.setPeriod("2023-S1");
        benchmark3.setReleaseFrequency(10.45);
        benchmark3.setLeadTime(150.75);
        benchmark3.setTimeToRepair(210.35);
        benchmark3.setBugIssuesRate(55.25);
    }

    @Test
    public void testFindAll() {
        when(benchmarkRepository.findAll()).thenReturn(Arrays.asList(benchmark1, benchmark2, benchmark3));

        List<Benchmark> result = benchmarkService.findAll();
        assertNotNull(result);
        assertEquals(3, result.size());
    }
    @Test
    public void testFindByProjectName_Success() {
        when(benchmarkRepository.findByProjectName("p1")).thenReturn(Optional.of(Arrays.asList(benchmark1, benchmark2, benchmark3)));

        List<Benchmark> result = benchmarkService.findByProjectName("p1");
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindByProjectName_NotFound() {
        when(benchmarkRepository.findByProjectName("p2")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            benchmarkService.findByProjectName("p2");
        });
    }
}

