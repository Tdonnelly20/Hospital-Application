package edu.wpi.cs3733.d22.teamV.main;

import edu.wpi.cs3733.d22.teamV.dao.*;
import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.map.EquipmentIcon;
import edu.wpi.cs3733.d22.teamV.map.Icon;
import edu.wpi.cs3733.d22.teamV.map.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.*;
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
  private RobotDao robotDao;
  private SanitationRequestDao sanitationRequestDao;
  private PathfindingDao pathfindingDao;

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
    robotDao = new RobotDao();
    sanitationRequestDao = new SanitationRequestDao();
    pathfindingDao = new PathfindingDao();

    triDirectionalityInit();
  }

  private void triDirectionalityInit() {}

  /** Choose type of DAO for the methods called */
  public enum Dao {
    Employee,
    Patient,
    Equipment,
    EquipmentDelivery,
    InternalPatientTransportation,
    LabRequest,
    LaundryRequest,
    LocationDao,
    MealRequest,
    MedicineDelivery,
    ReligiousRequest,
    RobotRequest,
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
      case MealRequest:
        return mealRequestDao;
      case MedicineDelivery:
        return medicineDeliveryDao;
      case ReligiousRequest:
        return religiousRequestDao;
      case RobotRequest:
        return robotDao;
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
  public void addServiceRequest(ServiceRequest request, Dao dao) {
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
      case MealRequest:
        mealRequestDao.addServiceRequest(request);
        break;
      case MedicineDelivery:
        medicineDeliveryDao.addServiceRequest(request);
        break;
      case ReligiousRequest:
        religiousRequestDao.addServiceRequest(request);
      case RobotRequest:
        robotDao.addServiceRequest(request);
        break;
      case SanitationRequest:
        sanitationRequestDao.addServiceRequest(request);
        break;

      default:
        System.out.println("addServiceRequest error");
    }
  }
  /**
   * Removes a service request based on type of request
   *
   * @param request
   * @throws IOException
   * @throws SQLException
   */
  public void addServiceRequest(ServiceRequest request) {
    if (!getEveryServiceRequest().contains(request)) {
      switch (request.getType()) {
        case "Equipment Delivery Request":
          equipmentDeliveryDao.addServiceRequest(request);
          break;
        case "Internal Patient Transportation Request":
          internalPatientTransportationDao.addServiceRequest(request);
          break;
        case "Lab Request":
          labRequestDao.addServiceRequest(request);
          break;
        case "Laundry Request":
          laundryRequestDao.addServiceRequest(request);
          break;
        case "Meal Delivery Request":
          mealRequestDao.addServiceRequest(request);
          break;
        case "Medicine Delivery Request":
          medicineDeliveryDao.addServiceRequest(request);
          break;
        case "Religious Request":
          religiousRequestDao.addServiceRequest(request);
          break;
        case "Sanitation Request":
          sanitationRequestDao.addServiceRequest(request);
          break;
        case "Robot Request":
          robotDao.addServiceRequest(request);
          break;
        default:
          System.out.println("AddServiceRequest error");
          System.out.println(request.getRequestName());
      }
    } else {
      System.out.println("Service request " + request.getServiceID() + " already exists");
      System.out.println(request.getRequestName());
    }
  }

  /**
   * Removes a service request based on type of request
   *
   * @param request
   * @throws IOException
   * @throws SQLException
   */
  public void removeServiceRequest(ServiceRequest request, Dao dao) {
    switch (dao) {
      case EquipmentDelivery:
        equipmentDeliveryDao.removeServiceRequest(request);
        break;
      case InternalPatientTransportation:
        internalPatientTransportationDao.removeServiceRequest(request);
        break;
      case LabRequest:
        labRequestDao.removeServiceRequest(request);
        break;
      case LaundryRequest:
        laundryRequestDao.removeServiceRequest(request);
        break;
      case MealRequest:
        mealRequestDao.removeServiceRequest(request);
        break;
      case MedicineDelivery:
        medicineDeliveryDao.removeServiceRequest(request);
        break;
      case ReligiousRequest:
        religiousRequestDao.removeServiceRequest(request);
        break;
      case RobotRequest:
        robotDao.removeServiceRequest(request);
        break;
      case SanitationRequest:
        sanitationRequestDao.removeServiceRequest(request);
        break;
      default:
        System.out.println("RemoveServiceRequest error");
    }
  }

  /**
   * Removes a service request based on type of request
   *
   * @param request
   * @throws IOException
   * @throws SQLException
   */
  public void removeServiceRequest(ServiceRequest request) {
    switch (request.getType()) {
      case "Equipment Delivery Request":
        equipmentDeliveryDao.removeServiceRequest(request);
        break;
      case "Internal Patient Transportation Request":
        internalPatientTransportationDao.removeServiceRequest(request);
        break;
      case "Lab Request":
        labRequestDao.removeServiceRequest(request);
        break;
      case "Laundry Request":
        laundryRequestDao.removeServiceRequest(request);
        break;
      case "Meal Request":
        mealRequestDao.removeServiceRequest(request);
        break;
      case "Medicine Delivery Request":
        medicineDeliveryDao.removeServiceRequest(request);
        break;
      case "Religious Request":
        religiousRequestDao.removeServiceRequest(request);
        break;
      case "Sanitation Request":
        sanitationRequestDao.removeServiceRequest(request);
        break;
      case "Robot Request":
        robotDao.removeServiceRequest(request);
        break;
      default:
        System.out.println("RemoveServiceRequest error");
        break;
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
      case MealRequest:
        return mealRequestDao.getAllServiceRequests();
      case MedicineDelivery:
        return medicineDeliveryDao.getAllServiceRequests();
      case ReligiousRequest:
        return religiousRequestDao.getAllServiceRequests();
      case RobotRequest:
        return robotDao.getAllServiceRequests();
      case SanitationRequest:
        return sanitationRequestDao.getAllServiceRequests();
      default:
        return new ArrayList<>();
    }
  }

  public ArrayList<ServiceRequest> getAllRequestsWithPatientID(int patientID) {
    ArrayList<ServiceRequest> serviceRequests = new ArrayList<>();
    for (ServiceRequest request : getEveryServiceRequest()) {
      if (request.patient.getPatientID() == patientID) {
        serviceRequests.add(request);
      }
    }

    return serviceRequests;
  }

  public ArrayList<ServiceRequest> getAllRequestsWithEmployeeID(int employeeID) {
    ArrayList<ServiceRequest> serviceRequests = new ArrayList<>();
    for (ServiceRequest request : getEveryServiceRequest()) {
      if (request.getEmployee().getEmployeeID() == employeeID) {
        serviceRequests.add(request);
      }
    }

    return serviceRequests;
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

  public void addLocation(Location location) {
    locationDao.addLocation(location);
    for (ServiceRequest request : location.getRequests()) {
      addServiceRequest(request);
    }
  }

  public void deleteLocation(String nodeID) {
    if (getLocation(nodeID) != null) {
      if (getLocation(nodeID).getRequests().size() > 0) {
        for (ServiceRequest request : getLocation(nodeID).getRequests()) {
          removeServiceRequest(request);
        }
      }
      locationDao.deleteLocation(nodeID);
    } else {
      System.out.println("Location does not exist");
    }
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
    equipmentDao.addEquipment(equipment);
  }

  public void removeEquipment(Equipment equipment) {
    equipmentDao.removeEquipment(equipment);
  }

  public Equipment getEquipment(String ID) {
    for (Equipment equipment : equipmentDao.getAllEquipment()) {
      if (equipment.getID().equals(ID)) {
        return equipment;
      }
    }
    return null;
  }

  public void removeEquipment(EquipmentIcon icon) {
    for (Equipment equipment : icon.getEquipmentList()) {
      removeEquipment(equipment);
    }
  }

  public void addEquipment(ArrayList<Equipment> equipment) {
    equipmentDao.addEquipment(equipment);
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
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests, Dao dao) {
    switch (dao) {
      case EquipmentDelivery:
        equipmentDeliveryDao.setAllServiceRequests(serviceRequests);
      case InternalPatientTransportation:
        internalPatientTransportationDao.setAllServiceRequests(serviceRequests);
      case LabRequest:
        labRequestDao.setAllServiceRequests(serviceRequests);
      case LaundryRequest:
        laundryRequestDao.setAllServiceRequests(serviceRequests);
      case MealRequest:
        mealRequestDao.setAllServiceRequests(serviceRequests);
      case MedicineDelivery:
        medicineDeliveryDao.setAllServiceRequests(serviceRequests);
      case ReligiousRequest:
        religiousRequestDao.setAllServiceRequests(serviceRequests);
      case RobotRequest:
        robotDao.setAllServiceRequests(serviceRequests);
      case SanitationRequest:
        sanitationRequestDao.setAllServiceRequests(serviceRequests);
      default:
        System.out.println("SetAllServiceRequests error");
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
    serviceIDCounter = highestID + 1;

    // Patients
    highestID = patientIDCounter;

    for (Patient patient : PatientDao.getAllPatients()) {
      if (patient.getPatientID() > highestID) {
        highestID = patient.getPatientID();
      }
    }
    patientIDCounter = highestID + 1;

    // Employees
    highestID = employeeIDCounter;

    for (Employee employee : employeeDao.getAllEmployees()) {
      if (employee.getEmployeeID() > highestID) {
        highestID = employee.getEmployeeID();
      }
    }
    employeeIDCounter = highestID + 1;
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

  public void updateLocations(Icon icon) {
    if (icon.iconType.equals(Icon.IconType.Equipment)) {
      ArrayList<Equipment> equipmentList =
          new ArrayList<>(((EquipmentIcon) icon).getEquipmentList());
      System.out.println(equipmentList.size());
      removeEquipment(((EquipmentIcon) icon));
      for (Equipment equipment : equipmentList) {
        equipment.updateLocation(icon.getXCoord(), icon.getYCoord());
        addEquipment(equipment);
      }
    } else {
      Location newLocation =
          new Location(
              icon.getLocation().getNodeID(),
              icon.getXCoord(),
              icon.getYCoord(),
              icon.getLocation().getFloor(),
              icon.getLocation().getBuilding(),
              icon.getLocation().getNodeType(),
              icon.getLocation().getLongName(),
              icon.getLocation().getShortName());
      locationDao.deleteLocation(icon.getLocation().getNodeID());
      locationDao.addLocation(newLocation);
      icon.setLocation(newLocation);
    }
    MapManager.getManager().setUpFloors();
  }
}
