package edu.wpi.cs3733.d22.teamV.main;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.dao.*;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestSystem {
  private EquipmentDao equipmentDao = new EquipmentDao();
  private EquipmentDeliveryDao equipmentDeliveryDao = new EquipmentDeliveryDao();
  private InternalPatientTransportationDao internalPatientTransportationDao =
      new InternalPatientTransportationDao();
  private LabRequestDao labRequestDao = new LabRequestDao();
  private LaundryRequestDao laundryRequestDao = new LaundryRequestDao();
  private LocationDao locationDao = new LocationDao();
  private MealRequestDao mealRequestDao = new MealRequestDao();
  private MedicineDeliveryDao medicineDeliveryDao = new MedicineDeliveryDao();
  private ReligiousRequestDao religiousRequestDao = new ReligiousRequestDao();
  private SanitationRequestDao sanitationRequestDao = new SanitationRequestDao();

  /**
   * Choose type of DAO for the methods called
   */
  public enum Dao {
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

  /**
   * Choose which CSV to load from
   * @throws IOException
   * @throws SQLException
   */
  public void loadFromCSV() throws IOException, SQLException {
    switch (dao) {
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

  /**
   * Choose which CSV to save to
   * @throws IOException
   */
  public void saveToCSV() throws IOException {
    switch (dao) {
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

  /**
   * Choose which table to create
   * @throws SQLException
   */
  public void createSQLTable() throws SQLException {
    switch (dao) {
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

  /**
   * Add to specified table based on type of request
   * @param request
   * @throws SQLException
   */
  public void addToSQLTable(ServiceRequest request) throws SQLException {
    switch (dao) {
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

  /**
   * Choose which table to remove from based on request
   * @param request
   * @throws IOException
   * @throws SQLException
   */
  public void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException {
    switch (dao) {
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

  /**
   * Creates new service request in specified DAO
   * @param request
   * @throws IOException
   * @throws SQLException
   */
  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {
    switch (dao) {
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

  /**
   * Removes a service request based on type of request
   * @param request
   * @throws IOException
   * @throws SQLException
   */
  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {
    switch (dao) {
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

  /**
   * Returns all service requests of a certain type
   * @return
   */
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    switch (dao) {
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

  /**
   * Getter specifically for location since it is not a service request
   * @return
   */
  public ArrayList<Location> getLocations() {
    return locationDao.getAllLocations();
  }

  /**
   * Returns ALL service requests of EVERY type
   * @return
   */
  public List<? extends ServiceRequest> getEveryServiceRequest() {
    return Stream.of(
            equipmentDeliveryDao.getAllServiceRequests(),
            // internalPatientTransportationDao.getAllServiceRequests(),
            labRequestDao.getAllServiceRequests(),
            // laundryRequestDao.getAllServiceRequests(),
            // mealRequestDao.getAllServiceRequests(),
            medicineDeliveryDao.getAllServiceRequests() // ,
            // religiousRequestDao.getAllServiceRequests(),
            // sanitationRequestDao.getAllServiceRequests()
            )
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  /**
   * Sets service requests of a certain type
   * @param serviceRequests
   * @throws SQLException
   */
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException {
    switch (dao) {
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
