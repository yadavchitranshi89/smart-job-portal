
package com.jobportal.smartjobportal.controller;

import com.jobportal.smartjobportal.entity.Job;
import com.jobportal.smartjobportal.repository.JobRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobRepository jobRepository;


    // =========================================================
    // TEST 1: GET ALL JOBS
    // =========================================================

    @Test
    void getAllJobs_ShouldReturnJobs() throws Exception {

        Job job = new Job(
                "ControllerTest Developer XYZ111",
                "TestCompany",
                "TestMumbaiXYZ111",
                "TEST_FULL_TIME_XYZ111",
                "Controller testing job"
        );

        jobRepository.save(job);

        mockMvc.perform(
                get("/api/jobs")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
        ));
    }


    // =========================================================
    // TEST 2: GET JOB BY ID
    // =========================================================

    @Test
    void getJobById_ShouldReturnJob() throws Exception {

        Job job = new Job(
                "ControllerTest Java Developer XYZ222",
                "TestCompany",
                "TestMumbaiXYZ222",
                "TEST_FULL_TIME_XYZ222",
                "Controller testing job by ID"
        );

        Job savedJob = jobRepository.save(job);

        mockMvc.perform(
                get("/api/jobs/" + savedJob.getId())
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(savedJob.getId()))
        .andExpect(jsonPath("$.title")
                .value("ControllerTest Java Developer XYZ222"))
        .andExpect(jsonPath("$.company")
                .value("TestCompany"));
    }


    // =========================================================
    // TEST 3: GET JOB BY ID - NOT FOUND
    // =========================================================

    @Test
    void getJobById_ShouldReturn404_WhenJobDoesNotExist()
            throws Exception {

        mockMvc.perform(
                get("/api/jobs/999999")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"));
    }


    // =========================================================
    // TEST 4: CREATE JOB
    // =========================================================

    @Test
    void createJob_ShouldReturnCreatedJob() throws Exception {

        String requestBody = """
                {
                    "title": "ControllerTest Python Developer XYZ333",
                    "company": "TestCompany",
                    "location": "TestPuneXYZ333",
                    "jobType": "TEST_FULL_TIME_XYZ333",
                    "description": "Controller POST test job"
                }
                """;

        mockMvc.perform(
                post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title")
                .value("ControllerTest Python Developer XYZ333"))
        .andExpect(jsonPath("$.company")
                .value("TestCompany"))
        .andExpect(jsonPath("$.location")
                .value("TestPuneXYZ333"));
    }


    // =========================================================
    // TEST 5: CREATE JOB - VALIDATION FAILURE
    // =========================================================

    @Test
    void createJob_ShouldReturn400_WhenTitleIsBlank()
            throws Exception {

        String requestBody = """
                {
                    "title": "",
                    "company": "TestCompany",
                    "location": "Mumbai",
                    "jobType": "Full Time",
                    "description": "Validation test"
                }
                """;

        mockMvc.perform(
                post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message")
                .value("Validation failed"))
        .andExpect(jsonPath("$.errors.title")
                .value("Title is required"));
    }


    // =========================================================
    // TEST 6: UPDATE JOB
    // =========================================================

    @Test
    void updateJob_ShouldReturnUpdatedJob() throws Exception {

        Job job = new Job(
                "ControllerTest Old Title XYZ444",
                "OldCompany",
                "OldLocationXYZ444",
                "TEST_FULL_TIME_XYZ444",
                "Old description"
        );

        Job savedJob = jobRepository.save(job);

        String requestBody = """
                {
                    "title": "ControllerTest Updated Title XYZ444",
                    "company": "UpdatedCompany",
                    "location": "UpdatedMumbaiXYZ444",
                    "jobType": "TEST_INTERNSHIP_XYZ444",
                    "description": "Updated description"
                }
                """;

        mockMvc.perform(
                put("/api/jobs/" + savedJob.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id")
                .value(savedJob.getId()))
        .andExpect(jsonPath("$.title")
                .value("ControllerTest Updated Title XYZ444"))
        .andExpect(jsonPath("$.company")
                .value("UpdatedCompany"))
        .andExpect(jsonPath("$.location")
                .value("UpdatedMumbaiXYZ444"))
        .andExpect(jsonPath("$.jobType")
                .value("TEST_INTERNSHIP_XYZ444"))
        .andExpect(jsonPath("$.description")
                .value("Updated description"));
    }


    // =========================================================
    // TEST 7: UPDATE JOB - NOT FOUND
    // =========================================================

    @Test
    void updateJob_ShouldReturn404_WhenJobDoesNotExist()
            throws Exception {

        String requestBody = """
                {
                    "title": "Updated Job",
                    "company": "TestCompany",
                    "location": "Mumbai",
                    "jobType": "Full Time",
                    "description": "Updated description"
                }
                """;

        mockMvc.perform(
                put("/api/jobs/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"));
    }


    // =========================================================
    // TEST 8: DELETE JOB
    // =========================================================

    @Test
    void deleteJob_ShouldDeleteJob() throws Exception {

        Job job = new Job(
                "ControllerTest Delete Job XYZ555",
                "TestCompany",
                "TestMumbaiXYZ555",
                "TEST_FULL_TIME_XYZ555",
                "Delete testing job"
        );

        Job savedJob = jobRepository.save(job);

        mockMvc.perform(
                delete("/api/jobs/" + savedJob.getId())
        )
        .andExpect(status().isOk())
        .andExpect(content()
                .string("Job deleted successfully"));

        assert !jobRepository.existsById(savedJob.getId());
    }


    // =========================================================
    // TEST 9: DELETE JOB - NOT FOUND
    // =========================================================

    @Test
    void deleteJob_ShouldReturn404_WhenJobDoesNotExist()
            throws Exception {

        mockMvc.perform(
                delete("/api/jobs/999999")
        )
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"));
    }


    // =========================================================
    // TEST 10: SEARCH JOBS BY TITLE
    // =========================================================

    @Test
    void searchJobs_ShouldReturnMatchingJobs() throws Exception {

        Job job = new Job(
                "ControllerTest Search Developer XYZ666",
                "TestCompany",
                "TestMumbaiXYZ666",
                "TEST_FULL_TIME_XYZ666",
                "Search testing job"
        );

        jobRepository.save(job);

        mockMvc.perform(
                get("/api/jobs/search")
                        .param("keyword", "XYZ666")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title")
                .value("ControllerTest Search Developer XYZ666"));
    }


    // =========================================================
    // TEST 11: SEARCH JOBS BY LOCATION
    // =========================================================

    @Test
    void searchJobsByLocation_ShouldReturnMatchingJobs()
            throws Exception {

        Job job = new Job(
                "ControllerTest Location Developer XYZ777",
                "TestCompany",
                "TestDelhiXYZ777",
                "TEST_FULL_TIME_XYZ777",
                "Location search test"
        );

        jobRepository.save(job);

        mockMvc.perform(
                get("/api/jobs/search/location")
                        .param("location", "XYZ777")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].location")
                .value("TestDelhiXYZ777"));
    }


    // =========================================================
    // TEST 12: SEARCH TITLE OR DESCRIPTION
    // =========================================================

    @Test
    void searchJobsByTitleOrDescription_ShouldReturnMatchingJobs()
            throws Exception {

        Job job = new Job(
                "ControllerTest Developer XYZ888",
                "TestCompany",
                "TestMumbaiXYZ888",
                "TEST_FULL_TIME_XYZ888",
                "UNIQUE_CONTROLLER_SEARCH_XYZ888"
        );

        jobRepository.save(job);

        mockMvc.perform(
                get("/api/jobs/search/all")
                        .param("keyword", "UNIQUE_CONTROLLER_SEARCH_XYZ888")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].description")
                .value("UNIQUE_CONTROLLER_SEARCH_XYZ888"));
    }


    // =========================================================
    // TEST 13: FILTER BY JOB TYPE
    // =========================================================

    @Test
    void filterJobsByJobType_ShouldReturnMatchingJobs()
            throws Exception {

        Job job = new Job(
                "ControllerTest Filter Developer XYZ999",
                "TestCompany",
                "TestMumbaiXYZ999",
                "TEST_INTERNSHIP_XYZ999",
                "Job type filter test"
        );

        jobRepository.save(job);

        mockMvc.perform(
                get("/api/jobs/filter")
                        .param("jobType", "TEST_INTERNSHIP_XYZ999")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].jobType")
                .value("TEST_INTERNSHIP_XYZ999"));
    }


    // =========================================================
    // TEST 14: FILTER BY LOCATION AND JOB TYPE
    // =========================================================

    @Test
    void filterJobs_ShouldReturnMatchingJobs() throws Exception {

        Job job = new Job(
                "ControllerTest Filter Developer ABC111",
                "TestCompany",
                "TestPuneABC111",
                "TEST_FULL_TIME_ABC111",
                "Location and type filter test"
        );

        jobRepository.save(job);

        mockMvc.perform(
                get("/api/jobs/filter/search")
                        .param("location", "ABC111")
                        .param("jobType", "TEST_FULL_TIME_ABC111")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].location")
                .value("TestPuneABC111"))
        .andExpect(jsonPath("$[0].jobType")
                .value("TEST_FULL_TIME_ABC111"));
    }


    // =========================================================
    // TEST 15: PAGINATION
    // =========================================================

    @Test
    void getJobsWithPagination_ShouldReturnPage()
            throws Exception {

        mockMvc.perform(
                get("/api/jobs/page")
                        .param("page", "0")
                        .param("size", "5")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.size").value(5))
        .andExpect(jsonPath("$.number").value(0));
    }


    // =========================================================
    // TEST 16: SORTING
    // =========================================================

    @Test
    void getJobsWithPagination_ShouldSupportSorting()
            throws Exception {

        mockMvc.perform(
                get("/api/jobs/page")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "title,asc")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.number").value(0));
    }
}

