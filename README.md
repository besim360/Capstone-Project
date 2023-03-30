# Capstone Project
 Application for searching techincal writing articles over a database. 


# Local Development Environment Setup

## Download Keycloak Image

> 

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