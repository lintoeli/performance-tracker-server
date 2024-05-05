package com.diversolab.servicies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.diversolab.entities.ColorRange;
import com.diversolab.repositories.ColorRangeRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ColorRangeService {

    private final ColorRangeRepository colorRangeRepository;

    // private List<String> metrics = new ArrayList<String>(Arrays.asList("releaseFrequency", "timeToRepair", "leadTime", "bugIssuesRate"));

    public List<ColorRange> findAll(){
        return this.colorRangeRepository.findAll();
    }
    
    public ColorRange findByMetric(String metric) {
        return this.colorRangeRepository.findByMetric(metric).orElseThrow(() -> new NoSuchElementException());
    }

    public ColorRange updateRange(String metric, ColorRange updatedColorRange){
        ColorRange existingColorRange = this.findByMetric(metric);
        if (existingColorRange != null) {
            existingColorRange.setGreen(updatedColorRange.getGreen());
            existingColorRange.setYellow(updatedColorRange.getYellow());
            existingColorRange.setRed(updatedColorRange.getRed());
            return colorRangeRepository.save(existingColorRange);
        }
        return null;
    }

    public ColorRange save(ColorRange newColorRange){
        return colorRangeRepository.save(newColorRange);
    }
    
}
