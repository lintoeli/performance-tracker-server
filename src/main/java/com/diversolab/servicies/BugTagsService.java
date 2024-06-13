package com.diversolab.servicies;


import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.diversolab.entities.BugTags;
import com.diversolab.repositories.BugTagsRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BugTagsService {
    
    private final BugTagsRepository bugTagsRepository;

    public BugTags findByAddress(String address){
        return bugTagsRepository.findByAddress(address).orElseThrow(() -> new NoSuchElementException());
    }
}
