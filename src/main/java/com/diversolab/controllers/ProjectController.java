package com.diversolab.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.diversolab.entities.Project;
import com.diversolab.servicies.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @CrossOrigin
    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> allProjects = projectService.findAll();
        return ResponseEntity.ok(allProjects);
    }

    @CrossOrigin
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable String projectId) {
    	Project project = projectService.findById(projectId);
        return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
    }

    @CrossOrigin
    @GetMapping("/by-name")//?name={nombre}
    public ResponseEntity<Project> getProjectByName(@RequestParam String name) {
    	Project project = projectService.findByName(name);
        return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        try {
            Project savedProject = projectService.save(project);
            return ResponseEntity.ok(savedProject);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * MÉTODO DE MIGRACIÓN DE DATOS
     * 
     * NO VOLVER A UTILIZAR
     */
    @CrossOrigin
    @PostMapping("/load-from-database")
    public void loadProjectFromDatabase() {
        this.projectService.loadFromDatabase();
    }
}
