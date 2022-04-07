package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.MedicineDelivery;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class MedicineDeliveryDao implements DaoInterface {
  // A local list of all medicine deliveries, updated via Vdb
  private static ArrayList<MedicineDelivery> allMedicineDeliveries;
  /** Initialize the arraylist */
  public MedicineDeliveryDao() {
    allMedicineDeliveries = new ArrayList<MedicineDelivery>();

    try {
      loadFromCSV();
      createSQLTable();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void loadFromCSV() throws IOException {

    FileReader fr = new FileReader(Vdb.currentPath + "\\MedicineDelivery.csv");
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
              data[1],
              data[2],
              Integer.parseInt(data[3]),
              Integer.parseInt(data[4]),
              data[5],
              data[6],
              data[7]);
      medicineDeliveries.add(newDelivery);
    }

    setAllServiceRequests(medicineDeliveries);

    System.out.println("Medicine delivery database made");
  }

  @Override
  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(Vdb.currentPath + "\\MedicineDelivery.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append(
        "patientFirstName,patientLastName,roomNumber,patientID,hospitalID,medicineName,dosage,requestDetails");

    for (ServiceRequest request : getAllServiceRequests()) {

      MedicineDelivery medicineDelivery = (MedicineDelivery) request;

      String[] outputData = {
        medicineDelivery.getPatientFirstName(),
        medicineDelivery.getPatientLastName(),
        medicineDelivery.getRoomNumber(),
        String.valueOf(medicineDelivery.getPatientID()),
        String.valueOf(medicineDelivery.getEmployeeID()),
        medicineDelivery.getMedicineName(),
        medicineDelivery.getDosage(),
        medicineDelivery.getRequestDetails()
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

  @Override
  public void createSQLTable() throws SQLException {
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet set = meta.getTables(null, null, "MEDICINES", new String[] {"TABLE"});
    String query = "";

    if (!set.next()) {
      System.out.println("Creating Medicine Delivery Tables!");
      query =
          "CREATE TABLE MEDICINES(patientFirstName char(50), patientLastName char(50), roomNumber char(50), patientID int, employeeID int, medicineName char(50), dosage char(50), requestDetails char(254))";
      statement.execute(query);

    } else {
      System.out.println("Medicine Delivery Tables Found! Dropping...");
      query = "DROP TABLE MEDICINES";
      statement.execute(query);
      createSQLTable(); // rerun the method to generate new tables
      return;
    }
  }

  @Override
  public void addToSQLTable(ServiceRequest request) throws SQLException {
    MedicineDelivery medicineDelivery = (MedicineDelivery) request;

    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query =
        "INSERT INTO MEDICINES("
            + "patientFirstName,patientLastName,roomNumber,patientID,employeeID,medicineName,dosage,requestDetails) VALUES "
            + "('"
            + medicineDelivery.getPatientFirstName()
            + "', '"
            + medicineDelivery.getPatientLastName()
            + "', '"
            + medicineDelivery.getRoomNumber()
            + "', "
            + medicineDelivery.getPatientID()
            + ", "
            + medicineDelivery.getEmployeeID()
            + ", '"
            + medicineDelivery.getMedicineName()
            + "','"
            + medicineDelivery.getDosage()
            + "','"
            + medicineDelivery.getRequestDetails()
            + "')";

    System.out.println(query);
    statement.execute(query);

    // Print out all the current entries...
    query =
        "SELECT patientFirstName,patientLastName,roomNumber,patientID,employeeID,medicineName,dosage,requestDetails FROM MEDICINES";

    ResultSet resultSet = statement.executeQuery(query);

    // A string array to contain the names of all the header values so I don't have to type this
    // bullshit out again
    String[] headerVals =
        new String[] {
          "patientFirstName",
          "patientLastName",
          "roomNumber",
          "patientID",
          "employeeID",
          "medicineName",
          "dosage",
          "requestDetails"
        };

    // Print out the result
    while (resultSet.next()) {
      for (String headerVal : headerVals) {
        System.out.print(resultSet.getString(headerVal).trim() + ", ");
      }
      System.out.println();
    }
  }

  @Override
  public void removeFromSQLTable(ServiceRequest request) {}

  @Override
  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {

    MedicineDelivery delivery = (MedicineDelivery) request;
    System.out.println("Adding to local arraylist...");
    allMedicineDeliveries.add(delivery); // Store a local copy

    System.out.println("Adding to database");

    addToSQLTable(request);
    saveToCSV();
  }

  @Override
  public void removeServiceRequest(ServiceRequest request) throws IOException {
    MedicineDelivery medicineDelivery = (MedicineDelivery) request;
    System.out.println(allMedicineDeliveries.size());
    allMedicineDeliveries.removeIf(
        value -> value.getPatientID() == medicineDelivery.getPatientID());
    System.out.println(allMedicineDeliveries.size());
    System.out.println(medicineDelivery.getPatientID());
    removeFromSQLTable(request);
    saveToCSV();
  }

  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {

    return allMedicineDeliveries;
  }

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
