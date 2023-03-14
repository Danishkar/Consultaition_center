package JavaCwPhase2_3;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class GUI3 {
    JFrame frame;
    JPanel pane1, pane2, pane3, pane4, cardPane;
    CardLayout card;
    JTable table;
    TableModel model;
    Doctor doctor,oldDoctor;
    Patient patient;
    Date dateByUser;
    LocalTime startTimeByUser;
    LocalTime endTimeByUser;
    String cost;
    String imagePath;
    public GUI3(ArrayList<Doctor> doctorList,WestminsterSkinConsultationManager manager,ArrayList<Consultation> consultations) {

//        secretKey for encrypting and decrypting the notes and image path.
        byte[] key = "secretkey0282929".getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

//        Arraylist for storing all the successfully added consultation.
        ArrayList<Consultation> addedConsultation = new ArrayList<>();
        addedConsultation.addAll(consultations);

//        creating the main frame
        frame = new JFrame("Skin Consultation Centre.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.gray);
        frame.setSize(1017, 545);

//        pane1 is the main panel for the first page
        pane1 = new JPanel();

//        adding background image to the main page
        JLabel background=new JLabel(new ImageIcon("D:/Downloads/hos4.jpg"));
        pane1.add(background);
        background.setLayout(new BorderLayout());

//        adding heading to the main page
        JLabel heading = new JLabel("Skin Consultation Centre");
        heading.setFont(new Font("Serif", Font.BOLD, 28));
        heading.setForeground(Color.DARK_GRAY);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(heading,BorderLayout.PAGE_START);

//        creating a sub panel for the main page
        JPanel subMain = new JPanel(new BorderLayout(5,5));
        JLabel subHeading = new JLabel("Select a row to add consultation with a Doctor: ");
        subHeading.setFont(new Font("Serif", Font.BOLD, 20));
        subHeading.setForeground(Color.DARK_GRAY);

//        creating the table for the doctors details
        String[] columnNames = { "Name", "Surname", "Date Of Birth", "Mobile Number", "License Number", "Specialisation" };
        String[][] data = new String[doctorList.size()][6];
        for (int i = 0; i < doctorList.size(); i++) {
            data[i][0] = doctorList.get(i).getName();
            data[i][1] = doctorList.get(i).getSurname();
            data[i][2] = doctorList.get(i).getDateOfBirth();
            data[i][3] = Integer.toString(doctorList.get(i).getMobileNumber());
            data[i][4] = doctorList.get(i).getLicenseNumber();
            data[i][5] = doctorList.get(i).getSpecialisation();
        }
        model = new DefaultTableModel(data,columnNames);
        table = new JTable(model);
        table.setPreferredSize(new Dimension(200, 100));
        table.setOpaque(false);
        table.setShowGrid(false);

//        set the column width for each column
        table.getColumnModel().getColumn(2).setPreferredWidth(15);

//        adding table to JScrollPane
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(50, 100));
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(BorderFactory.createEmptyBorder());

//        sorting the table
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
//        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

//        adding the sub heading and the table to the subMain panel
        subMain.add(subHeading,BorderLayout.NORTH);
        subMain.add(sp,BorderLayout.CENTER);
        subMain.setOpaque(false);

//        adding the subMain to the main page
        background.add(subMain,BorderLayout.CENTER);

//        adding a sort button the footer of the main page to sort the table alphabetically
        JButton sortByName = new JButton("Sort by Name");
        sortByName.setFont(new Font("Calibri", Font.PLAIN, 14));
        sortByName.setBackground(new Color(46, 218, 155));
        sortByName.setForeground(Color.white);

//        action listener for the sort button
        sortByName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.getRowSorter().toggleSortOrder(0); // order by name
            }
        });
//        footer panel for the main page
        JPanel footer = new JPanel();
        footer.add(sortByName);
        footer.setOpaque(false);
        background.add(footer,BorderLayout.PAGE_END);

//        main panel for the second page
        pane2 = new JPanel();
        pane2.setLayout(new BorderLayout());
        JPanel subMain2 = new JPanel();
        subMain2.setLayout(new GridLayout(1,2,5,5));
        JPanel j1 =new JPanel(null);

//        Jlabel for the error message
        JLabel errorMessage1 = new JLabel();
        errorMessage1.setBounds(30,260,500,50);
        errorMessage1.setFont(new Font("Calibri", Font.PLAIN, 20));
        errorMessage1.setForeground(Color.red);
        j1.add(errorMessage1);

//        back button for the second page
        JButton pane2Back = new JButton("< Back");
        pane2Back.setFont(new Font("Calibri", Font.PLAIN, 14));
        pane2Back.setBackground(new Color(46, 218, 155));
        pane2Back.setForeground(Color.white);

//        action listener for the back button
        pane2Back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorMessage1.setText("");
                pane2.removeAll();
                subMain2.removeAll();
                card.first(cardPane);

            }
        });

//        booked consultation button to view the details of the recently added consultation
        JButton bookedConsultation = new JButton("View Consultation");
        bookedConsultation.setFont(new Font("Calibri", Font.PLAIN, 20));
        bookedConsultation.setBackground(new Color(46, 218, 155));
        bookedConsultation.setForeground(Color.white);

//        mouse listener for the table
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                String selectedCellLicenseValue = (String) table.getValueAt(table.getSelectedRow() , 4);
                card.next(cardPane);
                for(int i = 0; i<doctorList.size();i++){
                    if ((doctorList.get(i).getLicenseNumber()).equals(selectedCellLicenseValue)){
//                        getting the selected doctor details from the
                        doctor = doctorList.get(i);
                        pane2.add(pane2Back,BorderLayout.WEST);
                        j1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Consultation with "+doctor.getName(),
                                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), new Color(46, 218, 155)));
                        JPanel j2 = new JPanel(null);
                        j2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Doctor's Details: ",
                                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), new Color(46, 218, 155)));
                        JLabel heading = new JLabel("Selected Doctor's Details");
                        heading.setFont(new Font("Calibri", Font.PLAIN, 20));
                        heading.setBounds(100, 60, 900, 50);
                        j2.add(heading);

                        JLabel name = new JLabel("Name => "+doctorList.get(i).getName());
                        name.setFont(new Font("Calibri", Font.PLAIN, 16));
                        name.setBounds(100, 80, 900, 70);
                        j2.add(name);

                        JLabel licenseNumber  = new JLabel("License number => "+doctorList.get(i).getLicenseNumber());
                        licenseNumber.setFont(new Font("Calibri", Font.PLAIN, 16));
                        licenseNumber.setBounds(100,100,900,90);
                        j2.add(licenseNumber);

                        JLabel specialisation = new JLabel("Specialisation => "+doctorList.get(i).getSpecialisation());
                        specialisation.setFont(new Font("Calibri", Font.PLAIN, 16));
                        specialisation.setBounds(100,120,900,110);
                        j2.add(specialisation);

                        subMain2.add(j2);
                        subMain2.add(j1);
                        break;
                    }
                }
//                adding subMain2 to the main panel
                pane2.add(subMain2,BorderLayout.CENTER);
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

//        Date field for the user input
        JLabel labelDate = new JLabel("Date: ");
        labelDate.setBounds(30,55,500,50);
        labelDate.setFont(new Font("Calibri", Font.PLAIN, 20));
        j1.add(labelDate);
        JTextField Date = new JTextField(12);
        Date.setBounds(130,60,300,40);
        Date.setFont(new Font("Calibri", Font.PLAIN, 18));
        j1.add(Date);

//        Start time field for the user input
        JLabel labelStartTime = new JLabel("Start Time: ");
        labelStartTime.setBounds(30,105,500,50);
        labelStartTime.setFont(new Font("Calibri", Font.PLAIN, 20));
        j1.add(labelStartTime);
        JTextField startTime = new JTextField(8);
        startTime.setBounds(130,110,300,40);
        startTime.setFont(new Font("Calibri", Font.PLAIN, 18));
        j1.add(startTime);

//        End time field for the user input
        JLabel labelEndTime = new JLabel("End Time: ");
        labelEndTime.setBounds(30,155,500,50);
        labelEndTime.setFont(new Font("Calibri", Font.PLAIN, 20));
        j1.add(labelEndTime);
        JTextField endTime = new JTextField(8);
        endTime.setBounds(130,160,300,40);
        endTime.setFont(new Font("Calibri", Font.PLAIN, 18));
        j1.add(endTime);

//        button to check it the date and the time entered by user is valid
        JButton addConsultation = new JButton("Add Consultation");
        addConsultation.setFont(new Font("Calibri", Font.PLAIN, 14));
        addConsultation.setBackground(new Color(46, 218, 155));
        addConsultation.setForeground(Color.white);
        addConsultation.setBounds(140,210,200,40);
        j1.add(addConsultation);

        JPanel pane3DoctorDetails = new JPanel(null);
        pane3DoctorDetails.setPreferredSize(new Dimension(380, 100));

//        action listener for the bookedConsultation to view recent consultation
        bookedConsultation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                retrieving the recently added consultation
                Consultation newConsultation = manager.newConsultation();

//                adding a back button for the fourth panel
                JButton pane4Back = new JButton("< Back");
                pane4Back.setFont(new Font("Calibri", Font.PLAIN, 14));
                pane4Back.setBackground(new Color(46, 218, 155));
                pane4Back.setForeground(Color.white);
//                action listener for the panel 4 back button
                pane4Back.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        card.previous(cardPane);
                    }
                });
                pane4.add(pane4Back,BorderLayout.WEST);

                JPanel mainConsultation = new JPanel(null);
                JPanel newConsultationDoctor = new JPanel(null);
                newConsultationDoctor.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Doctor's Details",
                    TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), new Color(46, 218, 155)));
//                panel to display the doctor's details.
                JPanel newDoctor = new JPanel(null);
//                display the doctor's name
                JLabel name1 = new JLabel("Name: ");
                name1.setFont(new Font("Calibri", Font.PLAIN, 17));
                name1.setBounds(80, 30, 900, 70);
                newDoctor.add(name1);
                JTextField name11 = new JTextField();
                name11.setEnabled(false);
                name11.setText(newConsultation.getDoctor().getName());
                name11.setBounds(200,45,200,35);
                newDoctor.add(name11);
//                display the doctor's license number
                JLabel licenseNumber1  = new JLabel("License number: ");
                licenseNumber1.setFont(new Font("Calibri", Font.PLAIN, 17));
                licenseNumber1.setBounds(80,60,900,90);
                newDoctor.add(licenseNumber1);
                JTextField licenseNumber11 = new JTextField();
                licenseNumber11.setEnabled(false);
                licenseNumber11.setText(newConsultation.getDoctor().getLicenseNumber());
                licenseNumber11.setBounds(200,85,200,35);
                newDoctor.add(licenseNumber11);
//                display the doctor's specialisation
                JLabel specialisation1 = new JLabel("Specialisation: ");
                specialisation1.setFont(new Font("Calibri", Font.PLAIN, 17));
                specialisation1.setBounds(80,100,900,90);
                newDoctor.add(specialisation1);
                JTextField specialisation11 = new JTextField();
                specialisation11.setEnabled(false);
                specialisation11.setText(newConsultation.getDoctor().getSpecialisation());
                specialisation11.setBounds(200,125,200,35);
                newDoctor.add(specialisation11);
//                display the doctor's surname
                JLabel surname1 = new JLabel("Surname: ");
                surname1.setFont(new Font("Calibri", Font.PLAIN, 17));
                surname1.setBounds(500, 30, 900, 70);
                newDoctor.add(surname1);
                JTextField surname11 = new JTextField();
                surname11.setEnabled(false);
                surname11.setText(newConsultation.getDoctor().getSurname());
                surname11.setBounds(620,45,200,35);
                newDoctor.add(surname11);
//                display the doctor's date of birth
                JLabel dateOfBirth1  = new JLabel("Date of Birth: ");
                dateOfBirth1.setFont(new Font("Calibri", Font.PLAIN, 17));
                dateOfBirth1.setBounds(500,60,900,90);
                newDoctor.add(dateOfBirth1);
                JTextField dateOfBirth11 = new JTextField();
                dateOfBirth11.setEnabled(false);
                dateOfBirth11.setText(newConsultation.getDoctor().getDateOfBirth());
                dateOfBirth11.setBounds(620,85,200,35);
                newDoctor.add(dateOfBirth11);
//                display the doctor's mobile number
                JLabel mobileNumber1  = new JLabel("Mobile number: ");
                mobileNumber1.setFont(new Font("Calibri", Font.PLAIN, 17));
                mobileNumber1.setBounds(500,100,900,90);
                newDoctor.add(mobileNumber1);
                JTextField mobileNumber11 = new JTextField();
                mobileNumber11.setEnabled(false);
                mobileNumber11.setText(Integer.toString(newConsultation.getDoctor().getMobileNumber()));
                mobileNumber11.setBounds(620,125,200,35);
                newDoctor.add(mobileNumber11);

                newConsultationDoctor.setBounds(0,10,900,240);
                newDoctor.setBounds(5,20,800,200);
                newConsultationDoctor.add(newDoctor);

//                panel to patient details
                JPanel newPatient = new JPanel(null);
                newPatient.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Patient's Details",
                        TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), new Color(46, 218, 155)));
//                display patient's unique ID
                JLabel uniqueID2  = new JLabel("Unique ID: ");
                uniqueID2.setFont(new Font("Calibri", Font.PLAIN, 17));
                uniqueID2.setBounds(280,20,900,90);
                newPatient.add(uniqueID2);
                JTextField uniqueID12 = new JTextField();
                uniqueID12.setEnabled(false);
                uniqueID12.setText(newConsultation.getPatient().getUniqueId());
                uniqueID12.setBounds(400,45,200,35);
                newPatient.add(uniqueID12);
//                display patient's name
                JLabel name2 = new JLabel("Name: ");
                name2.setFont(new Font("Calibri", Font.PLAIN, 17));
                name2.setBounds(80, 70, 900, 70);
                newPatient.add(name2);
                JTextField name12 = new JTextField();
                name12.setEnabled(false);
                name12.setText(newConsultation.getPatient().getName());
                name12.setBounds(200,90,200,35);
                newPatient.add(name12);
//                display patient's surname
                JLabel surname2 = new JLabel("Surname: ");
                surname2.setFont(new Font("Calibri", Font.PLAIN, 17));
                surname2.setBounds(500, 70, 900, 70);
                newPatient.add(surname2);
                JTextField surname12 = new JTextField();
                surname12.setEnabled(false);
                surname12.setText(newConsultation.getPatient().getSurname());
                surname12.setBounds(620,90,200,35);
                newPatient.add(surname12);
//                display patient's date of birth
                JLabel dateOfBirth2  = new JLabel("Date of Birth: ");
                dateOfBirth2.setFont(new Font("Calibri", Font.PLAIN, 17));
                dateOfBirth2.setBounds(80,120,900,90);
                newPatient.add(dateOfBirth2);
                JTextField dateOfBirth12 = new JTextField();
                dateOfBirth12.setEnabled(false);
                dateOfBirth12.setText(newConsultation.getPatient().getDateOfBirth());
                dateOfBirth12.setBounds(200,140,200,35);
                newPatient.add(dateOfBirth12);
//                display patient's mobile number
                JLabel mobileNumber2  = new JLabel("Mobile number: ");
                mobileNumber2.setFont(new Font("Calibri", Font.PLAIN, 17));
                mobileNumber2.setBounds(500,120,900,90);
                newPatient.add(mobileNumber2);
                JTextField mobileNumber12 = new JTextField();
                mobileNumber12.setEnabled(false);
                mobileNumber12.setText(Integer.toString(newConsultation.getPatient().getMobileNumber()));
                mobileNumber12.setBounds(620,140,200,35);
                newPatient.add(mobileNumber12);

                newPatient.setBounds(0,270,900,240);

//                panel to display the other consultation details.
                JPanel newConsultationDetails = new JPanel(null);
                newConsultationDetails.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Consultation Details",
                        TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), new Color(46, 218, 155)));
//                display consultation's date
                JLabel date = new JLabel("Date: ");
                date.setFont(new Font("Calibri", Font.PLAIN, 17));
                date.setBounds(80,20,900,90);
                newConsultationDetails.add(date);
                JTextField date1 = new JTextField();
                date1.setEnabled(false);
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                date1.setText(df.format(newConsultation.getDate()));
                date1.setBounds(200,40,200,35);
                newConsultationDetails.add(date1);
//                display consultation's cost
                JLabel cost = new JLabel("Cost: ");
                cost.setFont(new Font("Calibri", Font.PLAIN, 17));
                cost.setBounds(500, 25, 900, 70);
                newConsultationDetails.add(cost);
                JTextField cost1 = new JTextField();
                cost1.setEnabled(false);
                cost1.setText(newConsultation.getCost());
                cost1.setBounds(620,40,200,35);
                newConsultationDetails.add(cost1);
//                display consultation's start time
                JLabel startTime = new JLabel("Start Time: ");
                startTime.setFont(new Font("Calibri", Font.PLAIN, 17));
                startTime.setBounds(80,55,900,90);
                newConsultationDetails.add(startTime);
                JTextField startTime1 = new JTextField();
                startTime1.setEnabled(false);
                startTime1.setText(newConsultation.getStartTime().toString());
                startTime1.setBounds(200,80,200,35);
                newConsultationDetails.add(startTime1);
//                display consultation's end time
                JLabel endTime = new JLabel("End Time: ");
                endTime.setFont(new Font("Calibri", Font.PLAIN, 17));
                endTime.setBounds(500,55,900,90);
                newConsultationDetails.add(endTime);
                JTextField endTime1 = new JTextField();
                endTime1.setEnabled(false);
                endTime1.setText(newConsultation.getEndTime().toString());
                endTime1.setBounds(620,80,200,35);
                newConsultationDetails.add(endTime1);
//                display consultation's notes
                JLabel notes = new JLabel("Notes: ");
                notes.setFont(new Font("Calibri", Font.PLAIN, 17));
                notes.setBounds(80,90,900,90);
                newConsultationDetails.add(notes);
//                variables to decrypt the notes and the image's path.
                String notesDecry = "";
                String imagePathDecry = "";
                try {
                    Cipher cipher = Cipher.getInstance("AES");
                    cipher.init(Cipher.DECRYPT_MODE, secretKey);
//                    decrypting the notes text
                    notesDecry = new String(cipher.doFinal(Base64.getDecoder().decode(newConsultation.getNotes())));
                    if(imagePath != null){
//                        decrypting the images path
                        imagePathDecry = new String(cipher.doFinal(Base64.getDecoder().decode(imagePath)));
                    }else{
                        imagePathDecry = "";
                    }
                } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
                    ex.printStackTrace();
                }
//                displaying the consultation's notes
                JTextArea notes1 = new JTextArea(notesDecry);
                notes1.setEnabled(false);
                notes1.setEditable(false);
                notes1.setCursor(null);
                notes1.setFocusable(false);
                notes1.setLineWrap(true);
                notes1.setWrapStyleWord(true);
                notes1.setBounds(200,120,200,100);
                newConsultationDetails.add(notes1);
//                displaying the consultation's image
                JLabel image1 = new JLabel("Image: ");
                image1.setFont(new Font("Calibri", Font.PLAIN, 17));
                image1.setBounds(500,90,900,90);
                newConsultationDetails.add(image1);
                if(imagePathDecry == ""){
                    JLabel imagelabel = new JLabel("*No Image Found");
                    imagelabel.setForeground(Color.red);
                    imagelabel.setFont(new Font("Calibri", Font.PLAIN, 17));
                    imagelabel.setBounds(620,90,900,90);
                    newConsultationDetails.add(imagelabel);
                }
                JLabel photoLabel = new JLabel();
                ImageIcon icon = new ImageIcon(imagePathDecry);
                Image img = icon.getImage();
                Image newImg = img.getScaledInstance(570, 180, Image.SCALE_SMOOTH);
                icon.setImage(newImg);
                photoLabel.setIcon(icon);
                photoLabel.setBounds(520,150,300,180);
                newConsultationDetails.add(photoLabel);

                newConsultationDetails.setBounds(0,540,900,350);

                mainConsultation.setBounds(0,0,900,500);
                mainConsultation.setPreferredSize(new Dimension(900,900));

                mainConsultation.add(newConsultationDoctor);
                mainConsultation.add(newPatient);
                mainConsultation.add(newConsultationDetails);
                JPanel mainConsultation2 = new JPanel();
//                adding the consultation page to the scroll pane
                JScrollPane sp2 = new JScrollPane(mainConsultation,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                sp2.getViewport().setPreferredSize(new Dimension(900, 500));

                mainConsultation2.add(sp2);
                pane4.add(mainConsultation2,BorderLayout.CENTER);
                card.last(cardPane);
            }
        });

//        action listener for the addConsultation button
        addConsultation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Date.getText().equals("")){
                    errorMessage1.setText("*Date field cannot be kept empty");
                }else if(startTime.getText().equals("")){
                    errorMessage1.setText("*Start time field cannot be kept empty");
                }else if(endTime.getText().equals("")){
                    errorMessage1.setText("*End time cannot be kept empty");
                }else{
                    SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
                    boolean valid;
                    try{
                        dateByUser = d.parse(Date.getText());
                        try {
                            startTimeByUser = LocalTime.parse(startTime.getText());
                            try {
                                endTimeByUser = LocalTime.parse(endTime.getText());
                                valid = true;
                            } catch (DateTimeParseException event2) {
                                errorMessage1.setText("*Enter the end time in the valid format: hh:mm.");
                                valid = false;
                            }
                        } catch (DateTimeParseException event2) {
                            errorMessage1.setText("*Enter the start time in the valid format: hh:mm.");
                            valid = false;
                        }
                    }catch (ParseException event1){
                        errorMessage1.setText("*Enter the date in the valid format: dd-MM-yyyy");
                        valid = false;
                    }

                    if (valid == true){
                        boolean slotAvailable = manager.checkAvailability(dateByUser,startTimeByUser,endTimeByUser,doctor);
                        imagePath = null;
                        if(slotAvailable == true){
                            pane3DoctorDetails.removeAll();
                            JLabel pane3DoctorHeading = new JLabel("Doctor's Details: ");
                            pane3DoctorHeading.setBounds(30,20,500,50);
                            pane3DoctorHeading.setFont(new Font("Calibri", Font.BOLD, 20));
                            pane3DoctorDetails.add(pane3DoctorHeading);

                            JLabel pane3DoctorName = new JLabel("Name => "+doctor.getName());
                            pane3DoctorName.setFont(new Font("Calibri", Font.PLAIN, 16));
                            pane3DoctorName.setBounds(30, 50, 500, 50);
                            pane3DoctorDetails.add(pane3DoctorName);

                            JLabel pane3DoctorLicenseNumber  = new JLabel("License number => "+doctor.getLicenseNumber());
                            pane3DoctorLicenseNumber.setFont(new Font("Calibri", Font.PLAIN, 16));
                            pane3DoctorLicenseNumber.setBounds(30,80,500,50);
                            pane3DoctorDetails.add(pane3DoctorLicenseNumber);

                            JLabel pane3DoctorSpecialisation = new JLabel("Specialisation => "+doctor.getSpecialisation());
                            pane3DoctorSpecialisation.setFont(new Font("Calibri", Font.PLAIN, 16));
                            pane3DoctorSpecialisation.setBounds(30,110,500,50);
                            pane3DoctorDetails.add(pane3DoctorSpecialisation);

                            bookedConsultation.setBounds(30,160,220,50);

                            card.next(cardPane);

                            Date.setText("");
                            startTime.setText("");
                            endTime.setText("");
                        }else{
                            doctor = manager.randomDoctorAllocation(doctor,dateByUser,startTimeByUser,endTimeByUser);
                            if(doctor == null){
                                errorMessage1.setText("*No Doctor is Available");
                            }else{
                                pane3DoctorDetails.removeAll();
                                JLabel pane3DoctorHeading1 = new JLabel("Doctor's Details: ");
                                pane3DoctorHeading1.setBounds(30,20,500,50);
                                pane3DoctorHeading1.setFont(new Font("Calibri", Font.BOLD, 20));
                                pane3DoctorDetails.add(pane3DoctorHeading1);

                                JLabel pane3DoctorName1 = new JLabel("Name => "+doctor.getName());
                                pane3DoctorName1.setFont(new Font("Calibri", Font.PLAIN, 16));
                                pane3DoctorName1.setBounds(30, 50, 500, 50);
                                pane3DoctorDetails.add(pane3DoctorName1);

                                JLabel pane3DoctorLicenseNumber  = new JLabel("License number => "+doctor.getLicenseNumber());
                                pane3DoctorLicenseNumber.setFont(new Font("Calibri", Font.PLAIN, 16));
                                pane3DoctorLicenseNumber.setBounds(30,80,500,50);
                                pane3DoctorDetails.add(pane3DoctorLicenseNumber);

                                JLabel pane3DoctorSpecialisation = new JLabel("Specialisation => "+doctor.getSpecialisation());
                                pane3DoctorSpecialisation.setFont(new Font("Calibri", Font.PLAIN, 16));
                                pane3DoctorSpecialisation.setBounds(30,110,500,50);
                                pane3DoctorDetails.add(pane3DoctorSpecialisation);

                                JTextArea message = new JTextArea("*New doctor has been allocated to u since  the selected doctor doesn't have a slot  available in the given time slot");
                                message.setFont(new Font("Calibri", Font.BOLD, 16));
                                message.setBounds(30,160,300,55);
                                message.setBackground(Color.BLUE);
                                message.setEditable(false);
                                message.setCursor(null);
                                message.setOpaque(false);
                                message.setFocusable(false);
                                message.setLineWrap(true);
                                message.setWrapStyleWord(true);
                                message.setForeground(Color.red);
                                pane3DoctorDetails.add(message);

                                bookedConsultation.setBounds(30,220,220,50);

                                card.next(cardPane);
                                Date.setText("");
                                startTime.setText("");
                                endTime.setText("");
                            }
                        }
                    }
                }
            }
        });
//        panel for the 3rd page
        pane3 = new JPanel();
        pane3.setLayout(new BorderLayout());

        JPanel pane3Main = new JPanel(null);
        pane3Main.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Patient Details",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), new Color(46, 218, 155)));
//        field to get the patient's name
        JLabel name = new JLabel("Name: ");
        name.setBounds(30,20,500,50);
        name.setFont(new Font("Calibri", Font.PLAIN, 20));
        pane3Main.add(name);
        JTextField PName = new JTextField(8);
        PName.setBounds(165,25,350,35);
        PName.setFont(new Font("Calibri", Font.PLAIN, 16));
        pane3Main.add(PName);

//        field to get the patient's surname
        JLabel surname = new JLabel("Surname: ");
        surname.setBounds(30,70,500,50);
        surname.setFont(new Font("Calibri", Font.PLAIN, 20));
        pane3Main.add(surname);
        JTextField PSurname = new JTextField(8);
        PSurname.setBounds(165,75,350,35);
        PSurname.setFont(new Font("Calibri", Font.PLAIN, 16));
        pane3Main.add(PSurname);

//        field to get the patient's date of birth
        JLabel dob = new JLabel("Date of birth: ");
        dob.setBounds(30,120,500,50);
        dob.setFont(new Font("Calibri", Font.PLAIN, 20));
        pane3Main.add(dob);
        JTextField PDOB = new JTextField(8);
        PDOB.setBounds(165,125,350,35);
        PDOB.setFont(new Font("Calibri", Font.PLAIN, 16));
        pane3Main.add(PDOB);

//        field to get the patient's mobile number
        JLabel mobileNumber = new JLabel("Mobile number: ");
        mobileNumber.setBounds(30,180,500,50);
        mobileNumber.setFont(new Font("Calibri", Font.PLAIN, 20));
        pane3Main.add(mobileNumber);
        JTextField PMNumber = new JTextField(8);
        PMNumber.setBounds(165,185,350,35);
        PMNumber.setFont(new Font("Calibri", Font.PLAIN, 16));
        pane3Main.add(PMNumber);

//        field to get the patient's unique id
        JLabel patientId = new JLabel("Patient id: ");
        patientId.setBounds(30,240,500,50);
        patientId.setFont(new Font("Calibri", Font.PLAIN, 20));
        pane3Main.add(patientId);
        JTextField PId = new JTextField(8);
        PId.setBounds(165,245,350,35);
        PId.setFont(new Font("Calibri", Font.PLAIN, 16));
        pane3Main.add(PId);

//        field to get the patient's notes
        JLabel notes = new JLabel("Notes: ");
        notes.setBounds(30,300,500,50);
        notes.setFont(new Font("Calibri", Font.PLAIN, 20));
        pane3Main.add(notes);
        JTextField PNotes = new JTextField(6);
        PNotes.setBounds(165,305,350,35);
        PNotes.setFont(new Font("Calibri", Font.PLAIN, 16));
        pane3Main.add(PNotes);

//        field to get the patient's cost
        JLabel cost = new JLabel("Cost: ");
        cost.setBounds(30,360,500,50);
        cost.setFont(new Font("Calibri", Font.PLAIN, 20));
        pane3Main.add(cost);
        JTextField PCost = new JTextField(6);
        PCost.setBounds(165,365,350,35);
        PCost.setFont(new Font("Calibri", Font.PLAIN, 16));
        pane3Main.add(PCost);

//        button to upload photo
        JButton uploadPhoto = new JButton("Add Photo");
        uploadPhoto.setFont(new Font("Calibri", Font.PLAIN, 17));
        uploadPhoto.setBackground(new Color(46, 218, 155));
        uploadPhoto.setForeground(Color.white);
        uploadPhoto.setBounds(45,415,200,35);
        pane3Main.add(uploadPhoto);

//        code for the uploadPhoto frame
        JFrame uploadFrame = new JFrame("Upload");
        uploadFrame.setBackground(Color.gray);

        JPanel uploadMain = new JPanel();
        uploadMain.setLayout(new BorderLayout());

        JPanel uploadPanel = new JPanel();
        JLabel photo = new JLabel();
        uploadPanel.add(photo);
        uploadPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Add photo",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), new Color(46, 218, 155)));
        JPanel uploadFooter = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));

        JButton addPhoto = new JButton("+");
        addPhoto.setFont(new Font("Calibri", Font.PLAIN, 20));
        addPhoto.setBackground(new Color(46, 218, 155));
        addPhoto.setForeground(Color.white);
//        action listener for the add button
        addPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(uploadFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                    try {
//                        encrypting the image path
                        Cipher cipher = Cipher.getInstance("AES");
                        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                        imagePath = Base64.getEncoder().encodeToString(cipher.doFinal(selectedFile.getAbsolutePath().getBytes()));
                    } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException ex) {
                        ex.printStackTrace();
                    }
                    Image img = icon.getImage();
                    Image newImg = img.getScaledInstance(570, 380, Image.SCALE_SMOOTH);
                    icon.setImage(newImg);
                    photo.setIcon(icon);
                    if (icon.getImageLoadStatus() != MediaTracker.ERRORED) {
                        uploadMain.remove(addPhoto);
                        uploadFooter.setVisible(true);
                        uploadMain.add(uploadFooter,BorderLayout.SOUTH);
                    }
                }
            }
        });


        JButton submitPhoto = new JButton("Upload");
        submitPhoto.setFont(new Font("Calibri", Font.PLAIN, 20));
        submitPhoto.setBackground(new Color(46, 218, 155));
        submitPhoto.setForeground(Color.white);
//        action listener for the submit button
        submitPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadFrame.dispose();
                uploadPanel.removeAll();
                uploadFooter.setVisible(false);
                uploadPhoto.setText("Photo Added");
                uploadPhoto.setEnabled(false);
            }
        });

        JButton uploadPanelBack = new JButton("< Back");
        uploadPanelBack.setFont(new Font("Calibri", Font.PLAIN, 20));
        uploadPanelBack.setBackground(new Color(46, 218, 155));
        uploadPanelBack.setForeground(Color.white);
//        action listener for the upload panels back button
        uploadPanelBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadMain.remove(submitPhoto);
                uploadMain.remove(uploadPanelBack);
                uploadFooter.setVisible(false);
                uploadMain.add(addPhoto,BorderLayout.SOUTH);
            }
        });

//        action listener for upload button
        uploadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadFooter.add(uploadPanelBack);
                uploadFooter.add(submitPhoto);
                uploadMain.add(uploadPanel,BorderLayout.CENTER);
                uploadMain.add(addPhoto,BorderLayout.SOUTH);
                uploadFrame.add(uploadMain);
                uploadFrame.setSize(600, 500);
                uploadFrame.setVisible(true);
            }
        });

//        button for the saving consultation
        JButton saveConsultation = new JButton("Save Details");
        saveConsultation.setFont(new Font("Calibri", Font.PLAIN, 17));
        saveConsultation.setBackground(new Color(46, 218, 155));
        saveConsultation.setForeground(Color.white);
        saveConsultation.setBounds(295,415,200,35);
        pane3Main.add(saveConsultation);

//        JLabel for displaying the error message
        JLabel errorMessage2 = new JLabel();
        errorMessage2.setBounds(30,445,500,50);
        errorMessage2.setFont(new Font("Calibri", Font.PLAIN, 20));
        errorMessage2.setForeground(Color.red);
        pane3Main.add(errorMessage2);

//        action listener for the saveConsultation
        saveConsultation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateOfBirth = "";
                if(PName.getText().equals("")){
                    errorMessage2.setText("*Name filed cannot be kept empty");
                }else if(!PName.getText().matches("^[A-Za-z]+$")){
                    errorMessage2.setText("*Please enter a valid name (alphabetical characters only)");
                }else if (PSurname.getText().equals("")){
                    errorMessage2.setText("*Surname filed cannot be kept empty");
                }else if(!PSurname.getText().matches("^[A-Za-z]+$")){
                    errorMessage2.setText("*Please enter a valid surname (alphabetical characters only)");
                }else if (PDOB.getText().equals("")){
                    errorMessage2.setText("*Date of Birth filed cannot be kept empty");
                }else if (PMNumber.getText().equals("")){
                    errorMessage2.setText("*Mobile Number filed cannot be kept empty");
                }else  if(!PMNumber.getText().matches("^[0-9]*$") || PMNumber.getText().length() != 10){
                    errorMessage2.setText("*Please enter a valid mobile number (exactly 10 digits)");
                } else if (PId.getText().equals("")){
                    errorMessage2.setText("*Patient Id filed cannot be kept empty");
                }else if (PNotes.getText().equals("")){
                    errorMessage2.setText("*Notes filed cannot be kept empty");
                }
                else{
                    SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
                    boolean valid = false;
                    try{
                        dateOfBirth = d.format(d.parse(PDOB.getText()));
                        valid = true;
                    }catch (ParseException event){
                        errorMessage2.setText("*Enter the date in the valid format(dd-MM-yyyy:");
                        valid = false;
                    }
                    if(valid == true){
                        patient = new Patient(PName.getText(),PSurname.getText(),dateOfBirth,Integer.parseInt(PMNumber.getText()),PId.getText());
                        String notesEnc = "";
                        try {
//                            encrypting the notes text.
                            Cipher cipher = Cipher.getInstance("AES");
                            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                            notesEnc = Base64.getEncoder().encodeToString(cipher.doFinal(PNotes.getText().getBytes()));
                        } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException ex) {
                            ex.printStackTrace();
                        }
//                        adding the consultation to the array list
                        addedConsultation.add(manager.addConsultation(dateByUser,startTimeByUser,endTimeByUser,PCost.getText(),notesEnc,doctor,patient,imagePath));
                        manager.saveConsultationToFile();
                        errorMessage2.setText("*Successfully added the Consultation");
                        PName.setText("");
                        PSurname.setText("");
                        PDOB.setText("");
                        PMNumber.setText("");
                        PId.setText("");
                        PNotes.setText("");
                        PCost.setText("");
                        uploadPhoto.setText("Add Photo");
                        uploadPhoto.setEnabled(true);
                        pane3DoctorDetails.add(bookedConsultation);
                        pane3DoctorDetails.revalidate();
                        pane3DoctorDetails.repaint();
                    }
                }
            }
        });
//        back button for the panel 3
        JButton pane3Back = new JButton("< Back");
        pane3Back.setFont(new Font("Calibri", Font.PLAIN, 14));
        pane3Back.setBackground(new Color(46, 218, 155));
        pane3Back.setForeground(Color.white);
//        action listener for the back button
        pane3Back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorMessage1.setText("");
                errorMessage2.setText("");
                errorMessage1.setText("");
                pane2.removeAll();
                subMain2.removeAll();
                card.first(cardPane);
                pane4.removeAll();

            }
        });

        pane3.add(pane3Main,BorderLayout.CENTER);
        pane3.add(pane3DoctorDetails,BorderLayout.EAST);
        pane3.add(pane3Back,BorderLayout.WEST);

//        creating panel 4 to display the added consultation details
        pane4 = new JPanel();
        pane4.setLayout(new BorderLayout());




        cardPane = new JPanel();
//        creating aa card layout to switch between panels
        card = new CardLayout();

        cardPane.setLayout(card);
        cardPane.add(pane1, "First Pane");
        cardPane.add(pane2, "Second Pane");
        cardPane.add(pane3, "Third Pane");
        cardPane.add(pane4,"Fourth Pane");

        frame.add(cardPane);
        frame.setVisible(true);
    }
}
