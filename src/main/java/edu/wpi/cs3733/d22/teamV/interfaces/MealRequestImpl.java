package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.MealRequest;
import java.util.List;

public interface MealRequestImpl {

  List<MealRequest> getAllMealRequests();

  void addMealRequest(int userID, int patientID, String firstName, String lastName, String food);

  void removeMealRequest();
}
