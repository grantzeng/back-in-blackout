# Task 2c) 

Design questions: 

- _What are the entities_ and how do they relate?
- _What are the data and what are the operations_? 

# Data involved: 
Files
- file on server
- (to create) empty file on client 

Server/client access to file 
- whether file exists on server
- whether file exists on client (spec says complete _or_ downloading, but doesn't seem to matter?)

Transmission characteristics
- server upload bandwidth 
- client download bandwidth
- whether server or client has been rate limited 
    - have a counter in the two sockets. 

Client storage characteristics (NB: seems like space is completely allocated before transmission begins)
- whether max file cap is reached
- whether max storage space is reached

Whether client is in sending range of server

# Who is responsible for what?
- Server should be responsible for managing files + transmission. Server should be responsible for keeping track of whether client has "gone out of range". 
- File is basically just a container for data
- Connection objects should only know enough to transmit to another connection. 
    - Shouldn't _need_ 
    
    
# Where is all the data currently?
(Plus some notes about operations I might want to do on them)

```java
BlackoutController:  // world controller
- Map<String, NetworkNode> nodes;

NetworkNode:  // "does" i.e. moves and has a file server 
- Server server;
- List<NetworkNode> communicable; // put interface to stop the self loop in the UML diagram (also _restricts_ networknodes from knowing about things they don't use)

Server:     // "does" file server things (i.e. store files and manage transmissions
- name: String                      // just name it as '{$NETWORKNODE}Server'
- files: Map<String, File>          // check whether file with name already exists on server (server: do I have src file?; 
                                    //       - client: do I already have it/already receiving it?)
                                    // check whether max files is violated if a new file is added
                                    // check whether max bytes is violated if a new file is added
- maxSendingBandwidth: int          // check whether enough bandwidth  c.f. (divide max bandwidth by number uploading and check it isn't zero)
- maxRecievingBandwidth: int        // Check whether enough down bandwidth c.f. ditto
- connections: List<Connection>     // connection objcts

Connection: 
- source: String                    // Endpoint source (network node id -- this way server knows if need to REMOVE an endpoint)
- endpoint: Connection              // Connection object pipes data into other connection object
- cap: int                          // Server allocates cap every tick   
- usage: int                        // Connection not allowed to use more than cap
+ getEndpoint()                     // If this returns null then connection was removed on the server side 

```

_Questions_: Who should keep track of what `NetworkNodes` can talk to each other? The individual nodes? Or `BlackoutController`?



# Reminder
> It's about minimising coupling. That's why it's so frustrating because coupling is EVERYWHERE on the spec!


## 8:39PM 2023-03-01
Let's take a different approach. 

What if we just let coupling take its course. I think it's reasonable that a network node should know about other network nodes. We _could_ fix this later by inserting an interface to stop this self loop thing.
- Priority at this point is just _get_ something working and we can think about reducing coupling later.


## 9:09 2023-03-01
Yeah this isn't working. Write something that works, and then we'll refactor it. I think part of this exercise is making mistakes. 



## General idea of the system. 
1. _Registering a file to be sent_
```
server.alert(client)
server.request(filename) // sets up and returns a connection object
    // inside server: socket.bind(file), socket.unbind()
server.connect(endpoint)  // puts connection object 

```

# Idea: 
`NetworkNode` is a client of the `Servers` subsystem that does the transmission. 
- The only thing it's responsible for passing in is to tell _its_ server, to remove

```java
NetworkNode: 
- server: Server
- commnicable: List<NetworkNode>
+ updateServer() {
    server.setCommunicable(communicable.stream().map(node -> node.getName() + "server").collect(Collectors.toList());
}


Server: 
- connections: List<Connection>
+ setCommunicable(List<String> communicable) {
    connections = connections.stream().filter(conn -> communicable.contains(conn.getName())).collect(Collectors.toList());
}

```


# Let's try again. 
What's all the functionality involved for _registering_ a connection

```java
BlackoutController:
+ sendFile(String filename, String fromId, STring toId)  {
    NetworkNode sender = nodes.get(fromId); 
    NetworkNode receiver = nodes.get(toId); 
    sender.send(filename, receiver); // impleen
}


TransmissionFacade: 



```
# Let's try to explain it in English

## Initiating transmission
(Assume bandwidth etc. all been updated for the tick)

1. `server` is instructed by `BlackoutController` to send file `filename` to client `reciever`. 

2. Inside `server.initiate(filename, receiver)`: metadata is filename
    - Delegates initiation to its file server
    
3. Inside server's filenserver
    - fileServer checks if `filename` exists (if not, throw FileNotFoundException)
    - fileServer checks if it has enough bandwidth (if not, throw FileNotFoundException)
    - fileServer builds a JSON about storage requirements 
    - `receiver.accept(fileReqsJson, senderFileServer)`;

3. Inside `receiver.accept(metadata, senderFileServer)`; metadata is file requirements
    - Delgates accepting file transfer to its fileserver
    
4. Inside client's fileserver: `connect(FileToBeTransmittedMetadata, EmptyFile)
    - Receiver fserver checks if it has receiving bandwidth (if not throw NoBandwidthException)
    - Receiver fserver checks if it already exists (if not throw FileAlreadyExistsException)
    - Receiver fserver checks if it has storage space: 
        - Receiver checks if having new file will violate file cap
        - Receiver checks if having new file will violate storage cap
    
    - Create new empty file: `EmptyFile`
    - `Connection endpoint = new Connection(FileToBeTransmittedMetadata, EmptyFile)`
    - `connections.add(endpoint)`
    - `senderFileServer.connect(metadata, endpoint)` ; metadata is filename and server name 

4. Inside server's fileserver -- called `.connect()`: metadata is filename again

    - `Connection sourcepoint = new Connection(files.get(filename), locationName)` location is basically server name
    - `connections.add(sourcepoint)`
    
5. pass source point to the end point 
    - `endpoint.setSource(sourcepoint)`

> Brainspark: "data classes are bad" ...well, just send a JSON then.

### What this looks like as a class
```java

( Note: 
fileToBeTransmittedMetadata has: 
- source network node
- filename
- finalsize
)

NetworkNode: 
- server: Server
+ initiate(String filename, NetworkNode receiver)
+ accept(JSONObject fileToBeTransmittedMetadata, Server source)

Server: 
- files: Map<String, File>
- maxSendingBandwidth: int 
- maxReceivingBanwdith: int 
+ connect(JSONObject fileToBeTransmittedMetadata, Server destination) // create empty file, create endpoint socket and bind to empty file
+ connect(JSONObject fileToBeTransmittedMetadata, Connection endpoint) // create source socket and bind to source file, update endpoint socket


Connection:
- endpoint: Connection  // the other end of the connection
- resource: File        // file being read from or read to
- origin: String        // which network node data is coming from (from us or from somewhere else?) 
- bytesRemaining: int   // subtract until zero. server decides whether to allocate as 

OutgoingConnection:
- fp: int               // Keeps reference to
- limit: int            // last index of the file 
+ send(); 

IncomingConnection: 
- buffer: String
+ receive(); 


File
- sendingRequirements: JSONObject

```


### Getting transmission to tick over


### Closing off a transmission





# How sockets actually work
`bind()` connects socket to a local resource (a file in this case)

`listen()` tells socket to start accepting incoming TCP connections


