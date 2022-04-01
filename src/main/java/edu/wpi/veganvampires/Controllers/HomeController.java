package edu.wpi.veganvampires.Controllers;

import javafx.stage.Stage;

public class HomeController extends Controller {
  //Singleton design pattern
  private static final HomeController controller=new HomeController();

  private HomeController(){}

  private static class SingletonHelper{
    private static final HomeController controller=new HomeController();
  }

  public static HomeController getController(){
    return SingletonHelper.controller;
  }
  @Override
  public void start(Stage primaryStage) throws Exception {}
}
