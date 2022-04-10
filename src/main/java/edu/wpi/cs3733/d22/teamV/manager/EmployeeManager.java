package edu.wpi.cs3733.d22.teamV.manager;

import edu.wpi.cs3733.d22.teamV.objects.Employee;
import java.util.ArrayList;

public class EmployeeManager {
  private ArrayList<Employee> employeeList;

  public EmployeeManager() {
    employeeList = new ArrayList<>();
  }

  private static class SingletonHelper {
    private static final EmployeeManager manager = new EmployeeManager();
  }

  public static EmployeeManager getManager() {
    return EmployeeManager.SingletonHelper.manager;
  }

  public Employee getEmployee(int userID) {
    for (Employee he : employeeList) {
      if (he.getEmployeeID() == userID) {
        return he;
      }
    }
    return null;
  }
}
