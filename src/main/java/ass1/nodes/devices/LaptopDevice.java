package ass1.nodes.devices;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.LaptopDevice;
import static ass1.nodes.NetworkNodeType.StandardSatellite;
import static ass1.nodes.NetworkNodeType.RelaySatellite;
import static ass1.nodes.NetworkNodeType.TeleportingSatellite;

import java.util.Arrays;
import java.util.List;

import unsw.utils.Angle;

public class LaptopDevice extends Device {
    public LaptopDevice(String id, Angle radians) {
        super(id, radians);
    }
    
    @Override
    public NetworkNodeType type() {
        return LaptopDevice;
    }
        
    @Override 
    public List<NetworkNodeType> supports() {
        return Arrays.asList(StandardSatellite, RelaySatellite, TeleportingSatellite);
    }

    
    @Override
    public double range() {
        return 100000.0;
    }
}
