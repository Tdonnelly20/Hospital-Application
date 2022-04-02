package edu.wpi.veganvampires.objects;

public class MedicineDelivery {
  private Patient patient;
  private HospitalEmployee employee;
  private String medicineName, dosage, roomNumber, requestDetails;

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
    patient = new Patient(patientID, patientFirstName, patientLastName);
    employee = new HospitalEmployee(hospitalID);
    this.medicineName = medicineName;
  }

  public String getPatientFirstName() {
    return patient.getFirstName();
  }

  public String getPatientLastName() {
    return patient.getLastName();
  }

  public int getPatientID() {
    return patient.getPatientID();
  }

  public int getHospitalID() {
    return employee.getHospitalID();
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
