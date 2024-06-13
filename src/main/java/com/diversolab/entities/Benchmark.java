package com.diversolab.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "benchmarks")
public class Benchmark {

    @Id
    @SequenceGenerator(name = "benchmarks_id_seq", sequenceName = "benchmarks_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "benchmarks_id_seq")
    private Long id;

    @Column(name = "projectName")
    @NotNull
	private String projectName;

    @Column(name = "period")
    @NotNull
	private String period; //Formato 202X-SY

    @Column(name = "release_frequency")
    @NotNull
	private Double releaseFrequency;

    @Column(name = "lead_time")
    @NotNull
	private Double leadTime;

    @Column(name = "time_to_repair")
    @NotNull
	private Double timeToRepair;

    @Column(name = "bug_issues_rate")
    @NotNull
	private Double bugIssuesRate;
    
}
