# CMS Backend

## Introduction
This repository contains the backend code for a Content Management System (CMS).

## Getting Started

### Prerequisites
- [MySQL](https://dev.mysql.com/doc/mysql-installation-excerpt/8.0/en/) (if not already installed)

### Installation and Setup

1. **Install MySQL:**
   If MySQL is not already present, install it using Homebrew:
   ```sh
   brew install mysql
   ```
2. **Start MySQL Service:**
   Start the MySQL service using Homebrew services:
    ```sh
   brew services start mysql
   ```
3. ***Access MySQL Shell:***
  Access the MySQL shell by running:
  ```sh
  mysql -u root -p
  ```
4. **Create CMS Database:**
  In the MySQL shell, create the database:
  ```sql
  CREATE DATABASE IF NOT EXISTS cms;
  EXIT;
  ```
5. **Run Backend Application:**
  Run the backend application using Gradle Wrapper:
  ```sh
  ./gradlew bootRun
  ```



