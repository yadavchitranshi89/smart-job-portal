package com.jobportal.smartjobportal.service;

import com.jobportal.smartjobportal.entity.Job;
import com.jobportal.smartjobportal.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jobportal.smartjobportal.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    
    public Page<Job> getAllJobs(Pageable pageable) {
    return jobRepository.findAll(pageable);
}

    public Job getJobById(Long id) {
    return jobRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Job with ID " + id + " not found"));
}

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
    if (!jobRepository.existsById(id)) {
        throw new ResourceNotFoundException(
                "Job with ID " + id + " not found"
        );
    }

    jobRepository.deleteById(id);
}

   public Job updateJob(Long id, Job jobDetails) {

    Job job = jobRepository.findById(id)
        .orElseThrow(() ->
                new ResourceNotFoundException(
                        "Job with ID " + id + " not found"));

    job.setTitle(jobDetails.getTitle());
    job.setCompany(jobDetails.getCompany());
    job.setLocation(jobDetails.getLocation());
    job.setJobType(jobDetails.getJobType());
    job.setDescription(jobDetails.getDescription());

    return jobRepository.save(job);
   } 

   // Search by title only
   public List<Job> searchJobs(String keyword) {
    return jobRepository.findByTitleContainingIgnoreCase(keyword);
   }
   public List<Job> searchJobsByLocation(String location) {
    return jobRepository.findByLocationContainingIgnoreCase(location);
  }
  public List<Job> filterJobsByJobType(String jobType) {
    return jobRepository.findByJobTypeIgnoreCase(jobType);
  }
  public List<Job> filterJobs(String location, String jobType) {
    return jobRepository
            .findByLocationContainingIgnoreCaseAndJobTypeIgnoreCase(
                    location, jobType);
}

// Search by title OR description
 public List<Job> searchJobsByTitleOrDescription(String keyword) {
    return jobRepository
            .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    keyword, keyword);
}
}