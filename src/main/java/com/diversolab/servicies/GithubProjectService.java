package com.diversolab.servicies;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import com.diversolab.entities.GithubProject;
import com.diversolab.repositories.GithubProjectRepository;

@AllArgsConstructor
@Service
public class GithubProjectService {

    private final GithubProjectRepository githubProjectRepository;

    public GithubProject findById(String id) {
        return githubProjectRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public GithubProject findByAddress(String address) {
        return githubProjectRepository.findByAddress(address).orElseThrow(() -> new NoSuchElementException());
    }

    public List<GithubProject> findAll() {
        return githubProjectRepository.findAll();
    }

    public GithubProject save(GithubProject githubProject) {
        return githubProjectRepository.save(githubProject);
    }

    public boolean existsByAddress(String address) {
        return githubProjectRepository.existsByAddress(address);
    }
    
}

    