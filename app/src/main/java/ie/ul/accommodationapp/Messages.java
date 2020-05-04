package ie.ul.accommodationapp;

public class Messages {

    private String from, message, date;

    public Messages(){
        //empty public constructor needed to be a POJO model
    }

    public Messages(String from, String message, String date){
        this.from = from;
        this.message = message;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
