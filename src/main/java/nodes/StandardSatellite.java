package nodes; 


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections; 
import java.util.Arrays;


import unsw.utils.Angle;
import static unsw.utils.MathsHelper.CLOCKWISE;
import static unsw.utils.MathsHelper.ANTI_CLOCKWISE;  

import unsw.response.models.EntityInfoResponse;

import interfaces.Communicable; 
import interfaces.Movable; 

import networking.Packet; 

import nodes.AbstractNode; 

public class StandardSatellite extends AbstractNode implements Movable { 

 
    public StandardSatellite(String id, Angle angle, double height) { 
        super(id, angle, height); 
    }

    private static final List<String> supports = Arrays.asList("StandardSatellite", "TeleportingSatellite", "RelaySatellite", "HandheldDevice", "LaptopDevice"); 
    public double range() { 
        return 150_000; // km
    }
    
    public List<String> supports() { 
        return supports;
    }

    public String type() { 
        return "StandardSatellite"; 
    }




    private static final double LINEAR_SPEED = 2500.0; 

    public void move() {
        Angle delta = Angle.fromRadians(ANTI_CLOCKWISE * LINEAR_SPEED / height);
        angle = angle.subtract(delta); 
    }

    public EntityInfoResponse getInfo() {
        return super.getInfo("StandardSatellite"); 
    }

}