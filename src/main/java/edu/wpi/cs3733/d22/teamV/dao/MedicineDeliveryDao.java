package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.MedicineDelivery;
import edu.wpi.cs3733.d22.teamV.interfaces.MedicineDeliveryImpl;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MedicineDeliveryDao implements MedicineDeliveryImpl {
  private static ArrayList<MedicineDelivery>
      allMedicineDeliveries; // A local list of all medicine deliveries, updated via Vdb

  /** Initialize the arraylist */
  public MedicineDeliveryDao() {
    allMedicineDeliveries = new ArrayList<MedicineDelivery>();
  }

  public void setAllMedicineDeliveries(ArrayList<MedicineDelivery> medicineDeliveries) {
    allMedicineDeliveries = medicineDeliveries;
  }

  @Override
  public ArrayList<MedicineDelivery> getAllMedicineDeliveries() {
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
      String roomNumber,
      int patientID,
      int hospitalID,
      String medicineName,
      String dosage,
      String requestDetails) {
    MedicineDelivery newMedicineDelivery =
        new MedicineDelivery(
            patientFirstName,
            patientLastName,
            roomNumber,
            patientID,
            hospitalID,
            medicineName,
            dosage,
            requestDetails);

    System.out.println("Adding to local arraylist...");
    allMedicineDeliveries.add(newMedicineDelivery); // Store a local copy

    System.out.println("Adding to database");
    try {
      Connection connection = Vdb.Connect();
      Statement exampleStatement = connection.createStatement();
      Vdb.saveToFile(Vdb.Database.MedicineDelivery);
      // exampleStatement.execute(
      //    "INSERT INTO LOCATIONS VALUES (patientFirstName, patientLastName, roomNumber, patientID,
      // hospitalID, medicineName, dosage, requestDetails");
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Remove medication from the arraylist and database
   *
   * @param medicineName
   */
  @Override
  public void removeMedicationDelivery(String medicineName) {
    System.out.println("Removing from arraylist...");
    allMedicineDeliveries.removeIf(e -> e.getMedicineName().equals(medicineName));

    try {
      System.out.println("Removing from database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (MedicineDelivery e : allMedicineDeliveries)
        exampleStatement.execute(
            "DELETE FROM LOCATIONS WHERE medicineName.equals(e.getMedicineName())");

      Vdb.saveToFile(Vdb.Database.MedicineDelivery);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
