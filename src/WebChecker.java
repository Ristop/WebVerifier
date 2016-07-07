import sun.misc.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Risto on 7/7/2016.
 */
public class WebChecker {

    public void getResponse(String urlString, String content, int timeout) throws IOException {
        System.out.println("Getting response from web page");
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(timeout);
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        checkResponse(br, content);
    }

    private void checkResponse(BufferedReader br, String content) throws IOException {
        System.out.println("Checking response from web page");
        boolean correctContent = false;

        String line;
        while ((line = br.readLine()) != null){
            if (line.contains(content)){
                correctContent = true;
                break;
            }
        }

        if (correctContent){
            System.out.println("Veebileht töötab!");
        } else {
            System.out.println("Veebileht ei tööta!");
        }

        br.close();
    }

}
