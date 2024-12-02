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
        // 
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
