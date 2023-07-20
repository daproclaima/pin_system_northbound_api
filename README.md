#Vodafone Ziggo Tech case interview test NOBOUR Sébastien

This document explains:
- [What is this project?](#what-is-this-project)
- [How does it work?](#how-does-it-work)
- [What is it made of and how to install it?](#what-is-it-made-of-and-how-to-install-it)
- [How to use it?](#how-to-use-it)

## What is this project?
It is a REST API built with Java, working as a northbound system, between an orchestrator directly 
communicating with a PIN terminal and a southbound system.
The northbound system will activate a PIN terminal for a customer through an orchestrator.

## How does it work?
This REST API only contains a main endpoint: ["/activate"](#/activate-endpoint)

### /activate endpoint
This endpoint only uses the POST method and only accepts two input parameters: 
a "customer id" representing a customer and a "MAC Address" associated with the PIN terminal, 
in a body in JSON format as following:
```json
{"customerId": "12345", "macAddress": "AA:BB:CC:DD:EE:FF"}
```
Where "customerId" corresponds to "customer id" and is of type string, and "macAddress" to a "MAC Address" 
also of type string. This endpoint will process the customer id and mac address it received by sending 
them to a mocked http server faking a southbound system. This endpoint is designed to be consumed by an 
orchestrator system. Here is the business logic of this endpoint:

#### 1 It receives the body {"customerId": "12345", "macAddress": "AA:BB:CC:DD:EE:FF"}
Then the resource is created and the endpoint returns:
```json
{
  "status": "ACTIVE",
  "code": 201
}
```

#### 2 It receives the body {"customerId": "12345", "macAddress": "AA:BB:CC:DD:EE:AA"}
Then no resource is found and the endpoint returns:
```json
{
  "status": "INACTIVE",
  "code": 404
}
```

#### 3 It receives the body {"customerId": "11111", "macAddress": "AA:BB:CC:DD:EE:FF"}
Then the resource is found but can not be used due to a conflict with the existing customer. 
Then the endpoint returns:
```json
{
  "status": "INACTIVE",
  "code": 409
}
```

## What is it made of and how to install it?

### What you will find in the project
As I love to automate things such as deployment and regression testing, the project runs inside a docker container built from a custom docker image based on 
[maven:3.9.3-amazoncorretto-20](https://hub.docker.com/layers/library/maven/3.9.3-amazoncorretto-20/images/sha256-87c489b1bb451f5cf8c17e8f88737c3d5fe3764e67b7a4822d58c9d23c33036d?context=explore).
It will ease the deployment and installation on our different windowsOS or macOS machines.
The Java version is 20.0.2 SE from openSDK and the Apache Maven version is 3.9.3.

### How do I intend to develop this project
I intend to :
- create the project in a docker container so that we do not need to install java ecosystem tools on our machines
- use TDD to work faster and in small increments easier to follow
- run code syntax linter and formatter at every commit
- add a continuous deployment pipeline through github actions, checking the syntax, running all unit tests in parallel 
- at every push on the repository and do the same for integration tests, creating an artifact once all the tests are 
green.

### How to install it?
1. First, you need to have installed a docker machine on your computer. You can use Docker Engine or Orbstack. 
Then start the docker machine. To create and read the code, debug the code, I used intelliJ.

2. Decide what directory you want to download the code from the code repository, open your command line interface, 
go in the directory, and download the code with the following command in your command line interface:

```shell
git clone git@github.com:daproclaima/pin_system_northbound_api.git
cd pin_system_northbound_api
```

3. At the root of the project, start the project container with this docker-compose command:

```shell
docker-compose build
docker-compose run pin_system_northbound_api
```

It will build the container and start the a /bin/bash client CLI in the container. Do not exit this CLI

4. Inside the container shell, test the installation with:
```shell
java --version
## It should return :
# openjdk 20.0.2 2023-07-18
# OpenJDK Runtime Environment Corretto-20.0.2.9.1 (build 20.0.2+9-FR)
# OpenJDK 64-Bit Server VM Corretto-20.0.2.9.1 (build 20.0.2+9-FR, mixed mode, sharing)

## And run
./mvnw --version
## It should return :
# Apache Maven 3.9.3 (21122926829f1ead511c958d89bd2f672198ae9f)
# Maven home: /usr/share/maven
# Java version: 20.0.2, vendor: Amazon.com Inc., runtime: /usr/lib/jvm/java-20-amazon-corretto
# Default locale: en, platform encoding: UTF-8
# OS name: "linux", version: "6.3.12-orbstack-00210-ga4f4fae8883e", arch: "amd64", family: "unix"
```

5. Inside the container shell, install the project dependencies by running 
```shell 
./mvnw clean install
```

## How to use it?

### Start the spring boot server
To start the server run `docker-compose run -p 8080:8080 pin_system_northbound_api ./mvnw spring-boot:run`. To exit the server, enter 'CTRL + C'.
To start the server with debbug mode run 
```shell
docker-compose run -p 8080:8080 pin_system_northbound_api ./mvnw spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8080"
```

### Requests to submit to the REST API endpoint
You will find a file test file com/vodafoneziggotechcasescreening/com/pin_system_northbound_api/southbound/activationTerminal/ActivationTerminalControllerTest.java. 
It contains the 3 requests to apply against the REST API server as integration tests. 
The "create" request sends the [first request](#1-it-receives-the-body-customerid-12345-macaddress-aabbccddeeff).
The "not found" request sends the [second request](#2-it-receives-the-body-customerid-12345-macaddress-aabbccddeeaa).
The "conflict" request sends the [third request](#3-it-receives-the-body-customerid-11111-macaddress-aabbccddeeff).

### How to stop the project and remove it?
Run the following docker commands in your computer CLI:
```shell
docker-compose stop
docker container list --all
```
Find the container id of the pin_system_northbound_api_pin_system_northbound_api image, and run the following:
```shell
docker container rm <container_id>
```

### Run the tests
To run the tests, use
```shell
docker-compose run pin_system_northbound_api ./mvnw test -Dtest='ActivationTerminalControllerTest'
```
You can run tests for the class `ActivationTerminalControllerTest` to test the controller with integration tests.
For the moment the 3 integration tests fail with receiving a 400 code error. 
I probably did not configure an abstract AbstractControllerTest