![Java](https://img.shields.io/badge/17-red?style=plastic&logoColor=red&label=java&link=https%3A%2F%2Fwww.oracle.com%2Fjava%2Ftechnologies%2Fjavase%2Fjdk17-archive-downloads.html)
![Spring Boot](https://img.shields.io/badge/3.5.0-blue?style=plastic&logoColor=red&label=Spring%20Boot&link=https%3A%2F%2Fspring.io%2Fprojects%2Fspring-boot)
![PostgreSQL-14.17](https://img.shields.io/badge/PostgreSQL-yellow?style=plastic&link=https%3A%2F%2Fwww.postgresql.org%2F)

# 🍲 Recipes RESTful API

A Java Spring Boot RESTful API for managing cooking recipes. The API allows users to create, read, update, delete, and
search for recipes with flexible filters.

## 📚 Table of Contents

- [Technical Stack](#-technical-stack)
- [Features](#-features)
- [Design Choices](#-design-choices)
- [Setup Instructions](#-setup-instructions)
- [Project Structure](#-project-structure)
- [Domain Model](#-domain-model)
- [API Endpoints](#-api-endpoints)
- [Testing](#-testing)

---

## 🛠️ Technical Stack

- **Java 17**
- **Spring Boot 3.5.0**
- **JPA/Hibernate**
- **PostgreSQL 14.17**
- **Lombok**

---

## 🚀 Features

- **CRUD Operations**: Create, Read, Update, Delete recipes.
- **Search**: Filter recipes by vegetarian, servings, included/excluded ingredients, and instruction content.
- **RESTful API**: Follows REST principles and best practices.
- **Validation & Error Handling**: Input validation and meaningful error responses.
- **Modern Testing**: Integration tests run against a real PostgreSQL 14.17 instance using Testcontainers.

---

## ⚙️ Design Choices

- **Hexagonal Architecture**: Clear separation between domain, application, and infrastructure.
  - **Core**: Contains business logic, use cases, and domain models, isolated from infrastructure concerns.
  - **Adapters**:
    - Primary Adapters (AdaptersIn): REST controllers that handle HTTP requests and responses.
    - Secondary Adapters (AdaptersOut): Implement the ports defined in the core, such as database repositories and
      more if
      required.
  - **Ports (Interfaces)**: Define the operations required by the core, such as repositories and services.
- **DTOs**: Used for API requests/responses to decouple domain from transport.
- **Validation**: Uses Jakarta Bean Validation for input checks.
- **Exception Handling**: Centralized with meaningful HTTP status codes and messages.
- **Persistence**: JPA/Hibernate with PostgreSQL, auto schema update for development.

---

## 📦 Setup Instructions

### 1. **Clone the Repository**

```bash
git clone https://github.com/thalesmaiaa/recipes.git
cd recipes
```

### 2. **Database Setup**

- Ensure Docker is installed. [Docker Installation Guide](https://docs.docker.com/get-started/get-docker/)
- Start PostgreSQL using Docker Compose:

```bash
docker compose up -d
```

**Note**: If you wish, you can change the default database connection credentials
in [application.yml](./src/main/resources/application.yml#L5-L9)
and in [docker-compose.yml](./docker-compose.yml#L5-L7) as needed before starting the application.

### 3. **Build & Run the Application**

```bash
  ./mvnw clean install
  ./mvnw spring-boot:run
```

The API will be available at: `http://localhost:8080`

---

## 🗂️ Project Structure

```
src/
  main/
    java/com/recipes/
      adapter/in/         # REST controllers, DTOs, Exception handler
      core/               # Domain, use cases, ports, exceptions
      adapter/out/        # Persistence adapters
    resources/
      application.yml     # Spring Boot configuration
  test/                   # Unit and integration tests
```

---

## 🧩 Domain Model

- **Recipe**
  - `id`: Long
  - `title`: String
  - `description`: String
  - `ingredients`: List<String>
  - `instructions`: List<String>
  - `vegetarian`: Boolean (optional, default false)
  - `servingSize`: Integer

---

## 🌐 API Endpoints

### **Create Recipe**

`POST /v1/recipes`

- **Request Body**:
  ```json
  {
    "title": "Spaghetti Bolognese",
    "description": "A classic Italian pasta dish...",
    "ingredients": ["Spaghetti", "Beef", "Tomato sauce"],
    "instructions": ["Boil pasta", "Cook beef", "Mix sauce"],
    "vegetarian": false,
    "servingSize": 4
  }
  ```
- **Response**: `201 Created` with `Location` header

---

### **Get Recipe by ID**

`GET /v1/recipes/{id}`

- **Response**:
  ```json
  {
    "id": 1,
    "title": "Spaghetti Bolognese",
    "description": "A classic Italian pasta dish...",
    "ingredients": ["spaghetti"],
    "instructions": ["boil pasta", "cook beef", "mix sauce"],
    "vegetarian": false,
    "servingSize": 4
  }
  ```

---

### **Update Recipe**

`PATCH /v1/recipes/{id}`

- **Body**: Same as create (only include fields you want to update)
- **Response**: Updated recipe

---

### **Delete Recipe**

`DELETE /v1/recipes/{id}`

- **Response**: `204 No Content`

---

### **Search Recipes**

`GET /v1/recipes`

- **Flexible Filtering (all optional):**  
  This endpoint supports dynamic filtering using the Spring Data JPA Specification API. You can combine any of the
  following query parameters:

  - `includedIngredients` (`String[]`): Only recipes containing **all** specified ingredients are returned.
  - `excludedIngredients` (`String[]`): Recipes containing **any** of these ingredients are excluded.
  - `instruction` (`String`): Returns recipes where **any instruction step** contains the given string (
    case-insensitive).
  - `vegetarian` (`Boolean`): Filters recipes by their vegetarian status.
  - `servingSize` (`Integer`): Filters recipes by the exact serving size.
  - **Pagination:**
    - `page` (default: 0): Page number (starts at 0)
    - `size` (default: 20): Number of recipes per page
    - `sort={fieldName},{ASC|DESC}`: Sort results by field and direction

- **How Filtering Works:**  
  All provided filters are combined using logical **AND**. For example, you can search for vegetarian recipes that serve
  four people and include "Tomato" but exclude "Cheese."

- **Example Request:**

  ```
  GET /v1/recipes?includedIngredients=tomato,bread&excludedIngredients=cheese,garlic&vegetarian=true&servingSize=4&page=0&size=10
  ```

- **Response**: Paginated list of recipes

```json
{
  "content": [
    {
      "id": 1,
      "title": "Spaghetti Bolognese",
      "description": "A classic Italian pasta dish...",
      "ingredients": ["spaghetti"],
      "instructions": ["boil pasta", "cook beef", "mix sauce"],
      "vegetarian": false,
      "servingSize": 4
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## 🧪 Testing

- Unit tests for domain, use cases, and controllers.
- Integration tests using [Testcontainers](https://www.testcontainers.org/) to spin up a real PostgreSQL instance
  automatically.
- Run all tests:

> **Note:** Docker must be running on your machine for integration tests to work with Testcontainers.

```bash

 ./mvnw test

```
