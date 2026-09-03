package com.jobportal.smartjobportal.service;

import com.jobportal.smartjobportal.entity.Job;
import com.jobportal.smartjobportal.exception.ResourceNotFoundException;
import com.jobportal.smartjobportal.repository.JobRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;


    // =========================================================
    // Test 1: Get job when job exists
    // =========================================================

    @Test
    void getJobById_ShouldReturnJob_WhenJobExists() {

        Job job = new Job(
                "Java Developer",
                "TCS",
                "Mumbai",
                "Full Time",
                "Develop Java applications"
        );

        when(jobRepository.findById(1L))
                .thenReturn(Optional.of(job));

        Job result = jobService.getJobById(1L);

        assertNotNull(result);
        assertEquals("Java Developer", result.getTitle());
        assertEquals("TCS", result.getCompany());
        assertEquals("Mumbai", result.getLocation());

        verify(jobRepository, times(1))
                .findById(1L);
    }


    // =========================================================
    // Test 2: Get job when job does not exist
    // =========================================================

    @Test
    void getJobById_ShouldThrowException_WhenJobDoesNotExist() {

        when(jobRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> jobService.getJobById(99L)
        );

        verify(jobRepository, times(1))
                .findById(99L);
    }


    // =========================================================
    // Test 3: Save a job
    // =========================================================

    @Test
    void saveJob_ShouldSaveAndReturnJob() {

        Job job = new Job(
                "Python Developer",
                "Accenture",
                "Bangalore",
                "Full Time",
                "Develop Python applications"
        );

        when(jobRepository.save(job))
                .thenReturn(job);

        Job result = jobService.saveJob(job);

        assertNotNull(result);
        assertEquals("Python Developer", result.getTitle());
        assertEquals("Accenture", result.getCompany());

        verify(jobRepository, times(1))
                .save(job);
    }


    // =========================================================
    // Test 4: Delete job when job exists
    // =========================================================

    @Test
    void deleteJob_ShouldDeleteJob_WhenJobExists() {

        Long jobId = 1L;

        when(jobRepository.existsById(jobId))
                .thenReturn(true);

        doNothing().when(jobRepository)
                .deleteById(jobId);

        jobService.deleteJob(jobId);

        verify(jobRepository, times(1))
                .existsById(jobId);

        verify(jobRepository, times(1))
                .deleteById(jobId);
    }


    // =========================================================
    // Test 5: Delete job when job does not exist
    // =========================================================

    @Test
    void deleteJob_ShouldThrowException_WhenJobDoesNotExist() {

        Long jobId = 99L;

        when(jobRepository.existsById(jobId))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> jobService.deleteJob(jobId)
        );

        verify(jobRepository, times(1))
                .existsById(jobId);

        verify(jobRepository, never())
                .deleteById(jobId);
    }


    // =========================================================
    // Test 6: Update an existing job
    // =========================================================

    @Test
    void updateJob_ShouldUpdateAndReturnJob_WhenJobExists() {

        Job existingJob = new Job(
                "Java Developer",
                "TCS",
                "Mumbai",
                "Full Time",
                "Develop Java applications"
        );

        Job updatedDetails = new Job(
                "Senior Java Developer",
                "TCS",
                "Mumbai",
                "Full Time",
                "Develop advanced Java Spring Boot applications"
        );

        when(jobRepository.findById(1L))
                .thenReturn(Optional.of(existingJob));

        when(jobRepository.save(existingJob))
                .thenReturn(existingJob);

        Job result = jobService.updateJob(1L, updatedDetails);

        assertNotNull(result);

        assertEquals(
                "Senior Java Developer",
                result.getTitle()
        );

        assertEquals(
                "TCS",
                result.getCompany()
        );

        assertEquals(
                "Mumbai",
                result.getLocation()
        );

        assertEquals(
                "Full Time",
                result.getJobType()
        );

        assertEquals(
                "Develop advanced Java Spring Boot applications",
                result.getDescription()
        );

        verify(jobRepository, times(1))
                .findById(1L);

        verify(jobRepository, times(1))
                .save(existingJob);
    }


    // =========================================================
    // Test 7: Update job when job does not exist
    // =========================================================

    @Test
    void updateJob_ShouldThrowException_WhenJobDoesNotExist() {

        Job updatedDetails = new Job(
                "Senior Java Developer",
                "TCS",
                "Mumbai",
                "Full Time",
                "Develop advanced Java applications"
        );

        when(jobRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> jobService.updateJob(99L, updatedDetails)
        );

        verify(jobRepository, times(1))
                .findById(99L);

        verify(jobRepository, never())
                .save(any(Job.class));
    }


    // =========================================================
    // Test 8: Search jobs by title
    // =========================================================

    @Test
    void searchJobs_ShouldReturnMatchingJobs() {

        Job job = new Job(
                "Java Developer",
                "TCS",
                "Mumbai",
                "Full Time",
                "Develop Java applications"
        );

        when(
                jobRepository.findByTitleContainingIgnoreCase("Java")
        ).thenReturn(List.of(job));

        List<Job> result =
                jobService.searchJobs("Java");

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "Java Developer",
                result.get(0).getTitle()
        );

        verify(
                jobRepository,
                times(1)
        ).findByTitleContainingIgnoreCase("Java");
    }


    // =========================================================
    // Test 9: Search jobs by location
    // =========================================================

    @Test
    void searchJobsByLocation_ShouldReturnMatchingJobs() {

        Job job = new Job(
                "React Developer",
                "Tech Mahindra",
                "Mumbai",
                "Full Time",
                "Develop React applications"
        );

        when(
                jobRepository
                        .findByLocationContainingIgnoreCase("Mumbai")
        ).thenReturn(List.of(job));

        List<Job> result =
                jobService.searchJobsByLocation("Mumbai");

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "Mumbai",
                result.get(0).getLocation()
        );

        verify(
                jobRepository,
                times(1)
        ).findByLocationContainingIgnoreCase("Mumbai");
    }


    // =========================================================
    // Test 10: Search jobs by title OR description
    // =========================================================

    @Test
    void searchJobsByTitleOrDescription_ShouldReturnMatchingJobs() {

        Job job = new Job(
                "Backend Developer",
                "Wipro",
                "Pune",
                "Full Time",
                "Develop REST APIs using Java and Spring Boot"
        );

        when(
                jobRepository
                        .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                                "Java",
                                "Java"
                        )
        ).thenReturn(List.of(job));

        List<Job> result =
                jobService.searchJobsByTitleOrDescription("Java");

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "Backend Developer",
                result.get(0).getTitle()
        );

        verify(
                jobRepository,
                times(1)
        ).findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "Java",
                "Java"
        );
    }


    // =========================================================
    // Test 11: Filter jobs by job type
    // =========================================================

    @Test
    void filterJobsByJobType_ShouldReturnMatchingJobs() {

        Job job = new Job(
                "Java Developer",
                "TCS",
                "Mumbai",
                "Full Time",
                "Develop Java applications"
        );

        when(
                jobRepository.findByJobTypeIgnoreCase("Full Time")
        ).thenReturn(List.of(job));

        List<Job> result =
                jobService.filterJobsByJobType("Full Time");

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "Full Time",
                result.get(0).getJobType()
        );

        verify(
                jobRepository,
                times(1)
        ).findByJobTypeIgnoreCase("Full Time");
    }


    // =========================================================
    // Test 12: Filter jobs by location and job type
    // =========================================================

    @Test
    void filterJobs_ShouldReturnMatchingJobs() {

        Job job = new Job(
                "React Developer",
                "Tech Mahindra",
                "Mumbai",
                "Full Time",
                "Develop React web applications"
        );

        when(
                jobRepository
                        .findByLocationContainingIgnoreCaseAndJobTypeIgnoreCase(
                                "Mumbai",
                                "Full Time"
                        )
        ).thenReturn(List.of(job));

        List<Job> result =
                jobService.filterJobs(
                        "Mumbai",
                        "Full Time"
                );

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "Mumbai",
                result.get(0).getLocation()
        );

        assertEquals(
                "Full Time",
                result.get(0).getJobType()
        );

        verify(
                jobRepository,
                times(1)
        ).findByLocationContainingIgnoreCaseAndJobTypeIgnoreCase(
                "Mumbai",
                "Full Time"
        );
    }


    // =========================================================
    // Test 13: Get jobs with pagination
    // =========================================================

    @Test
    void getAllJobs_ShouldReturnPaginatedJobs() {

        Job job1 = new Job(
                "Java Developer",
                "TCS",
                "Mumbai",
                "Full Time",
                "Develop Java applications"
        );

        Job job2 = new Job(
                "Python Developer",
                "Accenture",
                "Bangalore",
                "Full Time",
                "Develop Python applications"
        );

        PageRequest pageable =
                PageRequest.of(0, 2);

        Page<Job> page =
                new PageImpl<>(
                        List.of(job1, job2),
                        pageable,
                        2
                );

        when(
                jobRepository.findAll(pageable)
        ).thenReturn(page);

        Page<Job> result =
                jobService.getAllJobs(pageable);

        assertNotNull(result);

        assertEquals(
                2,
                result.getTotalElements()
        );

        assertEquals(
                2,
                result.getContent().size()
        );

        assertEquals(
                "Java Developer",
                result.getContent()
                        .get(0)
                        .getTitle()
        );

        assertEquals(
                "Python Developer",
                result.getContent()
                        .get(1)
                        .getTitle()
        );

        verify(
                jobRepository,
                times(1)
        ).findAll(pageable);
    }
}