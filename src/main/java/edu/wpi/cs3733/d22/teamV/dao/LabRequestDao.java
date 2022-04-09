package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.LabRequest;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.Vdb;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabRequestDao extends DaoInterface {
  private static ArrayList<LabRequest> allLabRequests;

  /** Initialize the array list */
  public LabRequestDao() {
    allLabRequests = new ArrayList<>();
  }

  public static void setAllLabRequests(ArrayList<LabRequest> newRequests) {
    allLabRequests = newRequests;
  }

  @Override
  public void loadFromCSV() throws IOException, SQLException {

  }

  @Override
  public void saveToCSV() throws IOException {

  }

  @Override
  public void createSQLTable() throws SQLException {

  }

  @Override
  public void addToSQLTable(ServiceRequest request) throws SQLException {

  }

  @Override
  public void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException {

  }

  @Override
  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {

  }

  @Override
  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {

  }

  @Override
  public ArrayList<LabRequest> getAllServiceRequests() {
    return allLabRequests;
  }

  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests) throws SQLException {

  }

  public void addLabRequest(
      int userID, int patientID, String firstName, String lastName, String lab, String status) {
    LabRequest labRequest = new LabRequest(userID, patientID, firstName, lastName, lab, status);

    // System.out.println("Adding to local arraylist...");
    allLabRequests.add(labRequest);

    try {
      // System.out.println("Adding to CSV");
      Vdb.saveToFile(Vdb.Database.LabRequest);
      // System.out.println("Adding to database...");
      // Vdb.addToLabTable(userID, patientID, firstName, lastName, lab, status);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void removeLabRequest(int userID) {
    // System.out.println("Removing from arraylist...");
    allLabRequests.removeIf(l -> l.getPatient().getPatientID() == userID);

    try {
      // System.out.println("Removing from database...");
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
