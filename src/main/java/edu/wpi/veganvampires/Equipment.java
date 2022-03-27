package edu.wpi.veganvampires;

public class Equipment {
    String name;
    String description;
    int count;
    public Equipment(){
        name=null;
        description=null;
        count=0;
    }

    public Equipment(String n, String desc, int cnt){
        name=n;
        description=desc;
        count=cnt;
    }
}
