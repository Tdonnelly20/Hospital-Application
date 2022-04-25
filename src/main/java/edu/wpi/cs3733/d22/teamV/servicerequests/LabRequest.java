package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

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

  public LabRequest(
      int userID,
      int patientID,
      String nodeID,
      String lab,
      String status,
      int serviceID,
      String date) {
    if (date != "") {
      this.timeMade = Timestamp.valueOf(date);

    } else {
      this.timeMade = Timestamp.from(Instant.now());
    }
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    // System.out.println(patient.getFirstName() + " " + patient.getLastName());
    this.employee = RequestSystem.getSystem().getEmployeeDao().getEmployee(userID);
    notes = lab;
    this.lab = lab;
    this.patientID = patientID;
    this.userID = userID;
    this.type = "Lab Request";
    this.status = status;
    setServiceID(RequestSystem.getServiceID());
    setServiceID(RequestSystem.getServiceID());
  }

  public String getPatientFirstName() {
    return patient.getFirstName();
  }

  public String getPatientLastName() {
    return patient.getLastName();
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
