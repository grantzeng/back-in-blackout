package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections; 

import interfaces.Communicable; 
import interfaces.Movable; 
import interfaces.Uploadable; 

import nodes.DesktopDevice;
import nodes.HandheldDevice;
import nodes.LaptopDevice;
import nodes.RelaySatellite;
import nodes.StandardSatellite;
import nodes.TeleportingSatellite;
import nodes.AbstractNode; 

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {

    private Map<String, AbstractNode> topology = new HashMap<>(); 

    private int clock = 0;

    public void createDevice(String deviceId, String type, Angle position) {
        System.out.println("createDevice" + " " + deviceId + " " + type + " " +  position); 

        AbstractNode device = null; 
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

        assert !topology.containsKey(deviceId); 
        topology.put(deviceId, device);  

        // - For now every node will get a copy of the whole topology but later
        //   to simulate a real network each node should only get its neighbours
        for (Communicable node: topology.values() ) { 
            node.sync(topology); 
        }

        //System.out.println(topology); 

    }

    /**
     * @pre deviceId is a valid device id
     * @param deviceId
     */
    public void removeDevice(String deviceId) {

        System.out.println("removeDevice" + " " + deviceId); 

        Communicable device = topology.remove(deviceId); 
        
        for (Communicable node: topology.values()) { 
            node.sync(topology); 
        }
        
        //System.out.println(topology); 

   
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {

        System.out.println("createSatellite" + " " + satelliteId + " " + type + " " +  height + " " + position); 

        AbstractNode satellite = null; 
        switch (type) {
            case "StandardSatellite":
                satellite = new StandardSatellite(satelliteId, position, height);
                break;
            case "RelaySatellite":
                satellite = new RelaySatellite(satelliteId, position, height);
                break;
            case "TeleportingSatellite":
                satellite = new TeleportingSatellite(satelliteId, position, height);
                break;
            default:
                break;
        }
        assert satellite != null : "Satellite type is invalid"; 

        assert !topology.containsKey(satelliteId); 
        topology.put(satelliteId, satellite);  

        // - For now every node will get a copy of the whole topology but later
        //   to simulate a real network each node should only get its neighbours
        // for (Communicable node: topology.values() ) { 
        //     node.sync(true, satellite); 
        //     satellite.sync(true, node); 
        // }
      
        for (Communicable node: topology.values()) { 
            node.sync(topology); 
        }
        //System.out.println(topology); 

    }

    /**
     * @pre satelliteId is a valid satellite id
     * @param satelliteId
     */
    public void removeSatellite(String satelliteId) {
        System.out.println("removeDevice" + " " + satelliteId); 

        Communicable satellite = topology.remove(satelliteId); 
        
        for (Communicable node: topology.values()) { 
            node.sync(topology); 
        }
       
        //System.out.println(topology); 

    }

    /*
        The intuition here is recognising that whether something is a 'device' 
        or a 'satellite' is an accidental quality and only exists in mental
        model of the spec.  My first design tried treating this as some kind 
        of essential division between network nodes, but really that just caused
        more problems than helped 
    */
    public List<String> listDeviceIds() {
        //System.out.println("listDeviceIds"); 
        List<String> nodes = topology.values().stream()
                .filter(node -> node instanceof DesktopDevice || 
                                node instanceof HandheldDevice || 
                                node instanceof LaptopDevice) 
                .map(node -> ((AbstractNode) node).id)
                .collect(Collectors.toList());
        //System.out.println(nodes); 
        return nodes; 
    }          

    public List<String> listSatelliteIds() {
        //System.out.println("listSatelliteIds"); 

        List<String> nodes = topology.values().stream()
                .filter(node -> node instanceof StandardSatellite || 
                                node instanceof RelaySatellite || 
                                node instanceof TeleportingSatellite) 
                .map(node -> ((AbstractNode) node).id)
                .collect(Collectors.toList()); 
        //System.out.println(nodes); 
        return nodes; 
    }

    /**
     * @pre deviceId is a valid device id
     * @param deviceId
     * @param filename
     * @param content
     */

    public void addFileToDevice(String deviceId, String filename, String content) {
        AbstractNode device = ((AbstractNode) topology.get(deviceId)); 
        
        //2024-12-08 temporary debugging assert while the class structure of nodes is unclear
        String type =  device.getInfo().getType(); 
        assert type.equals("DesktopDevice") || type.equals("HandheldDevice") || type.equals("LaptopDevice");

        device.upload(filename, content); 

    }

    public EntityInfoResponse getInfo(String id) {
        // System.out.println("getInfo" + "  " + id); 
        return topology.get(id).getInfo(); 

    }

    public void simulate() {

        System.out.println("\n\n\nClock: " + clock); 
        
        // Move
        System.out.println("simulate - move"); 
        topology.values().stream()
            .filter(node -> node instanceof Movable)
            .map(node -> (Movable) node )
            .forEach(Movable::move); 

        // Update neighbours
        System.out.println("simulate - sync");
        for (Communicable node : topology.values()) {
            // node.sync will take the whole topology and censor it so that 
            // only the local topology is available to a node
            // - all subsequent node operations are done purely with packets and 
            //   message passing
            node.sync(topology); 
        }

        // Broadcast and do network stuffs 
        System.out.println("simulate - broadcast"); 
        for (Communicable node : topology.values()) { 
            node.broadcast(); 
        }

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
        return topology.get(id).discover(); 
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        /*
        
            Stub implentation to try to get things to flood neighbours with packets
        
         */
        AbstractNode node = topology.get(fromId); 
        node.broadcast(); 
        System.out.println("sendFile not implemented"); 
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        System.out.println("createDevice task3 not implemented, only calls original createDevice"); 
        createDevice(deviceId, type, position);

        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        System.out.println("createSlope not implemented"); 

        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }
}
