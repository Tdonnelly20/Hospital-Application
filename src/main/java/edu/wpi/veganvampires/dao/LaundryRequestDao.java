package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.LaundryRequestImpl;
import edu.wpi.veganvampires.objects.LaundryRequest;
import java.sql.*;
import java.util.ArrayList;

public class LaundryRequestDao implements LaundryRequestImpl {

  private static ArrayList<LaundryRequest> allLaundryRequests;

  /** Initialize the array list */
  public LaundryRequestDao() {
    allLaundryRequests = new ArrayList<LaundryRequest>();
  }

  public LaundryRequestDao(ArrayList<LaundryRequest> allLaundryRequests) {
    this.allLaundryRequests = allLaundryRequests;
  }

  public void setAllLaundryRequests(ArrayList<LaundryRequest> LaundryRequestArrayList) {
    allLaundryRequests = LaundryRequestArrayList;
  }

  /**
   * Getter
   *
   * @return
   */
  @Override
  public ArrayList<LaundryRequest> getAllLaundryRequests() {
    return allLaundryRequests;
  }

  /**
   * Adds equipment to the CSV
   *
   * @param userID
   * @param patientID
   * @param firstName
   * @param lastName
   * @param roomNumber
   * @param details
   * @throws SQLException
   */
  @Override
  public void addLaundryRequest(
      String userID,
      String patientID,
      String firstName,
      String lastName,
      int roomNumber,
      String details)
      throws SQLException {
    /*
    LaundryRequest newLaundryRequest =
            new LaundryRequest(userID, patientID, firstName, lastName, roomNumber, details);

    System.out.println("Adding to local arraylist...");
    allLaundryRequests.add(newLaundryRequest);

    System.out.println("Adding to database");
    try {
        Connection connection = Vdb.Connect();
        Statement exampleStatement = connection.createStatement();
        Vdb.saveToFile(Vdb.Database.LaundryRequest);
        exampleStatement.execute(
                "INSERT INTO LOCATIONS VALUES (newLaundryRequest.getUserID(), newLaundryRequest.getPatientID(), newLaundryRequest.getFirstName(), newLaundryRequest.getLastName(), newLaundryRequest.getRoomNumber(), newLaundryRequest.getDetails()) ");

    } catch (Exception e) {
        e.printStackTrace();
    }

     */
  }

  /**
   * TODO: Make sure that this doesn't remove someone else's equipment Remove the equipment by
   * finding the string of the equipment and using is to remove it from the arraylist
   *
   * @param laundry a string of the desired laundry request to remove
   * @throws SQLException
   */
  @Override
  public void removeLaundryRequest(String laundry) throws SQLException {
    /*
    System.out.println("Removing from arraylist...");
    allLaundryRequests.removeIf(e -> e.getLaundry().equals(laundry));

    try {
        System.out.println("Removing from database...");
        Connection connection;
        connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
        Statement exampleStatement = connection.createStatement();
        for (LaundryRequest e : allLaundryRequests)
            exampleStatement.execute("DELETE FROM LOCATIONS WHERE equipment.equals(e.getLaundry())");

        Vdb.saveToFile(Vdb.Database.LaundryRequest);
    } catch (Exception e) {
        e.printStackTrace();
    }
    */

  }
}
