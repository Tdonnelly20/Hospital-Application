package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class DaoInterface {

  public abstract void loadFromCSV() throws IOException, SQLException;

  public abstract void saveToCSV() throws IOException;

  public abstract void createSQLTable() throws SQLException;

  public abstract void addToSQLTable(ServiceRequest request) throws SQLException;

  //It's that easy!
  public void updateRequest(ServiceRequest request) throws SQLException, IOException{
    removeServiceRequest(request);
    addServiceRequest(request);
  }

  public abstract void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException;

  public abstract void addServiceRequest(ServiceRequest request) throws IOException, SQLException;

  public abstract void removeServiceRequest(ServiceRequest request)
      throws IOException, SQLException;

  public abstract ArrayList<? extends ServiceRequest> getAllServiceRequests();

  public abstract void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException;
}
