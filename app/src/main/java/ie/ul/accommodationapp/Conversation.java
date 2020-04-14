package ie.ul.accommodationapp;

public class Conversation {

    private String user;
    private String imgURL;//use the first house image as the profile image for each conversation
    private String time;//datetime?


    public Conversation(String user, String time){
        this.user = user;
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public java.lang.String getTime() {
        return time;
    }
}
