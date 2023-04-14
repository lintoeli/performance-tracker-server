package com.diversolab.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.diversolab.entities.GithubProject;

@Repository
public interface GithubProjectRepository extends JpaRepository<GithubProject, String> {

    Optional<GithubProject> findByAddress(String address);

    boolean existsByAddress(String address);

}