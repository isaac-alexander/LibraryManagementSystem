# Library Management System (Spring Boot)

This is a **Library Management System**  that allows:
- Admins to manage books (add, edit, delete)
- Users (students) to borrow and return books
- Tracking of borrowed books with **date and time**
- Detection of **overdue books**
- Role-based access control (ADMIN / STUDENT)

---

## Technologies Used
- Java (Spring Boot)
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- Bootstrap 5

---

## Setup Instructions / ## Cloning from GitHub

- Open a terminal or Git Bash
- Navigate to the folder where you want to clone the project
- Run the clone command (replace <repo-url> with your GitHub repository URL):
- git clone <repo-url>
- Navigate into the cloned folder:
- cd <project-folder-name>
- Open the project in your IDE  IntelliJ
- Configure the database connection in application.properties (see Database Configuration)

## Configure Database

Update your application.yml file with your MySQL details:
s
pring:
datasource:
url: jdbc:mysql://localhost:3306/library_db
username: root
password: your_password

jpa:
hibernate:
ddl-auto: update
show-sql: true
## full application.yml setup below

## Run the Application from the main class

App will start on:
http://localhost:9090


Default login accounts
Role        Username        Password
Admin       admin           12345
User        student         12345

## Features

## Admin
	•	Add new books
	•	Edit existing books
	•	Delete books
	•	View all borrowed books
	•	Return any book

## Student
	•	View all books
	•	Borrow books
	•	Return only books they borrowed
	•	View their borrowed books
	•	See overdue warnings

## Borrow System
	•	Books are borrowed for 5 minutes (for testing)
	•	After due time passes → marked as OVERDUE
	•	System shows warning for overdue books

- application.yml
  spring:
  application:
  name: library-management-system

  datasource:
  url: jdbc:mysql://localhost:3306/library_db?&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false
  username: your_username
  password: your_password


jpa:
database-platform: org.hibernate.dialect.MySQLDialect
hibernate:
ddl-auto: update

mvc:
hiddenmethod:
filter:
enabled: true

server:
error:
whitelabel:
enabled: false
port: 9090

logging:
level:
org:
hibernate:
SQL: DEBUG
