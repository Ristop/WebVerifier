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

    private int connectionFailures;

    public ResponseCheckerTask(String url, String content, int timeout, ArrayList<Integer> alertConditions,
                               String cellNumber, TextArea logArea) {
        this.url = url;
        this.content = content;
        this.timeout = timeout;
        this.alertConditions = alertConditions;
        this.cellNumber = cellNumber;
        this.log = new Log(logArea);

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
            log.write("request to " + this.url + " - timeout" + "\n");
            addFailure();
            e.printStackTrace();
        } catch (IOException e) {
            log.write("request to " + this.url + " - connection failed" + "\n");
            addFailure();
            e.printStackTrace();
        }

    }

    private void checkResponseContent(BufferedReader br, double endTime) throws IOException {
        boolean correctContent = false;

        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains(this.content)) {
                correctContent = true;
                break;
            }
        }

        if (correctContent) {
            log.write("request to " + url + " - returned 200 in " + endTime + " s\n");
            connectionFailures = 0;
        } else {
            log.write("request to " + url + " - returned 200 - invalid response\n");
            addFailure();
        }

        br.close();
    }

    private void addFailure() {
        connectionFailures++;
        if (alertConditions.contains(connectionFailures)) {
            log.write("Sending out SMS alert to " + this.cellNumber + "\n");
            sendAlert();
        }
    }

    private void sendAlert(){
        // TODO: 7/11/2016 sending alert messages
    }
}