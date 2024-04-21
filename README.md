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
     java -jar target/log-parser.jar
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
- **Performance Optimization:** Optimize the parsing and analysis algorithms for large log files.
- **Error Handling:** Enhance error handling to provide meaningful messages to users.
- **Logging:** Implement logging functionality to record application events and errors.
- **Configuration:** Allow users to configure various aspects of the application, such as log file format and analysis options.