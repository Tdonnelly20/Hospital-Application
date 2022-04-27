package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import java.sql.Timestamp;
import java.time.Instant;

public class EquipmentDelivery extends ServiceRequest {
  private final String equipment;
  private final int quantity;

  public EquipmentDelivery(
      int employeeID,
      int patientID,
      String nodeID,
      String equipment,
      String notes,
      int quantity,
      String status,
      int serviceID,
      String date) {
    if (date != "") {
      this.timeMade = Timestamp.valueOf(date);

    } else {
      this.timeMade = Timestamp.from(Instant.now());
    }
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.equipment = equipment;
    this.details = notes;
    notes = equipment + "x" + quantity;
    this.type = "Equipment Delivery Request";
    this.quantity = quantity;
    this.status = status;
    setServiceID(RequestSystem.getServiceID());
  }

  /** Used in alert system */
  public EquipmentDelivery(String nodeID, String equipment) {
    this.timeMade = Timestamp.from(Instant.now());
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(-1);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(-1);
    this.equipment = equipment;
    this.details = notes;
    this.type = "Equipment Delivery Request";
    this.quantity = 1;
    this.status = "Not Started";
    setServiceID(RequestSystem.getServiceID());
  }

  public String getEquipment() {
    return equipment;
  }

  public String getDetails() {
    return details;
  }

  public int getQuantity() {
    return quantity;
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
        .getDao(RequestSystem.Dao.EquipmentDelivery)
        .updateServiceRequest(this, getServiceID());
  }
}
