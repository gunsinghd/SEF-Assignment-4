package com.roadregistry.model;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.FileWriter;

/**
 * The Person class represents a person in the Road Registry system.
 * It holds personal details and manages demerit points issued for traffic offenses.
 */
public class Person {
    public String personID;
    public String firstName;
    public String lastName;
    public String address;
    public String birthdate; // Expected in "dd-MM-yyyy" format
    public HashMap<Date, Integer> demeritPoints = new HashMap<>(); // Stores demerit points with the offense date
    public boolean isSuspended = false; // Indicates whether the person is suspended

    /**
     * Adds demerit points to the person's record based on the offense date and point value.
     *
     * @param offenseDateStr The date of the offense in "dd-MM-yyyy" format.
     * @param points         The number of demerit points (1 to 6).
     * @return "Success" if the operation is successful; otherwise "Failed".
     */
    public String addDemeritPoints(String offenseDateStr, int points) {
        // Validate offense date format using regex pattern (DD-MM-YYYY)
        if (!offenseDateStr.matches("\\d{2}-\\d{2}-\\d{4}")) return "Failed";

        // Validate that points are within the allowed range (1–6)
        if (points < 1 || points > 6) return "Failed";

        try {
            // Setup date formatter and parse the offense date
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            formatter.setLenient(false);
            Date offenseDate = formatter.parse(offenseDateStr); // Offense date parsed from input
            Date today = new Date(); // Current system date

            // Add the offense to the internal demeritPoints map
            this.demeritPoints.put(offenseDate, points);

            // Calculate total demerit points accrued in the last 2 years
            int totalRecentPoints = 0;
            long twoYearsMillis = 2L * 365 * 24 * 60 * 60 * 1000; // Milliseconds in two years

            for (Map.Entry<Date, Integer> entry : this.demeritPoints.entrySet()) {
                long diff = today.getTime() - entry.getKey().getTime(); // Time difference from offense to today
                // Only include points if the offense occurred within the past 2 years
                if (diff <= twoYearsMillis && diff >= 0) {
                    totalRecentPoints += entry.getValue(); // Accumulate valid points
                }
            }

            // Parse the person's birthdate and calculate their current age
            Date birthDate = formatter.parse(this.birthdate);
            int age = (int) ((today.getTime() - birthDate.getTime()) / (365.25 * 24 * 60 * 60 * 1000));

            // Apply suspension rules based on age
            if ((age < 21 && totalRecentPoints > 6) || (age >= 21 && totalRecentPoints > 12)) {
                this.isSuspended = true; // Suspend if threshold exceeded
            }

            // Append the offense record to a TXT file named "demerits.txt"
            FileWriter writer = new FileWriter("demerits.txt", true); // true = append mode
            writer.write(this.personID + ", " + offenseDateStr + ", " + points + "\n"); // Log entry
            writer.close(); // Close file to flush changes

            return "Success"; // All conditions passed
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging
            return "Failed"; // Return failure if any exception occurs
        }
    }
}
