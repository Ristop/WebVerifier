import sun.misc.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Risto on 7/7/2016.
 */
public class WebChecker {

    public void getResponse(String urlString, String content) throws IOException {
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        checkResponse(br, content);
    }

    private void checkResponse(BufferedReader br, String content) throws IOException {

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
