# Domain modelling transmission
So far my ideas look like this: 
- There's some kind of `Connection` object. (Carl, the tutor said that historically designs that did work looked something like this)
- For the bizarre behaviour of the `TeleportingSatellite`, we create a special kind of `TeleportingConnection` object, which has more permissions re: editing file + file objects. 
- There's three phases of transmission: 
    1. Initiating a connection
    2. Transmission ticking over
    3. Closing transmission: _either because_: 
        - File has finished transmission 
        - Client network node "goes out of range" of server network node. 
        
        
- Absorbed the `Server` object into the `NetworkNode` object; after some discussion with the tutor it's basically -- there's no point in making another class just for the sake of reducing the number of lines of code and what's more important is "conceptual integrity". I basically ended up with a whole lot of functions that were just passing info to the `Server` object anyway. 
        
### Recapitulating the data involved 
Idea I have: 
- BlackoutController does all the initial set up
- Then we do movement and transmission
- Then BlackoutController also tells everything to clean themselves up. 

```java
Connection: 
- origin: NetworkNode // for now it's this, but change it to just a string later on
- resource: File
- endpoint: Connection
- bytesAllocated: int // bytes
- bytesUsed: int
- hit: boolean // either received data or tried to transmit data in a tick (if false then remove it) 
- buffer: String
- status: Status
+ 
+ flush() // Flushes buffer to resource



```

Big idea: 
- Every tick: 
    NetworkNode resets all the connection objects + removes any out of range connection objects + allocates resources
    _then_ it does transmission, then it does clean up. 
    
    
### Trying to decide whether some piece of data needs to go into connection object or into transmission object
Allocations of responsibilitiies: 
- Connection objects are respnosible for processing connections
    - not responsible for creating themselves
    - responsible for doing transmissions
    - not responsible for closing transmissions
- Server objects are responsible for 
    - opening/closing connections (say it wants to read or send data to somewhere else)
    - file management
    - telling connection objects how many bytes it wants to allocate it
    - (I will permit server objects to do some probing of connection objects for now, but we can try to decouple them later)
    


### Procedurally thinking about how this will work every tick

1. Server calculates up and down bandwidth for the session and allocates equally to connection objects depending on whether they are uploading or downloading. 


2. `sendFile()` is called on server. This should do the back and forth of creating connection object + giving it to a source object


3. Then transmit: `Server` hits a connection until it's rate limited. 


    
### Trying to model how this works 
```
server.alert(filename, client)

    - Check resource availability
    
client.accept(connection, int size)
    - check resource availability
    - 

```


Basically connection is just a data channel. If it's not set to hit in one tick, then the end point wasn't used. 

Setting up a data channel. 
1. client requests file requirements
   server responds with requirements (or throws exception if does not exist or is a partial file)

2. client opens a connection and tells it how much bandwidth it has
   server responds with a connection object (or throws bandwidth exception: put this "whichever has the least bytes thing" into one object )

3. client sets up connection object on its side (or throws except where file already exists) 


NetworkNode is responsible for cleaning up resources that aren't used before/after during each tick. 
NetworkNode is responsible for resetting each connection object + doing byte allocations each tick



### 2023-03-06 1:45PM 
Division of responsibility: 
- network node is responsible for cleaning up unusued connections (and file associated with them)

- server network node is responsible for the whole transmission process _because_ it's the one who's trying to send. 
    - So SERVER network node is the one that needs to be on its game. 
    - CLIENT network node just has to set up things on its end according to what server is telling it to do
    - SERVER closes its connection but also needs to tell CLIENT to close its connection. 

- connection objects are only responsible for transmission.
    
### 2:30PM 
- Just start with an obese connection class and we can refactor this later.


```java

class NetworkNode {
    String id;
    Map<String, File> files; 
    List<Connection> connections;
    int maxSendingBandwidth;
    int maxReceivingBandwidth;
    int filesCap; 
    int bytesCap;
    
    
    /*
       Methods that report on List<Connections>
    }
    */
    /*
    private int countReceiving() {
        return Math.toIntExact(connections.stream().filter(connection -> connection.getType == RECIEVING).count()); 
    }
    
    private int countSending() {
        return Math.toIntExact(connections.stream().filter(connection -> connection.getType == SENDING).count());
    }
    // Might be able to turn these into functions within functions to make it look smaller
    private void receivingChannelWidth() {
        return maxReceivingBandwidth / (countReceiving() + 1);
    }
    
    public boolean sendingChannelWidth() {
        return maxSendingBandwidth / (countSending() + 1);
    }
    
    */
    
    /* File sending functionality */
    public void sendFile(String filename, NetworkNode client) {
        // Check if I have the file
        if (!files.get(filename)) {
            throw new VirtualFileNotFoundException(filename);
        }
        
        if (sendingChannelWidth() == 0) {
            throw new VirtualFileNoBandwidthException(id);
        }
        
        // Create connection object, and give it to the clinet
        Connection sourcepoint = files.get(filename), this);
        connections.append(sourcepoint);
        client.acceptDataConnection(sourcepoint, files.get(filename).getSize());        
        
    }
    
    public void acceptDataConnection(Connection sourcepoint, String filename, int memoryRequired) {
    
        // recject if no down bandwidth 
        if (receivingChannelWidth() == 0) {
            throw new VirtualFileNoBandwidthException(id);
        }
    
        // Reject connection if file already exists
        if (files.get(filename)) {
            throw new VirtualFileAlreadyExistsException();
        }
        
        // Reject connection if reached file cap
        if (files.size() + 1 > filesCap) {
            throw new VirtualNoStorageSpace("Max Files Reached");
        }
        
        // Reject connection if reached bytes cap
        if (memoryUsage() + memoryRequired > bytesCap) {
            throw new VirtualNoStorageSpace("Max Bytes Reached");
        }
        
        // Create connection object 
        File emptyFile = files.put(filename, new File(filename, memoryRequired));
        Connection endpoint = new Connection(emptyFile, this, memoryRequired); 
        sourcepoint.connect(endpoint);
        connections.append(endpoint);
        
    }
    
    // Server clean up post transmission will remove unusued connection objects in the same tick. 
    
    
    public void transmitData() {
        // Server hammers each connection object until rate limited
        for (Connection connection: connections) {
            if (connection.getType() == SENDING) {
                connection.send();
            }
        }
    }
    
    
    public void beforeTick() {
        // Set up bandwidth 
    }
    
    public void afterTick() {
        // Clean up unused connection objects
        
        
        // Clean up any completed connection objects
        
        // Reset everything else 
    }

}


class NetworkNode



```
