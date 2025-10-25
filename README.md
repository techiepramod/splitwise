# Expense Sharing Backend

A secure, scalable backend system for managing shared expenses across multiple groups. Built with Spring Boot, JPA, and Google OAuth2, it supports role-based access, transactional integrity, and precise financial calculations.

---

## Features

- Google OAuth2 login
- Role-based access (USER, ADMIN)
- Expense creation with custom splits
- Balance computation from transactions
- Settlement handling with transactional safety
- Admin-only operations (delete group, reset balances)
- Precision with BigDecimal for financial accuracy

---

## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security (OAuth2)
- JPA + Hibernate
- H2
- Maven
- Postman (for API testing)

---

## Configure Google OAuth2
spring:
    security:
        oauth2:
            client:
                registration:
                    google:
                        client-id: YOUR_CLIENT_ID
                        client-secret: YOUR_CLIENT_SECRET