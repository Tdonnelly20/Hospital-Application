package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmployeeDao {
  private static ArrayList<Employee> allEmployees;

  public EmployeeDao() {
    allEmployees = new ArrayList<>();
    try {
      loadFromCSV();
      createSQLTable();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void loadFromCSV() throws IOException {
    String line = "";
    String file = VApp.currentPath + "\\Employees.csv";
    FileReader fr = new FileReader(file);
    BufferedReader br = new BufferedReader(fr);
    String splitToken = ","; // what we split the csv file with
    ArrayList<Employee> employees = new ArrayList<>();
    String headerLine = br.readLine();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);

      // LOOK AT THIS PIECE OF SHIT CODE I MADE. LOOK AT IT. ITS AMAZING
      ArrayList<Integer> patientIDs =
          IntStream.of(Arrays.stream(data[5].split(" ")).mapToInt(Integer::parseInt).toArray())
              .boxed()
              .collect(Collectors.toCollection(ArrayList::new));
      ArrayList<Integer> serviceIDs =
          IntStream.of(Arrays.stream(data[6].split(" ")).mapToInt(Integer::parseInt).toArray())
              .boxed()
              .collect(Collectors.toCollection(ArrayList::new));

      Employee newEmployee =
          new Employee(
              Integer.parseInt(data[0]),
              data[1],
              data[2],
              data[3],
              new ArrayList(Arrays.asList(data[4].split(" "))), // FIGHT ME I HATE FOR LOOPS
              patientIDs,
              serviceIDs,
              true);

      employees.add(newEmployee);
    }
    allEmployees = employees;
  }

  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(VApp.currentPath + "\\Employees.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append(
        "employeeID,employeeFirstName,employeeLastName,employeePosition,employeeSpecialties,patientIDs,serviceRequestIDs");
    for (Employee e : getAllEmployees()) {

      String specialties = "";
      String patientIDs = "";
      String serviceIDs = "";

      for (String str : e.getSpecialties()) {
        specialties += str + " ";
      }

      for (int ID : e.getPatientIDs()) {
        patientIDs += ID + " ";
      }

      for (int ID : e.getServiceRequestIDs()) {
        serviceIDs += ID + " ";
      }

      String[] outputData = {
        String.valueOf(e.getEmployeeID()),
        e.getFirstName(),
        e.getLastName(),
        e.getEmployeePosition(),
        specialties,
        patientIDs,
        serviceIDs
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

  public Employee getEmployee(int employeeID) {
    for (Employee e : allEmployees) {
      if (e.getEmployeeID() == employeeID) {
        return e;
      }
    }
    System.out.print("Unable to find employee with ID:" + employeeID);
    return null;
  }

  public void addEmployee(Employee employee) throws IOException, SQLException {
    allEmployees.add(employee);
    addToSQLTable(employee);
    saveToCSV();
  }

  public void removeEmployee(Employee employee) throws IOException, SQLException {
    allEmployees.removeIf(currEmployee -> employee.getEmployeeID() == currEmployee.getEmployeeID());
    removeFromSQLTable(employee);
    saveToCSV();
  }

  public ArrayList<Employee> getAllEmployees() {
    return allEmployees;
  }

  public void createSQLTable() throws SQLException {
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet set = meta.getTables(null, null, "EMPLOYEES", new String[] {"TABLE"});
    if (!set.next()) {
      statement.execute(
          "CREATE TABLE Employees(employeeID int, employeeFirstName char(30), employeeLastName char(30), employeePosition char(30), employeeSpecialties varchar(1000), patientIDs varchar(1000), serviceRequestIDs varchar(1000))");

    } else {
      statement.execute("DROP TABLE EMPLOYEES");
      createSQLTable();
      return;
    }

    for (Employee employee : allEmployees) {
      addToSQLTable(employee);
    }
  }

  public void addToSQLTable(Employee employee) throws SQLException {
    Connection connection = Vdb.Connect();
    Statement exampleStatement = connection.createStatement();
    String query =
        "INSERT INTO EMPLOYEES(employeeID, employeeFirstName, employeeLastName, employeePosition, employeeSpecialties, patientIDs, serviceRequestIDs) VALUES ("
            + employee.getEmployeeID()
            + ",'"
            + employee.getFirstName()
            + "','"
            + employee.getLastName()
            + "', '"
            + "Employee (Placeholder)"
            + "','";

    // add specialties
    for (String str : employee.getSpecialties()) {
      query += str + " ";
    }
    query += "','";

    // add patients
    for (int patientID : employee.getPatientIDs()) {
      query += patientID + " ";
    }

    query += "','";

    // add service requests
    for (int request : employee.getServiceRequestIDs()) {
      query += request + " ";
    }

    query += "')";

    exampleStatement.execute(query);
  }

  public void removeFromSQLTable(Employee employee) throws SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();
    query = "DELETE FROM EMPLOYEES WHERE employeeID = " + employee.getEmployeeID();
    statement.executeUpdate(query);
  }

  public void updateEmployee(Employee employee, int employeeID) throws SQLException, IOException {
    Employee emp = employee;
    emp.setEmployeeID(employeeID);
    removeEmployee(emp);
    allEmployees.add(emp);
    addToSQLTable(emp);
    saveToCSV();
  }
}
