package com.diversolab.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.diversolab.entities.ColorRange;
import com.diversolab.entities.Project;
import com.diversolab.servicies.ColorRangeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/colorRanges")
@RequiredArgsConstructor
public class ColorRangeController {

    private final ColorRangeService colorRangeService;

    @CrossOrigin
    @GetMapping("/all")
    public ResponseEntity<List<ColorRange>> getAllRanges() {
        List<ColorRange> allRanges = colorRangeService.findAll();
        return ResponseEntity.ok(allRanges);
    }

    @CrossOrigin
    @GetMapping("/by-metric")//?metric={metrica}
    public ResponseEntity<ColorRange> getRangeByMetric(@RequestParam String metric) {
    	ColorRange range = colorRangeService.findByMetric(metric);
        return range != null ? ResponseEntity.ok(range) : ResponseEntity.notFound().build();
    }
}
