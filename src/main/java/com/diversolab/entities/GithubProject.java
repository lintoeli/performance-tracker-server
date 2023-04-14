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
@Table(name = "github_projects")
public class GithubProject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "address", unique = true)
    @NotNull
	private String address;

    @Column(name = "name", unique = true)
	private String name;

    @Column(name = "release_frequency")
	private Double releaseFrequency;

    @Column(name = "lead_time_for_released_changes")
	private Double leadTimeForReleasedChanges;

    @Column(name = "time_to_repair_code")
	private Double timeToRepairCode;

    @Column(name = "bug_issues_rate")
	private Double bugIssuesRate;

    // @Column(name = "bug_label")
	// private String bugLabel;
    
}
