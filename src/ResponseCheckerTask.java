import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by Risto on 7/7/2016.
 */
public class ResponseCheckerTask extends TimerTask {

    private String url;
    private String content;
    private int timeout;

    public ResponseCheckerTask(String url, String content, int timeout) {
        System.out.println("Initializing response checker task");
        this.url = url;
        this.content = content;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        System.out.println("Executing response checker task");
        WebChecker wc = new WebChecker();
        try {
            wc.getResponse(url, content, timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
