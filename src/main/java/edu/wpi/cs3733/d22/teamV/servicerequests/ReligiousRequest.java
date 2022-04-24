package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;

public class ReligiousRequest extends ServiceRequest {
  private String religion;
  /**
   * @param employeeID
   * @param patientID
   * @param religion
   * @param specialRequests
   */
  public ReligiousRequest(
      int patientID, int employeeID, String roomLocation, String religion, String specialRequests) {
    this.location = Vdb.requestSystem.getLocation(roomLocation);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.religion = religion;
    this.details = specialRequests;
    this.type = "Religious Request";
    status = "Not Started";
    notes = religion;
    this.dao = RequestSystem.Dao.ReligiousRequest;
  }

  public int getPatientID() {
    return patient.getPatientID();
  }

  public int getEmployeeID() {
    return employee.getEmployeeID();
  }

  public String getRoomNumber() {
    return location.getShortName();
  }

  public String getFirstName() {
    return patient.getFirstName();
  }

  public String getLastName() {
    return patient.getLastName();
  }

  public void setServiceID(int serviceID) {
    super.setServiceID(serviceID);
    DirectionalAssoc.link(employee, patient, this);
    updateAllObservers();
  }

  public String getReligion() {
    return religion;
  }

  @Override
  public void update(DirectionalAssoc directionalAssoc) {
    super.update(directionalAssoc);
    Vdb.requestSystem
        .getDao(RequestSystem.Dao.ReligiousRequest)
        .updateServiceRequest(this, getServiceID());
  }

  public String getDetails() {
    return details;
  }
}
