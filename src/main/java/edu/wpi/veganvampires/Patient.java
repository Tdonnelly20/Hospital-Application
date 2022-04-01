package edu.wpi.veganvampires;


public class Patient {
    int patientID;
    String firstName;
    String lastName;
    HospitalEmployee[] hospitalEmployeeArr;
    String[] medicalHistoryArr;

    public Patient(int patientID, String firstName, String lastName, HospitalEmployee[] hospitalEmployeeArr, String[] medicalHistoryArr){
        this.patientID=patientID;
        this.firstName=firstName;
        this.lastName=lastName;
        this.hospitalEmployeeArr=hospitalEmployeeArr;
        this.medicalHistoryArr=medicalHistoryArr;
    }
}
