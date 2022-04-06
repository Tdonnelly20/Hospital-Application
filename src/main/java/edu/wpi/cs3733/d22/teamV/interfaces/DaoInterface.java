package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;

public interface DaoInterface {

  void createDB();

  void createTables();

  void addToTable(ServiceRequest request);

  void removeFromTable();

  void update(ServiceRequest request);

  void saveToDB();
}
