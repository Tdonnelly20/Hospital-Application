package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import java.sql.Timestamp;
import java.time.Instant;

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
    this.timeMade = Timestamp.from(Instant.now());
    this.dosage = dosage;
    this.requestDetails = requestDetails;
    this.nodeID = nodeID;
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.status = status;
    this.medicineName = medicineName;
    this.type = "Medicine Delivery Request";
    patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
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
    DirectionalAssoc.link(employee, patient, this);
    updateAllObservers();
  }

  @Override
  public void update(DirectionalAssoc directionalAssoc) {
    super.update(directionalAssoc);
    Vdb.requestSystem
        .getDao(RequestSystem.Dao.MedicineDelivery)
        .updateServiceRequest(this, getServiceID());
  }
}
