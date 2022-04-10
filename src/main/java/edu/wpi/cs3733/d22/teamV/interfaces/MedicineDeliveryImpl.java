package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.serviceRequest.MedicineDelivery;
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
