
New design
```java
BlackoutController: 
- nodes: Map<String, NetworkNode>

NetworkNode: 
- id: String
- position: Angle
- height: double
- server: Server

Server: 
- uploading: List<Connection>
- downloading: List<Connection>
- files: Map<String, File>
- maxUploads: int
- maxDownloads: int
- maxBytes: int
- maxFiles: int

File: 
- /* some uninteresting file stuff */


Connection: 
- sourceNode: NetworkNode
- destNode: NetworkNode
- sourceServer: Server
- destServer: Server
- sourceFile: File
- destFile: File
- fp: int
- fileSize: int
- fileName: String
- isActive: boolean
/*
    Gets references to parents to request whether in range
    
    Basically, the whole NetworkNode<--Server<---File chain deals with the non cross cutting concern
    
    Connection object is responsible for requesting information and cleaning up when it gets closed
*/



```

Procedural code to register a connection 
```java
// What are the function calls?
BlackoutController.sendFile(String filename, String fromId, String toId) {
    transmissionManager.sendFile(filename, nodes.get(fromId), nodes.get(toId));
}

TransmissionManager.sendFile(String filename, NetworkNode from, NetworkNode to) {
    Connection conn = new Connection(from, to, from.getServer(), to.getServer());
    
    File source = from.getFile(filename);
    conn.addSource(source);
    
    from.checkUploadResourcesAvailable(filename);
    to.checkDownloadResourcesAvailable(filename, source.getSize());
    File dest = to.addEmptyFile(filename, source.getSize());
    conn.addDest(dest);
    
    from.addUploadConnection(conn); // Adds connetion and updates everything
    to.addDownloadConnection(conn); // Adds connection and updates everything
    
}

// What's going on in the other parts of the system
File from.getFile(String filename) {
    return server.getFile(filename)
    
    // If not ffound, throws error
    // If partial download, throws error
}

void from.checkUploadingResourcesAvailable(filename){
    // Does requested file exist 
    // is file not only partial
    // Do we have enough uploading bandwidth
 }

void to.checkDownloadingResourcesAvailable(filename, size) {
    // Does file already exists on system
    // do we have downloading bandwidth
    // is server file cap constraint maintained
    // is server file size constraint maintained?
}

void to.addEmptyfile() {
    // if file doesn't exist
    /// make new empty file
    // return reference to it
}

void from.addUploadingconnection() {
    // passes to server object
    // server object adds it to list of uploads
}

void to.addDownloadingConnection() {
    // passes to server object, 
    // server object adds it to list of downloads
}

```

Procedural code for checking whether to tick over
```java
void connection.transmit() {
    if (!from.canSendTo(to)) {
        // goes out of range
        fromServer.removeUploadConnection(this);
        toServer.removeDownloadConnection(this, filename); // removes connection and the partial file
        isActive = false
        
        return; 
    }
    
   // Figure out what to do re transmission 
   // - basically close the transmission 
}


```

Procedural code for closing a connection that is out of range
```
```
Procedural code for closing a connection when file is done
```
```