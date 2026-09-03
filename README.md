\# Smart Job Portal REST API



A backend REST API for managing job listings, built using Spring Boot, Java, and MySQL.



\## Features



\- Create a new job

\- Get all jobs

\- Get job by ID

\- Update a job

\- Delete a job

\- Search jobs by title

\- Search jobs by location

\- Search by title or description

\- Filter jobs by job type

\- Filter by location and job type

\- Pagination

\- Sorting

\- Request validation

\- Global exception handling

\- DTO-based architecture

\- Swagger/OpenAPI documentation

\- Unit and integration testing



\## Tech Stack



\- Java 25

\- Spring Boot

\- Spring Data JPA

\- MySQL

\- Maven

\- JUnit

\- Mockito

\- Swagger / OpenAPI

\- HTML / CSS



\## Project Structure



```text

src

├── main

│   ├── java

│   │   └── com.jobportal.smartjobportal

│   │       ├── config

│   │       ├── controller

│   │       ├── dto

│   │       ├── entity

│   │       ├── exception

│   │       ├── repository

│   │       └── service

│   │

│   └── resources

│       ├── application.properties

│       └── templates

│

└── test

&#x20;   └── java

&#x20;       └── com.jobportal.smartjobportal

&#x20;           ├── controller

&#x20;           ├── repository

&#x20;           └── service



API Endpoints

Jobs

Method	Endpoint	Description

GET	/api/jobs	Get all jobs

GET	/api/jobs/{id}	Get job by ID

POST	/api/jobs	Create a job

PUT	/api/jobs/{id}	Update a job

DELETE	/api/jobs/{id}	Delete a job

Search

Method	Endpoint	Description

GET	/api/jobs/search?keyword={keyword}	Search by title

GET	/api/jobs/search/location?location={location}	Search by location

GET	/api/jobs/search/title-description?keyword={keyword}	Search title or description

Filters

Method	Endpoint	Description

GET	/api/jobs/filter?jobType={jobType}	Filter by job type

GET	/api/jobs/filter/location-type?location={location}\&jobType={jobType}	Filter by location and job type

Pagination and Sorting

GET /api/jobs/page?page=0\&size=5



Example with sorting:



GET /api/jobs/page?page=0\&size=5\&sortBy=title\&direction=asc

Validation



The API validates incoming job data and returns HTTP 400 Bad Request when required fields are invalid.



Exception Handling



The project includes:



ResourceNotFoundException

GlobalExceptionHandler



A request for a non-existing job returns HTTP 404 Not Found.



Swagger Documentation



Swagger/OpenAPI is included for interactive API documentation and testing.



Testing



The project contains tests for:



Service layer

Repository layer

Controller layer



Run all tests using:



mvn clean test

How to Run

1\. Clone the repository

git clone https://github.com/yadavchitranshi89/smart-job-portal.git

2\. Open the project

cd smart-job-portal

3\. Configure MySQL



Create the database:



CREATE DATABASE smart\_job\_portal;



Update your database configuration in:



src/main/resources/application.properties

4\. Run the application

mvn spring-boot:run

Author



Chitranshi Yadav





\### 3. Save and close Notepad



Then run:



```powershell

git status

