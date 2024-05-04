package com.diversolab.servicies;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.diversolab.entities.Project;
import com.diversolab.repositories.ProjectRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProjectService {
    
    private final ProjectRepository projectRepository;

    public Project findById(String id){
        return this.projectRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public List<Project> findAll(){
        return this.projectRepository.findAll();
    }

    public Project findByName(String name) {
        return this.projectRepository.findByName(name).orElseThrow(() -> new NoSuchElementException());
    }

    public Project save(Project project) {
        return this.projectRepository.save(project);
    }
}
