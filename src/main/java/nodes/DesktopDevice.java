package nodes; 

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import unsw.utils.Angle;


public class DesktopDevice { 

    private String id; 
    private Angle position; 
    private double height; 
    
    public DesktopDevice(String id, Angle radians) { 
        this.id = id; 
        this.radians = radians; 
    }

}