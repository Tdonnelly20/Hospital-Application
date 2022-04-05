package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.InternalPatientTransportationImpl;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.InternalPatientTransportation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InternalPatientTransportationDao implements InternalPatientTransportationImpl{

    // A local list of all internal patient transportations, updated via Vdb
    private static ArrayList<InternalPatientTransportation> allInternalPatientTransportations;

    /** Initialize the arraylist */
    public InternalPatientTransportationDao() {
        allInternalPatientTransportations = new ArrayList<InternalPatientTransportation>();
    }

    public void setAllInternalPatientTransportations(ArrayList<InternalPatientTransportation> internalPatientTransportations) {
        allInternalPatientTransportations = internalPatientTransportations;
    }

    @Override
    public ArrayList<InternalPatientTransportation> getInternalPatientTransportations() {
        return allInternalPatientTransportations;
    }

    /**
     * Receive an internal patient transportation from the controller, store it locally, then send it to Vdb
     *
     * @param patientFirstName
     * @param patientLastName
     * @param patientID
     * @param roomNumber
     * @param requestDetails
     */
    /*
    @Override
    public void addInternalPatientTransportation(
            String patientFirstName,
            String patientLastName,
            String roomNumber,
            int patientID,
            int hospitalID,
            String requestDetails) {
        InternalPatientTransportation newInternalPatientTransportation =
                new InternalPatientTransportation(
                        patientFirstName,
                        patientLastName,
                        roomNumber,
                        patientID,
                        hospitalID,
                        requestDetails);

        System.out.println("Adding to local arraylist...");
        allInternalPatientTransportations.add(newInternalPatientTransportation); // Store a local copy

        System.out.println("Adding to database");
        try {
            Connection connection = Vdb.Connect();
            Statement exampleStatement = connection.createStatement();
            Vdb.saveToFile(Vdb.Database.InternalPatientTransportation);
            // exampleStatement.execute(
            //    "INSERT INTO LOCATIONS VALUES (patientFirstName, patientLastName, roomNumber, patientID,
            // hospitalID, medicineName, dosage, requestDetails");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     */
}


