package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.ServiceRequests.MealRequest;
import java.util.List;

public interface MealRequestImpl {

  List<MealRequest> getAllMealRequests();

  void addMealRequest(int userID, int patientID, String firstName, String lastName, String food);

  void removeMealRequest();
}
