package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.MedicineDelivery;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class MedicineDeliveryDao extends DaoInterface {
  // A local list of all medicine deliveries
  private static ArrayList<MedicineDelivery> allMedicineDeliveries;

  /**
   *Create the SQL table, then load all the files into from the CSV
   */
  public MedicineDeliveryDao() {
    allMedicineDeliveries = new ArrayList<>();
    createSQLTable();
    loadFromCSV();
  }

  /**
   * Load all the service requests from the CSV
   */
  public void loadFromCSV() {
    try {

      FileReader fr = new FileReader(VApp.currentPath + "/MedicineDelivery.csv");
      BufferedReader br = new BufferedReader(fr);
      String splitToken = ","; // what we split the csv file with
      ArrayList<MedicineDelivery> medicineDeliveries = new ArrayList<>();

      String headerLine = br.readLine();
      String line;

      while ((line = br.readLine()) != null) // should create a database based on csv file
      {
        String[] data = line.split(splitToken);
        MedicineDelivery newDelivery =
            new MedicineDelivery(
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
        medicineDeliveries.add(newDelivery);
      }

      setAllServiceRequests(medicineDeliveries);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Save all service requests in the arraylist to the CSV
   */
  @Override
  public void saveToCSV() {
    try {

      FileWriter fw = new FileWriter(VApp.currentPath + "/MedicineDelivery.csv");
      BufferedWriter bw = new BufferedWriter(fw);
      bw.append("nodeID,patientID,employeeID,medicineName,dosage,status,requestDetails,serviceID");

      for (ServiceRequest request : getAllServiceRequests()) {

        MedicineDelivery medicineDelivery = (MedicineDelivery) request;

        String[] outputData = {
          medicineDelivery.getLocation().getNodeID(),
          String.valueOf(medicineDelivery.getPatientID()),
          String.valueOf(medicineDelivery.getEmployeeID()),
          medicineDelivery.getMedicineName(),
          medicineDelivery.getDosage(),
          medicineDelivery.getStatus(),
          medicineDelivery.getRequestDetails(),
          String.valueOf(medicineDelivery.getServiceID()),
          medicineDelivery.getTimeMade().toString()
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

  /**
   * Create the SQL table
   */
  @Override
  public void createSQLTable() {
    try {

      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "MEDICINES", new String[] {"TABLE"});
      String query = "";

      if (!set.next()) {
        query =
            "CREATE TABLE MEDICINES(nodeID char(50), patientID int, employeeID int, medicineName char(50), dosage char(50),  requestDetails char(254),status char(50), serviceID int,date_time timestamp )";
        statement.execute(query);

      } else {
        query = "DROP TABLE MEDICINES";
        statement.execute(query);
        createSQLTable(); // rerun the method to generate new tables
        return;
      }

      for (MedicineDelivery medicineDelivery : allMedicineDeliveries) {
        addToSQLTable(medicineDelivery);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add a specific request to the SQL table
   * @param request
   */
  @Override
  public void addToSQLTable(ServiceRequest request) {
    try {
      Connection connection = Vdb.Connect();
      MedicineDelivery medicineDelivery = (MedicineDelivery) request;
      String query = "INSERT INTO MEDICINES VALUES(?,?,?,?,?,?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, medicineDelivery.getLocation().getNodeID());
      statement.setInt(2, medicineDelivery.getPatientID());
      statement.setInt(3, medicineDelivery.getEmployeeID());
      statement.setString(4, medicineDelivery.getMedicineName());
      statement.setString(5, medicineDelivery.getDosage());
      statement.setString(6, medicineDelivery.getDetails());

      statement.setString(7, medicineDelivery.getStatus());
      statement.setInt(8, medicineDelivery.getServiceID());
      statement.setTimestamp(9, medicineDelivery.getTimeMade());
      statement.executeUpdate(); // uninit params

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Replace or update a service request with new info. Will replace the selected serviceID with the request
   * @param request
   * @param serviceID
   */
  @Override
  public void updateServiceRequest(ServiceRequest request, int serviceID) {
    MedicineDelivery delivery = (MedicineDelivery) request;
    delivery.setServiceID(serviceID);
    removeServiceRequest(delivery);
    allMedicineDeliveries.add(delivery);
    addToSQLTable(delivery);
    saveToCSV();
  }

  /**
   * Remove a specific request from the SQL table
   * @param request
   */
  @Override
  public void removeFromSQLTable(ServiceRequest request) {
    try {
      String query = "";
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();

      query = "DELETE FROM MEDICINES WHERE serviceID = " + request.getServiceID();
      statement.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add a request to the arraylist, then the SQL table, then save to the CSV
   * @param request
   */
  @Override
  public void addServiceRequest(ServiceRequest request) {
    int serviceID = RequestSystem.getServiceID();
    MedicineDelivery medicineDelivery = (MedicineDelivery) request;
    medicineDelivery.setServiceID(serviceID);
    allMedicineDeliveries.add(medicineDelivery); // Store a local copy

    addToSQLTable(request);
    saveToCSV();
  }

  /**
   * Remove a service request from the arraylist, then the SQL table, then save to the CSV
   * @param request
   */
  @Override
  public void removeServiceRequest(ServiceRequest request) {
    MedicineDelivery medicineDelivery = (MedicineDelivery) request;
    allMedicineDeliveries.removeIf(
        value -> value.getServiceID() == medicineDelivery.getServiceID());
    request.detachAll();
    removeFromSQLTable(request);
    saveToCSV();
  }

  /**
   * Get a list of all medicine deliveries
   * @return
   */
  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {

    return allMedicineDeliveries;
  }

  /**
   * Set the list of all service requests
   * @param serviceRequests
   */
  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests) {
    // Set all medicine deliveries
    allMedicineDeliveries = new ArrayList<>();

    // Convert to subtype
    for (ServiceRequest request : serviceRequests) {
      // Cast to subtype
      MedicineDelivery delivery = (MedicineDelivery) request;
      allMedicineDeliveries.add(delivery);
    }
  }
}
