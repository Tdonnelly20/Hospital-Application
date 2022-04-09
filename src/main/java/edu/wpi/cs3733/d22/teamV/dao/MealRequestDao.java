package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.MealRequest;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MealRequestDao extends DaoInterface {
  private static ArrayList<MealRequest> allMealRequests;

  /** Initialize the array list */
  public MealRequestDao() {
    allMealRequests = new ArrayList<MealRequest>();
    // TODO: Add info from the database to the local arraylist
  }

  public List<MealRequest> getAllMealRequests() {
    return allMealRequests;
  }

  public void addMealRequest(
      int userID, int patientID, String firstName, String lastName, String food) {
    MealRequest mealRequest = new MealRequest(userID, patientID, firstName, lastName, food);

    System.out.println("Adding to local arraylist...");
    allMealRequests.add(mealRequest);
    updateLabRequest(mealRequest);
  }

  private void updateLabRequest(MealRequest mealRequest) {
    System.out.println("Sending to database...");
  }

  public void removeMealRequest() {} // TODO

  @Override
  public void loadFromCSV() throws IOException, SQLException {}

  @Override
  public void saveToCSV() throws IOException {}

  @Override
  public void createSQLTable() throws SQLException {}

  @Override
  public void addToSQLTable(ServiceRequest request) throws SQLException {}

  @Override
  public void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException {}

  @Override
  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {}

  @Override
  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {}

  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return null;
  }

  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException {}
}
