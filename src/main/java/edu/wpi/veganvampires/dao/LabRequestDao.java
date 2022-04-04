package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.LabRequestImpl;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.LabRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabRequestDao implements LabRequestImpl {
  private static ArrayList<LabRequest> allLabRequests;

  /** Initialize the array list */
  public LabRequestDao() {
    allLabRequests = new ArrayList<LabRequest>();
    // createLabTable();
    // TODO: Add info from the database to the local arraylist
  }

  public void createLabTable() throws SQLException {
    Connection connection = Vdb.Connect();
    Statement newStatement = connection.createStatement();
    newStatement.execute(
        "CREATE TABLE LAB (UserID int, "
            + "PatientID int, "
            + "FirstName String"
            + "LastName String"
            + "Lab String");
  }

  @Override
  public List<LabRequest> getAllLabRequests() {
    return allLabRequests;
  }

  @Override
  public void addLabRequest(
      int userID, int patientID, String firstName, String lastName, String lab, String status) {
    LabRequest labRequest = new LabRequest(userID, patientID, firstName, lastName, lab, status);

    System.out.println("Adding to database");
    try {
      Connection connection = Vdb.Connect();
      Statement exampleStatement = connection.createStatement();
      Vdb.saveToFile(Vdb.Database.LabRequest);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateLabRequest(LabRequest labRequest) {
    System.out.println("Sending to database...");
  }

  @Override
  public void removeLabRequest(String labRequestID) {
    System.out.println("Removing from arraylist...");
    allLabRequests.removeIf(e -> e.getLab().equals(labRequestID)); // TODO Fix this

    try {
      System.out.println("Removing from database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (LabRequest e : allLabRequests)
        exampleStatement.execute(
            "DELETE FROM LOCATIONS WHERE medicineName.equals(e.getMedicineName())");

      Vdb.saveToFile(Vdb.Database.LabRequest);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  // TODO

  public void setAllLabRequests(ArrayList<LabRequest> labs) {
    allLabRequests = labs;
  }
}
