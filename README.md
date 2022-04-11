# Depth Chart Exercise

### developer: Ranjaka De Mel

### Overview:
This app computes 4 mains scenarios of the depth chart as per the requirements.

Please refer to `exercise.txt` for full details.

### Usage 

To run some business logic implemented on the application so far, use `StartupConfig` class and your test methods. 
There is already a sample done for you..

This application uses h2 database for storage and config is in `application.yml` file

### Running app
To run in terminal, please compile code using `mvn`
then use `java -jar <depthchart-<version>.jar>`

Alternatively you can run in your favorite IDE as usual using maven

### developer notes 
This code enforces proper linting using spotless. To ensure the code does not fail to compile run spotless when 
running `mvn install` in the following way:
```bash
mvn spotless:apply clean install
```

To add a new sport with the correspodning positions use `createSport()` method

Application has the provision to add a controller hence some of the DTOs are already being added for future reference
