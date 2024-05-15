package com.diversolab.repositories;

import org.springframework.stereotype.Repository;
import com.diversolab.entities.BugTags;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BugTagsRepository extends JpaRepository<BugTags, String>{
    Optional<BugTags> findByProjectName(String name);
}
