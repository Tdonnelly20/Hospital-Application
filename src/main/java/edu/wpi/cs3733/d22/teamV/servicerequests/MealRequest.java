package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;

public class MealRequest extends ServiceRequest {
  private String mealName, nodeID, allergy, status;
  /**
   * @param patientID
   * @param employeeID
   * @param mealName
   * @param allergy
   * @param requestDetails
   */
  public MealRequest(
      String nodeID,
      int patientID,
      int employeeID,
      String mealName,
      String allergy,
      String status,
      String requestDetails) {
    this.allergy = allergy;
    this.details = requestDetails;
    this.nodeID = nodeID;
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.status = status;
    patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.mealName = mealName;
    notes = "Meal: " + mealName + " Allergies: " + allergy;
    this.type = "Meal Delivery Request";
    this.dao = RequestSystem.Dao.MealRequest;
  }

  public String getPatientFirstName() {
    return patient.getFirstName();
  }

  public String getPatientLastName() {
    return patient.getLastName();
  }

  public int getPatientID() {
    return patient.getPatientID();
  }

  public int getEmployeeID() {
    return employee.getEmployeeID();
  }

  public String getMealName() {
    return mealName;
  }

  public String getAllergy() {
    return allergy;
  }

  public String getRequestDetails() {
    return details;
  }

  public String getStatus() {
    return status;
  }

  public String getNodeID() {
    return nodeID;
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
        .getDao(RequestSystem.Dao.MealRequest)
        .updateServiceRequest(this, getServiceID());
  }
}
