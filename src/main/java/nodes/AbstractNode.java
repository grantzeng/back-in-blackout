package nodes; 
/*
    Abstract class for a node in the network 

    - Does the base implementation of communication
*/
import java.util.HashMap;
import java.util.Map; 
import java.util.List;
import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections; 

import interfaces.Communicable; 
import interfaces.Uploadable; 

import networking.Packet; 
import networking.Probe; 

import files.File; 
import static files.File.COMPLETE; 

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse; 

import unsw.utils.Angle; 


import unsw.utils.MathsHelper; 

public abstract class AbstractNode implements Communicable {  //, Uploadable


    // An abstract node should only know about local topology and communicate with the network via packets
    // - BlackoutController will pass in whole topology, but it is the responsibility of each network node to censor itself
    //   of references it does not need
    protected Map<String, AbstractNode> communicables = new HashMap<>(); 


    protected List<Packet> buf = new ArrayList<Packet>(); // "buffer"; queue of packets received

    /*
        2024-12-12
        Replace this with getters so you can expose external state
        without risking people modifying (so thereby maintain _some_ kind of encapsulation)

        I think that's the point of getters. 
    
     */
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

        Packet ping = new Packet("D", id, "not_a_filename", 0, "ping", "popty ping");

        for (Communicable node : communicables.values()) { 
            node.listen(ping); 
        }

    }

    public void listen(Packet p) { 

        System.out.println(p.data +  " - " + id + " " + p.from); 

        // if (p.type == "ping") { 
        //     if !supports().contains(p.data) { 
        //         System.out.println("Not replying because sender is incompatible"); 
        //         return; 
        //     }

        //     communicables.values().stream()
        //         .map(node -> {  
        //             Packet reply = new Packet(node.id, this.id, "", -1, "ping-reply", "hi!"); 
        //             node.listen(reply);     
        //         }); 

        // }

        buf.add(p); 
        System.out.println(buf); 
    }

    // I think this boilerplate was a way of passing subclass information to super class
    // - this is just a temporary fix until I can figure out a way to replace abstract class altogether 
    //   and get rid of this trying to breathe through a straw kind of situation with data passing
    public abstract double range(); 
    
    public abstract List<String> supports(); 

    public abstract String type(); 
    
    public String id() { 
        return id; 
    }


    public void sync(Map<String, AbstractNode> topology) {
        // blackout controller passes in the whole topology
        // node is responsible for censoring itself to only immediate neighbours
        // - visible and communicable. 

        communicables = topology.values().stream() 
            .filter(node -> MathsHelper.isVisible(node.height, node.angle, this.height, this.angle) )
            .filter(node -> MathsHelper.getDistance(node.height, node.angle, this.height, this.angle) <= range())
            .filter(node -> supports().contains(node.type()))
            .collect(Collectors.toMap(
                AbstractNode::id, 
                node -> node
            ));
        
    }

    /*
        Function for determining communicability
    
    */



    /*
    
        
        
        Idea: 
        - Let's just flood neighbours first 
        - Then figure out how to handle relays 
    
        Basically the idea is that the objects talk to each other without us
        having to explicitly do a graph search
    
     */

    public List<String> discover() { 
        System.out.println(this.id + ".discover()"); 
        List<String> replies = communicables.values()
            .stream()
            .map(node -> {
                // Send out a probe to all neighbours
                Probe probe = new Probe(this.id, node.id, this.id, type());
                return node.discoverReply(probe); 
            })
            .flatMap(ids -> ids.stream()) // Some nodes have more than one replies e.g. if is relay
            .collect(Collectors.toList()); 
        return replies; 
    }

    public List<String> discoverReply(Probe probe) { 
        
        if (type() != "RelaySatellite") { 
            if (
                communicables.containsKey(probe.from) && 
                supports().contains(probe.type)
            ) { 
                // just return your id because you're compatible
                return Arrays.asList(this.id); 
            } else { 
                System.out.println(this.id + " - " + " not replying  to probe because incompatible or not a neighbour");
                return Arrays.asList();
            }
        }
    
        
        
        // // Normal behaviour
        // if (communicables.containsKey(p.from) &&)  {
        //     // Reply to probe

        // }

        //    // relay behaviour, forward it to all neighbours
        // // and collect responses 

         System.out.println("relay satelites currently not handled");
        return Arrays.asList();

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