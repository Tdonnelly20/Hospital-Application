package edu.wpi.veganvampires;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) {}

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
  public static void main(String[] args) {
    char state = 'D';
    switch (state) {
      default:
        System.out.print("Menu");
    }
  }
}
