package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DaoInterface {

  void loadFromCSV() throws IOException;

  void saveToCSV() throws IOException;

  void createSQLTable() throws SQLException;

  void addToSQLTable(ServiceRequest request) throws SQLException;

  void removeFromSQLTable(int serviceID);

  void addServiceRequest(ServiceRequest request) throws IOException, SQLException;

  void removeServiceRequest(int serviceID) throws IOException;

  ArrayList<? extends ServiceRequest> getAllServiceRequests();

  void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests);
}
