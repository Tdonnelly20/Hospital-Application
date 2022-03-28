package edu.wpi.veganvampires.Dao;

import edu.wpi.veganvampires.Features.MedicineDelivery;
import edu.wpi.veganvampires.Interfaces.MedicineDeliveryImpl;
import java.util.ArrayList;
import java.util.List;

public class MedicineDeliveryDao implements MedicineDeliveryImpl {
  private ArrayList<MedicineDelivery> allMedicineDeliveries;

  @Override
  public List<MedicineDelivery> getAllMedicineDeliveries() {
    return allMedicineDeliveries;
  }

  @Override
  public void addMedicationDelivery(
      String patientFirstName, String patientLastName, int patientID, String medicineName) {
    MedicineDelivery newMedicineDelivery =
        new MedicineDelivery(patientFirstName, patientLastName, patientID, medicineName);
    allMedicineDeliveries.add(newMedicineDelivery);
  }

  @Override
  public void removeMedicationDelivery() {}
}
