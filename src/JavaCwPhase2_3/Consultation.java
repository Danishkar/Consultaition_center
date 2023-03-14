package JavaCwPhase2_3;
import java.time.LocalTime;
import java.util.Date;
public class Consultation {
    private Date date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String cost;
    private String notes;
    private Doctor doctor;
    private Patient patient;
    private String imagePath;

    public Consultation(Date date, LocalTime startTime,LocalTime endTime, String cost, String notes, Doctor doctor, Patient patient, String imagePath) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
        this.notes = notes;
        this.doctor = doctor;
        this.patient = patient;
        this.imagePath = imagePath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
