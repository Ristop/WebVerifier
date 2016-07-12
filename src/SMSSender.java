import com.messente.Messente;
import com.messente.response.MessenteResponse;

/**
 * Created by Risto on 7/12/2016.
 */

public class SMSSender {
    public static final String API_USERNAME = "587ff2770d4d01c35363c5d952ae35ea";
    public static final String API_PASSWORD = "7006c262d865f671f85d018952d234cb";
    public static final String SMS_SENDER_ID = "+37256646750";

    public String SMS_RECIPIENT;

    public SMSSender(String SMS_RECIPIENT) {
        this.SMS_RECIPIENT = SMS_RECIPIENT;
    }

    public void send(String SMS_TEXT) {
        // Create Messente client
        Messente messente = new Messente(API_USERNAME, API_PASSWORD);

        // Create response object
        MessenteResponse response = null;

        try {
            // Send SMS
            response = messente.sendSMS(SMS_SENDER_ID, SMS_RECIPIENT, SMS_TEXT);

            // Checking the response status
            if (response.isSuccess()) {

                // Get Messente server full response
                System.out.println("Server response: " + response.getResponse());

                // Get unique message ID part of the response(can be used for retrieving message delivery status later)
                System.out.println("SMS unique ID: " + response.getResult());

            } else {
                // In case of failure get failure message
                throw new RuntimeException(response.getResponseMessage());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Failed to send SMS! " + e.getMessage());
        }
    }

}
