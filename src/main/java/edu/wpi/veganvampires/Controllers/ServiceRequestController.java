package edu.wpi.veganvampires.Controllers;

import java.awt.*;
import javafx.stage.Stage;

public class ServiceRequestController extends Controller {

  //Singleton design pattern
  private static final ServiceRequestController controller=new ServiceRequestController();

  private ServiceRequestController(){}

  private static class SingletonHelper{
    private static final ServiceRequestController controller=new ServiceRequestController();
  }

  public static ServiceRequestController getController(){
    return ServiceRequestController.SingletonHelper.controller;
  }
  
  @Override
  public void start(Stage primaryStage) {}
}
