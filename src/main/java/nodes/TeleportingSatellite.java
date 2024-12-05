package nodes; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections; 

import unsw.utils.Angle;

import unsw.response.models.EntityInfoResponse;

import interfaces.Communicable; 
import interfaces.NetworkNode; 


import network.Packet;

public class TeleportingSatellite implements Communicable, NetworkNode { 

    private String id; 
    private Angle angle; 
    private double height; 

    private Map<String, Communicable> topology = new HashMap<>(); 

    
    public TeleportingSatellite(String id, Angle angle, double height) { 
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
        if (add) { 
            topology.put(node.getId(), node); 
        } else { 
            topology.remove(node); 
        }
    }
    
    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, angle, height, "TeleportingSatellite", null);
    }
}