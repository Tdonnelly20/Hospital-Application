package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.objects.MedicineDelivery;
import java.util.List;

public interface MedicineDeliveryImpl {

  List<MedicineDelivery> getAllMedicineDeliveries();

  void addMedicationDelivery(
      String patientFirstName,
      String patientLastName,
      String roomNumber,
      int patientID,
      int hospitalID,
      String medicineName,
      String dosage,
      String requestDetails);

  void removeMedicationDelivery(String medicineName);
}
