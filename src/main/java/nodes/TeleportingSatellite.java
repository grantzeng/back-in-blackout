package nodes; 

import unsw.utils.Angle;

import interfaces.Communicable; 

import network.Packet;

public class TeleportingSatellite implements Communicable { 

    private String id; 
    private Angle angle; 
    private double height; 
    
    public TeleportingSatellite(String id, Angle angle, double height) { 
        this.id = id; 
        this.angle = angle; 
        this.height = height; 
    }


    public void broadcast() {
    }

    public void accept(Packet p) {
    }

    public void acknowledge() {
    }

    public void sync(boolean add, Communicable node) {
        
    }
    
}