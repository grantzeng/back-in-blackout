package nodes; 


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections; 

import unsw.utils.Angle;

import unsw.response.models.EntityInfoResponse;

import interfaces.Communicable; 

import networking.Packet; 

import nodes.AbstractNode; 

public class StandardSatellite extends AbstractNode { 

    private String id; 
    private Angle angle; 
    private double height; 
    
    public StandardSatellite(String id, Angle angle, double height) { 
        super(); 
        this.id = id; 
        this.angle = angle; 
        this.height = height; 
    }

    public String getId() { 
        return this.id; 
    }

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, angle, height, "StandardSatellite", new HashMap<>());
    }

}