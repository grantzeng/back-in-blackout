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


public class HandheldDevice implements Communicable { 

    private String id; 
    private Angle angle; 
    private double height; 
    private Map<String, Communicable> topology = new HashMap<>(); 

    
    public HandheldDevice(String id, Angle angle) { 
        this.id = id; 
        this.angle = angle; 
        this.height = RADIUS_OF_JUPITER; 
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
        return new EntityInfoResponse(id, angle, height, "HandheldDevice", new HashMap<>());
    }


}