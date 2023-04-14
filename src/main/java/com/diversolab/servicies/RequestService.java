package com.diversolab.servicies;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import com.diversolab.entities.GithubProject;
import com.diversolab.entities.Request;
import com.diversolab.repositories.GithubProjectRepository;
import com.diversolab.repositories.RequestRepository;

import jakarta.transaction.Transactional;

@AllArgsConstructor
@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final GithubProjectRepository githubProjectRepository;

    @Transactional
    public Request newRequest(String address) {

        Request request = new Request();

        try{
            GithubProject githubProject = new GithubProject();
            githubProject.setAddress(address);
            githubProject = githubProjectRepository.save(githubProject);
    
            request.setGithubProject(githubProject);
            requestRepository.save(request);

        }catch(Error e){
            request = null;
        }

        return request;

    }

    public Request findById(String id) {
        return requestRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    public Request save(Request request) {
        return requestRepository.save(request);
    }
    
}

    