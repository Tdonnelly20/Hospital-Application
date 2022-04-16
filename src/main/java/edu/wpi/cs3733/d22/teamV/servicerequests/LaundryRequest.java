package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;

public class LaundryRequest extends ServiceRequest {
  String details;

  public LaundryRequest(
      int employeeID, int patientID, String nodeID, String details, String status) {
    this.location = RequestSystem.getSystem().getLocationDao().getLocation(nodeID);
    this.employee = new Employee(employeeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.details = details;
    this.status = status;
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
}
