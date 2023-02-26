package ass1.nodes.devices;

import ass1.nodes.NetworkNode;

public abstract class Device extends NetworkNode {

    public Device(){};
    
    @Override
    public void move() {
        System.out.println("Devices don't move");
        return; 
    }

}
