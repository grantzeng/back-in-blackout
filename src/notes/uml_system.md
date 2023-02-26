# A possible UML of all the entities 



```java

NetworkNode: abstract class
- position: Angle
- height: Integer
- server: Server
- motion: Motion
- visible: List<NetworkNode> // Blackout controller pipes _in_ what entities should be visible to it, and it's the responsiblity of each network node to enforce its own constraints 
+ protected supports(): List<NetworkNodeTypes>
+ move(): abstract


Server: 
// Files on server 
- files: Map<String, File>
// Storage and transmission constraints
- maxStorage: Integer
- maxFiles: Integer
- uploadBandwidth: Integer
- downloadBandwidth: Integer
// Active upload and download sessions
- sessions: List<Socket>

Motion: 
- linearVelocity: double 
+ move() 

Socket: 
- resource: File //
- buffer: String
- yield()
- 
- flush()

File: 
- 
```




General rule of thumb seems to be: 
- no more than six or seven variables in class...any more and it's the class is doing too much. 