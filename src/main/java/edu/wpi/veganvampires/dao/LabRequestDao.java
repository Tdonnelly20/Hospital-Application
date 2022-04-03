package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.LabRequestImpl;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.LabRequest;
import edu.wpi.veganvampires.objects.Location;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LabRequestDao implements LabRequestImpl {
  private static ArrayList<LabRequest> allLabRequests;

  /** Initialize the array list */
  public LabRequestDao() {
    allLabRequests = new ArrayList<LabRequest>();
    // TODO: Add info from the database to the local arraylist
  }

  @Override
  public List<LabRequest> getAllLabRequests() {
    return allLabRequests;
  }

  @Override
  public void addLabRequest(
      int userID, int patientID, String firstName, String lastName, String lab, String status) {
    LabRequest labRequest = new LabRequest(userID, patientID, firstName, lastName, lab, status);

    System.out.println("Adding to local arraylist...");
    allLabRequests.add(labRequest);

    try {
      Connection connection = Vdb.Connect();
      Statement st = connection.createStatement();
        st.execute(
                "INSERT INTO LABS VALUES (userID, patientID, firstName, lastName, lab, status)");
      Vdb.saveToFile(Vdb.Database.LabRequest);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateLabRequest(LabRequest labRequest) {
    System.out.println("Sending to database...");

  }

  @Override
  public void removeLabRequest(LabRequest req) {
    try {
      Connection connect = Vdb.Connect();

      for (int i = 0; i < allLabRequests.size(); i++) {
        if (allLabRequests.get(i).getPatient().getHospitalEmployeeNum() == (req.getPatient().getHospitalEmployeeNum())) //TODO make primary key for requests, and get setters and getters to work
        {
          Statement st = connect.createStatement();
          st.execute("DELETE FROM LABS WHERE userID = req.getPatient().getHospitalEmployeeNum()"); //TODO Make this reliant of a primary key
        }
      }
    }
    catch(SQLException e){
      e.printStackTrace();
    }
  }
}
