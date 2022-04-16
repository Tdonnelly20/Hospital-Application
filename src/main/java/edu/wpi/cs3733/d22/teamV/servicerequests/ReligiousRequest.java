package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;

public class ReligiousRequest extends ServiceRequest {
  private int patientID;
  private int employeeID;
  private int roomNumber;
  private String religion;
  private int serviceID;

  /**
   * @param employeeID
   * @param patientID
   * @param religion
   * @param specialRequests
   */
  public ReligiousRequest(
      int patientID, int employeeID, String roomLocation, String religion, String specialRequests) {
    this.patientID = employeeID;
    this.location = Vdb.requestSystem.getLocation(roomLocation);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.employeeID = patientID;
    this.religion = religion;
    this.type = "Religious Request";
  }

  public int getPatientID() {
    return patientID;
  }

  public int getServiceID() {
    return serviceID;
  }

  public void setServiceID(int serviceID) {
    super.setServiceID(serviceID);
    DirectionalAssoc.link(employee, patient, this);
    updateAllObservers();
  }

  public int getEmpID() {
    return employeeID;
  }

  public String getReligion() {
    return religion;
  }
}
