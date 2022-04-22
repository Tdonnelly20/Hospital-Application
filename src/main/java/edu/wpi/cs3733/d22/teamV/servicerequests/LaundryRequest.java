package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import java.sql.Timestamp;
import java.time.Instant;

public class LaundryRequest extends ServiceRequest {
  String details;

  public LaundryRequest(
      int employeeID, int patientID, String nodeID, String details, String status) {
    this.timeMade = Timestamp.from(Instant.now());
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.employee = RequestSystem.getSystem().getEmployeeDao().getEmployee(employeeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.details = details;
    this.status = status;
    this.type = "Laundry Request";
  }

  public String getDetails() {
    return details;
  }

  public int getEmployeeID() {
    return employee.getEmployeeID();
  }

  public int getPatientID() {
    return patient.getPatientID();
  }

  public String getFirstName() {
    return patient.getFirstName();
  }

  public String getLastName() {
    return patient.getFirstName();
  }

  public String getLocationID() {
    return location.getNodeID();
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
        .getDao(RequestSystem.Dao.LaundryRequest)
        .updateServiceRequest(this, getServiceID());
  }
}
