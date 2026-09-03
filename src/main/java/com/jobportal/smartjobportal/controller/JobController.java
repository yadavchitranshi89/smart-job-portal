package com.jobportal.smartjobportal.controller;

import com.jobportal.smartjobportal.entity.Job;
import com.jobportal.smartjobportal.service.JobService;
import com.jobportal.smartjobportal.dto.JobRequest;
import com.jobportal.smartjobportal.dto.JobResponse;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@Tag(
    name = "Jobs",
    description = "APIs for creating, updating, deleting, searching, filtering and retrieving jobs"
)
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // GET all jobs
    @Operation(
        summary = "Get all jobs",
        description = "Returns a list of all available jobs."
    )
    @GetMapping
    public List<JobResponse> getAllJobs() {
        return jobService.getAllJobs()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET jobs with pagination
    @Operation(
        summary = "Get jobs with pagination",
        description = "Returns jobs using pagination and sorting."
    )
    @GetMapping("/page")
    public Page<JobResponse> getJobsWithPagination(Pageable pageable) {
        return jobService.getAllJobs(pageable)
                .map(this::convertToResponse);
    }

    // SEARCH jobs by title
    @Operation(
        summary = "Search jobs by title",
        description = "Searches for jobs using keywords in the job title."
    )
    @GetMapping("/search")
    public List<JobResponse> searchJobs(@RequestParam String keyword) {
        return jobService.searchJobs(keyword)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // SEARCH jobs by location
    @Operation(
        summary = "Search jobs by location",
        description = "Returns jobs available in the specified location."
    )
    @GetMapping("/search/location")
    public List<JobResponse> searchJobsByLocation(
            @RequestParam String location) {

        return jobService.searchJobsByLocation(location)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // SEARCH jobs by title OR description
    @Operation(
        summary = "Search jobs by title or description",
        description = "Searches for jobs where the keyword appears in the title or description."
    )
    @GetMapping("/search/all")
    public List<JobResponse> searchJobsByTitleOrDescription(
            @RequestParam String keyword) {

        return jobService.searchJobsByTitleOrDescription(keyword)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // FILTER jobs by job type
    @Operation(
        summary = "Filter jobs by job type",
        description = "Returns jobs matching the specified job type such as Full Time or Internship."
    )
    @GetMapping("/filter")
    public List<JobResponse> filterJobsByJobType(
            @RequestParam String jobType) {

        return jobService.filterJobsByJobType(jobType)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // FILTER jobs by location and job type
    @Operation(
        summary = "Filter jobs by location and job type",
        description = "Returns jobs matching both the specified location and job type."
    )
    @GetMapping("/filter/search")
    public List<JobResponse> filterJobs(
            @RequestParam String location,
            @RequestParam String jobType) {

        return jobService.filterJobs(location, jobType)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET job by ID
    @Operation(
        summary = "Get job by ID",
        description = "Returns a specific job using its unique ID."
    )
    @GetMapping("/{id}")
    public JobResponse getJobById(@PathVariable Long id) {
        return convertToResponse(jobService.getJobById(id));
    }

    // CREATE a new job
    @Operation(
        summary = "Create a new job",
        description = "Creates and saves a new job in the database."
    )
    @PostMapping
    public JobResponse createJob(
            @Valid @RequestBody JobRequest request) {

        Job job = new Job(
                request.getTitle(),
                request.getCompany(),
                request.getLocation(),
                request.getJobType(),
                request.getDescription()
        );

        Job savedJob = jobService.saveJob(job);

        return convertToResponse(savedJob);
    }

    // UPDATE a job
    @Operation(
        summary = "Update a job",
        description = "Updates an existing job using its unique ID."
    )
    @PutMapping("/{id}")
    public JobResponse updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request) {

        Job jobDetails = new Job(
                request.getTitle(),
                request.getCompany(),
                request.getLocation(),
                request.getJobType(),
                request.getDescription()
        );

        Job updatedJob = jobService.updateJob(id, jobDetails);

        return convertToResponse(updatedJob);
    }

    // DELETE a job
    @Operation(
        summary = "Delete a job",
        description = "Deletes an existing job using its unique ID."
    )
    @DeleteMapping("/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return "Job deleted successfully";
    }

    // Convert Job entity to JobResponse DTO
    private JobResponse convertToResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getJobType(),
                job.getDescription()
        );
    }
}