package com.diversolab.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "benchmarks")
public class Benchmark {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true)
    @NotNull
	private String name;

    @Column(name = "release_frequency")
    @NotNull
	private String releaseFrequency;

    @Column(name = "lead_time_for_released_changes")
    @NotNull
	private String leadTimeForReleasedChanges;

    @Column(name = "time_to_repair_code")
    @NotNull
	private String timeToRepairCode;

    @Column(name = "bug_issues_rate")
    @NotNull
	private String bugIssuesRate;
    
}
