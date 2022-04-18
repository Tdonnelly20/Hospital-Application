package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;

public class MedicineDelivery extends ServiceRequest {
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
    this.location = Vdb.requestSystem.getLocationDao().getLocation(nodeID);
    this.status = status;
    patient = Vdb.requestSystem.getPatientDao().getPatientFromID(patientID);
    hospitalEmployee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.medicineName = medicineName;
    this.type = "Medicine Delivery";
    this.dao = RequestSystem.Dao.MedicineDelivery;
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
    return hospitalEmployee.getEmployeeID();
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
