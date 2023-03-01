package ass1.nodes.devices;

import ass1.nodes.NetworkNodeType;
import unsw.utils.Angle;

public class LaptopDevice extends Device {
    public LaptopDevice(String id, Angle radians) {
        super(id, radians);
    }

    public NetworkNodeType type() {
        return NetworkNodeType.LaptopDevice;
    }
}
