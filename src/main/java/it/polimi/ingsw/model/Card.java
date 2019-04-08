package it.polimi.ingsw.model;

public class Card {
    private String id;
    private String name;

    public Card(String id, String name){
        this.id=id;
        this.name=name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return "Card {id: "+id+", name: "+name+"}";
    }
}
