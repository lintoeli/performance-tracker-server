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
@Table(name = "projects")
public class Project {

    @Id
    @SequenceGenerator(name = "projects_id_seq", sequenceName = "projects_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projects_id_seq")
    private Long id;

    @Column(name = "title", unique = true)
    @NotNull
	private String title;

    @Column(name = "name", unique = true)
    @NotNull
	private String name;

    @Column(name = "address", unique = true)
    @NotNull
	private String address;

    @Column(name = "release_frequency")
	private Double releaseFrequency;

    @Column(name = "lead_time")
	private Double leadTime;

    @Column(name = "time_to_repair")
	private Double timeToRepair;

    @Column(name = "bug_issues_rate")
	private Double bugIssuesRate;

}