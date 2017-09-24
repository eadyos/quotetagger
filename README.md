# QuoteTagger
Spring Boot rest api for storing/retrieving famous quotes and tagging them with labels

The service is online and [available for demo here](https://afternoon-bayou-23485.herokuapp.com/swagger-ui.html)

I am currently working on a front-end application to use the api as part of a new project.  It should get published soon.

Functionality includes:

1) CRUD operations on Quotes and Tags
2) Ability to create and remove associations between Quotes and Tags
3) The ability to search for a Tag by name.

## About

This rest api web service was built using Spring Boot and Gradle on JDK 1.8.  It uses a simple in-memory H2/SQL database for storage.

To run with gradle, clone the project and then run:

**gradle bootRun**

## Swagger

Documentation for the web-service was built with [Swagger](https://swagger.io)

When run locally, it can be viewed at http://localhost:8080/swagger-ui.html
