package nodes; 

import unsw.utils.Angle;

import unsw.response.models.EntityInfoResponse;

import interfaces.Communicable; 
import interfaces.NetworkNode; 


import network.Packet; 

public class RelaySatellite implements Communicable, NetworkNode { 

    private String id; 
    private Angle angle; 
    private double height; 
    
    public RelaySatellite(String id, Angle angle, double height) { 
        this.id = id; 
        this.angle = angle; 
        this.height = height;  
    }

    public String getId() { 
        return this.id; 
    }
    
    public void broadcast() {
    }

    public void accept(Packet p) {
    }

    public void acknowledge() {
    }

    public void sync(boolean add, Communicable node) {
        
    }

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, angle, height, "RelaySatellite", null);
    }
}