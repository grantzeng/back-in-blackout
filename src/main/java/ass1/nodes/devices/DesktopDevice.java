package ass1.nodes.devices;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.DesktopDevice;
import unsw.utils.Angle;

public class DesktopDevice extends Device {

    public DesktopDevice(String id, Angle degrees) {
        super(id, degrees);
    }
    
    public NetworkNodeType type() {
        return DesktopDevice;
    }

}
