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

import networking.Packet; 


public abstract class AbstractNode implements Communicable { 

    protected Map<String, Communicable> topology = new HashMap<>(); 
    protected List<Packet> inbox = new ArrayList<Packet>(); 

    protected AbstractNode() {
        // Nothing to create for now

    }

    public void broadcast() { 
            if (this.inbox.size() > 0) { 
            return; 
        }

        Packet ping = new Packet("D", getId(), "popty ping" + getId(), "DesktopDevice"); 

        for (Communicable node : topology.values()) { 
            node.accept(ping); 
        }

    }

    public void accept(Packet p) { 
        inbox.add(p); 
    }

    public void acknowledge() {
        if (this.inbox.size() > 2) { 
            inbox.clear(); 
        }

    }

    public void sync(boolean add, Communicable node) {
        if (add) { 
            topology.put(node.getId(), node); 
        } else { 
            topology.remove(node); 
        }

    }


    /*
        I REALLY DO NOT LIKE THIS STUPID GETTER.

        FIND A BETTER WAY THAN USING ABSTRACT CLASSES LATER. 

        There's got to be a better way than using implementation inheritance here
        because I'm just going to run into this problem of how do you make sure
        a module has enough information and you will just end up with lots of stupid
        getters. I mean it's all code that is conceptually working on the same 
        data even if it's not all part of it 
    */
    public abstract String getId(); 

}