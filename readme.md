# 🍔 Hungrify – Backend Service

Hungrify is a **Spring Boot–based backend** for a modern food ordering platform, designed with an **AI-ready architecture**, clean domain separation, and production-grade APIs.

This repository focuses **only on backend services**. Frontend is intentionally excluded.

---

## 🚀 Key Features

- User, Restaurant, Delivery Partner & Admin management
- Menu & inventory management
- Cart and order lifecycle (place → prepare → deliver)
- Delivery assignment & tracking
- Ratings & reviews
- Admin & restaurant dashboards
- AI-assisted menu intelligence

---

## 🧠 AI Features

Hungrify is built with **AI-first data modeling** and pluggable intelligence.

- **Ingredient Parsing (AI-assisted)**  
  Converts free-text dish descriptions into structured ingredients, allergens, and tags.  
  `POST /restaurants/menu/parse-ingredients`

- **Allergen & Dietary Intelligence**
  - Auto-derived flags: vegan, gluten-free, nut-free, etc.
  - Ingredient removability & substitution awareness

- **Nutrition & Taste Profiling**
  - Calories, macros, spice score, taste profile
  - Enables smart filtering & future recommendations

- **AI-Ready Design**
  - JSON-based feature storage
  - Service layer is model-agnostic
  - Easily extensible to LLMs (OpenAI, Gemini, LLaMA)

> Current implementation uses deterministic logic, designed to upgrade seamlessly to ML/LLMs.

---

## 🏗️ Tech Stack

- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- MySQL
- Swagger / OpenAPI 3.0
- Maven

---

## 🗄️ Database Highlights

- Relational schema with strategic JSON fields
- Item snapshots preserve historical order accuracy
- Ingredient-level allergen modeling
- Optimized for both transactions & analytics

Core tables:  
`users`, `restaurants`, `food_items`, `food_item_profiles`, `ingredients`,  
`orders`, `order_items`, `deliveries`, `reviews`

---

## 📮 API Documentation

- **Swagger / OpenAPI**
  `/openapi/hungrifySwagger.yml`

- **Postman Collection**
  - Included in the repository
  - Covers auth, menu, orders, deliveries, admin & AI endpoints
  - Ready to import and test

---

## ▶️ Run Locally

### Database
```sql
CREATE DATABASE hungrify_db;
USE hungrify_db;
SOURCE sampledata.sql;
```

### Backend
```bash
mvn clean install
mvn spring-boot:run
```

### Base URL
```
http://localhost:8080
```

---

## 🔐 Security

- JWT-based authentication
- Role-based authorization
- Soft deletes & audit timestamps
- Secure order & payment handling

---

## 📄 License

MIT License
