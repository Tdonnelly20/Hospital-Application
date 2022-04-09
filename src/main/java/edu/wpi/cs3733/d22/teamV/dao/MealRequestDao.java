package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.MealRequest;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;


public class MealRequestDao extends DaoInterface {
  private static ArrayList<MealRequest> allMealRequests;

  /** Initialize the array list */
  public MealRequestDao() {
    allMealRequests = new ArrayList<MealRequest>();
    // TODO: Add info from the database to the local arraylist
  }

  public void loadFromCSV() throws IOException, SQLException {
    FileReader fr = new FileReader(Vdb.currentPath + "\\MealRequest.csv");
    BufferedReader br = new BufferedReader(fr);
    String splitToken = ","; // what we split the csv file with
    ArrayList<MealRequest> mealRequests = new ArrayList<>();

    String headerLine = br.readLine();
    String line;

    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      MealRequest newDelivery =
          new MealRequest(
              Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], data[3], data[4]);
      newDelivery.setServiceID(Integer.parseInt(data[5]));
      mealRequests.add(newDelivery);
    }

    setAllServiceRequests(mealRequests);
  }

  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(Vdb.currentPath + "\\MealRequest.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("userID,patientID,firstName,lastName,meal,serviceID");

    for (ServiceRequest request : getAllServiceRequests()) {

      MealRequest mealRequest = (MealRequest) request;

      String[] outputData = {
        String.valueOf(mealRequest.getUserID()),
        String.valueOf(mealRequest.getPatientID()),
        mealRequest.getFirstName(),
        mealRequest.getLastName(),
        mealRequest.getMeal(),
        String.valueOf(mealRequest.getServiceID())
      };
      bw.append("\n");
      for (String s : outputData) {
        bw.append(s);
        bw.append(',');
      }
    }

    bw.close();
    fw.close();
  }

  public void createSQLTable() throws SQLException {
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet set = meta.getTables(null, null, "MEALS", new String[] {"TABLE"});
    String query = "";

    if (!set.next()) {
      query =
          "CREATE TABLE MEALS(userID int, patientID int, firstName char(50), lastName char(50), meal char(50), serviceID int)";
      statement.execute(query);

    } else {
      query = "DROP TABLE MEALS";
      statement.execute(query);
      createSQLTable(); // rerun the method to generate new tables
      return;
    }

    for (MealRequest mealRequest : allMealRequests) {
      addToSQLTable(mealRequest);
    }
  }

  public void addToSQLTable(ServiceRequest request) throws SQLException {
    MealRequest mealRequest = (MealRequest) request;

    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query =
        "INSERT INTO MEALS("
            + "userID,patientID,firstName,lastName,meal,serviceID) VALUES "
            + "("
            + mealRequest.getUserID()
            + ","
            + mealRequest.getPatientID()
            + ", '"
            + mealRequest.getFirstName()
            + "','"
            + mealRequest.getLastName()
            + "','"
            + mealRequest.getMeal()
            + "',"
            + mealRequest.getServiceID()
            + ")";

    statement.execute(query);
  }

  public void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query = "DELETE FROM MEALS WHERE serviceID = " + request.getServiceID();
    statement.execute(query);
  }

  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {
    int serviceID = Vdb.getServiceID();
    MealRequest mealRequest = (MealRequest) request;
    mealRequest.setServiceID(serviceID);
    allMealRequests.add(mealRequest); // Store a local copy

    addToSQLTable(request);
    saveToCSV();
  }

  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {
    MealRequest mealRequest = (MealRequest) request;
    allMealRequests.removeIf(value -> value.getServiceID() == mealRequest.getServiceID());
    removeFromSQLTable(request);
    saveToCSV();
  }

  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return allMealRequests;
  }

  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException {
    // Set all medicine deliveries
    allMealRequests = new ArrayList<>();

    // Convert to subtype
    for (ServiceRequest request : serviceRequests) {
      // Cast to subtype
      MealRequest delivery = (MealRequest) request;
      allMealRequests.add(delivery);
    }
  }
}
