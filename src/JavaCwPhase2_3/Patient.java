package JavaCwPhase2_3;

public class Patient extends Person {
    private String uniqueId;

    public Patient(String name, String surname, String dateOfBirth, Integer mobileNumber, String uniqueId) {
        super(name, surname, dateOfBirth, mobileNumber);
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
