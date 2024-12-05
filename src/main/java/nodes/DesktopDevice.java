package nodes; 

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList; 
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections; 

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import unsw.response.models.EntityInfoResponse;

import unsw.utils.Angle;

import interfaces.Communicable; 

import networking.Packet; 


public class DesktopDevice implements Communicable { 

    private String id; 
    private Angle angle; 
    private double height; 
    private Map<String, Communicable> topology = new HashMap<>(); 
    
    private List inbox; 
    
    public DesktopDevice(String id, Angle angle) { 
        this.id = id; 
        this.angle = angle; 
        this.height = RADIUS_OF_JUPITER;
        this.inbox = new ArrayList<Packet>();  
    }

    public String getId() { 
        return this.id; 
    }

    public void broadcast() {
        System.out.println(id + " is broadcasting"); 

        if (this.inbox.size() > 0) { 
            return; 
        }

        Packet ping = new Packet("D", id, "popty ping" + id, "DesktopDevice"); 

        for (Communicable node : topology.values()) { 
            node.accept(ping); 
        }

    }

    public void accept(Packet p) {
        inbox.add(p); 
    }

    public void acknowledge() {

        System.out.println("-----------received------------" + id + "--------------");       
        System.out.println(inbox); 

        if (this.inbox.size() > 2) { 
            inbox.clear(); 
        }

    }

    public void sync(boolean add, Communicable node) {
        if (add) { 
            topology.put(node.getId(), node); 
        } else { 
            topology.remove(node); 
        }
    }

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, angle, height, "DesktopDevice", new HashMap<>());
    }
}