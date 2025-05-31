# SEF Assignment 4 – Road Registry (Group 67)

##Contributor

**Gundeep Dhillon**  
Student ID: **s4102442**

---

##Task Overview – Person Class

This repository contains the implementation and unit testing of core functions related to the `Person` class in the Road Registry platform. My responsibility was focused on the implementation of the `addDemeritPoints()` function, which includes:

- Validating offense dates (format: `dd-MM-yyyy`)
- Validating demerit point range (1–6)
- Calculating total points from the last 2 years
- Applying suspension rules based on age:
  - Under 21: suspend if points > 6
  - 21 or older: suspend if points > 12
- Writing offense data to `demerits.txt`
- Returning `"Success"` or `"Failed"` accordingly

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
