package edu.wpi.cs3733.d22.teamV.main;

import edu.wpi.cs3733.d22.teamV.dao.*;
import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RequestSystem {
  public static int serviceIDCounter = 0;
  public static int patientIDCounter = 0;
  public static int employeeIDCounter = 0;

  private LocationDao locationDao;
  private PatientDao patientDao;
  private EmployeeDao employeeDao;
  private EquipmentDao equipmentDao;
  private EquipmentDeliveryDao equipmentDeliveryDao;
  private InternalPatientTransportationDao internalPatientTransportationDao;
  private LabRequestDao labRequestDao;
  private LaundryRequestDao laundryRequestDao;
  private MealRequestDao mealRequestDao;
  private MedicineDeliveryDao medicineDeliveryDao;
  private ReligiousRequestDao religiousRequestDao;
  private SanitationRequestDao sanitationRequestDao;

  public RequestSystem() {}

  public void init() throws SQLException, IOException {
    locationDao = new LocationDao();
    patientDao = new PatientDao();
    employeeDao = new EmployeeDao();

    equipmentDao = new EquipmentDao();
    equipmentDeliveryDao = new EquipmentDeliveryDao();
    internalPatientTransportationDao = new InternalPatientTransportationDao();
    labRequestDao = new LabRequestDao();
    laundryRequestDao = new LaundryRequestDao();
    mealRequestDao = new MealRequestDao();
    medicineDeliveryDao = new MedicineDeliveryDao();
    religiousRequestDao = new ReligiousRequestDao();
    sanitationRequestDao = new SanitationRequestDao();
  }

  /** Choose type of DAO for the methods called */
  public enum Dao {
    Equipment,
    EquipmentDelivery,
    InternalPatientTransportation,
    LabRequest,
    LaundryRequest,
    LocationDao,
    MealRequest,
    MedicineDelivery,
    ReligiousRequest,
    SanitationRequest
  }

  private static class SingletonMaker {
    private static final RequestSystem requestSystem = new RequestSystem();
  }

  public static RequestSystem getSystem() {
    return SingletonMaker.requestSystem;
  }

  public DaoInterface getDao(Dao dao) {
    switch (dao) {
      case EquipmentDelivery:
        return equipmentDeliveryDao;
      case InternalPatientTransportation:
        return internalPatientTransportationDao;
      case LabRequest:
        return labRequestDao;
      case LaundryRequest:
        return laundryRequestDao;
      case LocationDao:
        return locationDao;
      case MealRequest:
        return mealRequestDao;
      case MedicineDelivery:
        return medicineDeliveryDao;
      case ReligiousRequest:
        return religiousRequestDao;
      case SanitationRequest:
        return sanitationRequestDao;
      default:
        return null;
    }
  }

  /**
   * Creates new service request in specified DAO
   *
   * @param request
   * @throws IOException
   * @throws SQLException
   */
  public void addServiceRequest(ServiceRequest request, Dao dao) throws IOException, SQLException {
    switch (dao) {
      case EquipmentDelivery:
        equipmentDeliveryDao.addServiceRequest(request);
        break;
      case InternalPatientTransportation:
        internalPatientTransportationDao.addServiceRequest(request);
        break;
      case LabRequest:
        labRequestDao.addServiceRequest(request);
        break;
      case LaundryRequest:
        laundryRequestDao.addServiceRequest(request);
        break;
      case LocationDao:
        locationDao.addServiceRequest(request);
        break;
      case MealRequest:
        mealRequestDao.addServiceRequest(request);
        break;
      case MedicineDelivery:
        medicineDeliveryDao.addServiceRequest(request);
        break;
      case ReligiousRequest:
        religiousRequestDao.addServiceRequest(request);
        break;
      case SanitationRequest:
        sanitationRequestDao.addServiceRequest(request);
        break;
      default:
        System.out.println("L + touch grass");
    }
  }

  /**
   * Removes a service request based on type of request
   *
   * @param request
   * @throws IOException
   * @throws SQLException
   */
  public void removeServiceRequest(ServiceRequest request, Dao dao)
      throws IOException, SQLException {
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
      case MealRequest:
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
   * Removes a service request based on type of request
   *
   * @param request
   * @throws IOException
   * @throws SQLException
   */
  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {
    switch (request.getType()) {
      case "Equipment Delivery":
        equipmentDeliveryDao.removeServiceRequest(request);
      case "Internal Patient Transportation Request":
        internalPatientTransportationDao.removeServiceRequest(request);
      case "Lab Request":
        labRequestDao.removeServiceRequest(request);
      case "Laundry Request":
        laundryRequestDao.removeServiceRequest(request);
      case "Meal Request":
        mealRequestDao.removeServiceRequest(request);
      case "Medicine Delivery":
        medicineDeliveryDao.removeServiceRequest(request);
      case "Religious Request":
        religiousRequestDao.removeServiceRequest(request);
      case "Sanitation Request":
        sanitationRequestDao.removeServiceRequest(request);
      default:
        System.out.println("L + touch grass");
    }
  }

  /**
   * Returns all service requests of a certain type
   *
   * @return
   */
  public ArrayList<? extends ServiceRequest> getAllServiceRequests(Dao dao) {
    switch (dao) {
      case EquipmentDelivery:
        return equipmentDeliveryDao.getAllServiceRequests();
      case InternalPatientTransportation:
        return internalPatientTransportationDao.getAllServiceRequests();
      case LabRequest:
        return labRequestDao.getAllServiceRequests();
      case LaundryRequest:
        return laundryRequestDao.getAllServiceRequests();
      case LocationDao:
        return locationDao.getAllServiceRequests();
      case MealRequest:
        return mealRequestDao.getAllServiceRequests();
      case MedicineDelivery:
        return medicineDeliveryDao.getAllServiceRequests();
      case ReligiousRequest:
        return religiousRequestDao.getAllServiceRequests();
      case SanitationRequest:
        return sanitationRequestDao.getAllServiceRequests();
      default:
        return new ArrayList<>();
    }
  }

  public EmployeeDao getEmployeeDao() {
    return employeeDao;
  }

  public PatientDao getPatientDao() {
    return patientDao;
  }

  public LocationDao getLocationDao() {
    return locationDao;
  }

  /**
   * Getter specifically for location since it is not a service request
   *
   * @return
   */
  public ArrayList<Location> getLocations() {
    return locationDao.getAllLocations();
  }

  public Location getLocation(String nodeID) {
    return locationDao.getLocation(nodeID);
  }

  public void deleteLocation(String nodeID) {
    locationDao.deleteLocation(nodeID);
  }
  /**
   * Getter specifically for equipment since it is not a service request
   *
   * @return
   */
  public ArrayList<Equipment> getEquipment() {
    return equipmentDao.getAllEquipment();
  }

  public void addEquipment(Equipment equipment) {
    equipmentDao.getAllEquipment().add(equipment);
  }

  public void deleteEquipment(Equipment equipment) {
    equipmentDao.getAllEquipment().remove(equipment);
  }

  public Equipment getEquipment(String ID) {
    for (Equipment equipment : equipmentDao.getAllEquipment()) {
      if (equipment.getID().equals(ID)) {
        return equipment;
      }
    }
    return null;
  }

  public ArrayList<Patient> getPatients() {
    return patientDao.getAllPatients();
  }

  public ArrayList<Employee> getEmployees() {
    return employeeDao.getAllEmployees();
  }

  /**
   * Returns ALL service requests of EVERY type
   *
   * @return
   */
  public ArrayList<? extends ServiceRequest> getEveryServiceRequest() {
    ArrayList<ServiceRequest> allRequests = new ArrayList<>();
    allRequests.addAll(equipmentDeliveryDao.getAllServiceRequests());
    allRequests.addAll(internalPatientTransportationDao.getAllServiceRequests());
    allRequests.addAll(labRequestDao.getAllServiceRequests());
    allRequests.addAll(laundryRequestDao.getAllServiceRequests());
    allRequests.addAll(mealRequestDao.getAllServiceRequests());
    allRequests.addAll(medicineDeliveryDao.getAllServiceRequests());
    allRequests.addAll(religiousRequestDao.getAllServiceRequests());
    allRequests.addAll(sanitationRequestDao.getAllServiceRequests());

    return allRequests;
  }

  /**
   * Sets service requests of a certain type
   *
   * @param serviceRequests
   * @throws SQLException
   */
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests, Dao dao)
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
      case MealRequest:
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

  public void getMaxIDs() {
    // Service Requests
    int highestID = serviceIDCounter;
    ArrayList<ServiceRequest> allServiceRequests = new ArrayList<ServiceRequest>();
    allServiceRequests = (ArrayList<ServiceRequest>) getEveryServiceRequest();

    for (ServiceRequest request : allServiceRequests) {
      if (request.getServiceID() > highestID) {
        highestID = request.getServiceID();
      }
    }
    serviceIDCounter = highestID;

    // Patients
    highestID = patientIDCounter;

    for (Patient patient : PatientDao.getAllPatients()) {
      if (patient.getPatientID() > highestID) {
        highestID = patient.getPatientID();
      }
    }
    patientIDCounter = highestID;

    // Employees
    highestID = employeeIDCounter;

    for (Employee employee : employeeDao.getAllEmployees()) {
      if (employee.getEmployeeID() > highestID) {
        highestID = employee.getEmployeeID();
      }
    }
    employeeIDCounter = highestID;
  }

  public static int getServiceID() {
    return serviceIDCounter++;
  }

  public static int getPatientID() {
    return patientIDCounter++;
  }

  public static int getEmployeeID() {
    return employeeIDCounter++;
  }
}
