package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.ComputerRequest;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.io.IOException;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Predicate;

public class ComputerRequestDao extends DaoInterface {
  private static ArrayList<ComputerRequest>
      allComputerRequests; // A local list of all Computer requests, updated via Vdb

  /** Initialize the arraylist */
  public ComputerRequestDao() {
    allComputerRequests = new ArrayList<ComputerRequest>();
    loadFromCSV();
    createSQLTable();
  }

  @Override
  public void loadFromCSV() {
    try {
      String line = "";
      FileReader fr = null;

      fr = new FileReader(VApp.currentPath + "/ComputerRequest.CSV");

      BufferedReader br = new BufferedReader(fr);
      String headerLine = br.readLine();
      String splitToken = ",";
      ArrayList<ComputerRequest> requests = new ArrayList<>();
      while ((line = br.readLine()) != null) // should create a database based on csv file
      {
        String[] data;
        data = line.split(splitToken);
        ComputerRequest request =
            new ComputerRequest(
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]),
                data[2],
                data[3],
                data[4],
                data[5],
                Integer.parseInt(data[6]),
                data[7]);
        requests.add(request);
      }
      allComputerRequests = requests;

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void saveToCSV() {
    FileWriter fw = null;
    try {
      fw = new FileWriter(VApp.currentPath + "/ComputerRequest.csv");
      BufferedWriter bw = new BufferedWriter(fw);
      bw.append("PatientID,EmpID,Location,Type,Details,status,serviceID,Date");

      for (ServiceRequest request : getAllServiceRequests()) {
        ComputerRequest computerRequest = (ComputerRequest) request;
        String[] outputData = {
          Integer.toString(computerRequest.getPatientID()),
          Integer.toString(computerRequest.getEmployeeID()),
          computerRequest.getNodeID(),
          computerRequest.getTypeName(),
          computerRequest.getRequestDetails(),
          computerRequest.getStatus(),
          Integer.toString(computerRequest.getServiceID()),
          computerRequest.getTimeMade().toString()
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
      String query = "";
      Connection connection = Vdb.Connect();
      Statement statement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();

      ResultSet set = meta.getTables(null, null, "COMPUTERREQUESTS", new String[] {"TABLE"});
      if (!set.next()) {
        statement.execute(
            "CREATE TABLE COMPUTERREQUESTS(pID int, empID int, roomLocation char(40), type char(30), details char(150), status char(50),serviceID int, date_time timestamp )");
        // System.out.println(r);
      } else {
        statement.execute("DROP TABLE COMPUTERREQUESTS");
        createSQLTable();
        return;
      }
      for (ComputerRequest req : allComputerRequests) {
        addToSQLTable(req);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addToSQLTable(ServiceRequest Request) {
    try {
      ComputerRequest newComputerRequest = (ComputerRequest) Request;
      Connection connection = Vdb.Connect();
      String query = "INSERT INTO COMPUTERREQUESTS VALUES(?,?,?,?,?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, newComputerRequest.getPatientID());
      statement.setInt(2, newComputerRequest.getEmployeeID());
      statement.setString(3, newComputerRequest.getNodeID());
      statement.setString(4, newComputerRequest.getTypeName());
      statement.setString(5, newComputerRequest.getRequestDetails());
      statement.setString(6, newComputerRequest.getStatus());
      statement.setInt(7, newComputerRequest.getServiceID());
      statement.setTimestamp(8, newComputerRequest.getTimeMade());
      statement.executeUpdate();
      statement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateServiceRequest(ServiceRequest request, int serviceID) {
    ComputerRequest newRequest = (ComputerRequest) request;
    request.setServiceID(serviceID);
    int index = -1;
    for (int i = 0; i < allComputerRequests.size(); i++) {
      if (allComputerRequests.get(i).getServiceID() == request.getServiceID()) {
        index = i;
        break;
      }
    }
    if (index > -1) {
      System.out.println("UPDATING");
      allComputerRequests.set(index, newRequest);
      saveToCSV();
      updateSQLTable(request);
    }
  }
  // fname char(15),lname char(15), pID int, empID int, roomLocation char(40), type char(30),
  // details char(150),serviceID int)");
  public void updateSQLTable(ServiceRequest request) {
    // also needs to add to csv

    try {

      ComputerRequest newRequest = (ComputerRequest) request;
      Connection connection = Vdb.Connect();
      String query =
          "UPDATE COMPUTERREQUESTS SET pID=?, empID=?,roomLocation=?,type=?,details=?, status=?, date_time=? WHERE serviceID=?";
      PreparedStatement statement = connection.prepareStatement(query); // error here?
      statement.setInt(1, newRequest.getPatientID());
      statement.setInt(2, newRequest.getEmployeeID());
      statement.setString(3, newRequest.getNodeID());
      statement.setString(4, newRequest.getTypeName());
      statement.setString(5, newRequest.getRequestDetails());
      statement.setString(6, newRequest.getStatus());
      statement.setTimestamp(7, newRequest.getTimeMade());
      statement.setInt(8, newRequest.getServiceID());

      statement.executeUpdate();
      statement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeFromSQLTable(ServiceRequest request) {
    try {

      int serviceID = request.getServiceID();
      Connection connection = Vdb.Connect();
      String query = "DELETE FROM COMPUTERREQUESTS" + " WHERE serviceID=?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, serviceID);
      statement.executeUpdate();
      statement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addServiceRequest(ServiceRequest request) {
    ComputerRequest newRequest = (ComputerRequest) request;
    request.setServiceID(newRequest.getServiceID());
    allComputerRequests.add(newRequest);
    addToSQLTable(request);
    saveToCSV();
  }

  @Override
  public void removeServiceRequest(ServiceRequest request) {
    int serviceID = request.getServiceID();
    Predicate<ComputerRequest> condition =
        ComputerRequest -> ComputerRequest.getServiceID() == serviceID;
    boolean result = allComputerRequests.removeIf(condition);
    request.detachAll();
    removeFromSQLTable(request);
    saveToCSV();
  }

  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return allComputerRequests;
  }

  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests) {
    allComputerRequests.clear();
    for (ServiceRequest r : serviceRequests) {
      ComputerRequest request = (ComputerRequest) r;
      allComputerRequests.add(request);
    }
    createSQLTable();
  }
}
