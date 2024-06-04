package com.diversolab.repositories;

import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.diversolab.entities.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String>{

    Optional<Project> findByName(String name);
    Optional<Project> findByAddress(String address);
    
}
