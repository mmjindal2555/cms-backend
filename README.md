# cms-backend

Steps to run CMS backend in localhost
Install MySQL (if not already present) - 
brew install mysql

Start MySQL Service - 
brew services start mysql

Access MySQL shell - 
mysql -u root -p

In MySQL shell, create the database - 
CREATE DATABASE cms;
EXIT;

Run the backend application - 
./gradlew bootRun

