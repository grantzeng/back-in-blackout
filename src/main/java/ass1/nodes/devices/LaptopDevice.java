package ass1.nodes.devices;

import ass1.nodes.NetworkNodeType;
import unsw.utils.Angle;

public class LaptopDevice extends Device {
    public LaptopDevice(String id, Angle radians) {
        super(id, radians);
    }
    
    @Override
    public NetworkNodeType type() {
        return NetworkNodeType.LaptopDevice;
    }
    
    @Override
    public double range() {
        return 100000.0;
    }
}
