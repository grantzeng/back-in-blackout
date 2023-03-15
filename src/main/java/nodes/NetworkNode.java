package nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helpers.AngleNormaliser;
import networking.Connection;
import networking.File;
import networking.Server;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class NetworkNode {
    private String id;
    private Angle position;
    private double height;
    private Server server;

    public enum NodeType {
        DesktopDevice, HandheldDevice, LaptopDevice, RelaySatellite, StandardSatellite, TeleportingSatellite
    }

    protected NetworkNode(String id, Angle position, double height) {
        this.id = id; 
        this.position = position; 
        this.height = height; 
        this.server = new Server(this, 0, 0, 0, 0);
    }
    
    /*
        Motion and position
    */
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

    public abstract void move();

    /*
        File server behaviour is delegated to server instance.
    */
    protected abstract Server setServer();
    public abstract Server getServer(); 
    
    public File getFile(String filename) {
        server.getFile(filename);
    }
    
    public File addEmptyFile(String filename, int size) {
        return server.addEmptyFile(filename, size);
    }
    
    public void addUploadConnection(Connection conn) {
        server.addUploadingConnection(conn);
    }
    
    public void addDownloadConnection(Connection conn) {
        server.addDownloadingConnection(conn);
    }
    
    public void removeDownloadConnection(Connection conn) {
        server.removeDownloadConnection(conn);
    }
    
    public removeDownloadConnection(Connection conn, String filename) {
        server.removeDownloadConnection(conn, filename);
    }
    
    public void removeUploadConnection(Connection conn) {
        server.removeUploadConnection(conn);
    }
    
    // Try to deal with communicability
    /*
    protected abstract List<NodeType> supports();
    
    
    public boolean canSendTo(NetworkNode node) {
        return supports().contains(node.type()) && MathsHelper.getDistance(height, position, node.getHeight(), node.getPosition()) < range();
    }
    
    public abstract NodeType type();

    protected abstract double range();
    */

    
    /*
        Entity info response
    */
    
    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, position, height, type().toString(), server.getServerInfoResponse());
    }
    
    
    
}
