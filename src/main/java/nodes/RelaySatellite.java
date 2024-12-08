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


public class RelaySatellite extends AbstractNode implements Movable { 
    
    private Angle angle; 
    private double height; 
    
    public RelaySatellite(String id, Angle angle, double height) { 
        super(id); 
        this.angle = angle; 
        this.height = height;  
    }

    private static final double LINEAR_SPEED = 2500.0; 
    private double cw = ANTI_CLOCKWISE; 

    public void move() {
        Angle prev = angle; 
        Angle delta = Angle.fromRadians( cw * LINEAR_SPEED / height); 
        angle = angle.subtract(delta); 

        double prevDegrees = prev.toDegrees(); 
        double angleDegrees = angle.toDegrees(); 

        if(!(prevDegrees > 140.0 && prevDegrees < 190.0)) { return; }

        if ((cw == ANTI_CLOCKWISE && angleDegrees > 180.0) || 
            (cw == CLOCKWISE && angleDegrees < 140.0)
        ) {
            // Relay satellite reverse on the next tick
            cw = -1.0 * cw; 
        }

    }

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, angle, height, "RelaySatellite", new HashMap<>());
    }
}