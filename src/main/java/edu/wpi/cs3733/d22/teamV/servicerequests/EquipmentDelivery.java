package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;

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
      String status) {
    System.out.println(Vdb.requestSystem);
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.equipment = equipment;
    this.details = notes;
    notes = equipment + "x" + quantity;
    this.type = "Equipment Delivery Request";
    this.quantity = quantity;
    this.status = status;
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

  public String getPatientFirstName() {
    return patient.getFirstName();
  }

  public String getPatientLastName() {
    return patient.getLastName();
  }

  public String getLocationName() {
    return location.getNodeID();
  }

  public int getEmployeeID() {
    return super.getEmployee().getEmployeeID();
  }

  public int getPatientID() {
    return patient.getPatientID();
  }

  @Override
  public void update(DirectionalAssoc directionalAssoc) {
    super.update(directionalAssoc);
    Vdb.requestSystem
        .getDao(RequestSystem.Dao.EquipmentDelivery)
        .updateServiceRequest(this, getServiceID());
  }
}
