package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
public class LabRequest extends ServiceRequest {
  private final Patient patient;
  private final String lab;
  private String status;
  private int userID;
  private int patientID;
  private String firstName;
  private String lastName;

  public LabRequest(int userID, int patientID, String nodeID, String lab, String status) {
    this.timeMade= Timestamp.from(Instant.now());
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    // System.out.println(patient.getFirstName() + " " + patient.getLastName());
    this.employee = RequestSystem.getSystem().getEmployeeDao().getEmployee(userID);

    this.lab = lab;
    this.status = status;
    this.patientID = patientID;
    this.userID = userID;
    this.type = "Lab Request";
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
    return patient.getLastName();
  }

  public String getNodeID() {
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
        .getDao(RequestSystem.Dao.LabRequest)
        .updateServiceRequest(this, getServiceID());
  }
}
