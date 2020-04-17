package ie.ul.accommodationapp;

public class Conversation {

    private String name;
    private String image;//use the first house image as the profile image for each conversation
    //private String time;//datetime?

    // Firebase RTDB models require an empty constructor
    public Conversation(){

    }

    public Conversation(String user, String img){
        this.name = user;
        this.image = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
