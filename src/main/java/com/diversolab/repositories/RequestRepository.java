package com.diversolab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.diversolab.entities.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, String> {

}