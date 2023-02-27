package ass1.nodes.devices;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.LaptopDevice;
import unsw.utils.Angle;

public class LaptopDevice extends Device {
    public LaptopDevice(String id, Angle radians) {
        super(id, radians);
    }

    public NetworkNodeType type() {
        return LaptopDevice;
    }
}
