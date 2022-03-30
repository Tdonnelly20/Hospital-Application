package edu.wpi.veganvampires.Dao;

import edu.wpi.veganvampires.Features.MealRequest;
import edu.wpi.veganvampires.Interfaces.MealRequestImpl;
import edu.wpi.veganvampires.Vdb;
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
    Vdb.addMealRequest(mealRequest);
  }

  @Override
  public void removeMealRequest() {} // TODO
}
