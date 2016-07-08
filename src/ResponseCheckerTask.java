import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private TextArea logArea;

    private int connectionFailures = 0;

    public ResponseCheckerTask(String url, String content, int timeout, ArrayList<Integer> alertConditions, String cellNumber, TextArea logArea) {
        this.url = url;
        this.content = content;
        this.timeout = timeout;
        this.alertConditions = alertConditions;
        this.cellNumber = cellNumber;
        this.logArea = logArea;
    }

    @Override
    public void run() {
        try {
            getResponse(timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getResponse(int timeout) throws IOException {
        URL url = new URL(this.url);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(timeout);

        InputStream is;
        try {
            long startTime = System.nanoTime();
            is = con.getInputStream();
            Double endTime = ((Double) ((System.nanoTime() - startTime) / 10000000.0)).intValue() / 100.0;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            checkResponseContent(br, endTime);
        } catch (IOException e) {
            logArea.appendText("request to " + this.url + " - timeout");
            addFailure();
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
            logArea.appendText("request to " + url + " - returned 200 in " + endTime + " ns\n");
            connectionFailures = 0;
        } else {
            logArea.appendText("request to " + url + " - returned 200 but did not include required string: " + this.content);
            addFailure();
        }

        br.close();
    }

    private void addFailure() {
        connectionFailures++;
        if (alertConditions.contains(connectionFailures)){
            logArea.appendText(" - sending out SMS alert to " + this.cellNumber + "\n");
        } else {
            logArea.appendText("\n");
        }
    }
}