# SEF Assignment 4 – Road Registry (Group 67)

##Contributor

**Gundeep Dhillon**  
Student ID: **s4102442**

**Yee Man Chan**  
Student ID: **s4024211**

**Tuong Duy Duong**
Student ID: **s3479994**

**Ko Yun Chu**
Student ID: **s4076701**

---

##Task Overview – Person Class

This repository contains the implementation and unit testing of core functions related to the Person class in the Road Registry platform. The implementation includes three main functions with comprehensive validation and testing.

Functions Implemented

1. addPerson()
Purpose: Adds a new person to the system by storing their information in a TXT file.

Key Features:
- Validates person ID format (10 characters: 2 digits [2-9], 6 characters with ≥2 special chars, 2 uppercase letters)
- Validates name format (1-50 characters, letters/spaces/hyphens/apostrophes only)
- Validates address format (Street Number|Street|City|State|Country, State must be Victoria)
- Validates birthdate format (DD-MM-YYYY, not in future, not >100 years old)
- Prevents duplicate person IDs
- Stores data in comma-separated format: ID,FirstName,LastName,Address,Birthday,0,false
- Returns true if successful, false otherwise

2. updatePersonalDetails()
Purpose: Updates existing person's details in the TXT file with specific business rules.

Key Features:
- Updates person ID, firstName, lastName, address, and birthdate
- Maintains demerit points and suspension status
- Business Rules:
  - Under 18: address cannot be changed
  - If birthday changes: no other details can be changed
  - If person ID starts with even number: ID cannot be changed
- Comprehensive validation for all updated fields
- Returns true if successful, false otherwise

3. addDemeritPoints()
Purpose: Records demerit points for traffic offenses and manages suspension status.

Key Features:
- Validating offense dates (format: `dd-MM-yyyy`)
- Validating demerit point range (1–6)
- Calculating total points from the last 2 years
- Applying suspension rules based on age:
  - Under 21: suspend if points > 6
  - 21 or older: suspend if points > 12
- Writing offense data to `demerits.txt`
- Returning `"Success"` or `"Failed"` accordingly


Validation Methods
The implementation includes comprehensive validation methods:
- isValidPersonID() - Validates 10-character person ID format
- isValidName() - Validates name format and length
- isValidAddress() - Validates address format with Victoria state requirement
- isValidBirthdate() - Validates date format and reasonable date range
- calculateAge() - Calculates age from birthdate
- isFirstEven() - Checks if first character of ID is even number
- personExists() - Prevents duplicate person entries

File Structure

person.txt: Stores person data (comma-separated)
demerits.txt: Stores offense records
Person.java: Main class implementation
Test files: JUnit test cases for all functions
---

##Key Features

- **Java 8**
- **JUnit 5 Unit Testing**
- **Maven Project Structure**
- **GitHub Actions Integration** for continuous testing

---

##Unit Testing

Implemented **5 test cases** using JUnit 5 for:
- Valid input (under and over 21)
- Invalid date formats
- Invalid point values
- Suspension threshold crossing

Tests are located in:
