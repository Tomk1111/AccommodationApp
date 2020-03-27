package ie.ul.accommodationapp;

public class Message {

    private String id;
    private String text;
    private String name;
    private String uuid;

    public Message() {
    }

    public Message(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

}

