package asak.pro.sms_application.services.models;

/**
 * Created by maanadev on 1/25/16.
 */
public class Receiver_details {

    private String responseType;
    private String msgId;


    public Receiver_details(String type, String msgId) {
        this.msgId = msgId;
        this.responseType = type;

    }

    public String getResponseType() {
        return responseType;
    }

    public String getMsgId() {
        return msgId;
    }


}
