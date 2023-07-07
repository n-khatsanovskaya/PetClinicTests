# PetClinicTests

This repository contains automated tests for the Pet Clinic API. The Pet Clinic API serves as a backend system for managing veterinary clinics and their associated data. The goal of this project is to automate various test cases to ensure the API functions correctly.

## Table of Contents
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Tests](#running-the-tests)
- [Generating Allure Report](#generating-allure-report)

## Getting Started

To get started with these automated tests, follow the instructions below.

## Prerequisites

Make sure you have the following prerequisites installed on your system:
- Java Development Kit (JDK) - version 11
- Maven - latest version

## Installation
1. Clone the repository:

```
git clone https://github.com/n-khatsanovskaya/PetClinicTests.git
```

2. Navigate to the project directory:
```
cd PetClinicTests
```
3. Install the project dependencies using Maven:
```
mvn clean install
```
## Running the Tests
To run the automated tests, execute the following command:

```
mvn test
```
The tests will be executed, and the results will be displayed in the console.

## Generating Allure Report
To generate an Allure report for the test execution, follow the steps below:

1. Ensure you have Allure Command Line (CLI) installed on your system.

2. Navigate to the project directory.

3. Run the following command to generate the Allure report:

```
allure serve target/allure-results
```
The Allure report will be generated and displayed in your default browser