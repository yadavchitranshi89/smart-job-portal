
package com.jobportal.smartjobportal.repository;

import com.jobportal.smartjobportal.entity.Job;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    // Test 1: Find jobs by title
    @Test
    void findByTitleContainingIgnoreCase_ShouldReturnMatchingJobs() {

        Job job = new Job(
                "RepoTest Java Developer XYZ123",
                "TestCompany",
                "TestMumbaiXYZ123",
                "TEST_FULL_TIME_XYZ123",
                "Develop Java applications"
        );

        jobRepository.save(job);

        List<Job> result =
                jobRepository.findByTitleContainingIgnoreCase(
                        "XYZ123"
                );

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "RepoTest Java Developer XYZ123",
                result.get(0).getTitle()
        );
    }


    // Test 2: Find jobs by location
    @Test
    void findByLocationContainingIgnoreCase_ShouldReturnMatchingJobs() {

        Job job = new Job(
                "RepoTest React Developer XYZ456",
                "TestCompany",
                "TestMumbaiXYZ456",
                "TEST_FULL_TIME_XYZ456",
                "Develop React applications"
        );

        jobRepository.save(job);

        List<Job> result =
                jobRepository.findByLocationContainingIgnoreCase(
                        "XYZ456"
                );

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "TestMumbaiXYZ456",
                result.get(0).getLocation()
        );
    }


    // Test 3: Find jobs by job type
    @Test
    void findByJobTypeIgnoreCase_ShouldReturnMatchingJobs() {

        Job job = new Job(
                "RepoTest Python Developer XYZ789",
                "TestCompany",
                "TestBangaloreXYZ789",
                "TEST_INTERNSHIP_XYZ789",
                "Develop Python applications"
        );

        jobRepository.save(job);

        List<Job> result =
                jobRepository.findByJobTypeIgnoreCase(
                        "TEST_INTERNSHIP_XYZ789"
                );

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "TEST_INTERNSHIP_XYZ789",
                result.get(0).getJobType()
        );
    }


    // Test 4: Find jobs by location and job type
    @Test
    void findByLocationAndJobType_ShouldReturnMatchingJobs() {

        Job job = new Job(
                "RepoTest Backend Developer XYZ999",
                "TestCompany",
                "TestPuneXYZ999",
                "TEST_FULL_TIME_XYZ999",
                "Develop REST APIs"
        );

        jobRepository.save(job);

        List<Job> result =
                jobRepository
                        .findByLocationContainingIgnoreCaseAndJobTypeIgnoreCase(
                                "XYZ999",
                                "TEST_FULL_TIME_XYZ999"
                        );

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "TestPuneXYZ999",
                result.get(0).getLocation()
        );

        assertEquals(
                "TEST_FULL_TIME_XYZ999",
                result.get(0).getJobType()
        );
    }


    // Test 5: Find jobs by title OR description
    @Test
    void findByTitleOrDescription_ShouldReturnMatchingJobs() {

        Job job = new Job(
                "RepoTest Backend Developer ABC555",
                "TestCompany",
                "TestPuneABC555",
                "TEST_FULL_TIME_ABC555",
                "Description contains UNIQUE_REPO_TEST_ABC555"
        );

        jobRepository.save(job);

        List<Job> result =
                jobRepository
                        .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                                "UNIQUE_REPO_TEST_ABC555",
                                "UNIQUE_REPO_TEST_ABC555"
                        );

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "RepoTest Backend Developer ABC555",
                result.get(0).getTitle()
        );
    }
}
