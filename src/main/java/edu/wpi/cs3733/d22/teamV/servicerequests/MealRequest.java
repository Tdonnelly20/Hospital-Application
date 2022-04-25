package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import java.sql.Timestamp;
import java.time.Instant;

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
      String requestDetails,
      String status,
      int serviceID,
      String date) {
    if (date != "") {
      this.timeMade = Timestamp.valueOf(date);

    } else {
      this.timeMade = Timestamp.from(Instant.now());
    }
    this.allergy = allergy;
    this.details = requestDetails;
    this.nodeID = nodeID;
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.mealName = mealName;
    notes = "Meal: " + mealName + " Allergies: " + allergy;
    this.type = "Meal Delivery Request";
    this.dao = RequestSystem.Dao.MealRequest;
    this.status = status;
    setServiceID(RequestSystem.getServiceID());
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
