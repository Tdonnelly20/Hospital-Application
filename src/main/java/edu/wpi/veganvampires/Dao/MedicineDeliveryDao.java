package edu.wpi.veganvampires.Dao;

import edu.wpi.veganvampires.Features.MedicineDelivery;
import edu.wpi.veganvampires.Interfaces.MedicineDeliveryImpl;
import edu.wpi.veganvampires.Vdb;
import java.util.ArrayList;
import java.util.List;

public class MedicineDeliveryDao implements MedicineDeliveryImpl {
  private static ArrayList<MedicineDelivery>
      allMedicineDeliveries; // A local list of all medicine deliveries, updated via Vdb

  /** Initialize the arraylist */
  public MedicineDeliveryDao() {
    allMedicineDeliveries = new ArrayList<MedicineDelivery>();
    // TODO: Add info from the database to the local arraylist
  }

  @Override
  public List<MedicineDelivery> getAllMedicineDeliveries() {
    return allMedicineDeliveries;
  }

  /**
   * Receive a medicine delivery from the controller, store it locally, then send it to Vdb
   *
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param medicineName
   * @param dosage
   * @param requestDetails
   */
  @Override
  public void addMedicationDelivery(
      String patientFirstName,
      String patientLastName,
      int patientID,
      String medicineName,
      String dosage,
      String requestDetails) {
    MedicineDelivery newMedicineDelivery =
        new MedicineDelivery(
            patientFirstName, patientLastName, patientID, medicineName, dosage, requestDetails);

    System.out.println("Adding to local arraylist...");
    allMedicineDeliveries.add(newMedicineDelivery); // Store a local copy
    updateMedicineDeliveryDB(newMedicineDelivery); // Store on database
  }

  // Send to the database
  private void updateMedicineDeliveryDB(MedicineDelivery newMedicineDelivery) {
    System.out.println("Sending to database...");
    Vdb.addMedicineDelivery(newMedicineDelivery);
  }

  @Override
  public void removeMedicationDelivery() {} // TODO
}
