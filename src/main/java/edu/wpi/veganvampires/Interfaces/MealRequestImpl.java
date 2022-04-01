package edu.wpi.veganvampires.Interfaces;

import edu.wpi.veganvampires.Classes.MealRequest;
import java.util.List;

public interface MealRequestImpl {

  List<MealRequest> getAllMealRequests();

  void addMealRequest(int userID, int patientID, String firstName, String lastName, String food);

  void removeMealRequest();
}
