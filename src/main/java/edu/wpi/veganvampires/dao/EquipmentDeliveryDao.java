package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.EquipmentDeliveryImpl;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.EquipmentDelivery;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDeliveryDao implements EquipmentDeliveryImpl {
  private static ArrayList<EquipmentDelivery> allEquipmentDeliveries;

  /** Initialize the array list */
  public EquipmentDeliveryDao() {
    allEquipmentDeliveries = new ArrayList<EquipmentDelivery>();
    // TODO: Add info from the database to the local arraylist
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

  @Override
  public void removeEquipmentDelivery(String equipment) {}

  private void updateEquipmentDeliveryDB(EquipmentDelivery newEquipmentDelivery) {
    System.out.println("Sending to database...");
    Vdb.addEquipmentDelivery(newEquipmentDelivery);
  }

  @Override
  public void removeEquipmentDelivery() {} // TODO
}
