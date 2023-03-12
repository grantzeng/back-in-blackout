package networking;

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
    private String id;
    private Map<String, File> files = new HashMap<>();
    private Map<String, Server> communicable = new HashMap<>();
    private List<Connection> uploading = new ArrayList<>();
    private List<Connection> downloading = new ArrayList<>();
    private int maxUpload;
    private int maxDownload;
    private int maxBytes;
    private int maxFiles;

    public Server(NetworkNode owner, int maxFiles, int maxBytes, int maxUpload, int maxDownload) {
        this.owner = owner;
        id = owner.getId();
        this.maxUpload = maxUpload;
        this.maxDownload = maxDownload;
        this.maxBytes = maxBytes;
        this.maxFiles = maxFiles;
    }

    public String getId() {
        return id;
    }

    public void addFile(File file) {
        files.put(file.getFilename(), file);
        /*
         * if (!Arrays.asList(DesktopDevice, LaptopDevice,
         * HandheldDevice).contains(owner.type())) {
         * System.out.println("Can only add complete files to Devices"); return; }
         * files.put(file.getFilename(), file);
         */
    }

    public File createFile(String filename, int size)
            throws VirtualFileAlreadyExistsException, VirtualFileNoStorageSpaceException {
        if (files.get(filename) != null) {
            throw new VirtualFileAlreadyExistsException(filename + " already exists on " + id + "'s server");
        }

        checkStorageSpaceAvailable(size);

        File emptyFile = new File(filename, size);
        return emptyFile;
    }

    public File getFile(String filename) throws VirtualFileNotFoundException {
        File file = files.get(filename);

        if (file == null) {
            throw new VirtualFileNotFoundException(filename + " does not exist on " + id + "'s server");
        }

        if (!file.isComplete()) {
            throw new VirtualFileNotFoundException(filename + " is incomplete on " + id + "'s server");
        }

        return files.get(filename);
    }

    public NetworkNode getOwner() {
        return owner;
    }

    public void setCommunicable(Map<String, Server> communicable) {
        this.communicable = communicable;
    }

    public void allocateBandwidthToConnections() {
        int uploads = uploading.size();
        if (uploads != 0) {
            uploading.stream().forEach(c -> c.setUpspeed(uploads == 1 ? maxUpload : maxUpload / uploads));
        }

        int downloads = downloading.size();

        if (downloads != 0) {
            downloading.stream().forEach(c -> c.setDownspeed(downloads == 1 ? maxDownload : maxDownload / downloads));
        }
    }

    public void checkStorageSpaceAvailable(int bytesRequired) throws VirtualFileNoStorageSpaceException {
        int usage = files.values().stream().map(f -> f.getSize()).reduce(0, Integer::sum);

        if (bytesRequired + usage > maxBytes) {
            throw new VirtualFileNoStorageSpaceException("Max bytes reached");
        }

        if (files.size() + 1 > maxFiles) {
            throw new VirtualFileNoStorageSpaceException("Max files reached");
        }
    }

    public void checkUploadingBandwidthAvailable() throws VirtualFileNoBandwidthException {
        if (uploading.size() == 0) {
            return;
        }

        if (maxUpload / (uploading.size() + 1) == 0) {
            throw new VirtualFileNoBandwidthException(id);
        }
    }

    public void checkDownloadingBandwidthAvailable() throws VirtualFileNoBandwidthException {
        if (downloading.size() == 0) {
            return;
        }

        if (maxDownload / (downloading.size() + 1) == 0) {
            throw new VirtualFileNoBandwidthException(id);
        }
    }

    public void checkFileNotAlreadyExists(String filename) throws VirtualFileAlreadyExistsException {
        if (files.get(filename) != null) {
            throw new VirtualFileAlreadyExistsException(filename + " already exists on " + id + "'s server");
        }
    }

    public void openUploadingConnection(Connection uploading) {
        this.uploading.add(uploading);
    }

    public void openDownloadingConnection(Connection downloading) {
        this.downloading.add(downloading);
    }

    public void unplug(Connection connection) {
        uploading.remove(connection);
        downloading.remove(connection);
    }

    public void unplug(Connection connection, File file) {
        unplug(connection);
        files.remove(file.getFilename());
    }

    public Map<String, FileInfoResponse> getServerInfoResponse() {

        Map<String, FileInfoResponse> info = new HashMap<>();

        for (File file : files.values()) {
            info.put(file.getFileInfoResponse().getFilename(), file.getFileInfoResponse());
        }

        return info;
    }

}
