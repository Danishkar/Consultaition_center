package JavaCwPhase2_3;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class WestminsterSkinConsultationManager implements SkinConsultationManager {
    ArrayList<Doctor> doctorList = new ArrayList<>();
    ArrayList<Consultation> consultations = new ArrayList<>();
//    specify the format for the Date.
    SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");

    @Override
//    method to add a new doctor.
    public void addNewDoctor(){
        if (doctorList.size() == 10){
            System.out.println("Maximum number of doctors in the center as been reached");
        }
        else{
            Scanner input = new Scanner(System.in);

            System.out.println(" ");
            System.out.print("Enter the name of the doctor: ");
            String name = input.next();
            while (!name.matches("^[A-Za-z]+$")) {
                System.out.print("Please enter a valid name (alphabetical characters only):");
                name = input.next();
            }

            System.out.print("Enter the surname of the doctor: ");
            String surname = input.next();
            while (!surname.matches("^[A-Za-z]+$")) {
                System.out.print("Please enter a valid surname (alphabetical characters only):");
                surname = input.next();
            }

            System.out.print("Enter the doctor's date of birth(dd-mm-yyyy): ");
            String dateOfBirth;
            while(true){
                try{
                    dateOfBirth = d.format(d.parse(input.next()));
                    break;
                }catch (ParseException e){
                    System.out.print("Enter the date of birth in the valid format(dd-mm-yyyy):");
                }
            }

            System.out.print("Enter the doctor's mobile number: ");
            String mobileNumberStr = input.next();
            while (!mobileNumberStr.matches("^[0-9]*$") || mobileNumberStr.length() != 10) {
                System.out.print("Please enter a valid mobile number (exactly 10 digits)");
                mobileNumberStr = input.next();
            }
            int mobileNumber = Integer.parseInt(mobileNumberStr);

            System.out.print("Enter the doctor's license number: ");
            String licenseNumber = input.next();

            System.out.print("Enter the doctor's specialisation: ");
            String specialisation = input.next();
            while (!specialisation.matches("^[A-Za-z]+$")) {
                System.out.print("Please enter a valid specialisation (alphabetical characters only):");
                specialisation = input.next();
            }

            doctorList.add(new Doctor(name, surname, dateOfBirth, mobileNumber, licenseNumber, specialisation));

            System.out.println("Successfully added the doctors details with the name "+ name);
            System.out.println(" ");
        }
    }

    @Override
//    method to delete a doctor.
    public void deleteADoctor() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the license number of the doctor to delete: ");
        String licenseNumber = input.next();
        for(int i = 0; i<doctorList.size();i++){
            if ((doctorList.get(i)).getLicenseNumber().equals(licenseNumber)){
                System.out.println(" ");
                System.out.println("==========================================================================");
                System.out.println(" ");
                System.out.println("Deleted Doctor's details");
                System.out.println("------------------------");
                System.out.println("Surname         :"+doctorList.get(i).getSurname());
                System.out.println("Name            :"+doctorList.get(i).getName());
                System.out.println("Date of birth   :"+doctorList.get(i).getDateOfBirth());
                System.out.println("Mobile number   :"+doctorList.get(i).getMobileNumber());
                System.out.println("License number  :"+doctorList.get(i).getLicenseNumber());
                System.out.println("Specialisation  :"+doctorList.get(i).getSpecialisation());
                System.out.println(" ");
                doctorList.remove(i);
                System.out.println("Number of doctors remaining in the center is : "+doctorList.size());
                System.out.println(" ");
                System.out.println("==========================================================================");
                System.out.println(" ");
                break;
            }
        }
    }

    @Override
//    method to print the list of doctors that are added.
    public void printListOfDoctors() {
        doctorList.sort((p1, p2) -> p1.getSurname().compareTo(p2.getSurname()));
        System.out.println(" ");
        System.out.println("==========================================================================");
        for(int i = 0; i<doctorList.size();i++){
            System.out.println(" ");
            System.out.println("Doctor's details");
            System.out.println("----------------");
            System.out.println("Surname           :"+doctorList.get(i).getSurname());
            System.out.println("Name              :"+doctorList.get(i).getName());
            System.out.println("Date of birth     :"+doctorList.get(i).getDateOfBirth());
            System.out.println("Mobile number     :"+doctorList.get(i).getMobileNumber());
            System.out.println("License number    :"+doctorList.get(i).getLicenseNumber());
            System.out.println("Specialisation    :"+doctorList.get(i).getLicenseNumber());
            System.out.println(" ");
        }
        System.out.println("==========================================================================");
        System.out.println(" ");
    }

    @Override
//    method to save doctors details to a file.
    public void save() {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("doctors-data.txt", false));
            for(int x=0; x < doctorList.size();x++){
                String detail = doctorList.get(x).getName()+","+doctorList.get(x).getSurname()+","+doctorList.get(x).getDateOfBirth()+","
                        +doctorList.get(x).getMobileNumber()+","+doctorList.get(x).getLicenseNumber()+","+doctorList.get(x).getSpecialisation() ;
                writer.write(detail);
                writer.newLine();

            }
            writer.flush();
            System.out.println(" ");
            System.out.println("Successfully written data to the file");
            System.out.println(" ");
            writer.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
//    method to retrieve doctors stored data from the file.
    public void retrieveData(){
        File file = new File("doctors-data.txt");
        if(file.exists()){
            try (BufferedReader reader = new BufferedReader(new FileReader(file)))
            {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] detail = line.split("[,]",0);
                    doctorList.add(new Doctor(detail[0], detail[1], detail[2], Integer.parseInt(detail[3]), detail[4], detail[5]));
                }
                System.out.println("Loaded data from the file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    method to create the GUI
    public void createGui(WestminsterSkinConsultationManager manager){
        GUI3 gui;
        gui = new GUI3(doctorList,manager,consultations);
    }

//    method to check if the doctor is available in a specific date and time.
    public boolean checkAvailability(Date date, LocalTime startTime, LocalTime endTime, Doctor doctor){
        boolean available = false;
        if(consultations.size() == 0){
            available = true;
        }else{
            for( int x=0; x<consultations.size();x++){
                if(consultations.get(x).getDoctor().getLicenseNumber().equals(doctor.getLicenseNumber())){
                    if(consultations.get(x).getDate().equals(date) && !(consultations.get(x).getStartTime().isAfter(endTime) && consultations.get(x).getEndTime().isBefore(startTime))){
                        available = false;
                        break;
                    }else if (!(consultations.get(x).getDate().equals(date))){
                        available = true;
                    }
                }else if(!(consultations.get(x).getDoctor().getLicenseNumber().equals(doctor.getLicenseNumber()))){
                    available = true;
                }
            }
        }
        return available;
    }

//    method to add consultation with specific doctor and a patient.
    public Consultation addConsultation(Date date, LocalTime startTime,LocalTime endTime, String cost, String notes, Doctor doctor, Patient patient, String imagePath){
        Consultation newConsultation = new Consultation(date,startTime,endTime,cost,notes,doctor,patient,imagePath);
        consultations.add(newConsultation);
        return newConsultation;
    }

//    method to randomly allocate a doctor when the user selected doctor is not available
    public Doctor randomDoctorAllocation(Doctor selectedDoctor,Date date, LocalTime startTime, LocalTime endTime){
        ArrayList<Doctor> newDoctor  = new ArrayList<>();
        Doctor randomDoctor;
        for (Doctor doctor : doctorList) {
            if (!(selectedDoctor.getLicenseNumber().equals(doctor.getLicenseNumber()))) {
                boolean availability = checkAvailability(date, startTime, endTime, doctor);
                if (availability) {
                    newDoctor.add(doctor);
                }
            }
        }
        if(newDoctor.size() == 0){
            randomDoctor = null;
        }else{
            Random random = new Random();
            int index = random.nextInt(newDoctor.size());
            randomDoctor = newDoctor.get(index);
        }
        return randomDoctor;
    }

//    method to return the recently added consultation
    public Consultation newConsultation(){
        return consultations.get(consultations.size()-1);
    }

//    saving the consultation in the array list to a file.
    public void saveConsultationToFile(){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("consultation-data.txt", false));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String path;
            for (Consultation consultation : consultations) {
                    path = consultation.getImagePath();
                String detail = formatter.format(consultation.getDate()) + "," + consultation.getStartTime() + "," + consultation.getEndTime() + "," + consultation.getCost() + ","
                        + consultation.getNotes() + "," + consultation.getDoctor().getName() + "," + consultation.getDoctor().getSurname() + "," + consultation.getDoctor().getDateOfBirth() + ","
                        + consultation.getDoctor().getMobileNumber() + "," + consultation.getDoctor().getLicenseNumber() + "," + consultation.getDoctor().getSpecialisation() + "," + consultation.getPatient().getName() + ","
                        + consultation.getPatient().getSurname() + "," + consultation.getPatient().getDateOfBirth() + "," + consultation.getPatient().getMobileNumber() + "," + consultation.getPatient().getUniqueId() + ","
                        + path;
                writer.write(detail);
                writer.newLine();
            }
            writer.flush();
            System.out.println("Successfully written consultation data to the file");
            writer.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

//    reading the consultation data from the file to an arraylist.
    public void retrieveDataFromConsultation(){
        File file = new File("consultation-data.txt");
        if(file.exists()){
            try (BufferedReader reader = new BufferedReader(new FileReader(file)))
            {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                String line;
                String path;
                while ((line = reader.readLine()) != null) {
                    String[] detail = line.split("[,]",0);
                    path = detail[16];
                    consultations.add(new Consultation(formatter.parse(detail[0]), LocalTime.parse(detail[1]), LocalTime.parse(detail[2]), detail[3], detail[4], new Doctor(detail[5],detail[6],detail[7],Integer.parseInt(detail[8]),detail[9],detail[10]),new Patient(detail[11],detail[12],detail[13],Integer.parseInt(detail[14]),detail[15]),path));
                }
                System.out.println("Loaded data from the consultation file.");
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        WestminsterSkinConsultationManager manager = new WestminsterSkinConsultationManager();
        manager.retrieveData();
        manager.retrieveDataFromConsultation();
        System.out.println("-------------------------Skin Consultation Center-------------------------");
        System.out.println(" ");
        Scanner input = new Scanner(System.in);
        label:
        while (true){
            System.out.println("-------------------------------Console Menu-------------------------------");
            System.out.println("Enter '1' to add new doctor");
            System.out.println("Enter '2' to delete a doctor");
            System.out.println("Enter '3' to print the list of doctors");
            System.out.println("Enter '4' to save the doctors details to a file");
            System.out.println("Enter '5' to open the graphical user interface");
            System.out.println("Enter 'Q' to quit the program");
            System.out.println("--------------------------------------------------------------------------");
            System.out.print("User's Choice: ");
            String userChoice = input.next();
            switch (userChoice) {
                case "1":
                    manager.addNewDoctor();
                    break;
                case "2":
                    manager.deleteADoctor();
                    break;
                case "3":
                    manager.printListOfDoctors();
                    break;
                case "4":
                    manager.save();
                    break;
                case "5":
                    manager.createGui(manager);
                    break;
                case "Q":
                    System.out.println("Program terminated!");
                    break label;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

}
