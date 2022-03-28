package edu.wpi.veganvampires.Features;

public class MedicineDelivery {
  private final String patientFirstName, patientLastName, medicineName, dosage, requestDetails;
  private final int patientID;

  /**
   * Creates a basic data structure for holding medicine delivery request
   *
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param medicineName
   * @param dosage
   * @param requestDetails
   */
  public MedicineDelivery(
      String patientFirstName,
      String patientLastName,
      int patientID,
      String medicineName,
      String dosage,
      String requestDetails) {
    this.dosage = dosage;
    this.requestDetails = requestDetails;
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

  public String getDosage() {
    return dosage;
  }

  public String getRequestDetails() {
    return requestDetails;
  }
}
