package com.diversolab.servicies;


import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.diversolab.entities.BugTags;
import com.diversolab.repositories.BugTagsRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BugTagsService {
    
    private final BugTagsRepository bugTagsRepository;

    public BugTags findByProjectName(String projectName){
        return bugTagsRepository.findByProjectName(projectName).orElseThrow(() -> new NoSuchElementException());
    }
}
