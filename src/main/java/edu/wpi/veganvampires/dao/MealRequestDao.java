package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.MealRequestImpl;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.MealRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MealRequestDao implements MealRequestImpl {
  private static ArrayList<MealRequest>
      allMealDeliveries; // A local list of all medicine deliveries, updated via Vdb

  /** Initialize the arraylist */
  public MealRequestDao() {
    allMealDeliveries = new ArrayList<MealRequest>();
  }

  public void setAllMealDeliveries(ArrayList<MealRequest> mealDeliveries) {
    allMealDeliveries = mealDeliveries;
  }

  public ArrayList<MealRequest> getAllMealRequests() {
    return allMealDeliveries;
  }

  /**
   * Receive a meal delivery from the controller, store it locally, then send it to Vdb
   *
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param mealName
   * @param allergy
   * @param requestDetails
   */
  @Override
  public void addMealRequest(
      String patientFirstName,
      String patientLastName,
      String roomNumber,
      int patientID,
      int hospitalID,
      String mealName,
      String allergy,
      String requestDetails) {
    MealRequest newMealDelivery =
        new MealRequest(
            patientFirstName,
            patientLastName,
            roomNumber,
            patientID,
            hospitalID,
            mealName,
            allergy,
            requestDetails);

    System.out.println("Adding to local arraylist...");
    allMealDeliveries.add(newMealDelivery); // Store a local copy

    System.out.println("Adding to database");
    try {
      Connection connection = Vdb.Connect();
      Statement exampleStatement = connection.createStatement();
      Vdb.saveToFile(Vdb.Database.MealDelivery);
      // exampleStatement.execute(
      //    "INSERT INTO LOCATIONS VALUES (patientFirstName, patientLastName, roomNumber, patientID,
      // hospitalID, mealName, allergy, requestDetails");
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Remove meal from the arraylist and database
   *
   * @param mealName
   */
  @Override
  public void removeMealRequest(String mealName) {
    System.out.println("Removing from arraylist...");
    allMealDeliveries.removeIf(e -> e.getMealName().equals(mealName));

    try {
      System.out.println("Removing from database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (MealRequest e : allMealDeliveries)
        exampleStatement.execute("DELETE FROM LOCATIONS WHERE mealName.equals(e.getMealName())");

      Vdb.saveToFile(Vdb.Database.MealDelivery);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<MealRequest> getAllMealDeliveries() {
    return allMealDeliveries;
  };
}
