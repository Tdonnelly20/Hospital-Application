package edu.wpi.veganvampires.Features;

public class MedicineDelivery {
  private final String patientFirstName, patientLastName, medicineName;
  private final int patientID;

  public MedicineDelivery(
      String patientFirstName, String patientLastName, int patientID, String medicineName) {
    this.patientFirstName = patientFirstName;
    this.patientLastName = patientLastName;
    this.patientID = patientID;
    this.medicineName = medicineName;
  }

  public String getPatientFirstName() {
    return patientFirstName;
  }

  public String getPatientLastName() {
    return patientLastName;
  }

  public int getPatientID() {
    return patientID;
  }

  public String getMedicineName() {
    return medicineName;
  }
}
