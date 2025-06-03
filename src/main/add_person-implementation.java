import java.util.HashMap;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Person {
    
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<Date, Integer> demeritPoints;
    private boolean isSuspended;
    
    // Constructor
    public Person(String personID, String firstName, String lastName, String address, String birthdate) {
        this.personID = personID.trim();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address.trim();
        this.birthdate = birthdate.trim();
    }

    public boolean addPerson() {
        if (!isValidPersonID(personID) || !isValidAddress(address) || !isValidBirthdate(birthdate)) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("person.txt", true))) {
            writer.write(personID + "|" + firstName + "|" + lastName + "|" + address + "|" + birthdate);
            writer.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isValidPersonID(String id) {
        if (id.length() != 10 || !id.matches("^[2-9]{2}.{6}[A-Z]{2}$")) return false;
        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            if (!Character.isLetterOrDigit(id.charAt(i))) specialCount++;
        }
        return specialCount >= 2;
    }

    private boolean isValidAddress(String addr) {
        String[] parts = addr.split("\\|");
        return parts.length == 5 && parts[3].equalsIgnoreCase("Victoria");
    }

    private boolean isValidBirthdate(String dob) {
        return dob.matches("^\\d{2}-\\d{2}-\\d{4}$");
    }
}