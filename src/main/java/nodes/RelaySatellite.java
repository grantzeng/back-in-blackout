package nodes; 

import unsw.utils.Angle;

public class RelaySatellite { 

    private String id; 
    private Angle position; 
    private double height; 
    
    public RelaySatellite(String id, Angle radians, double height) { 
        this.id = id; 
        this.radians = radians; 
    }

}