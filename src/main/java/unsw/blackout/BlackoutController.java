package unsw.blackout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import interfaces.Communicable; 
import interfaces.Movable; 
import interfaces.NetworkNode; 
import interfaces.Uploadable; 

import nodes.DesktopDevice;
import nodes.HandheldDevice;
import nodes.LaptopDevice;
import nodes.NetworkNode;
import nodes.RelaySatellite;
import nodes.StandardSatellite;
import nodes.TeleportingSatellite;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {

    private Map<String, Communicable> topology = new HashMap<>(); 

    private int clock = 0;

    public void createDevice(String deviceId, String type, Angle position) {
        System.out.println("createDevice" + " " + deviceId + " " + type + " " +  position); 

        // Create device
        Communicable device = null; 
        switch (type) {
            case "HandheldDevice":
                device = new HandheldDevice(deviceId, position);
                break;
            case "LaptopDevice":
                device = new LaptopDevice(deviceId, position);
                break;
            case "DesktopDevice":
                device = new DesktopDevice(deviceId, position);
                break;
            default:
                break;
        }
        assert device != null : "Device type is invalid"; 

        assert !map.containsKey(deviceId); 
        topology.put(deviceId, device);  

        // - For now every node will get a copy of the whole topology but later
        //   to simulate a real network each node should only get its neighbours
        for (Communicable node: topology.values() ) { 
            node.sync(true, device); 
        }

        for (Communicable node: topology.values()) {
            device.sync(true, node); 
        }
    }

    /**
     * @pre deviceId is a valid device id
     * @param deviceId
     */
    public void removeDevice(String deviceId) {
   
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
      
    }

    /**
     * @pre satelliteId is a valid satellite id
     * @param satelliteId
     */
    public void removeSatellite(String satelliteId) {
       
    }

    public List<String> listDeviceIds() {
       
    }

    public List<String> listSatelliteIds() {
       
    }

    /**
     * @pre deviceId is a valid device id
     * @param deviceId
     * @param filename
     * @param content
     */

    public void addFileToDevice(String deviceId, String filename, String content) {
       
    }

    public EntityInfoResponse getInfo(String id) {
      
    }

    public void simulate() {

        clock++;
    }

    /**
     * Simulate for the specified number of minutes. You shouldn't need to modify
     * this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    /**
     * @param id
     * @return
     */
    public List<String> communicableEntitiesInRange(String id) {
        
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {

    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }
}
