package edu.wpi.veganvampires;

import edu.wpi.veganvampires.EquipmentDelivery;
import edu.wpi.veganvampires.Interfaces.EquipmentDeliveryImpl;
import edu.wpi.veganvampires.Vdb;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDeliveryDao implements EquipmentDeliveryImpl {
  private static ArrayList<EquipmentDelivery> allEquipmentDeliveries;

  /** Initialize the array list */
  public EquipmentDeliveryDao() {
    allEquipmentDeliveries = new ArrayList<EquipmentDelivery>();
    try{
      Vdb.CreateDB();
      allEquipmentDeliveries = Vdb.equipment;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<EquipmentDelivery> getAllEquipmentDeliveries() {
    return allEquipmentDeliveries;
  }

  @Override
  public void addEquipmentDelivery(String location, String equipment, String notes, int quantity) {
    EquipmentDelivery newEquipmentDelivery =
        new EquipmentDelivery(location, equipment, notes, quantity);

    System.out.println("Adding to local arraylist...");
    allEquipmentDeliveries.add(newEquipmentDelivery);
    updateEquipmentDeliveryDB(newEquipmentDelivery);
  }

  private void updateEquipmentDeliveryDB(EquipmentDelivery newEquipmentDelivery) {
    System.out.println("Sending to database...");
    //Vdb.addEquipmentDelivery(newEquipmentDelivery);
  }

  @Override
  public void removeEquipmentDelivery(String equipment) {
    allEquipmentDeliveries.removeIf(e -> e.getEquipment().equals(equipment));
  } // TODO
}
