package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmployeeDao {
  private static ArrayList<Employee> allEmployees;
  private static final Employee nullEmployee = new Employee();
  /** Create Employee Dao constructor */
  public EmployeeDao() {
    nullEmployee.setEmployeeID(-1);
    nullEmployee.setFirstName("Not");
    nullEmployee.setLastName("Found");
    allEmployees = new ArrayList<>();
    createSQLTable();
    loadFromCSV();
  }

  /** Load the employees from the CSV file, call them to add to SQL */
  public void loadFromCSV() {
    try {

      String line = "";
      String file = VApp.currentPath + "/Employees.csv";
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String splitToken = ","; // what we split the csv file with
      ArrayList<Employee> employees = new ArrayList<>();
      String headerLine = br.readLine();
      while ((line = br.readLine()) != null) // should create a database based on csv file
      {
        String[] data = line.split(splitToken);
        ArrayList<Integer> patientIDs;
        ArrayList<Integer> serviceIDs;
        ArrayList<String> specialties;
        // LOOK AT THIS PIECE OF SHIT CODE I MADE. LOOK AT IT. ITS AMAZING

        try {
          specialties = new ArrayList(Arrays.asList(data[4].split(" ")));
        } catch (Exception e) {
          specialties = new ArrayList<>();
        }

        try {
          patientIDs =
              IntStream.of(Arrays.stream(data[5].split(" ")).mapToInt(Integer::parseInt).toArray())
                  .boxed()
                  .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
          patientIDs = new ArrayList<>();
        }

        try {
          serviceIDs =
              IntStream.of(Arrays.stream(data[6].split(" ")).mapToInt(Integer::parseInt).toArray())
                  .boxed()
                  .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
          serviceIDs = new ArrayList<>();
        }

        Employee newEmployee =
            new Employee(
                Integer.parseInt(data[0]),
                data[1],
                data[2],
                data[3],
                specialties, // FIGHT ME I HATE FOR LOOPS
                patientIDs,
                serviceIDs,
                true);

        employees.add(newEmployee);
      }
      allEmployees = employees;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Save all instances in the arraylist to the CSV */
  public void saveToCSV() {
    try {

      FileWriter fw = new FileWriter(VApp.currentPath + "/Employees.csv");
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
          specialties.trim(),
          patientIDs.trim(),
          serviceIDs.trim()
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
   * Get an employee object from an ID number
   *
   * @param employeeID
   * @return
   */
  public Employee getEmployee(int employeeID) {
    for (Employee e : allEmployees) {
      if (e.getEmployeeID() == employeeID) {
        return e;
      }
    }
    System.out.print("Unable to find employee with ID:" + employeeID);
    return nullEmployee; // Return the null employee
  }

  /**
   * Add a new employee to the database
   *
   * @param employee
   */
  public void addEmployee(Employee employee) {
    allEmployees.add(employee);
    addToSQLTable(employee);
    saveToCSV();
  }

  /**
   * Remove a selected employee based on ID
   *
   * @param employee
   */
  public void removeEmployee(Employee employee) {
    replaceEmployee(employee);
    for (ServiceRequest request : employee.getServiceRequestList()) {
      Vdb.requestSystem.removeServiceRequest(request);
    }
  }

  /**
   * A helper method for updating employees in the Employee database
   *
   * @param employee
   */
  public void replaceEmployee(Employee employee) {
    allEmployees.removeIf(currEmployee -> employee.getEmployeeID() == currEmployee.getEmployeeID());
    removeFromSQLTable(employee);
    saveToCSV();
  }

  /**
   * Get the list of all employees
   *
   * @return all employees
   */
  public ArrayList<Employee> getAllEmployees() {
    return allEmployees;
  }

  /** Create the SQL table for the employees */
  public void createSQLTable() {
    try {

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
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add an employee to the SQL table
   *
   * @param employee
   */
  public void addToSQLTable(Employee employee) {
    try {

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
              + employee.getEmployeePosition()
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Remove an employee from the SQL table
   *
   * @param employee
   */
  public void removeFromSQLTable(Employee employee) {
    try {

      String query = "";
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();
      query = "DELETE FROM EMPLOYEES WHERE employeeID = " + employee.getEmployeeID();
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Update an employee with a specific ID and replace it with another
   *
   * @param employee the replacement
   * @param employeeID the ID to replace
   */
  public void updateEmployee(Employee employee, int employeeID) {
    Employee oldEmployee = getEmployee(employeeID);
    replaceEmployee(oldEmployee);
    employee.setEmployeeID(employeeID);
    allEmployees.add(employee);
    addToSQLTable(employee);
    saveToCSV();
  }
}
