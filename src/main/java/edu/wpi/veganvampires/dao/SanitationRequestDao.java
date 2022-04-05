package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.SanitationRequestImpl;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.SanitationRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SanitationRequestDao implements SanitationRequestImpl {
  private static ArrayList<SanitationRequest>
      allSanitationRequests; // A local list of all sanitation requests, updated via Vdb

  /** Initialize the arraylist */
  public SanitationRequestDao() {
    allSanitationRequests = new ArrayList<SanitationRequest>();
    // TODO: Add info from the database to the local arraylist
  }

  @Override
  public ArrayList<SanitationRequest> getAllSanitationRequests() {
    return allSanitationRequests;
  }

  /**
   * Receive a medicine delivery from the controller, store it locally, then send it to Vdb
   *
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param hospitalID
   * @param roomLocation
   * @param hazardName
   * @param requestDetails
   */
  @Override
  public void addSanitationRequest(
      String patientFirstName,
      String patientLastName,
      int patientID,
      int hospitalID,
      String hazardName,
      String roomLocation,
      String requestDetails) {
    SanitationRequest newSanitationRequest =
        new SanitationRequest(
            patientFirstName,
            patientLastName,
            patientID,
            hospitalID,
            roomLocation,
            hazardName,
            requestDetails);

    System.out.println("Adding to local arraylist...");
    allSanitationRequests.add(newSanitationRequest); // Store a local copy

    System.out.println("Adding to database");
    try {
      Connection connection = Vdb.Connect();
      Statement exampleStatement = connection.createStatement();
      Vdb.saveToFile(Vdb.Database.SanitationRequest);
      // exampleStatement.execute(
      //    "INSERT INTO LOCATIONS VALUES (patientFirstName, patientLastName, roomLocation,
      // patientID,
      // hospitalID, hazardName, requestDetails");
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeSanitationRequest(String hazardName) {
    System.out.println("Removing from arraylist...");
    allSanitationRequests.removeIf(e -> e.getHazardName().equals(hazardName));

    try {
      System.out.println("Removing from database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (SanitationRequest e : allSanitationRequests)
        exampleStatement.execute(
            "DELETE FROM LOCATIONS WHERE medicineName.equals(e.getMedicineName())");

      Vdb.saveToFile(Vdb.Database.SanitationRequest);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
