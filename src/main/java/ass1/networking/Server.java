package ass1.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ass1.file.File;
import ass1.networking.connections.Connection;
import unsw.response.models.FileInfoResponse;

public class Server {
    private Map<String, File> files;
    private List<Connection> connections;

    private int sendingBandwidth;
    private int receivingBandwidth;
    private int bytesCap;
    private int fileCountCap;

    public Server(int bytesCap, int fileCountCap, int sendingBandwidth, int receivingBandwidth) {
        this.sendingBandwidth = sendingBandwidth;
        this.receivingBandwidth = receivingBandwidth;
        this.bytesCap = bytesCap;
        this.fileCountCap = fileCountCap;
        files = new HashMap<String, File>();
        connections = new ArrayList<Connection>();
    }

    public void addFile(File file) {
        files.put(file.getFilename(), file);
    }

    //public void connect(JSONObject fileToBeTransmitted, Server destination) {};

    //public void connect(JSONObject fileToBeTransmittedMetadata, Connection endpoint) {};


    public Map<String, FileInfoResponse> contentsToString() {
        Map<String, FileInfoResponse> info = new HashMap<>();

        for (File file: files.values()) {
            info.put(
                file.getFileInfoResponse().getFilename(),
                file.getFileInfoResponse()
            );
        }

        return info;
    }


}
