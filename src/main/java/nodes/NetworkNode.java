package nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helpers.AngleNormaliser;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public abstract class NetworkNode {
    private String id;
    private Angle position;
    private double height;
    private List<NetworkNode> visible = new ArrayList<>(); 

    public enum NodeType {
        DesktopDevice, HandheldDevice, LaptopDevice, RelaySatellite, StandardSatellite, TeleportingSatellite
    }

    protected NetworkNode(String id, Angle position, double height) {
        this.id = id; 
        this.position = position; 
        this.height = height; 
    }
        
    protected abstract List<NodeType> supports();

    public abstract NodeType type();

    protected abstract double range();

    public String getId() {
        return id;
    }

    public double getHeight() {
        return height;
    }

    protected void setHeight(double height) {
        this.height = height;
    }

    public Angle getPosition() {
        return position;
    }

    protected void setPosition(Angle position) {
        AngleNormaliser normaliser = new AngleNormaliser();
        this.position = normaliser.normalise(position);

    }

    public void setVisible(List<NetworkNode> visible) {
        this.visible = visible;
    }

    public List<NetworkNode> getVisible() {
        return visible;
    }

    public abstract void move();

    protected abstract Map<String, FileInfoResponse> getServerInfoResponse(); 
    
    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, position, height, type().toString(), getServerInfoResponse());
    }

}
