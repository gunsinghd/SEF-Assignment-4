package com.roadregistry.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Unit tests for the Person class's addDemeritPoints() method.
 * Each test case checks different validation rules and suspension logic.
 */
public class PersonTest {

    /**
     * Test case: Adding valid demerit points for a person under 21.
     * Expectation: Points added successfully and person is not suspended.
     */
    @Test
    public void testValidDemeritPointsUnder21_NoSuspension() {
        Person p = new Person();
        p.birthdate = "01-01-2006"; // Person is under 21
        p.personID = "99ABCDEF";
        p.demeritPoints = new HashMap<>();
        String result = p.addDemeritPoints("01-01-2024", 3); // Valid date and point
        assertEquals("Success", result); // Should succeed
        assertFalse(p.isSuspended); // Points < 6, should not be suspended
    }

    /**
     * Test case: Providing an invalid date format to the method.
     * Expectation: The method should return "Failed".
     */
    @Test
    public void testInvalidDateFormat() {
        Person p = new Person();
        p.birthdate = "01-01-2000";
        p.personID = "88XYZLMN";
        p.demeritPoints = new HashMap<>();
        String result = p.addDemeritPoints("2024-01-01", 3); // Wrong format (YYYY-MM-DD)
        assertEquals("Failed", result); // Should fail due to invalid format
    }

    /**
     * Test case: Providing a demerit point value outside the valid range.
     * Expectation: The method should return "Failed".
     */
    @Test
    public void testInvalidPointValue() {
        Person p = new Person();
        p.birthdate = "01-01-2000";
        p.personID = "77QWERTY";
        p.demeritPoints = new HashMap<>();
        String result = p.addDemeritPoints("01-01-2024", 10); // Invalid points (greater than 6)
        assertEquals("Failed", result); // Should fail due to invalid points
    }

    /**
     * Test case: Person is over 21 and accumulates more than 12 points in 2 years.
     * Expectation: The person should be suspended.
     */
    @Test
    public void testSuspensionOver21() {
        Person p = new Person();
        p.birthdate = "01-01-1990"; // Person is over 21
        p.personID = "66ZXCVBN";
        p.demeritPoints = new HashMap<>();
        // Accumulate 14 points in total
        p.addDemeritPoints("01-01-2024", 5);
        p.addDemeritPoints("01-02-2024", 5);
        String result = p.addDemeritPoints("01-03-2024", 4);
        assertEquals("Success", result); // Points accepted
        assertTrue(p.isSuspended); // Suspension threshold exceeded
    }

    /**
     * Test case: Person is under 21 and accumulates more than 6 points in 2 years.
     * Expectation: The person should be suspended.
     */
    @Test
    public void testSuspensionUnder21() {
        Person p = new Person();
        p.birthdate = "01-01-2007"; // Person is under 21
        p.personID = "55MNBVCX";
        p.demeritPoints = new HashMap<>();
        // Accumulate 7 points in total
        p.addDemeritPoints("01-01-2024", 3);
        p.addDemeritPoints("01-02-2024", 2);
        String result = p.addDemeritPoints("01-03-2024", 2);
        assertEquals("Success", result); // Points accepted
        assertTrue(p.isSuspended); // Suspension threshold exceeded
    }
}
