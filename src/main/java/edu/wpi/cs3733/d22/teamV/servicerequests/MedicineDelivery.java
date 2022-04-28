package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import java.sql.Timestamp;
import java.time.Instant;

public class MedicineDelivery extends ServiceRequest {
  private String medicineName, dosage;
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
      String requestDetails,
      String status,
      String date) {
    if (date.equals("")) {
      this.timeMade = Timestamp.from(Instant.now());
    } else {
      this.timeMade = Timestamp.valueOf(date);
    }
    this.dosage = dosage;
    this.details = requestDetails;
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.status = status;
    this.medicineName = medicineName;
    this.type = "Medicine Delivery Request";
    notes = medicineName + ": " + dosage;
    patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.status = status;
  }

  public MedicineDelivery(
      String nodeID,
      int patientID,
      int employeeID,
      String medicineName,
      String dosage,
      String requestDetails,
      String status) {
    this.timeMade = Timestamp.from(Instant.now());
    this.dosage = dosage;
    this.details = requestDetails;
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.status = status;
    this.medicineName = medicineName;
    this.type = "Medicine Delivery Request";
    notes = medicineName + ": " + dosage;
    patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.status = status;
    setServiceID(RequestSystem.getServiceID());
  }

  public String getMedicineName() {
    return medicineName;
  }

  public String getDosage() {
    return dosage;
  }

  public String getRequestDetails() {
    return details;
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
