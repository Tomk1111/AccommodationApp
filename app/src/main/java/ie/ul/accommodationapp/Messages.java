package ie.ul.accommodationapp;

public class Messages {

    private String from, message;

    public Messages(){
        //empty public constructor needed to be a POJO model
    }

    public Messages(String from, String message){
        this.from = from;
        this.message = message;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
