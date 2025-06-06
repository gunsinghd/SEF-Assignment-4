package com.roadregistry.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Struct;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.SimpleDateFormat;

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
     * Adds a person to the system by storing their information in a TXT file.
     * The person's information must meet all validation conditions.
     *
     * @return true if person is successfully added, false otherwise
     */
    public boolean addPerson() {
        //validate all required fields before adding to file
        if (!isValidPersonID(personID) || !isValidName(firstName) || !isValidName(lastName) || 
            !isValidAddressForAddPerson(address) || !isValidBirthdateForAddPerson(birthdate)) {
            return false;
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("person.txt", true))) {
            //write person data in comma-separated format to match updatePersonalDetails expectations
            //format: ID, First name, Last name, Address, Birthday, Demerit Points, is suspended
            writer.write(personID + "," + firstName + "," + lastName + "," + address + "," + birthdate + ",0,false");
            writer.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Boolean updatePersonalDetails(String personID, String newID, String newFirstName, String newLastName, String newAddress, String newBirthdate) {

        //assume the format of text file is: ID, First name, Last name, Address, Birthday, Demerit Points, is suspended
        String filePath = "person.txt";
        List<String> newlines = new ArrayList<>();
        Boolean updated = false;

        //read the person.txt (original data) and separate to parts
        try {
           List<String> lines = Files.readAllLines(Paths.get(filePath));
           for (String line : lines) {

               String[] parts = line.split(",");
               // check the format fit with the assumed format
               if (parts.length!=7){
                   return false;
               }
               String oriId = parts[0].trim();
               String oriFirstName = parts[1].trim();
               String oriLastName = parts[2].trim();
               String oriAddress = parts[3].trim();
               String oriBirthdate = parts[4].trim();
               String oriDemeritPoints = parts[5].trim();
               String oriSuspended = parts[6].trim();


                // allow user input null value(doesn't change), if user decide to keep the original value then keep using the old one
               if (oriId.equals(personID)) {
                   String updateId = (newID == null || newID.isEmpty()) ? oriId : newID;
                   String updateFirstName = (newFirstName == null || newFirstName.isEmpty()) ? oriFirstName : newFirstName;
                   String updateLastName = (newLastName == null || newLastName.isEmpty()) ? oriLastName : newLastName;
                   String updateAddress = (newAddress == null || newAddress.isEmpty()) ? oriAddress : newAddress;
                   String updateBirthdate = (newBirthdate == null || newBirthdate.isEmpty()) ? oriBirthdate : newBirthdate;

                   //check if the updated value is same as original value, if yes then skip the validation
                   if(!updateId.equals(oriId) && !isValidId(updateId)) {
                       return false;
                   }
                   if(!updateFirstName.equals(oriFirstName) && !isValidName(updateFirstName)){
                       return false;
                   }
                   if(!updateLastName.equals(oriLastName)&& !isValidName(updateLastName)) {
                       return false;
                   }
                   if (!updateAddress.equals(oriAddress) && !isValidAddress(updateAddress)) {
                       return false;
                   }
                   if (!updateBirthdate.equals(oriBirthdate) && !isValidBirthdate(updateBirthdate)) {
                       return false;
                   }

                   int age = calculateAge(oriBirthdate);

                   //checking if the value is updated, for some condition might need to use
                   boolean idUpdated = !updateId.equals(oriId);
                   boolean NameUpdated = !updateLastName.equals(oriLastName) || !updateFirstName.equals(oriFirstName);
                   boolean addressUpdated = !updateAddress.equals(oriAddress);
                   boolean birthdateUpdated = !updateBirthdate.equals(oriBirthdate);

                   //condition 1:If a person is under 18, their address cannot be changed.
                   if(!birthdateUpdated && age< 18 && addressUpdated) {
                       return false;
                   }
                   //condition 2:If a person's birthday is going to be changed, then no other personal detail (i.e, person's ID, firstName, lastName, address) can be changed.
                   if(birthdateUpdated && (idUpdated || NameUpdated || addressUpdated)) {
                       return false;
                   }
                   //condition 3:If the first character/digit of a person's ID is an even number, then their ID cannot be changed.
                   if (idUpdated && isFirstEven(oriId)) {
                       return false;
                   }
                   // if the updated value pass all validation, then add it in to newLine list
                   String updateLine = String.join(",", updateId, updateFirstName, updateLastName, updateAddress, updateBirthdate, oriDemeritPoints, oriSuspended);
                   newlines.add(updateLine);
                   updated = true;
                   
                   // Update the current Person object's fields
                   this.personID = updateId;
                   this.firstName = updateFirstName;
                   this.lastName = updateLastName;
                   this.address = updateAddress;
                   this.birthdate = updateBirthdate;
                   
               }else {
                   // Keep non-matching lines unchanged
                   newlines.add(line);
               }
           }
           //if updated is true(means every validation pass) then write it back to txt file.
           if(updated){
               Files.write(Paths.get(filePath), newlines);
           }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return updated;
    }

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
            formatter.setLenient(false); // Make parsing strict to reject invalid dates
            Date offenseDate = formatter.parse(offenseDateStr); // Offense date parsed from input
            
            // Additional validation: ensure the parsed date matches the input string
            // This catches edge cases where parsing might succeed but change the date
            String reformattedDate = formatter.format(offenseDate);
            if (!reformattedDate.equals(offenseDateStr)) {
                return "Failed";
            }
            
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
            try (FileWriter writer = new FileWriter("demerits.txt", true)) { // Use try-with-resources
                writer.write(this.personID + ", " + offenseDateStr + ", " + points + "\n"); // Log entry
            }

            return "Success"; // All conditions passed
        } catch (Exception e) {
            // Any parsing exception means invalid date or other error
            return "Failed"; // Return failure if any exception occurs
        }
    }

    // validation method: pass date and format to Australia format, and caculate the period between this year and birthdate
    public int calculateAge(String date) {
        try{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthdate = LocalDate.parse(date, formatter);
        return Period.between(birthdate, LocalDate.now()).getYears();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // validation method: check if passed id fit with addPerson function's requirement, id has to be 10 char long
    // , the first two char should be a digit between 2-9, and last two char should be uppercase
    public boolean isValidId(String id) {

        if (id.length() != 10) {
            return false;
        }

        if(!id.substring(0,2).matches("[2-9]{2}")){
            return false;
        }

        String midPart = id.substring(2,8);
        int specialSymbol = 0;
        for (int i = 0; i < midPart.length(); i++) {
            char c = midPart.charAt(i);
            if(!Character.isLetterOrDigit(c)){
                specialSymbol++;
            }
        }

        if(specialSymbol <2){
            return false;
        }

        String lastTwoCharPart = id.substring(8);
        if(!lastTwoCharPart.matches("[A-Z]{2}")){
            return false;
        }

        return true;
    }

    //validation method: checking the input name's length should be between 1-50, and only allow char, space, dash, and single quotation,
    //digits not allow
    public boolean isValidName(String name) {
        return name != null && name.matches("^[\\p{L}\\s'-]{1,50}$");
    }

    //validation method: checking the input address fit the requirement: separate by using "|" and state must be victoria
    public boolean isValidAddress(String address){
        if (address == null) {
            return false;
        }
        String[] parts = address.split("\\|");

        return parts.length == 5 && parts[3].equals("Victoria");
    }

    //validation method: checking input birthdate is not after the date time right now and the period between birthdate and today doesn't exceed 100 years
    public boolean isValidBirthdate(String birthdate){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate birthdateDate = LocalDate.parse(birthdate, formatter);
            return !birthdateDate.isAfter(LocalDate.now()) && birthdateDate.isAfter(LocalDate.now().minusYears(100));
        }catch (Exception e){
            return false;
        }
    }

    //validation method: checking the first char of user id is even or odd,to check if the data of that user can be changed or not
    public boolean isFirstEven(String id){
        char firstChar = id.charAt(0);
        return Character.isDigit(firstChar)&&((firstChar-'0')% 2 == 0);
    }

    //validation method: check if personID meets the addPerson function requirements
    //id must be 10 char long, first two chars are digits 2-9, at least 2 special chars in middle 6 positions, last two chars uppercase
    private boolean isValidPersonID(String id) {
        if (id == null || id.length() != 10) {
            return false;
        }
        
        //check first two characters are digits between 2-9
        if (!id.substring(0, 2).matches("[2-9]{2}")) {
            return false;
        }
        
        //check middle 6 characters (positions 2-7) have at least 2 special characters
        String midPart = id.substring(2, 8);
        int specialCount = 0;
        for (int i = 0; i < midPart.length(); i++) {
            if (!Character.isLetterOrDigit(midPart.charAt(i))) {
                specialCount++;
            }
        }
        if (specialCount < 2) {
            return false;
        }
        
        //check last two characters are uppercase letters
        String lastTwoChars = id.substring(8);
        if (!lastTwoChars.matches("[A-Z]{2}")) {
            return false;
        }
        
        return true;
    }

    //validation method: check address format for addPerson function
    //address should be: Street Number|Street|City|State|Country with State = Victoria
    private boolean isValidAddressForAddPerson(String addr) {
        if (addr == null) {
            return false;
        }
        String[] parts = addr.split("\\|");
        return parts.length == 5 && parts[3].equals("Victoria");
    }

    //validation method: check birthdate format for addPerson function  
    //birthdate should be in DD-MM-YYYY format
    private boolean isValidBirthdateForAddPerson(String dob) {
        if (dob == null) {
            return false;
        }
        //check format and validate it's a real date
        if (!dob.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            return false;
        }
        
        //additional validation: check if it's not in future and not more than 100 years ago
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate birthdateDate = LocalDate.parse(dob, formatter);
            return !birthdateDate.isAfter(LocalDate.now()) && birthdateDate.isAfter(LocalDate.now().minusYears(100));
        } catch (Exception e) {
            return false;
        }
    }
}