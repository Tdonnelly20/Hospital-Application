package edu.wpi.veganvampires.Classes;

public class MedicineDelivery {
  private final String patientFirstName,
      patientLastName,
      medicineName,
      dosage,
      roomNumber,
      requestDetails;
  private final int patientID, hospitalID;

  /**
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param hospitalID
   * @param medicineName
   * @param dosage
   * @param requestDetails
   */
  public MedicineDelivery(
      String patientFirstName,
      String patientLastName,
      String roomNumber,
      int patientID,
      int hospitalID,
      String medicineName,
      String dosage,
      String requestDetails) {
    this.dosage = dosage;
    this.requestDetails = requestDetails;
    this.roomNumber = roomNumber;
    this.patientFirstName = patientFirstName;
    this.patientLastName = patientLastName;
    this.patientID = patientID;
    this.hospitalID = hospitalID;
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

  public int getHospitalID() {
    return hospitalID;
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

  public String getRoomNumber() {
    return roomNumber;
  }
}
