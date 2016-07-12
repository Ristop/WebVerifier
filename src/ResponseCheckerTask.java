import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by Risto on 7/7/2016.
 */

public class ResponseCheckerTask extends TimerTask {

    private String url;
    private String content;
    private int timeout;
    private ArrayList<Integer> alertConditions;
    private String cellNumber;
    private Log log;
    private SMSSender smsSender;

    private int connectionFailures;

    private String logText;
    private String requestInfoText;

    public ResponseCheckerTask(String url, String content, int timeout, ArrayList<Integer> alertConditions,
                               String cellNumber, TextArea logArea) {
        this.url = url;
        this.content = content;
        this.timeout = timeout;
        this.alertConditions = alertConditions;
        this.cellNumber = cellNumber;
        this.log = new Log(logArea);

        this.logText = "request to " + this.url + " - ";
        this.smsSender = new SMSSender(cellNumber);
        this.connectionFailures = 0;
    }

    @Override
    public void run() {
        getResponse(timeout);
    }

    private void getResponse(int timeout) {
        try {
            // Set up the connection to the site
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            // Calculate the connection time
            long startTime = System.nanoTime();
            int responseCode = connection.getResponseCode();
            Double endTime = ((Double) ((System.nanoTime() - startTime) / 10000000.0)).intValue() / 100.0;

            // If the response code is 200, then get the content and check the data
            if (responseCode == 200) {
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                checkResponseContent(br, endTime);
            }
        } catch (SocketTimeoutException e) {
            this.requestInfoText = "timeout";
            log.writeWithTimestamp(this.logText + this.requestInfoText);
            addFailure();
        } catch (IOException e) {
            this.requestInfoText = "connection failed";
            log.writeWithTimestamp(this.logText + this.requestInfoText);
            addFailure();
        }

    }

    /*
    * Checks if the content of the web page includes required string
    */
    private void checkResponseContent(BufferedReader br, double endTime) throws IOException {
        boolean correctContent = false;

        // Check the content line by line
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains(this.content)) {
                correctContent = true;
                break;
            }
        }

        if (correctContent) { // Request successful
            this.requestInfoText = "returned 200 in " + endTime + " s";
            log.writeWithTimestamp(this.logText + this.requestInfoText + "\n");
            if (connectionFailures != 0){
                connectionFailures = 0;
                sendAlert("Site " + this.url + " up!");
            }
        } else { // Returned 200 but the content was invalid
            this.requestInfoText = "invalid content";
            log.writeWithTimestamp(this.logText + this.requestInfoText);
            addFailure();
        }

        br.close();
    }

    /*
    * Adds one failure to the counter and checks if it needs to send out an alert message.
    */
    private void addFailure() {
        connectionFailures++;
        if (alertConditions.contains(connectionFailures)) {
            log.write(" - sending out SMS alert to " + this.cellNumber + "\n");
            sendAlert("Site " + this.url + " down! Reason: " + this.requestInfoText);
        } else {
            log.write("\n");
        }
    }

    private void sendAlert(String alertText){
        this.smsSender.send(alertText);
    }
}