package com.diversolab.performancetracker.project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.diversolab.entities.Project;
import com.diversolab.repositories.ProjectRepository;
import com.diversolab.servicies.GithubProjectService;
import com.diversolab.servicies.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private GithubProjectService githubProjectService;

    private Project firstProject;
    private Project secondProject;

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
        
    }

    @Test
    public void testFindAll() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(firstProject, secondProject));
        List<Project> projects = projectService.findAll();
        assertEquals(2, projects.size());
    }

    @Test
    public void testFindByName_Success() {
        when(projectRepository.findByName("p1")).thenReturn(Optional.of(firstProject));
        //when(projectRepository.findByName("p2")).thenReturn(Optional.of(secondProject));

        Project result = projectService.findByName("p1");
        assertNotNull(result);
        assertEquals("test/p1", result.getAddress());
        assertEquals("Project n1", result.getTitle());
    }

    @Test
    public void testFindByName_NotFound() {
        when(projectRepository.findByName("testProject")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            projectService.findByName("testProject");
        });
    }

    @Test
    public void testFindByAddress_Success() {
        when(projectRepository.findByAddress("test/p2")).thenReturn(Optional.of(secondProject));
        Project result = projectService.findByAddress("test/p2");
        assertNotNull(result);
        assertEquals("Project n2", result.getTitle());
    }

    @Test
    public void testFindByAddress_NotFound() {
        when(projectRepository.findByAddress("testAddress")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            projectService.findByAddress("testAddress");
        });
    }
}

