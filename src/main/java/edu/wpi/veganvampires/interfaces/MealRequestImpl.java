package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.objects.MealRequest;
import java.util.List;

public interface MealRequestImpl {

  List<MealRequest> getAllMealDeliveries();

  void addMealRequest(
      String patientFirstName,
      String patientLastName,
      String roomNumber,
      int patientID,
      int hospitalID,
      String mealName,
      String dosage,
      String requestDetails);

  void removeMealRequest(String mealName);
}
