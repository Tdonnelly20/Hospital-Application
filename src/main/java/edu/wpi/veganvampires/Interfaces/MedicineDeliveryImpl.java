package edu.wpi.veganvampires.Interfaces;

import edu.wpi.veganvampires.Classes.MedicineDelivery;
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

  void removeMedicationDelivery();
}
