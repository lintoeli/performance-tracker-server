package com.diversolab.servicies;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.diversolab.entities.ColorRange;
import com.diversolab.entities.Project;
import com.diversolab.repositories.ColorRangeRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ColorRangeService {

    private final ColorRangeRepository colorRangeRepository;
    private final ProjectService projectService;

    private String[] metrics = {"releaseFrequency", "leadTime", "timeToRepair", "bugIssuesRate"};

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

    public void defineRanges() {
        this.colorRangeRepository.deleteAll();
        List<Project> projects = projectService.findAll();

        for (String metric : metrics) { // Asumiendo que 'metrics' es una lista de strings
            List<Double> values = projects.stream()
                                          .map(project -> project.returnMetric(metric)) // Asumiendo que Project tiene un método para obtener valores por métrica
                                          .mapToDouble(Double::doubleValue)
                                          .boxed()
                                          .collect(Collectors.toList());

            Collections.sort(values);

            int lowPercentileIndex = values.size() / 3;
            int highPercentileIndex = (int) Math.ceil(2.0 * values.size() / 3);

            ColorRange range = new ColorRange();
            range.setMetric(metric);
            range.setGreen(formatRange(values.get(highPercentileIndex) + 0.01, Collections.max(values)));
            range.setYellow(formatRange(values.get(lowPercentileIndex) + 0.01, values.get(highPercentileIndex)));
            range.setRed(formatRange(Collections.min(values), values.get(lowPercentileIndex)));

            this.colorRangeRepository.save(range);
        }
    }

    private String formatRange(double start, double end) {
        return String.format("{start: %f, end: %f}", start, end);
    }
    
}
