package edu.wpi.veganvampires.Interfaces;

import edu.wpi.veganvampires.Features.MedicineDelivery;
import java.util.List;

public interface MedicineDeliveryImpl {

  public List<MedicineDelivery> getAllMedicineDeliveries();

  public void addMedicationDelivery(
      String patientFirstName, String patientLastName, int patientID, String medicineName);

  public void removeMedicationDelivery();
}
