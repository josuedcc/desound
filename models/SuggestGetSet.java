package cc.devjo.desound.models;

/**
 * Created by Josue on 19/12/2015.
 */
public class SuggestGetSet {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SuggestGetSet(String id, String name) {
        this.id = id;
        this.name = name;
    }

    String id,name;
}
