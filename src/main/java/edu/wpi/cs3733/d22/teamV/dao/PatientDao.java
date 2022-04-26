package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PatientDao {
  private static final Patient nullPatient = new Patient("Not", "Found");
  // List of all patients
  private static ArrayList<Patient> allPatients;

  /** Default constructor, loads SQL table then from CSV */
  public PatientDao() {
    nullPatient.setPatientID(-1);
    allPatients = new ArrayList<>();
    createSQLTable();
    loadFromCSV();
  }

  /** Load all the Patients from the CSV to the arraylist, add them to SQL */
  public void loadFromCSV() {

    try {
      String line = "";
      String file = VApp.currentPath + "/Patients.csv";
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String splitToken = ","; // what we split the csv file with
      ArrayList<Patient> patients = new ArrayList<>();
      String headerLine = br.readLine();
      while ((line = br.readLine()) != null) // should create a database based on csv file
      {
        String[] data = line.split(splitToken);
        ArrayList<Integer> employeeIDs;
        ArrayList<Integer> serviceIDs;
        // LOOK AT THIS PIECE OF SHIT CODE I MADE. LOOK AT IT. ITS AMAZING

        try {
          employeeIDs =
              IntStream.of(Arrays.stream(data[5].split(" ")).mapToInt(Integer::parseInt).toArray())
                  .boxed()
                  .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
          employeeIDs = new ArrayList<>();
        }

        try {
          serviceIDs =
              IntStream.of(Arrays.stream(data[6].split(" ")).mapToInt(Integer::parseInt).toArray())
                  .boxed()
                  .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
          serviceIDs = new ArrayList<>();
        }

        Patient newPatient = new Patient(data[1], data[2], employeeIDs, serviceIDs);

        newPatient.setPatientID(Integer.parseInt(data[0]));
        patients.add(newPatient);
      }
      allPatients = patients;

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Save all patients from the arraylist to the CSV */
  public void saveToCSV() {
    try {
      FileWriter fw = new FileWriter(VApp.currentPath + "/Patients.csv");
      BufferedWriter bw = new BufferedWriter(fw);
      bw.append("patientID, patientFirstName, patientLastName, employeeIDs, serviceRequestIDs");
      for (Patient p : getAllPatients()) {

        String employeeIDs = "";
        String serviceIDs = "";

        for (int ID : p.getEmployeeIDs()) {
          employeeIDs += ID + " ";
        }

        for (int ID : p.getServiceRequestIDs()) {
          serviceIDs += ID + " ";
        }

        String[] outputData = {
          String.valueOf(p.getPatientID()),
          p.getFirstName(),
          p.getLastName(),
          employeeIDs,
          serviceIDs
        };

        bw.append("\n");
        for (String s : outputData) {
          bw.append(s);
          bw.append(',');
        }
      }
      bw.close();
      fw.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add a new patient to the arraylist, then to the SQL table, then save to CSV
   *
   * @param patient
   */
  public void addPatient(Patient patient) {
    patient.setPatientID(RequestSystem.getPatientID());
    allPatients.add(patient);
    addToSQLTable(patient);
    saveToCSV();
  }

  /**
   * Remove a patient from the arraylist then all the associated service requests
   *
   * @param patient
   */
  public void removePatient(Patient patient) {
    replacePatient(patient);
    for (ServiceRequest request : patient.getServiceRequestList()) {
      Vdb.requestSystem.removeServiceRequest(request);
    }
  }

  /**
   * A helper method for updating a patient with new info instead of removing
   *
   * @param patient
   */
  public void replacePatient(Patient patient) {
    allPatients.removeIf(currPatient -> patient.getPatientID() == currPatient.getPatientID());
    removeFromSQLTable(patient);
    saveToCSV();
  }

  /**
   * Getter for all patients
   *
   * @return all patients
   */
  public static ArrayList<Patient> getAllPatients() {
    return allPatients;
  }

  /** Create the SQL table for all patients */
  public void createSQLTable() {
    try {
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "PATIENTS", new String[] {"TABLE"});
      if (!set.next()) {
        statement.execute(
            "CREATE TABLE Patients(patientID int, patientFirstName char(30), patientLastName char(30), employeeIDs varchar(1000), serviceRequestIDs varchar(1000))");

      } else {
        statement.execute("DROP TABLE PATIENTS");
        createSQLTable();
        return;
      }

      for (Patient patient : allPatients) {
        addToSQLTable(patient);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add a patient to the SQL table
   *
   * @param patient
   */
  public void addToSQLTable(Patient patient) {
    try {

      Connection connection = Vdb.Connect();
      Statement exampleStatement = connection.createStatement();
      String query =
          "INSERT INTO PATIENTS(patientID, patientFirstName, patientLastName, employeeIDs, serviceRequestIDs) VALUES ("
              + patient.getPatientID()
              + ",'"
              + patient.getFirstName()
              + "','"
              + patient.getLastName()
              + "', '";

      // add employees
      for (int employeeID : patient.getEmployeeIDs()) {
        query += employeeID + " ";
      }

      query += "','";

      // add service requests
      for (int request : patient.getServiceRequestIDs()) {
        query += request + " ";
      }

      query += "')";

      exampleStatement.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Remove a patient from the SQL table
   *
   * @param patient
   */
  public void removeFromSQLTable(Patient patient) {
    try {
      String query = "";
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();
      query = "DELETE FROM PATIENTS WHERE patientID = " + patient.getPatientID();
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get a patient with a specific ID
   *
   * @param patientID
   * @return a patient if it exists, if not return nullPatient
   */
  public Patient getPatient(int patientID) {
    for (Patient patient : allPatients) {
      if (patient.getPatientID() == patientID) {
        return patient;
      }
    }
    System.out.println("No patient with ID: " + patientID);
    return nullPatient;
  }

  /**
   * Update a specific patient with the ID and replace it
   *
   * @param patient to replace
   * @param patientID to replace patientID
   */
  public void updatePatient(Patient patient, int patientID) {
    Patient oldPatient = getPatient(patientID);
    patient.setPatientID(patientID);
    replacePatient(oldPatient);
    allPatients.add(patient);
    addToSQLTable(patient);
    saveToCSV();
  }
}
