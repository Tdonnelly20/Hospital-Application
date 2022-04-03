package edu.wpi.veganvampires.objects;

public class MealRequest {
  private Patient patient;
  private HospitalEmployee employee;
  private String mealName, allergy, roomNumber, requestDetails;

  /**
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param hospitalID
   * @param mealName
   * @param allergy
   * @param requestDetails
   */
  public MealRequest(
      String patientFirstName,
      String patientLastName,
      String roomNumber,
      int patientID,
      int hospitalID,
      String mealName,
      String allergy,
      String requestDetails) {
    this.allergy = allergy;
    this.requestDetails = requestDetails;
    this.roomNumber = roomNumber;
    patient = new Patient(patientID, patientFirstName, patientLastName);
    employee = new HospitalEmployee(hospitalID);
    this.mealName = mealName;
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

  public String getMealName() {
    return mealName;
  }

  public String getAllergy() {
    return allergy;
  }

  public String getRequestDetails() {
    return requestDetails;
  }

  public String getRoomNumber() {
    return roomNumber;
  }
}
