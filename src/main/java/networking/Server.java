package networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nodes.NetworkNode;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
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

    public void addFile(File file) throws VirtualFileNoStorageSpaceException {
        int usage = files.values().stream().map(f -> f.getSize()).reduce(0, Integer::sum);

        if (file.getSize() + usage > maxBytes) {
            throw new VirtualFileNoStorageSpaceException("Max bytes reached");
        }

        if (files.size() + 1 > maxFiles) {
            throw new VirtualFileNoStorageSpaceException("Max files reached");
        }

        files.put(file.getFilename(), file);

        return;
    }

    public File getFile(String filename) {
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

        Map<String, FileInfoResponse> info = new HashMap<>();

        for (File file : files.values()) {
            info.put(file.getFileInfoResponse().getFilename(), file.getFileInfoResponse());
        }

        return info;
    }

}
