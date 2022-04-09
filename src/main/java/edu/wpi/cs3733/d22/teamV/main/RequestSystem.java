package edu.wpi.cs3733.d22.teamV.main;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.dao.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RequestSystem {
    private EquipmentDao equipmentDao = new EquipmentDao();
    private EquipmentDeliveryDao equipmentDeliveryDao = new EquipmentDeliveryDao();
    private InternalPatientTransportationDao internalPatientTransportationDao = new InternalPatientTransportationDao();
    private LabRequestDao labRequestDao = new LabRequestDao();
    private LaundryRequestDao laundryRequestDao = new LaundryRequestDao();
    private LocationDao locationDao = new LocationDao();
    private MealRequestDao mealRequestDao = new MealRequestDao();
    private MedicineDeliveryDao medicineDeliveryDao = new MedicineDeliveryDao();
    private ReligiousRequestDao religiousRequestDao = new ReligiousRequestDao();
    private SanitationRequestDao sanitationRequestDao = new SanitationRequestDao();

    public enum Dao{
        Equipment,
        EquipmentDelivery,
        InternalPatientTransportation,
        LabRequest,
        LaundryRequest,
        LocationDao,
        MealRequestDao,
        MedicineDelivery,
        ReligiousRequest,
        SanitationRequest
    }
    Dao dao;

    public void loadFromCSV() throws IOException, SQLException{
        switch(dao){
            case Equipment:
                equipmentDao.loadFromCSV();
            case EquipmentDelivery:
                equipmentDeliveryDao.loadFromCSV();
            case InternalPatientTransportation:
                internalPatientTransportationDao.loadFromCSV();
            case LabRequest:
                labRequestDao.loadFromCSV();
            case LaundryRequest:
                laundryRequestDao.loadFromCSV();
            case LocationDao:
                locationDao.loadFromCSV();
            case MealRequestDao:
                mealRequestDao.loadFromCSV();
            case MedicineDelivery:
                medicineDeliveryDao.loadFromCSV();
            case ReligiousRequest:
                religiousRequestDao.loadFromCSV();
            case SanitationRequest:
                sanitationRequestDao.loadFromCSV();
            default:
                System.out.println("L + touch grass");
        }
    }

    public void saveToCSV() throws IOException{
        switch(dao){
            case EquipmentDelivery:
                equipmentDeliveryDao.saveToCSV();
            case InternalPatientTransportation:
                internalPatientTransportationDao.saveToCSV();
            case LabRequest:
                labRequestDao.saveToCSV();
            case LaundryRequest:
                laundryRequestDao.saveToCSV();
            case LocationDao:
                locationDao.saveToCSV();
            case MealRequestDao:
                mealRequestDao.saveToCSV();
            case MedicineDelivery:
                medicineDeliveryDao.saveToCSV();
            case ReligiousRequest:
                religiousRequestDao.saveToCSV();
            case SanitationRequest:
                sanitationRequestDao.saveToCSV();
            default:
                System.out.println("L + touch grass");
        }
    }

    public void createSQLTable() throws SQLException{
        switch(dao){
            case Equipment:
                equipmentDao.createSQLTable();
            case EquipmentDelivery:
                equipmentDeliveryDao.createSQLTable();
            case InternalPatientTransportation:
                internalPatientTransportationDao.createSQLTable();
            case LabRequest:
                labRequestDao.createSQLTable();
            case LaundryRequest:
                laundryRequestDao.createSQLTable();
            case LocationDao:
                locationDao.createSQLTable();
            case MealRequestDao:
                mealRequestDao.createSQLTable();
            case MedicineDelivery:
                medicineDeliveryDao.createSQLTable();
            case ReligiousRequest:
                religiousRequestDao.createSQLTable();
            case SanitationRequest:
                sanitationRequestDao.createSQLTable();
            default:
                System.out.println("L + touch grass");
        }
    }

    public void addToSQLTable(ServiceRequest request) throws SQLException{
        switch(dao){
            case EquipmentDelivery:
                equipmentDeliveryDao.addToSQLTable(request);
            case InternalPatientTransportation:
                internalPatientTransportationDao.addToSQLTable(request);
            case LabRequest:
                labRequestDao.addToSQLTable(request);
            case LaundryRequest:
                laundryRequestDao.addToSQLTable(request);
            case LocationDao:
                locationDao.addToSQLTable(request);
            case MealRequestDao:
                mealRequestDao.addToSQLTable(request);
            case MedicineDelivery:
                medicineDeliveryDao.addToSQLTable(request);
            case ReligiousRequest:
                religiousRequestDao.addToSQLTable(request);
            case SanitationRequest:
                sanitationRequestDao.addToSQLTable(request);
            default:
                System.out.println("L + touch grass");
        }
    }

    public void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException{
        switch(dao){
            case EquipmentDelivery:
                equipmentDeliveryDao.removeFromSQLTable(request);
            case InternalPatientTransportation:
                internalPatientTransportationDao.removeFromSQLTable(request);
            case LabRequest:
                labRequestDao.removeFromSQLTable(request);
            case LaundryRequest:
                laundryRequestDao.removeFromSQLTable(request);
            case LocationDao:
                locationDao.removeFromSQLTable(request);
            case MealRequestDao:
                mealRequestDao.removeFromSQLTable(request);
            case MedicineDelivery:
                medicineDeliveryDao.removeFromSQLTable(request);
            case ReligiousRequest:
                religiousRequestDao.removeFromSQLTable(request);
            case SanitationRequest:
                sanitationRequestDao.removeFromSQLTable(request);
            default:
                System.out.println("L + touch grass");
        }
    }

    public void addServiceRequest(ServiceRequest request) throws IOException, SQLException{
        switch(dao){
            case EquipmentDelivery:
                equipmentDeliveryDao.addServiceRequest(request);
            case InternalPatientTransportation:
                internalPatientTransportationDao.addServiceRequest(request);
            case LabRequest:
                labRequestDao.addServiceRequest(request);
            case LaundryRequest:
                laundryRequestDao.addServiceRequest(request);
            case LocationDao:
                locationDao.addServiceRequest(request);
            case MealRequestDao:
                mealRequestDao.addServiceRequest(request);
            case MedicineDelivery:
                medicineDeliveryDao.addServiceRequest(request);
            case ReligiousRequest:
                religiousRequestDao.addServiceRequest(request);
            case SanitationRequest:
                sanitationRequestDao.addServiceRequest(request);
            default:
                System.out.println("L + touch grass");
        }
    }

    public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException{
        switch(dao){
            case EquipmentDelivery:
                equipmentDeliveryDao.removeServiceRequest(request);
            case InternalPatientTransportation:
                internalPatientTransportationDao.removeServiceRequest(request);
            case LabRequest:
                labRequestDao.removeServiceRequest(request);
            case LaundryRequest:
                laundryRequestDao.removeServiceRequest(request);
            case LocationDao:
                locationDao.removeServiceRequest(request);
            case MealRequestDao:
                mealRequestDao.removeServiceRequest(request);
            case MedicineDelivery:
                medicineDeliveryDao.removeServiceRequest(request);
            case ReligiousRequest:
                religiousRequestDao.removeServiceRequest(request);
            case SanitationRequest:
                sanitationRequestDao.removeServiceRequest(request);
            default:
                System.out.println("L + touch grass");
        }
    }

    public ArrayList<? extends ServiceRequest> getAllServiceRequests(){
        switch(dao){
            case Equipment:
                equipmentDao.getAllServiceRequests();
            case EquipmentDelivery:
                equipmentDeliveryDao.getAllServiceRequests();
            case InternalPatientTransportation:
                internalPatientTransportationDao.getAllServiceRequests();
            case LabRequest:
                labRequestDao.getAllServiceRequests();
            case LaundryRequest:
                laundryRequestDao.getAllServiceRequests();
            case LocationDao:
                locationDao.getAllServiceRequests();
            case MealRequestDao:
                mealRequestDao.getAllServiceRequests();
            case MedicineDelivery:
                medicineDeliveryDao.getAllServiceRequests();
            case ReligiousRequest:
                religiousRequestDao.getAllServiceRequests();
            case SanitationRequest:
                sanitationRequestDao.getAllServiceRequests();
            default:
                return new ArrayList<>();
        }
    }

    public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests) throws SQLException{
        switch(dao){
            case EquipmentDelivery:
                equipmentDeliveryDao.setAllServiceRequests(serviceRequests);
            case InternalPatientTransportation:
                internalPatientTransportationDao.setAllServiceRequests(serviceRequests);
            case LabRequest:
                labRequestDao.setAllServiceRequests(serviceRequests);
            case LaundryRequest:
                laundryRequestDao.setAllServiceRequests(serviceRequests);
            case LocationDao:
                locationDao.setAllServiceRequests(serviceRequests);
            case MealRequestDao:
                mealRequestDao.setAllServiceRequests(serviceRequests);
            case MedicineDelivery:
                medicineDeliveryDao.setAllServiceRequests(serviceRequests);
            case ReligiousRequest:
                religiousRequestDao.setAllServiceRequests(serviceRequests);
            case SanitationRequest:
                sanitationRequestDao.setAllServiceRequests(serviceRequests);
            default:
                System.out.println("L + touch grass");
        }
    }
}
