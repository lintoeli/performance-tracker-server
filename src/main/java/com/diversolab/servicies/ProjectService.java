package com.diversolab.servicies;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.diversolab.entities.GithubProject;
import com.diversolab.entities.Project;
import com.diversolab.repositories.ProjectRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final GithubProjectService githubProjectService;

    public Project findById(String id){
        return this.projectRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public List<Project> findAll(){
        return this.projectRepository.findAll();
    }

    public Project findByName(String name) {
        return this.projectRepository.findByName(name).orElseThrow(() -> new NoSuchElementException());
    }

    public Project findByAddress(String name){
        return this.projectRepository.findByAddress(name).orElseThrow(() -> new NoSuchElementException());
    }

    public Project save(Project project) {
        return this.projectRepository.save(project);
    }

    public boolean existsByAddress(String address){
        Project project = this.projectRepository.findByAddress(address).orElseThrow(() -> new NoSuchElementException());
        return project != null;
    }

    public void loadFromDatabase(){
        // Recuperamos los proyectos de la tabla GitHubProjects
        List<GithubProject> GHProjects = this.githubProjectService.findAll();
        // Recorremos los proyectos de GitHub
        for (GithubProject p : GHProjects) {
            Project project = new Project();
            // Info del proyecto:
            project.setTitle(p.getName());
            project.setAddress(p.getAddress());
            project.setName(p.getName().toLowerCase().replace(" ", ""));

            // Metricas de prueba:
            project.setBugIssuesRate(p.getBugIssuesRate());
            project.setLeadTime(p.getLeadTimeForReleasedChanges());
            project.setReleaseFrequency(p.getReleaseFrequency());
            project.setTimeToRepair(p.getTimeToRepairCode());

            // Guardamos el proyecto en la BBDD:
            try{
                this.save(project);
            }catch(Exception e){
                System.out.println("Error al guardar el proyecto: " + project.getName());
            }
            
        }
    }
}
