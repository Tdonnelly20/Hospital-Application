package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.util.ArrayList;

public abstract class DaoInterface {

  public abstract void loadFromCSV();

  public abstract void saveToCSV();

  public abstract void createSQLTable();

  public abstract void addToSQLTable(ServiceRequest request);

  public abstract void updateServiceRequest(ServiceRequest request, int serviceID);

  public abstract void removeFromSQLTable(ServiceRequest request);

  public abstract void addServiceRequest(ServiceRequest request);

  public abstract void removeServiceRequest(ServiceRequest request);

  public abstract ArrayList<? extends ServiceRequest> getAllServiceRequests();

  public abstract void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests);
}
