package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;

public class RobotRequest extends ServiceRequest {
  private Employee employee;
  private String details, nodeID, status;
  private int botID;

  public RobotRequest(int hospitalID, int botID, String nodeID, String details, String status) {
    employee = new Employee(hospitalID);
    this.botID = botID;
    this.nodeID = nodeID;
    this.details = details;
    this.status = status;
  }

  public int getEmployeeID() {
    return employee.getEmployeeID();
  }

  public int getBotID() {
    return botID;
  }

  public String getNodeID() {
    return nodeID;
  }

  public String getDetails() {
    return details;
  }

  public String getStatus() {
    return status;
  }

  @Override
  public void update(DirectionalAssoc directionalAssoc) {
    super.update(directionalAssoc);
    Vdb.requestSystem
        .getDao(RequestSystem.Dao.RobotRequest)
        .updateServiceRequest(this, getServiceID());
  }
}
