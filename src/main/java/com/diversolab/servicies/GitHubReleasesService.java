package com.diversolab.servicies;

import java.util.List;

import org.springframework.stereotype.Service;

import com.diversolab.entities.github.GithubRelease;
import com.diversolab.repositories.GitHubReleasesRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GitHubReleasesService {
    private final GitHubReleasesRepository gitHubReleasesRepository;

    public void saveAll(List<GithubRelease> releases){
        this.gitHubReleasesRepository.saveAll(releases);
    }

    public void deleteAll(){
        this.gitHubReleasesRepository.deleteAll();
    }
}
