package networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nodes.NetworkNode;
import unsw.response.models.FileInfoResponse;

public class Server {

    private NetworkNode owner; 
    private String id; 
    private Map<String, File> files = new HashMap<>(); 
    private Map<String, Server> communicable = new HashMap<>();
    private List<Connection> sending = new ArrayList<>(); 
    private List<Connection> receiving = new ArrayList<>();
    private int maxUpload; 
    private int maxDownload; 
    private int maxBytes; 
    private int maxFiles; 
    
    public Server(NetworkNode owner, int maxFiles, int maxBytes, int maxUpload, int maxDownload) {
        this.owner = owner; 
        this.maxUpload = maxUpload;
        this.maxDownload = maxDownload; 
        this.maxBytes = maxBytes; 
        this.maxFiles = maxFiles; 
    }
    
    public File getFile(String filename){ 
        return files.get(filename);
    }
    
    public NetworkNode getOwner() {
        return owner;
    }
    
    public void allocateBandwidthToConnections() {
        
    }
    
    public void closeConnection(Connection connection) {
        
    }
    

    public Map<String, FileInfoResponse> getServerInfoResponse() {
        return null;
    }

}
