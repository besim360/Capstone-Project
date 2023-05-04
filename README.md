# Capstone Project
 Application for searching techincal writing articles over a database. 


# Local Development Environment Setup

## Download Keycloak Image

>  docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:21.0.1 start-dev

## Development Environment Frontend Setup

```bash

$ cd frontend

$ npm install

$ quasar dev

```

## MongoDB Local Setup and Instructions

```bash

$ cd middle/mongodb-docker

```

Pull latest docker image for mongodb
    - docker pull mongo:latest
Run image
    - docker run -d -p 2717:27017 -v ~/mongodb-docker:/data/db --name mymongo mongo

Check to make sure docker container is running
    - docker ps

Setup MongoDB with our DB and Collections 
    - run dbStartupScript.py in config folder

Now youre good to go! 

To see the collections and data you can run commands through cli or download the mongo db extention on Visual Studio
    - If you decided to use extension, connect to the database with the connection string "mongodb://localhost:2717"

## Running the API Dev Mode

uvicorn index:app --reload

## Backend Local Setup and Instructions
- Open project in IntelliJ or install Maven
- Download dependencies through Maven
- Either download PDFs and database through Murphy's Dropbox or get them through the discord and extract them to your backend/spring/ directory
- If building database index from scratch, uncomment the two beans in Application.java
- If database index is already present, ensure it is not rebuilt as it is currently additive and leaving the beans uncommented can double the size of the database every time
- Run spring-boot:run command through IntelliJ or Maven
- NOTE: IntelliJ is preconfigured for JDK version 19.0, although later versions should not have any issues
- WARNING: Make sure no existing version of /data/ or /database/ is present in /backend/spring/ before extracting an archived version of the database otherwise corruption errors may occur
