package com.diversolab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diversolab.entities.Project;
import com.diversolab.entities.github.GithubRelease;

@Repository
public interface GitHubReleasesRepository extends JpaRepository<GithubRelease, String>{
    
}
