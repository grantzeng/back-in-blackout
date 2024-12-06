package interfaces; 

/*
    
    Represents that an entity is movable
    - Initially only implemented by satellites (just filter by instanceof Moveable)
    - but design allows this to be implemented by devices later

 */

public interface Movable {
    public void move(); 
}