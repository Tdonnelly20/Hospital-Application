package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.objects.InternalPatientTransportation;
import edu.wpi.veganvampires.objects.MedicineDelivery;
import java.util.List;

public interface InternalPatientTransportationImpl {
    List<InternalPatientTransportation> getAllInternalPatientTransportations();

    void addInternalPatientTransportation(
            String patientFirstName,
            String patientLastName,
            String roomNumber,
            int patientID,
            int hospitalID,
            String requestDetails);

    void removeInternalPatientTransportation();
}