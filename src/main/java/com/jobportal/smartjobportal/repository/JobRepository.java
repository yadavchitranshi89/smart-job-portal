package com.jobportal.smartjobportal.repository;

import com.jobportal.smartjobportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByTitleContainingIgnoreCase(String title);

    List<Job> findByDescriptionContainingIgnoreCase(String description);

    List<Job> findByLocationContainingIgnoreCase(String location);

    List<Job> findByJobTypeIgnoreCase(String jobType);

    List<Job> findByLocationContainingIgnoreCaseAndJobTypeIgnoreCase(
        String location,
        String jobType
);
    List<Job> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String title,
        String description
);
}