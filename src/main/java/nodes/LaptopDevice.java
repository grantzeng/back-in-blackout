package nodes; 

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import unsw.utils.Angle;

import interfaces.Communicable; 

import network.Packet; 

public class LaptopDevice implements Communicable{ 

    private String id; 
    private Angle angle; 
    private double height; 
    
    public LaptopDevice(String id, Angle angle) { 
        this.id = id; 
        this.angle = angle; 
        this.height = RADIUS_OF_JUPITER; 
    }


   
    public void broadcast() {
    }

    public void accept(Packet p) {
    }

    public void acknowledge() {
    }

    public void sync(boolean add, Communicable node) {
        
    }

}