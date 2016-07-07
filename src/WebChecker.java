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
    public static void main(String[] args) throws IOException {
        String link = "https://www.oracle.com/index.html";
        try {
            getResponse(link);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void getResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = br.readLine()) != null){
            System.out.println(line);
        }

        br.close();
    }
}
