# Second-Hand Clothes Marketplace REST API

## Application Description

This project is a second-hand clothes marketplace REST API developed using **Java**, **Spring Boot**, and **Spring Security**. It allows users to register, authenticate, and perform CRUD operations on garments they wish to sell. The application uses **JWT** (JSON Web Tokens) for secure authentication, and **Swagger** is integrated for API documentation and testing. The database is managed using **PostgreSQL**.

## Prerequisites

Before running the application, ensure that the following are installed:

- **Java 17** (JDK 17 or higher)
- **PostgreSQL** (for the database)

## Setup Instructions

### 1. Clone the Repository and Create PostgreSQL Schema

Ensure that a PostgreSQL database schema is created: for instance

```sql
CREATE DATABASE marketplace;
```
### 2. Configure application.properties

The application uses a PostgreSQL database. In the src/main/resources/application.properties file, configure the following properties based on your setup:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/db_name
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
```

### 3. Running the Application

To run the application, execute the main method of the Spring Boot application class (@SpringBootApplication class).
The server port is preconfigured as 9090.

### 4. Access Swagger UI for REST APIs

Once the application is running, you can access the Swagger API documentation and interact with the API at:

```
http://localhost:9090/swagger-ui.html
```
This provides an interactive interface to explore all available API endpoints.


## API Endpoints 

### Login Endpoints:

- **`POST /api/v1/register`**  
  User registration with full name, address and username and password. Returns a JWT for secure access to other endpoints. 
- Please NOTE, username should be unique.

- **`POST /api/v1/authenticate`**  
  Login with username and password. Returns a JWT for secure access to other endpoints.

### Authenticated Endpoints:

- **`POST /api/v1/garments`**  
  Publish a new garment (requires authentication).
- **`PUT /api/v1/garments/{id}`**  
  Update a garment (only the publisher of the garment can update it).
- **`DELETE /api/v1/garments/{id}`**  
  Unpublish a garment (only the publisher of the garment can delete it).


### Public Endpoints:

- **`GET /api/v1/clothes`**  
  List all garments. Supports query parameters for searching garments based on criteria (e.g., type, size, price).

- **`GET /api/v1/clothes/{id}`**  
  Retrieve details of a specific garment by its ID.


## JWT Authentication

The application uses **JWT** (JSON Web Tokens) for secure authentication. The secret keys for generating and verifying JWTs are stored in the `application.properties` file for testing purposes.

### Secret Key Configuration

In the `application.properties` file, you can find the following properties related to JWT:

```properties
jwt.public.key=your_public_key_here
jwt.private.key=your_private_key_here
```
