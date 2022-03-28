package edu.wpi.veganvampires.Interfaces;

import edu.wpi.veganvampires.Features.MedicineDelivery;
import java.util.List;

public interface MedicineDeliveryImpl {

  List<MedicineDelivery> getAllMedicineDeliveries();

  void addMedicationDelivery(
      String patientFirstName,
      String patientLastName,
      int patientID,
      String medicineName,
      String dosage,
      String requestDetails);

  void removeMedicationDelivery();
}
