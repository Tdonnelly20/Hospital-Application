package edu.wpi.veganvampires.objects;

public class InternalPatientTransportation {
    private Patient patient;
    private HospitalEmployee employee;
    private String roomNumber, requestDetails;

    /**
     * @param patientFirstName
     * @param patientLastName
     * @param patientID
     * @param hospitalID
     * @param roomNumber
     * @param requestDetails
     */
    public InternalPatientTransportation(
            String patientFirstName,
            String patientLastName,
            String roomNumber,
            int patientID,
            int hospitalID,
            String requestDetails){
        patient = new Patient(patientID,patientFirstName,patientLastName);
        employee = new HospitalEmployee(hospitalID);
        this.roomNumber = roomNumber;
        this.requestDetails = requestDetails;
    }

    public String getPatientFirstName() {
        return patient.getFirstName();
    }

    public String getPatientLastName() {
        return patient.getLastName();
    }

    public int getPatientID() {
        return patient.getPatientID();
    }

    public int getHospitalID() {
        return employee.getHospitalID();
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRequestDetails() {
        return requestDetails;
    }
}

