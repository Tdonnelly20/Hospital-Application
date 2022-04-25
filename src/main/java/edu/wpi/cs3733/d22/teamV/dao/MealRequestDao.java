package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.MealRequest;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class MealRequestDao extends DaoInterface {
  // A local list of all medicine deliveries, updated via Vdb
  private static ArrayList<MealRequest> allMealDeliveries;
  /** Initialize the arraylist */
  public MealRequestDao() {
    allMealDeliveries = new ArrayList<MealRequest>();
    loadFromCSV();
    createSQLTable();
  }

  public void loadFromCSV() {
    try {

      FileReader fr = new FileReader(VApp.currentPath + "/MealRequest.csv");
      BufferedReader br = new BufferedReader(fr);
      String splitToken = ","; // what we split the csv file with
      ArrayList<MealRequest> mealDeliveries = new ArrayList<>();

      String headerLine = br.readLine();
      String line;

      while ((line = br.readLine()) != null) // should create a database based on csv file
      {
        String[] data = line.split(splitToken);
        MealRequest newDelivery =
            new MealRequest(
                data[0],
                Integer.parseInt(data[1]),
                Integer.parseInt(data[2]),
                data[3],
                data[4],
                data[5],
                data[6],
                Integer.parseInt(data[7]),
                data[8]);

        newDelivery.setServiceID(Integer.parseInt(data[7]));
        mealDeliveries.add(newDelivery);
      }

      setAllServiceRequests(mealDeliveries);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void saveToCSV() {
    try {

      FileWriter fw = new FileWriter(VApp.currentPath + "/MealRequest.csv");
      BufferedWriter bw = new BufferedWriter(fw);
      bw.append("nodeID,patientID,employeeID,mealName,allergy,status,requestDetails,serviceID");

      for (ServiceRequest request : getAllServiceRequests()) {

        MealRequest mealDelivery = (MealRequest) request;

        String[] outputData = {
          mealDelivery.getLocation().getNodeID(),
          String.valueOf(mealDelivery.getPatientID()),
          String.valueOf(mealDelivery.getEmployeeID()),
          mealDelivery.getMealName(),
          mealDelivery.getAllergy(),
          mealDelivery.getStatus(),
          mealDelivery.getRequestDetails(),
          String.valueOf(mealDelivery.getServiceID()),
          mealDelivery.getTimeMade().toString()
        };
        bw.append("\n");
        for (String s : outputData) {
          bw.append(s);
          bw.append(',');
        }
      }

      bw.close();
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void createSQLTable() {
    try {

      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "MEALS", new String[] {"TABLE"});
      String query = "";

      if (!set.next()) {
        query =
            "CREATE TABLE MEALS(nodeID char(50), patientID int, employeeID int, mealName char(50), allergy char(50), requestDetails char(254),status char(50),  serviceID int,date_time timestamp )";
        statement.execute(query);

      } else {
        query = "DROP TABLE MEALS";
        statement.execute(query);
        createSQLTable(); // rerun the method to generate new tables
        return;
      }

      for (MealRequest mealDelivery : allMealDeliveries) {
        addToSQLTable(mealDelivery);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addToSQLTable(ServiceRequest request) {

    try {
      Connection connection = Vdb.Connect();
      MealRequest mealDelivery = (MealRequest) request;
      String query = "INSERT INTO MEALS VALUES(?,?,?,?,?,?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, mealDelivery.getLocation().getNodeID());
      statement.setInt(2, mealDelivery.getPatientID());
      statement.setInt(3, mealDelivery.getEmployeeID());
      statement.setString(4, mealDelivery.getMealName());
      statement.setString(5, mealDelivery.getAllergy());
      statement.setString(6, mealDelivery.getDetails());

      statement.setString(7, mealDelivery.getStatus());
      statement.setInt(8, mealDelivery.getServiceID());
      statement.setTimestamp(9, mealDelivery.getTimeMade());
      statement.executeUpdate(); // uninit params

      /*

           String query = "";
           Connection connection = Vdb.Connect();
           assert connection != null;
           Statement statement = connection.createStatement();
           query =
               "INSERT INTO MEALS("
                   + "nodeID,patientID,employeeID,mealName,allergy,requestDetails,status,serviceID,date_time) VALUES "
                   + "('"
                   + mealDelivery.getLocation().getNodeID()
                   + "', "
                   + mealDelivery.getPatientID()
                   + ", "
                   + mealDelivery.getEmployeeID()
                   + ", '"
                   + mealDelivery.getMealName()
                   + "','"
                   + mealDelivery.getAllergy()
                   + "','"
                   + mealDelivery.getRequestDetails()
                   + "','"
                   + mealDelivery.getStatus()
                   + "',"
                   + mealDelivery.getServiceID()
                   + "','"
                   + mealDelivery.getTimeMade()
                   + ")";

           statement.execute(query);

      */

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateServiceRequest(ServiceRequest request, int serviceID) {
    MealRequest delivery = (MealRequest) request;
    delivery.setServiceID(serviceID);
    removeServiceRequest(delivery);
    allMealDeliveries.add(delivery);
    addToSQLTable(delivery);
    saveToCSV();
  }

  @Override
  public void removeFromSQLTable(ServiceRequest request) {
    try {

      String query = "";
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();

      query = "DELETE FROM MEALS WHERE serviceID = " + request.getServiceID();
      statement.execute(query);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addServiceRequest(ServiceRequest request) {
    int serviceID = RequestSystem.getServiceID();
    MealRequest mealDelivery = (MealRequest) request;
    mealDelivery.setServiceID(serviceID);
    allMealDeliveries.add(mealDelivery); // Store a local copy

    addToSQLTable(request);
    saveToCSV();
  }

  @Override
  public void removeServiceRequest(ServiceRequest request) {
    MealRequest mealDelivery = (MealRequest) request;
    allMealDeliveries.removeIf(value -> value.getServiceID() == mealDelivery.getServiceID());
    request.detachAll();
    removeFromSQLTable(request);
    saveToCSV();
  }

  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {

    return allMealDeliveries;
  }

  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests) {
    // Set all meal deliveries
    allMealDeliveries = new ArrayList<>();

    // Convert to subtype
    for (ServiceRequest request : serviceRequests) {
      // Cast to subtype
      MealRequest delivery = (MealRequest) request;
      allMealDeliveries.add(delivery);
    }
  }
}
