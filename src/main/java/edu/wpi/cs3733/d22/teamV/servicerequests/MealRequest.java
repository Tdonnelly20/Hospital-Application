package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.Vdb;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MealRequest extends ServiceRequest {
  private int employeeID;
  private int patientID;
  private String meal;
  private String nodeID;

  public MealRequest(int employeeID, int patientID, String meal, String nodeID, String status) {
    this.employeeID = employeeID;
    this.patientID = patientID;
    this.location = Vdb.requestSystem.getLocationDao().getLocation(nodeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatientFromID(patientID);
    this.hospitalEmployee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.nodeID = nodeID;
    this.status = status;
    this.meal = meal;
    this.type = "Meal Request";
  }
}
