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

---

## ⚙️ Design Choices

- **Hexagonal Architecture**: Clear separation between domain, application, and infrastructure.
  - Core: Contains business logic, use cases, and domain models, isolated from infrastructure concerns.
  - Adapters:
    - Primary Adapters: REST controllers that handle HTTP requests and responses.
    - Secondary Adapters: Implement the ports defined in the core, such as database repositories and more if
      required.
  - Ports (Interfaces): Define the operations required by the core, such as repositories and services.
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
docker-compose up -d
```

**Note**: If you want, you can change the default database connection credentials
in [application.yml](./src/main/resources/application.yml#L5-L9)
and in [docker-compose.yml](./docker-compose.yml#L5-L7) as needed before starting the application.

### 3. **Build & Run the Application**

```bash
  mvn clean install
  mvn spring-boot:run
```

The API will be available at: `http://localhost:8080`

---

## 🗂️ Project Structure

```
src/
  main/
    java/com/recipes/
      adapter/in/         # REST controllers & DTOs
      core/               # Domain, use cases, ports, exceptions
      adapter/out/        # Persistence adapters (not shown)
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
    "name": "Spaghetti Bolognese",
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
    "name": "Spaghetti Bolognese",
    "description": "A classic Italian pasta dish...",
    "ingredients": ["Spaghetti", "Beef", "Tomato sauce"],
    "instructions": ["Boil pasta", "Cook beef", "Mix sauce"],
    "vegetarian": false,
    "servingSize": 4
  }
  ```

---

### **Update Recipe**

`PATCH /v1/recipes/{id}`

- **Body**: Same as create (fields to update)
- **Response**: Updated recipe

---

### **Delete Recipe**

`DELETE /v1/recipes/{id}`

- **Response**: `204 No Content`

---

### **Search Recipes**

`GET /v1/recipes`

- **Query Parameters** (all optional):

  - `includedIngredients`: List of ingredients to include
  - `excludedIngredients`: List of ingredients to exclude
  - `instruction`: String to search in instructions
  - `vegetarian`: Boolean
  - `servingSize`: Integer
  - Pagination: `page` (default = 0 ), `size` (default = 20), `sort={fieldName},{ASC || DESC}`.

- **Response**: Paginated list of recipes

---

## 🧪 Testing

- Unit tests for domain, use cases, and controllers.
- Run all tests:

```bash

 mvn test

```
