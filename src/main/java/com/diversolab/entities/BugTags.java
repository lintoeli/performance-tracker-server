package com.diversolab.entities;

import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "bugTags")
public class BugTags {

    @Id
    @SequenceGenerator(name = "bugTags_id_seq", sequenceName = "bugTags_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bugTags_id_seq")
    private Long id;

    @Column(name = "address", unique = true)
    @NotNull
	private String address;

    @Column(name = "bugLabel")
	private String bugLabel; 

    @Column(name = "bugTitle")
	private String bugTitle;
    
}
