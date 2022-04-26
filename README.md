A5 Repo for CS6310 Fall 2021 Group 90
# To Install Docker go to:
```
https://docs.docker.com/get-docker/
```


## Assignment 5 - Instructions

### Requirements
- Docker installed
- Maven installed (to implement and run locally)

### How to Run
#### Download dependencies with Maven
In a terminal window, run:
```
mvn clean package
```
The above command will:
1) Clean maven dependencies and compiled sources
2) Generate a Jar with all executables and dependencies
   1) The above jar should contain the same files which are containerized with Docker, in order to see the contents in the Jar run: `jar xf <package.jar>` 

#### Run from Docker
##### Starting Containers
- Make sure Docker is running locally
- In a terminal window (located in the same directory of the Dockerfile and docker-compose.yml) run:
  - `docker-compose build`
  - `docker-compose run app-server`
- Notes:
  - In some occasions, the database container will not execute changes done in either the sql/create_tables.sql or sql/fill_tables.sql. This could be that the previous container's volume is still present, and docker will not replace it. In this situation, try running:
    - `docker-compose down`
    - `docker volume rm <volume_name>`
    - `docker-compose up --build -d`
    - `docker-compose run database` (in one terminal - this process could be killed after succeeded)
    - `docker-compose run app-server` (in another terminal)

##### Stopping Containers
- Simply stop the command with Ctrl+C or Ctrl+D
- Once stopped, consider running `docker-compose down` to be safe the every thing is stopped correctly

##### See all running containers
```
docker-compose ps
```

or

```
docker ps
```

##### Verifying the Database
- Verify the postgres container is running
- Run `docker exec -it <postgres_container_id> bash`
- Once in the container, run:
  - `psql -U postgres`
  - To see all tables, run `\d`
  - Make any desired CRUD operation, i.e `SELECT * FROM table`

