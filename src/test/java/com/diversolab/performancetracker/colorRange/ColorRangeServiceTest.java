package com.diversolab.performancetracker.colorRange;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.diversolab.entities.ColorRange;
import com.diversolab.entities.Project;
import com.diversolab.repositories.ColorRangeRepository;
import com.diversolab.servicies.ColorRangeService;
import com.diversolab.servicies.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ColorRangeServiceTest {

    @InjectMocks
    private ColorRangeService colorRangeService;

    @Mock
    private ColorRangeRepository colorRangeRepository;

    @Mock
    private ProjectService projectService;

    private Project firstProject;
    private Project secondProject;
    private Project thirdProject;
    private Project fourthProject;

    @BeforeEach
    public void setUp() {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setTitle("Project n1");
        project1.setName("p1");
        project1.setAddress("test/p1");
        project1.setReleaseFrequency(10.45);
        project1.setLeadTime(150.75);
        project1.setTimeToRepair(210.35);
        project1.setBugIssuesRate(55.25);
        this.firstProject = project1;

        Project project2 = new Project();
        project2.setId(2L);
        project2.setTitle("Project n2");
        project2.setName("p2");
        project2.setAddress("test/p2");
        project2.setReleaseFrequency(15.90);
        project2.setLeadTime(175.40);
        project2.setTimeToRepair(180.20);
        project2.setBugIssuesRate(75.60);
        this.secondProject = project2;

        Project project3 = new Project();
        project3.setId(3L);
        project3.setTitle("Project n3");
        project3.setName("p3");
        project3.setAddress("test/p3");
        project3.setReleaseFrequency(12.00);
        project3.setLeadTime(160.00);
        project3.setTimeToRepair(220.00);
        project3.setBugIssuesRate(60.00);
        this.thirdProject = project3;

        Project project4 = new Project();
        project4.setId(4L);
        project4.setTitle("Project n4");
        project4.setName("p4");
        project4.setAddress("test/p4");
        project4.setReleaseFrequency(18.00);
        project4.setLeadTime(190.00);
        project4.setTimeToRepair(200.00);
        project4.setBugIssuesRate(80.00);
        this.fourthProject = project4;
        
    }



    @Test
    public void testFindByMetric_Success() {
        ColorRange colorRange = new ColorRange();
        colorRange.setMetric("releaseFrequency");
        when(colorRangeRepository.findByMetric("releaseFrequency")).thenReturn(Optional.of(colorRange));

        ColorRange result = colorRangeService.findByMetric("releaseFrequency");
        assertNotNull(result);
        assertEquals("releaseFrequency", result.getMetric());
    }

    @Test
    public void testFindByMetric_NotFound() {
        when(colorRangeRepository.findByMetric("fakeMetric")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            colorRangeService.findByMetric("fakeMetric");
        });
    }

    @Test
    public void testDeleteAll() {
        doNothing().when(colorRangeRepository).deleteAll();
        colorRangeService.deleteAll();
        verify(colorRangeRepository, times(1)).deleteAll();
    }

    @Test
    public void testDefineRanges() {
        when(projectService.findAll()).thenReturn(Arrays.asList(firstProject, secondProject, thirdProject, fourthProject));
        doNothing().when(colorRangeRepository).deleteAll();
        when(colorRangeRepository.save(any(ColorRange.class))).thenReturn(new ColorRange());

        colorRangeService.defineRanges();

        verify(colorRangeRepository, times(4)).save(any(ColorRange.class));
    }
}

