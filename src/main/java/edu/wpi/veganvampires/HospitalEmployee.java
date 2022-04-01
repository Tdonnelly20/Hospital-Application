package edu.wpi.veganvampires;

public abstract class HospitalEmployee {
    int hospitalID;
    String firstName;
    String lastName;
    Patient[] patientArr;

    public HospitalEmployee(int hospitalID, String firstName, String lastName, Patient[] patientArr){
        this.hospitalID=hospitalID;
        this.firstName=firstName;
        this.lastName=lastName;
        this.patientArr=patientArr;
    }
}
