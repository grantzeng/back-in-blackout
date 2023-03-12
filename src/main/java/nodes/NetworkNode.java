package nodes;

import java.util.List;

import helpers.AngleNormaliser;
import networking.Server;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public abstract class NetworkNode {
    private String id; 
    private Angle position; 
    private int height; 
    private List<NetworkNode> visible; 
    private Server server;
    
    public enum NodeType {
        DesktopDevice, HandheldDevice, LaptopDevice, RelaySatellite, StandardSatellite, TeleportingSatellite
    }
    
    protected abstract List<NodeType> supports(); 
    protected abstract NodeType type(); 
    protected abstract int range(); 
    
    public String getId() {
        return id;
    }
    
    protected int getHeight() {
        return height; 
    }
    
    protected void setHeight(int height) {
        this.height = height; 
    }
    
    protected Angle getPosition() {
        return position; 
    }
    
    protected void setPosition(Angle position) {
        AngleNormaliser normaliser = new AngleNormaliser();
        this.position = normaliser.normalise(position);

    }
    
    public void setVisible(List<NetworkNode> visible) {
        this.visible = visible;
    }
    
    public abstract void move();
    
    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, position, height, type().toString(), server.getServerInfoResponse());
    }
    
    
}
