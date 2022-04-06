package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.MealRequest;
import edu.wpi.cs3733.d22.teamV.interfaces.MealRequestImpl;
import java.util.ArrayList;
import java.util.List;

public class MealRequestDao implements MealRequestImpl {
  private static ArrayList<MealRequest> allMealRequests;

  /** Initialize the array list */
  public MealRequestDao() {
    allMealRequests = new ArrayList<MealRequest>();
    // TODO: Add info from the database to the local arraylist
  }

  @Override
  public List<MealRequest> getAllMealRequests() {
    return allMealRequests;
  }

  @Override
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

  @Override
  public void removeMealRequest() {} // TODO
}
