# Payara Engineering Task

## How To Run the Application

1. **Prerequisites:**
   - Ensure you have Java 21 installed on your system.
   - Clone the repository to your local machine.

2. **Compilation:**
   - Navigate to the root directory of the project.
   - Compile the application using Maven:
     ```
     mvn clean package
     ```

3. **Execution:**
   - Run the application using the generated JAR file:
     ```
     java -jar target/logparser-1.0-SNAPSHOT.jar
     ```

## How to Run the Tests for the Application

1. **Prerequisites:**
   - Ensure you have Java 21 and Maven installed on your system.
   - Clone the repository to your local machine.

2. **Running Tests:**
   - Navigate to the root directory of the project.
   - Run the tests using Maven:
     ```
     mvn test
     ```

## How to Generate the HTML Report

- After running the application, the HTML report will be generated automatically.
- Open the `report.html` file using a web browser to view the report.

## Further Improvements for the Application

To further improve the application before distribution, consider implementing the following enhancements:

- **User Interface:** Develop a user-friendly interface for better interaction.
- **Configurability:** Allow users to specify log file paths dynamically to enhance flexibility.
- **Increase Unit Test Coverage:** Write more unit tests to validate functionality and ensure code quality.
