package nodes; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections; 
import java.util.Arrays;


import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import unsw.response.models.EntityInfoResponse;

import unsw.utils.Angle;

import interfaces.Communicable; 

import networking.Packet; 

import nodes.AbstractNode; 

public class LaptopDevice extends AbstractNode { 

    public LaptopDevice(String id, Angle angle) { 
        super(id, angle, RADIUS_OF_JUPITER); 
    }

    private static final List<String> supports = Arrays.asList("StandardSatellite", "TeleportingSatellite", "RelaySatellite"); 

    public double range() { 
        return 100_000; // km
    }
    
    public List<String> supports() { 
        return supports;
    }

    public String type() { 
        return "LaptopDevice"; 
    }

    public EntityInfoResponse getInfo() {
        return super.getInfo("LaptopDevice"); 
    }
}