package networking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nodes.NetworkNode;
import unsw.response.models.FileInfoResponse;

public class Server {

    private NetworkNode owner; 
    private String id; 
    private Map<String, File> files; 
    private Map<String, Server> communicable; 
    private List<Connection> sending; 
    private List<Connection> receiving; 
    private int maxUpload; 
    private int maxDownload; 
    private int maxBytes; 
    private int maxFiles; 
    
    
    public File getFile(String filename){ 
        return files.get(filename);
    }
    
    public void allocateBandwidthToConnections() {
        
    }
    
    public void closeConnection(Connection connection) {
        
    }
    

    public Map<String, FileInfoResponse> getServerInfoResponse() {
        return null;
    }

}
