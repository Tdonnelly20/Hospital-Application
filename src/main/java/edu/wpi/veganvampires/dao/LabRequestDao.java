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
    allLabRequests = new ArrayList<>();
  }

  public static void setAllLabRequests(ArrayList<LabRequest> newRequests) {
    allLabRequests = newRequests;
  }

  @Override
  public List<LabRequest> getAllLabRequests() {
    return allLabRequests;
  }

  @Override
  public void addLabRequest(
      int userID, int patientID, String firstName, String lastName, String lab, String status) {
    LabRequest labRequest = new LabRequest(userID, patientID, firstName, lastName, lab, status);

    System.out.println("Adding to local arraylist...");
    allLabRequests.add(labRequest);

    try {
      System.out.println("Adding to CSV");
      Vdb.saveToFile(Vdb.Database.LabRequest);
      // System.out.println("Adding to database...");
      // Vdb.addToLabTable(userID, patientID, firstName, lastName, lab, status);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeLabRequest(int userID) {
    System.out.println("Removing from arraylist...");
    allLabRequests.removeIf(l -> l.getPatient().getPatientID() == userID);

    try {
      System.out.println("Removing from database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (LabRequest l : allLabRequests)
        exampleStatement.execute("DELETE FROM LOCATIONS WHERE userID = l.getUserID()");

      Vdb.saveToFile(Vdb.Database.EquipmentDelivery);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
