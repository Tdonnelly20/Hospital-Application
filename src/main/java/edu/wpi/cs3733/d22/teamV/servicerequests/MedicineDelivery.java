package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;

public class MedicineDelivery extends ServiceRequest {
  private Patient patient;
  private Employee employee;
  private String medicineName, nodeID, dosage, status, requestDetails;
  /**
   * @param patientID
   * @param employeeID
   * @param medicineName
   * @param dosage
   * @param requestDetails
   */
  public MedicineDelivery(
      String nodeID,
      int patientID,
      int employeeID,
      String medicineName,
      String dosage,
      String status,
      String requestDetails) {
    this.dosage = dosage;
    this.requestDetails = requestDetails;
    this.nodeID = nodeID;
    this.location = Vdb.requestSystem.getLocations().get(location.getID());
    this.status = status;
    patient = Vdb.requestSystem.getPatients().get(patientID);
    employee = Vdb.requestSystem.getEmployees().get(employeeID);
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
    return employee.getEmployeeID();
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

  public String getStatus() {
    return status;
  }

  public String getNodeID() {
    return nodeID;
  }

  public void setServiceID(int serviceID) {
    super.setServiceID(serviceID);
  }
}
