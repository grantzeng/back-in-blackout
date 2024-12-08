package nodes; 


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections; 

import unsw.utils.Angle;
import static unsw.utils.MathsHelper.CLOCKWISE;
import static unsw.utils.MathsHelper.ANTI_CLOCKWISE;  

import unsw.response.models.EntityInfoResponse;

import interfaces.Communicable; 
import interfaces.Movable; 

import networking.Packet; 

import nodes.AbstractNode; 


public class StandardSatellite extends AbstractNode implements Movable { 


    private Angle angle; 
    private double height; 
    
    public StandardSatellite(Angle angle, double height) { 
        super(id); 
        this.angle = angle; 
        this.height = height; 
    }

    private static final double LINEAR_SPEED = 2500.0; 

    public void move() {
        System.out.println("StandardSatellite.move()"); 
        Angle delta = Angle.fromRadians(ANTI_CLOCKWISE * LINEAR_SPEED / height);
        angle = angle.subtract(delta); 
    }

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, angle, height, "StandardSatellite", new HashMap<>());
    }

}