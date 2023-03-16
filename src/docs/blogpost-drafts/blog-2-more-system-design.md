# Dealing with "going out of range" 

Well my current design is like this: 
```
BlackoutController <>---- NetworkNode <>---- Server <>---- Sockets <>---- Files 
                                                    <>---- Connections<> ----|
```

So the division of labour is: 
- `BlackoutController` is responsible for propagating any global state changes 
- `NetworkNode` needs to perform two functions: (1) move (2) store + transmit files
    - We give a server object to a network node because there's a lot of functionality we can abstract out of it
    - _whereas_ for motion, there's not as much involved so I am happy to just leave it in the network node 
- `Server` stores and transmits files. 
    - Has a `Map<String, File>` to store files
    - Transmission management is by way of opening /closing connection objects. 
    
    
In terms of "going out of range" or closing stuff -- we need to remove the connection object. 

The connection object is really a socket: 
- Socket contains a buffer. 

Idea is that `Server` will keep tapping a socket until rate limited either by self or by target. 

# UML of everything
> What to do: plan out UML of current set up
> Then assess whether this design is extensible to cover the other tasks

> Things to do -- try to distribute the file transmission across classes.

Basically I want to carefully plan out where all the data should go and how it should be encapsulated before even typing any code out 

As a general rule of thumb I've decided that: 
- Any more than 6-7 instance variables is too much for a class. 

```java
BlackoutController
- Map<String, NetworkNode> nodes
- int clock; 
+ addFileToDevice(String deviceId, String filename, String content)
+ sendFile()
- updateVisibilityGraph()

```
`NetworkNode`s in an inheritance hierachy. The job of a network node is to (1) move + allow movement to interfere with transmission. (2) File management and transmission is delegated to the server object. 
```java
NetworkNode: 
- Angle position
- int height
- double linearVelocity
- Server server
+ setCommunicableNodes(List<NetworkNode> visible) // Propagates information to server
+ move(): void abstract 
+ getPosition(): Angle protected 
+ setPosition(Angle newPosition): void protected
+ supports(): Set<NetworkNodeTypes> abstract protected 
+ range(): int abstract protected // sending range 
+ tickover(): 

Device extends NetworkNode
+ move() // Devices do not move 
+ addFileToDevice(String filename, String content) // calls server object

DesktopDevice extends Device
HandheldDevice extends Device
LaptopDevice extends Device

Satellite extends NetworkNode

RelaySatellite extends Satellite
+ move() 
- reverse()

StandardSatellite extends Satellite
+ move() 

TeleportingSatellite extends Satellite
+ move() 
- teleport() 

```
Networking things: responsibility of server is (1) store files (2) manage connections, including enforcing server constraints. Responsibility of connection object is solely to just transmit data from file to file -- if server doesn't want to transmit a file, then it should remove connection object. 

```java
Server
- Map<String, File> files       // file objects
- List<Connection> connections  // connection objects
- maxBytes: int // server constraints
- maxFiles: int
- maxUploadBandwidth: int
- maxDownloadBandwidth: int
- setUploadBandwidth(): void private
- setDownloadBandwidth(): void private // Basically, calculate it from the files 
+ Server(int maxBytes, int maxFiles, int maxUploadBandwidth, int maxDownloadBandwidth)
+ add(File file) // addFileToDevice -> addFileToDevice -> add() 
- retrive(String filename): File // Throws file not found error
+ reset(): 



Connection: 
- File resource
- Connection 
- int fp
- String buffer
- boolean wasHit 
+ transmit() 
+ receive() 
```





File objects: basically it is just a container for data that is being transmitted around. I don't think it should be the responsibility of the file to keep track of its transmission status -- that should be responsibility of something else. 
```java
File
- String filename
- String data
- int size
+ File(String filename, String data, int size) // for adding a file to a device 
+ File(String filename, int size) // For clients creating empty files
+ read(int position): String
+ append(String string): void
+ getFileInfoResponse(): FileInfoResponse
```

# Mocking up some code for transmission 
At the level of `BlackoutController`: 
- Only want to retrive the two objects, and then delegate registration of transmission ("tell the two objects to talk to each other")
```java
public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
    NetworkNode source = nodes.get(fromId); 
    NetworkNode target = nodes.get(toId); 
    
    
    source.send(filename, target)
}

```




### Alternative implementation

Then inside the `NetworkNode` class


```java
NetworkNode: 
- connect(filename, target)


```
```go
upload(String filename, NetworkNode to) {
    check file exists 
    check uploading bandwidth sufficient
    
    to.download(filename, size, this)
}

download(String filename, int size, NetworkNode from) {
    
    check receiving bandwidth sufficient
    check file not already exists
    check enough storage space
    
    create empty file 
    create download socket
    
    
    from.connect(filename, socket)
}

connect(filename, target) {
    File resource = getFile(filename);
    create upload socket 
}

bind(connection)


```