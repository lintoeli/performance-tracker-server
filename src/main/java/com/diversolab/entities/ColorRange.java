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
@Table(name = "colorRanges")
public class ColorRange {

    @Id
    @SequenceGenerator(name = "colorRanges_id_seq", sequenceName = "colorRanges_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "colorRanges_id_seq")
    private Long id;

    @Column(name = "metric", unique = true)
    @NotNull
	private String metric;

    @Column(name = "green")
    @NotNull
	private String green; // Formato {start: Double, end: Double}

    @Column(name = "yellow")
    @NotNull
	private String yellow; // Formato {start: Double, end: Double}

    @Column(name = "red")
    @NotNull
	private String red; // Formato {start: Double, end: Double}
}
