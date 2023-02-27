package ass1.nodes.devices;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.LaptopDevice;
import unsw.utils.Angle;

public class LaptopDevice extends Device {
    public LaptopDevice(String id, Angle degrees) {
        super(id, degrees);
    }

    public NetworkNodeType type() {
        return LaptopDevice;
    }
}
