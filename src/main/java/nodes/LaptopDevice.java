package nodes; 

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import unsw.utils.Angle;


public class LaptopDevice { 

    private String id; 
    private Angle position; 
    private double height; 
    
    public LaptopDevice(String id, Angle radians) { 
        this.id = id; 
        this.radians = radians; 
        this.height = RADIUS_OF_JUPITER; 
    }

}