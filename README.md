## api - version 1.1

This is a small public api service that allows the users to query against the SiBBr's occurrence data portal information.

Basic queries implemented:
- Occurrence search given a scientific name
   - scientificname: the basic taxonomic filter
   - fields: define the amount of fields to be returned, a more extense or a more reduced one.
   - ignoreNullCoordinates: define if the query should ignore records with null coordinates or not.
   - limit: define max. number of records to be returned by the query.
- Search occurrences by resource
- List available resources

Information returned:
- List of all occurrences that match the filters, amount of records returned, the amount of time it took the query to complete and the scientificname used as filter to the query;

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
