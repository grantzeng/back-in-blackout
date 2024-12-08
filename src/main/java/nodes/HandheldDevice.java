package nodes; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections; 

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import unsw.response.models.EntityInfoResponse;

import unsw.utils.Angle;

import interfaces.Communicable; 

import networking.Packet; 

import nodes.AbstractNode; 


public class HandheldDevice extends AbstractNode { 

    private Angle angle; 
    private double height; 

    public HandheldDevice(String id, Angle angle) { 
        super(id); 
        this.id = id; 
        this.angle = angle; 
        this.height = RADIUS_OF_JUPITER; 
    }

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, angle, height, "HandheldDevice", new HashMap<>());
    }


}