import java.util.Timer;

/**
 * Created by Risto on 7/7/2016.
 */
public class Main {
    public static void main(String[] args) {
        Timer timer = new Timer();

        String url = "https://www.oracle.com/index.html";
        String content = "products and solutions for every";

        timer.scheduleAtFixedRate(new ResponseCheckerTask(url, content), 1000, 1000);
    }
}
