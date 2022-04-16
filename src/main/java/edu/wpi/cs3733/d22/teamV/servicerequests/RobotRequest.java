package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.objects.Employee;

public class RobotRequest extends ServiceRequest{
    private Employee employee;
    private String details, nodeID, status;
    private int botID;

    public RobotRequest(int hospitalID, int botID, String nodeID, String details, String status){
        employee = new Employee(hospitalID);
        this.botID = botID;
        this.nodeID = nodeID;
        this.details = details;
        this.status = status;
    }

    public int getEmployeeID() {
        return employee.getEmployeeID();
    }

    public int getBotID() {
        return botID;
    }

    public String getNodeID() {
        return nodeID;
    }

    public String getDetails() {
        return details;
    }

    public String getStatus(){
        return status;
    }
}
