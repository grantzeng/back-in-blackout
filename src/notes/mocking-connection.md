# Mocking up connection 
```java

class BlackoutController {
    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
    
        NetworkNode src = nodes.get(fromId); 
        NetworkNode dest = nodes.get(toId); 
        
        src.alert(fileName, dest); 
    
        // TODO: Task 2 c)
    }

}

class NetworkNode {
    Server server;
    
    
    public void alert(String filename, NetworkNode client) {
        server.push(filename, to); 
    }
    
    public void request(String filename, int size, NetworkNode server) {
        server.bind(filename, socket, this);
    }
    
}

class Server {
    Map<String, File> files;
    List<Connection> connections;
    
    
    public void alert(String filename, NetworkNode client) {
        // Executed in server
        if file not exists: throw exception
        if insufficient upload bandwidth: throw exception 
        
        to.request(String filename, int size, this);
        
    }
    
    public void request(String filename, int size, NetworkNode server) {
        // Executed in client 
        if insufficent download bandwidth: throw exception
        if file already exists: throw exception
        if no storage space: throw exception 
        
        // Malloc empty file 
        File empty = new File(filename, size); 
        files.put(filename, empty); 
      
        // Create incoming socket 
        // - bind to empty file
        // - register where we would be requesting from
        Connection socket = new Connection(empty, server.getName());
      
        // Give reference to server 
        from.bind(filename, socket, this);
        
    }
    
    public void bind(String filename, Connection conn, NetworkNode client) {
    
        Connection socket = new Connection(files.get(filename), client.getName()); 
        
        socket.bind(conn);        
    }
    
    

}
```

###
Removing the layer of network node
```go

send(filename, client) {
    // execute in server
    if file not exists: throw exception 
    client.alert(filename, this); 
}

alert(filename, client) {
    // execute in server 
    client.request(filename, this)
}

request(filename, server) {
    if file already exists: throw exception 
    
    server.query(filename, client);
}

query(filename, client) {
    
}

```


### Trying a different way



```java
class BlackoutController {
    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        
        NetworkNode src = nodes.get(fromId); 
        NetworkNode dest = nodes.get(toId);    
        
        src.request(filename, dest); 
    }

}


Class NetworkNode {
}
```
Removing the NetworkNode layer from the discussion and just focusing on how servers can talk to each other
```java
Class Server {
    


}
```


## Change of plan

What if... we created the sockets first before binding them to a resource


## Change of plan again: 
- Go take a break 
- read about TCP 

- How about we implement the normal talking back and forth between sockets first and then figure out how to add the exceptions to throw in the right way


### What a connection object looks like
```java

class Connection {

    File resource;

    String buffer; 
    void write(String string) {
        // writes to buffer
    }
    
    String read(int position) {
        // read from buffer
        return
    } 

    void flush() {
        // flush buffer into resource
        // resource.append(buffer); 
        // buffer.reset(); 
    }
    
    Connection endpoint;
    int fp = 0; 
    void send() {
        // send a byte from the resource to the end poit
    }
    
    
    
    
}



```