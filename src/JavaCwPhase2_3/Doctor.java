package JavaCwPhase2_3;

public class Doctor extends Person {

    private String licenseNumber;
    private String specialisation;
    public Doctor(String name, String surname, String dateOfBirth, Integer mobileNumber, String licenseNumber, String specialisation) {
        super(name, surname, dateOfBirth, mobileNumber);
        this.licenseNumber = licenseNumber;
        this.specialisation = specialisation;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }
}
