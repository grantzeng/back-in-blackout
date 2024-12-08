package nodes; 
/*
    Abstract class for a node in the network 

    - Does the base implementation of communication
*/
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList; 
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections; 

import interfaces.Communicable; 
import interfaces.Uploadable; 

import networking.Packet; 

import files.File; 
import static files.File.COMPLETE; 

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse; 

import unsw.utils.Angle; 


public abstract class AbstractNode implements Communicable {  //, Uploadable

    protected Map<String, Communicable> topology = new HashMap<>(); 
    protected List<Packet> buf = new ArrayList<Packet>(); // "buffer"; queue of packets received

    public String id; 
    public Angle angle; 
    public double height; 

    protected AbstractNode(String id, Angle angle, double height) {
        this.id = id; 
        this.angle = angle; 
        this.height = height; 
    }


    /*
    
        Functions for sending and receiving packets
        - send packets out
        - get backets in 
        - update current understanding of network topology
    */

    public void broadcast() { 

        Packet ping = new Packet("D", id, "not_a_filename", 0, false, "popty ping");

        for (Communicable node : topology.values()) { 
            node.listen(ping); 
        }

    }

    public void listen(Packet p) { 
        buf.add(p); 
    }

    public void sync(boolean add, Communicable node) {
        if (add) { 
            topology.put(((AbstractNode) node).id, node); 
        } else { 
            topology.remove(node); 
        }
    }

    /*
    
        Functions for processing packets queue
        - things that need to be sent out
        - data that has to be read into particular packets
    */


    /*
        Functions for adding files to devices
        - Currently I don't have a better way of doing this by composition 
          so this also forces satellites to be able to have files to add to them
          but fixing this is a problem for later

    */
    protected Map<String, File> store = new HashMap<>(); 
 
    public void upload(String fname, String data) {
        File f = new File(fname, data.length(), File.COMPLETE, data); 
        store.put(fname, f); 
    }

    /*
        I REALLY DO NOT LIKE THIS STUPID GETTER.

        FIND A BETTER WAY THAN USING ABSTRACT CLASSES LATER. 

        There's got to be a better way than using implementation inheritance here
        because I'm just going to run into this problem of how do you make sure
        a module has enough information and you will just end up with lots of stupid
        getters. I mean it's all code that is conceptually working on the same 
        data even if it's not all part of it 

        Introducing abstract class introduces the problem of how to pass information 
        between a subclass and the abstract class both ways. 
    */
    // UPDATE 2024-12-08 - got rid of it by just passing the id in
    // public abstract String getId(); 

    protected EntityInfoResponse getInfo(String nodetype) { 


        Map<String, FileInfoResponse> finfos = new HashMap<>();

        for (File f : store.values()) {
            FileInfoResponse finfo = f.getFileInfoResponse(); 
            finfos.put(finfo.getFilename(), finfo);
        }

        return new EntityInfoResponse(id, angle, height, nodetype, finfos);

    }
}