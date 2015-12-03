## api - version 1.2

This is a small public api service that allows the users to query against the SiBBr's occurrence data portal information.

Basic queries implemented:
- Occurrence search given a scientific name
- Occurrence search filtered by resource given a scientific name
- Statistics
- Administrative user management
- Login

Information returned:
- List of all occurrences that match the filters, with only three fields for every record: an internal identifier (auto_id), that leads to the occurrence page in the [explorer](https://github.com/sibbr/explorador); decimallatitude (latitude), and decimal longitude (decimallongitude);

Database setup:
- Connect to database entering your database credentials directly to the DatabaseConnection class, under the connectToDatabaseOrDie() method.

Building application:
- mvn clean package

Running application:
- java -jar target/api-{version}.jar

Interface
- The application provides a basic html page on the root "/" that provides text information and examples for the api's usage.

Licence
- This code is provided as free software, under the terms of the GNU-GPL-v2. 

Dependencies
- Java 1.8
- Spring 4
- Spring boot 1.2.5.RELEASE
- Maven
- Thymeleaf
- Postgres JDBC driver 9.4-1202-jdbc42
- Jackson 0.9.5(for JSON support)
