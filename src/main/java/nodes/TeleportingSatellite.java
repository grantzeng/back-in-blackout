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


public class TeleportingSatellite extends AbstractNode implements Movable { 

    public TeleportingSatellite(String id, Angle angle, double height) { 
        super(id, angle, height); 
    }
    
    private static final double LINEAR_SPEED = 1000.0; 
    private double cw = CLOCKWISE; 

    public void move() {
        Angle delta = Angle.fromRadians(cw * LINEAR_SPEED/ height); 

        Angle newAngle = angle.subtract(delta); 

        if ((cw == CLOCKWISE && newAngle.toDegrees() > 180.0) || 
            (cw == ANTI_CLOCKWISE && newAngle.toDegrees() < 180.0)
        ){
            // Teleport 
            angle = Angle.fromDegrees(0); 
            cw = -1.0 * cw; 
        } else { 
            angle = newAngle; 
        }

    }
    
    public EntityInfoResponse getInfo() {
        return super.getInfo("TeleportingSatellite"); 
    }
}