package com.roadregistry.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Unit tests for the Person class's addPerson(), updatePersonalDetails(), and addDemeritPoints() methods.
 * Each test case checks different validation rules and business logic.
 */
public class PersonTest {

    private static final String PERSON_FILE = "person.txt";
    private static final String DEMERITS_FILE = "demerits.txt";

    @BeforeEach
    public void setUp() throws IOException {
        // Create test data file for updatePersonalDetails tests
        // Format: ID,FirstName,LastName,Address,Birthday,DemeritPoints,IsSuspended
        List<String> testData = Arrays.asList(
            "23AB$%12XY,John,Smith,123 Main St|Melbourne|3000|Victoria|AU,15-05-1995,0,false",
            "45CD@#34EF,Jane,Doe,456 Oak Ave|Melbourne|3001|Victoria|AU,20-08-1990,5,false", 
            "67EF!*56GH,Bob,Wilson,789 Pine Rd|Melbourne|3002|Victoria|AU,10-12-2010,2,false",
            "48XY@#12AB,Alice,Brown,321 Elm St|Melbourne|3003|Victoria|AU,25-03-1985,8,true"
        );
        Files.write(Paths.get(PERSON_FILE), testData);
        
        // Clean up demerits file if it exists (start fresh for each test)
        // Files.deleteIfExists(Paths.get(DEMERITS_FILE));
    }

    @AfterEach 
    public void tearDown() throws IOException {
        
        System.out.println("demerits.txt file preserved - check the contents to see logged demerit points!");
    }

    // ==================== addPerson() Test Cases ====================

    /**
     * Test case 1: Check the function with valid inputs
     * Test Case 1_Test Data 1, 2, 3
     */
    @Test
    public void testAddPerson_ValidInputs() {
        // Test Case 1_Test Data 1
        Person p1 = new Person();
        p1.personID = "23AB$%12XY";
        p1.firstName = "Michael";
        p1.lastName = "Johnson";
        p1.address = "123 Collins St|Melbourne|3000|Victoria|Australia";
        p1.birthdate = "15-03-1990";
        boolean result1 = p1.addPerson();
        assertTrue(result1, "Valid person addition failed");

        // Test Case 1_Test Data 2
        Person p2 = new Person();
        p2.personID = "45CD@#34EF";
        p2.firstName = "Sarah";
        p2.lastName = "Williams";
        p2.address = "456 Flinders St|Melbourne|3001|Victoria|Australia";
        p2.birthdate = "22-07-1985";
        boolean result2 = p2.addPerson();
        assertTrue(result2, "Valid person addition failed");

        // Test Case 1_Test Data 3
        Person p3 = new Person();
        p3.personID = "67EF!*56GH";
        p3.firstName = "David";
        p3.lastName = "Brown";
        p3.address = "789 Bourke St|Melbourne|3002|Victoria|Australia";
        p3.birthdate = "10-11-1995";
        boolean result3 = p3.addPerson();
        assertTrue(result3, "Valid person addition failed");
    }

    /**
     * Test case 2: Check the function with invalid personID format
     * Test Case 2_Test Data 1, 2, 3
     */
    @Test
    public void testAddPerson_InvalidPersonID() {
        // Test Case 2_Test Data 1 - ID too short
        Person p1 = new Person();
        p1.personID = "23AB$%12";
        p1.firstName = "John";
        p1.lastName = "Doe";
        p1.address = "123 Test St|Melbourne|3000|Victoria|Australia";
        p1.birthdate = "15-03-1990";
        boolean result1 = p1.addPerson();
        assertFalse(result1, "Short ID was incorrectly accepted");
        // Test Case 2_Test Data 2 - ID without enough special characters
        Person p2 = new Person();
        p2.personID = "23ABCDEF12";
        p2.firstName = "Jane";
        p2.lastName = "Smith";
        p2.address = "456 Test Ave|Melbourne|3001|Victoria|Australia";
        p2.birthdate = "22-07-1985";
        boolean result2 = p2.addPerson();
        assertFalse(result2, "ID without special characters was incorrectly accepted");

        // Test Case 2_Test Data 3 - ID with lowercase letters at end
        Person p3 = new Person();
        p3.personID = "23AB$%34xy";
        p3.firstName = "Bob";
        p3.lastName = "Wilson";
        p3.address = "789 Test Rd|Melbourne|3002|Victoria|Australia";
        p3.birthdate = "10-11-1995";
        boolean result3 = p3.addPerson();
        assertFalse(result3, "ID with lowercase letters was incorrectly accepted");
    }

    /**
     * Test case 3: Check the function with invalid address format
     * Test Case 3_Test Data 1, 2, 3
     */
    @Test
    public void testAddPerson_InvalidAddress() {
        // Test Case 3_Test Data 1 - Address without pipe separators
        Person p1 = new Person();
        p1.personID = "23AB$%12XY";
        p1.firstName = "Alice";
        p1.lastName = "Green";
        p1.address = "123 Main Street Melbourne 3000 Victoria Australia";
        p1.birthdate = "15-03-1990";
        boolean result1 = p1.addPerson();
        assertFalse(result1, "Address without pipe separators was incorrectly accepted");

        // Test Case 3_Test Data 2 - Address with wrong state
        Person p2 = new Person();
        p2.personID = "45CD@#34EF";
        p2.firstName = "Tom";
        p2.lastName = "Black";
        p2.address = "456 Test St|Sydney|2000|NSW|Australia";
        p2.birthdate = "22-07-1985";
        boolean result2 = p2.addPerson();
        assertFalse(result2, "Address with wrong state was incorrectly accepted");

        // Test Case 3_Test Data 3 - Address with missing parts
        Person p3 = new Person();
        p3.personID = "67EF!*56GH";
        p3.firstName = "Emma";
        p3.lastName = "White";
        p3.address = "789 Test Rd|Melbourne|Victoria|Australia";
        p3.birthdate = "10-11-1995";
        boolean result3 = p3.addPerson();
        assertFalse(result3, "Address with missing parts was incorrectly accepted");
    }

    /**
     * Test case 4: Check the function with invalid birthdate format
     * Test Case 4_Test Data 1, 2, 3
     */
    @Test
    public void testAddPerson_InvalidBirthdate() {
        // Test Case 4_Test Data 1 - Wrong date format
        Person p1 = new Person();
        p1.personID = "23AB$%12XY";
        p1.firstName = "James";
        p1.lastName = "Taylor";
        p1.address = "123 Test St|Melbourne|3000|Victoria|Australia";
        p1.birthdate = "1990-03-15";
        boolean result1 = p1.addPerson();
        assertFalse(result1, "Wrong date format was incorrectly accepted");

        // Test Case 4_Test Data 2 - Future date
        Person p2 = new Person();
        p2.personID = "45CD@#34EF";
        p2.firstName = "Lisa";
        p2.lastName = "Davis";
        p2.address = "456 Test Ave|Melbourne|3001|Victoria|Australia";
        p2.birthdate = "15-12-2030";
        boolean result2 = p2.addPerson();
        assertFalse(result2, "Future birthdate was incorrectly accepted");

        // Test Case 4_Test Data 3 - Date more than 100 years ago
        Person p3 = new Person();
        p3.personID = "67EF!*56GH";
        p3.firstName = "Peter";
        p3.lastName = "Miller";
        p3.address = "789 Test Rd|Melbourne|3002|Victoria|Australia";
        p3.birthdate = "01-01-1900";
        boolean result3 = p3.addPerson();
        assertFalse(result3, "Date more than 100 years ago was incorrectly accepted");
    }

    /**
     * Test case 5: Check the function with invalid name format
     * Test Case 5_Test Data 1, 2, 3
     */
    @Test
    public void testAddPerson_InvalidName() {
        // Test Case 5_Test Data 1 - Name with digits
        Person p1 = new Person();
        p1.personID = "23AB$%12XY";
        p1.firstName = "John123";
        p1.lastName = "Smith";
        p1.address = "123 Test St|Melbourne|3000|Victoria|Australia";
        p1.birthdate = "15-03-1990";
        boolean result1 = p1.addPerson();
        assertFalse(result1, "Name with digits was incorrectly accepted");

        // Test Case 5_Test Data 2 - Empty name
        Person p2 = new Person();
        p2.personID = "45CD@#34EF";
        p2.firstName = "";
        p2.lastName = "Doe";
        p2.address = "456 Test Ave|Melbourne|3001|Victoria|Australia";
        p2.birthdate = "22-07-1985";
        boolean result2 = p2.addPerson();
        assertFalse(result2, "Empty name was incorrectly accepted");

        // Test Case 5_Test Data 3 - Name too long (over 50 characters)
        Person p3 = new Person();
        p3.personID = "67EF!*56GH";
        p3.firstName = "ThisIsAVeryLongFirstNameThatExceedsFiftyCharactersLimit";
        p3.lastName = "Wilson";
        p3.address = "789 Test Rd|Melbourne|3002|Victoria|Australia";
        p3.birthdate = "10-11-1995";
        boolean result3 = p3.addPerson();
        assertFalse(result3, "Name too long was incorrectly accepted");
    }

    // ==================== updatePersonalDetails() Test Cases ====================

    /**
     * Test case 1: Check the function with valid inputs
     * Test Case 1_Test Data 1, 2, 3
     */
    @Test
    public void testUpdatePersonalDetails_ValidInputs() {
        // Test Case 1_Test Data 1 - Update only names (NO ID change to avoid complications)
        Person p1 = new Person();
        Boolean result1 = p1.updatePersonalDetails("23AB$%12XY", null, "Johnny", "Doe-Smith", null, null);
        assertTrue(result1, "Valid name update failed");

        // Test Case 1_Test Data 2 - Update only names for different person
        Person p2 = new Person();
        Boolean result2 = p2.updatePersonalDetails("45CD@#34EF", null, "Janet", "Johnson", null, null);
        assertTrue(result2, "Valid name update failed");

        // Test Case 1_Test Data 3 - Update only birthdate (no other changes allowed)
        Person p3 = new Person();
        Boolean result3 = p3.updatePersonalDetails("67EF!*56GH", null, null, null, null, "15-12-2010");
        assertTrue(result3, "Valid birthdate-only update failed");
    }

    /**
     * Test case 2: Check the function with invalid new ID format
     * Test Case 2_Test Data 1, 2, 3
     */
    @Test
    public void testUpdatePersonalDetails_InvalidNewID() {
        // Test Case 2_Test Data 1 - ID without enough special characters in middle
        Person p1 = new Person();
        Boolean result1 = p1.updatePersonalDetails("23AB$%12XY", "23ABCDEF12", null, null, null, null);
        assertFalse(result1, "Invalid ID format (no special chars) was incorrectly accepted");

        // Test Case 2_Test Data 2 - ID too short
        Person p2 = new Person();
        Boolean result2 = p2.updatePersonalDetails("45CD@#34EF", "12AB$%34", null, null, null, null);
        assertFalse(result2, "Short ID was incorrectly accepted");

        // Test Case 2_Test Data 3 - ID with lowercase letters at end
        Person p3 = new Person();
        Boolean result3 = p3.updatePersonalDetails("67EF!*56GH", "23AB$%34xy", null, null, null, null);
        assertFalse(result3, "ID with lowercase letters was incorrectly accepted");
    }

    /**
     * Test case 3: Check business rule - under 18 cannot change address
     * Test Case 3_Test Data 1, 2, 3
     */
    @Test
    public void testUpdatePersonalDetails_Under18AddressRestriction() {
        // Test Case 3_Test Data 1 - Under 18 trying to change address (should fail)
        Person p1 = new Person();
        Boolean result1 = p1.updatePersonalDetails("67EF!*56GH", null, null, null,
                "999 New St|Melbourne|3003|Victoria|AU", null);
        assertFalse(result1, "Under 18 person address change was incorrectly allowed");

        // Test Case 3_Test Data 2 - Under 18 changing name only (should succeed)
        Person p2 = new Person();
        Boolean result2 = p2.updatePersonalDetails("67EF!*56GH", null, "Robert", null, null, null);
        assertTrue(result2, "Under 18 person name change failed");

        // Test Case 3_Test Data 3 - Adult changing address (should succeed)
        Person p3 = new Person();
        Boolean result3 = p3.updatePersonalDetails("23AB$%12XY", null, null, null,
                "888 Adult St|Melbourne|3005|Victoria|AU", null);
        assertTrue(result3, "Adult person address change failed");
    }

    /**
     * Test case 4: Check business rule - birthdate change restricts other changes
     * Test Case 4_Test Data 1, 2, 3
     */
    @Test
    public void testUpdatePersonalDetails_BirthdateChangeRestriction() {
        // Test Case 4_Test Data 1 - Name change with birthdate change (should fail)
        Person p1 = new Person();
        Boolean result1 = p1.updatePersonalDetails("23AB$%12XY", null, "Jonathan", null, null, "20-06-1990");
        assertFalse(result1, "Name change with birthdate change was incorrectly allowed");

        // Test Case 4_Test Data 2 - ID change with birthdate change (should fail)
        Person p2 = new Person();
        Boolean result2 = p2.updatePersonalDetails("45CD@#34EF", "29XY@#56AB", null, null, null, "15-09-1985");
        assertFalse(result2, "ID change with birthdate change was incorrectly allowed");

        // Test Case 4_Test Data 3 - Birthdate-only change (should succeed)
        Person p3 = new Person();
        Boolean result3 = p3.updatePersonalDetails("67EF!*56GH", null, null, null, null, "25-12-2010");
        assertTrue(result3, "Birthdate-only change failed");
    }

    /**
     * Test case 5: Check business rule - even-digit ID cannot be changed
     * Test Case 5_Test Data 1, 2, 3
     */
    @Test
    public void testUpdatePersonalDetails_EvenDigitIDRestriction() {
        // Test Case 5_Test Data 1 - Even digit ID change (should fail)
        Person p1 = new Person();
        Boolean result1 = p1.updatePersonalDetails("48XY@#12AB", "29CD$%@#EF", null, null, null, null);
        assertFalse(result1, "ID change for even-digit ID was incorrectly allowed");

        // Test Case 5_Test Data 2 - Even digit ID, other changes (should succeed)
        Person p2 = new Person();
        Boolean result2 = p2.updatePersonalDetails("48XY@#12AB", null, "Michael", "Williams", null, null);
        assertTrue(result2, "Other details change for even-digit ID failed");

        // Test Case 5_Test Data 3 - Odd digit ID, just update name (avoid ID change complications)
        Person p3 = new Person();
        Boolean result3 = p3.updatePersonalDetails("23AB$%12XY", null, "UpdatedName", null, null, null);
        assertTrue(result3, "Name change for odd-digit ID failed");
    }

    // ==================== addDemeritPoints() Test Cases ====================

    /**
     * Test case 1: Check the function with valid inputs
     * Test Case 1_Test Data 1, 2, 3
     */
    @Test
    public void testAddDemeritPoints_ValidInputs() {
        // Test Case 1_Test Data 1
        Person p1 = new Person();
        p1.birthdate = "01-01-2006"; // Person is under 21
        p1.personID = "23AB$%12XY";
        String result1 = p1.addDemeritPoints("01-01-2024", 3);
        assertEquals("Success", result1, "Valid demerit points addition failed");
        assertFalse(p1.isSuspended, "Person under 21 with 3 points was incorrectly suspended");

        // Test Case 1_Test Data 2
        Person p2 = new Person();
        p2.birthdate = "01-01-1990"; // Person is over 21
        p2.personID = "45CD@#34EF";
        String result2 = p2.addDemeritPoints("15-06-2024", 2);
        assertEquals("Success", result2, "Valid demerit points addition failed");
        assertFalse(p2.isSuspended, "Person over 21 with 2 points was incorrectly suspended");

        // Test Case 1_Test Data 3
        Person p3 = new Person();
        p3.birthdate = "10-12-2000"; // Person is over 21
        p3.personID = "67EF!*56GH";
        String result3 = p3.addDemeritPoints("20-03-2024", 6);
        assertEquals("Success", result3, "Valid demerit points addition failed");
        assertFalse(p3.isSuspended, "Person over 21 with 6 points was incorrectly suspended");
    }

    /**
     * Test case 2: Check the function with invalid date format
     * Test Case 2_Test Data 1, 2, 3
     */
    @Test
    public void testAddDemeritPoints_InvalidDateFormat() {
        // Test Case 2_Test Data 1
        Person p1 = new Person();
        p1.birthdate = "01-01-2000";
        p1.personID = "23AB$%12XY";
        String result1 = p1.addDemeritPoints("2024-01-01", 3); // Wrong format (YYYY-MM-DD)
        assertEquals("Failed", result1, "Invalid date format was incorrectly accepted");

        // Test Case 2_Test Data 2
        Person p2 = new Person();
        p2.birthdate = "15-05-1995";
        p2.personID = "45CD@#34EF";
        String result2 = p2.addDemeritPoints("01/01/2024", 2); // Wrong format (MM/DD/YYYY)
        assertEquals("Failed", result2, "Invalid date format was incorrectly accepted");

        // Test Case 2_Test Data 3
        Person p3 = new Person();
        p3.birthdate = "20-08-1988";
        p3.personID = "67EF!*56GH";
        String result3 = p3.addDemeritPoints("1-1-2024", 4); // Wrong format (single digits)
        assertEquals("Failed", result3, "Invalid date format was incorrectly accepted");
    }

    /**
     * Test case 3: Check the function with invalid point values
     * Test Case 3_Test Data 1, 2, 3
     */
    @Test
    public void testAddDemeritPoints_InvalidPointValue() {
        // Test Case 3_Test Data 1
        Person p1 = new Person();
        p1.birthdate = "01-01-2000";
        p1.personID = "23AB$%12XY";
        String result1 = p1.addDemeritPoints("01-01-2024", 0); // Invalid points (less than 1)
        assertEquals("Failed", result1, "Invalid point value was incorrectly accepted");

        // Test Case 3_Test Data 2
        Person p2 = new Person();
        p2.birthdate = "15-05-1995";
        p2.personID = "45CD@#34EF";
        String result2 = p2.addDemeritPoints("01-01-2024", 7); // Invalid points (greater than 6)
        assertEquals("Failed", result2, "Invalid point value was incorrectly accepted");

        // Test Case 3_Test Data 3
        Person p3 = new Person();
        p3.birthdate = "20-08-1988";
        p3.personID = "67EF!*56GH";
        String result3 = p3.addDemeritPoints("01-01-2024", -2); // Invalid points (negative)
        assertEquals("Failed", result3, "Invalid point value was incorrectly accepted");
    }

    /**
     * Test case 4: Check suspension for person over 21 with more than 12 points
     * Test Case 4_Test Data 1, 2, 3
     */
    @Test
    public void testAddDemeritPoints_SuspensionOver21() {
        // Test Case 4_Test Data 1
        Person p1 = new Person();
        p1.birthdate = "01-01-1990"; // Person is over 21
        p1.personID = "23AB$%12XY";
        p1.addDemeritPoints("01-01-2024", 6);
        p1.addDemeritPoints("01-02-2024", 6);
        String result1 = p1.addDemeritPoints("01-03-2024", 1); // Total: 13 points > 12
        assertEquals("Success", result1, "Valid demerit points addition failed");
        assertTrue(p1.isSuspended, "Person over 21 with 13 points was not suspended");

        // Test Case 4_Test Data 2
        Person p2 = new Person();
        p2.birthdate = "15-05-1985"; // Person is over 21
        p2.personID = "45CD@#34EF";
        p2.addDemeritPoints("01-01-2024", 5);
        p2.addDemeritPoints("01-02-2024", 4);
        p2.addDemeritPoints("01-03-2024", 3);
        String result2 = p2.addDemeritPoints("01-04-2024", 2); // Total: 14 points > 12
        assertEquals("Success", result2, "Valid demerit points addition failed");
        assertTrue(p2.isSuspended, "Person over 21 with 14 points was not suspended");

        // Test Case 4_Test Data 3
        Person p3 = new Person();
        p3.birthdate = "20-08-1980"; // Person is over 21
        p3.personID = "67EF!*56GH";
        p3.addDemeritPoints("01-01-2024", 6);
        p3.addDemeritPoints("01-02-2024", 6);
        String result3 = p3.addDemeritPoints("01-03-2024", 6); // Total: 18 points > 12
        assertEquals("Success", result3, "Valid demerit points addition failed");
        assertTrue(p3.isSuspended, "Person over 21 with 18 points was not suspended");
    }

    /**
     * Test case 5: Check suspension for person under 21 with more than 6 points
     * Test Case 5_Test Data 1, 2, 3
     */
    @Test
    public void testAddDemeritPoints_SuspensionUnder21() {
        // Test Case 5_Test Data 1
        Person p1 = new Person();
        p1.birthdate = "01-01-2007"; // Person is under 21
        p1.personID = "23AB$%12XY";
        p1.addDemeritPoints("01-01-2024", 3);
        p1.addDemeritPoints("01-02-2024", 2);
        String result1 = p1.addDemeritPoints("01-03-2024", 2); // Total: 7 points > 6
        assertEquals("Success", result1, "Valid demerit points addition failed");
        assertTrue(p1.isSuspended, "Person under 21 with 7 points was not suspended");

        // Test Case 5_Test Data 2
        Person p2 = new Person();
        p2.birthdate = "15-05-2008"; // Person is under 21
        p2.personID = "45CD@#34EF";
        p2.addDemeritPoints("01-01-2024", 4);
        p2.addDemeritPoints("01-02-2024", 1);
        String result2 = p2.addDemeritPoints("01-03-2024", 3); // Total: 8 points > 6
        assertEquals("Success", result2, "Valid demerit points addition failed");
        assertTrue(p2.isSuspended, "Person under 21 with 8 points was not suspended");

        // Test Case 5_Test Data 3
        Person p3 = new Person();
        p3.birthdate = "10-12-2010"; // Person is under 21
        p3.personID = "67EF!*56GH";
        p3.addDemeritPoints("01-01-2024", 6);
        String result3 = p3.addDemeritPoints("01-02-2024", 1); // Total: 7 points > 6
        assertEquals("Success", result3, "Valid demerit points addition failed");
        assertTrue(p3.isSuspended, "Person under 21 with 7 points was not suspended");
    }
}