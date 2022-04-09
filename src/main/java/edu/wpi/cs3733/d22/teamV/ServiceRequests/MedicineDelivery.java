package edu.wpi.cs3733.d22.teamV.ServiceRequests;

import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.HospitalEmployee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;

public class MedicineDelivery extends ServiceRequest {
  private Patient patient;
  private HospitalEmployee employee;
  private String medicineName, nodeID, dosage, requestDetails;
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
      String nodeID,
      int patientID,
      int hospitalID,
      String medicineName,
      String dosage,
      String requestDetails) {
    this.dosage = dosage;
    this.requestDetails = requestDetails;
    this.nodeID = nodeID;
    this.location = Vdb.locationDao.getLocation(nodeID);
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

  public int getEmployeeID() {
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

  public String getNodeID() {
    try {
      return location.getNodeID();
    } catch (NullPointerException e) {
      return "(Not found)" + nodeID;
    }
  }

  public void setServiceID(int serviceID) {
    super.setServiceID(serviceID);
  }
}
