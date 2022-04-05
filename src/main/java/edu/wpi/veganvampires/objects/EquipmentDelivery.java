package edu.wpi.veganvampires.objects;

public class EquipmentDelivery {
  private final int empID;
  private final int patientID;
  private final String patientFirstName;
  private final String patientLastName;
  private final String location;
  private final String equipment, notes;
  private final int quantity;
  private final String status;
  //int employeeID,
  //int patientID,
  //String patientFirstName,
  //String patientLastName,
  //String location,
  //String equipment,
  //String notes,
  //int quantity,
  public EquipmentDelivery(int eID, int pID, String fname, String lname, String location, String equipment, String notes, int quantity, String status) {
    this.empID=eID;
    this.patientID=pID;
    this.patientFirstName=fname;
    this.patientLastName=lname;
    this.location = location;
    this.equipment = equipment;
    this.notes = notes;
    this.quantity = quantity;
    this.status=status;
  }
  public int getEmpID(){
    return empID;
  }

  public int getPatientID(){
    return patientID;
  }

  public String getPatientFirstName(){
    return patientFirstName;
  }

  public String getPatientLastName(){
    return patientLastName;
  }

  public String getLocation() {
    return location;
  }

  public String getEquipment() {
    return equipment;
  }

  public String getNotes() {
    return notes;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getStatus(){
    return status;
  }
}
