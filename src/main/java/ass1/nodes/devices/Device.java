package ass1.nodes.devices;

import ass1.nodes.NetworkNode;
import ass1.file.File;
import unsw.utils.Angle;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Map;

public abstract class Device extends NetworkNode {
    protected Device(String id, Angle radians) {
        super(id, RADIUS_OF_JUPITER, radians);
        setLinearVelocity(0.0);
        setServer(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public void move() {
        System.out.println("Devices don't move");
        return;
    }

    
    public void addFile(File file) { 
        Map<String, File> files = getFiles(); 
        files.put(file.getFilename(), file);
        setFiles(files);
    }
     

}
