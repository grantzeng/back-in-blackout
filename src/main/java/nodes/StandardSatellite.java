package nodes; 

import unsw.utils.Angle;

public class StandardSatellite { 

    private String id; 
    private Angle position; 
    private double height; 
    
    public StandardSatellite(String id, Angle radians, double height) { 
        this.id = id; 
        this.radians = radians; 
    }

}