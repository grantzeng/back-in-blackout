package networking;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nodes.NetworkNode.NodeType.DesktopDevice;
import static nodes.NetworkNode.NodeType.HandheldDevice;
import static nodes.NetworkNode.NodeType.LaptopDevice;

import nodes.NetworkNode;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;

import unsw.response.models.FileInfoResponse;

public class Server {
    private NetworkNode owner;

    private Map<String, File> files = new HashMap<>();
    private List<Connection> uploading = new ArrayList<>();
    private List<Connection> downloading = new ArrayList<>();

    private int maxUpload;
    private int maxDownload;
    private int maxBytes;
    private int maxFiles;

    public Server(NetworkNode owner, int maxBytes, int maxFiles, int maxUpload, int maxDownload) {
        this.owner = owner;
        this.maxBytes = maxBytes;
        this.maxFiles = maxFiles;
        this.maxUpload = maxUpload;
        this.maxDownload = maxDownload;
    }

    /*
     * Adding, retriving files from server
     */

    public void addFile(File file) {
        files.put(file.getFilename(), file);
    }

    public File createEmptyFile(String filename, int size)
            throws VirtualFileAlreadyExistsException, VirtualFileNoStorageSpaceException {
        if (files.get(filename) != null) {
            throw new VirtualFileAlreadyExistsException(filename + " already exists on " + owner);
        }

        File emptyFile = new File(filename, size);
        files.put(filename, emptyFile);
        return emptyFile;
    }

    public File getFile(String filename) throws VirtualFileNotFoundException {
        File file = files.get(filename);

        if (file == null) {
            throw new VirtualFileNotFoundException(filename + " does not exist on " + owner);
        }

        if (!file.isComplete()) {
            throw new VirtualFileNotFoundException(filename + " is incomplete on " + owner);
        }

        return files.get(filename);
    }
    
    public void removeFile(String filename) {
        files.remove(filename);
    }
    /*
     * Connections management
     */

    public void addUploadConnection(Connection conn) {
        uploading.add(conn);
    }

    public void addDownloadConnection(Connection conn) {
        downloading.add(conn);
    }

    public void removeUploadConnection(Connection conn) {
        uploading.remove(conn);
    }

    public void removeDownloadConnection(Connection conn) {
        downloading.remove(conn);
    }

    public void removeDownloadConnection(Connection conn, String filename) {
        downloading.remove(conn);
        files.remove(filename);
    }

    /*
     * Transmission resource checks
     */
    public void checkUploadResourcesAvailable(String filename)
            throws VirtualFileNotFoundException, VirtualFileNoBandwidthException {
        getFile(filename);

        if (uploading.size() != 0 && maxUpload / (uploading.size() + 1) == 0) {
            throw new VirtualFileNoBandwidthException(owner.getId());
        }
    }

    public void checkDownloadResourcesAvailable(String filename, int filesize)
        throws VirtualFileAlreadyExistsException, VirtualFileNoBandwidthException, VirtualFileNoStorageSpaceException{
        if (files.get(filename) != null) {
            throw new VirtualFileAlreadyExistsException(filename);
        }
        
        if (downloading.size() != 0 && maxDownload / (downloading.size() + 1) == 0) {
            throw new VirtualFileNoBandwidthException(owner.getId());
        }
       
        int usage = files.values().stream().map(f -> f.getSize()).reduce(0, Integer::sum);
        if (filesize + usage > maxBytes) {
            throw new VirtualFileNoStorageSpaceException("Max bytes reached");
        }

        if (files.size() + 1 > maxFiles) {
            throw new VirtualFileNoStorageSpaceException("Max files reached");
        }
    
    }
    /*
     * public void allocateBandwidthToConnections() { int uploads =
     * uploading.size(); if (uploads != 0) { int bandwidth = uploads == 1 ?
     * maxUpload : maxUpload / uploads; System.out.println(this +
     * " upload bandwidth is " + bandwidth); uploading.stream().forEach(c ->
     * c.setUpspeed(bandwidth)); }
     * 
     * int downloads = downloading.size(); if (downloads != 0) { int bandwidth =
     * downloads == 1 ? maxDownload : maxDownload / downloads;
     * System.out.println(this + " download bandwidth is " + bandwidth);
     * downloading.stream().forEach(c -> c.setDownspeed(bandwidth)); } }
     */

    public Map<String, FileInfoResponse> getServerInfoResponse() {

        Map<String, FileInfoResponse> info = new HashMap<>();

        for (File file : files.values()) {
            info.put(file.getFileInfoResponse().getFilename(), file.getFileInfoResponse());
        }

        return info;
    }

}
