import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by Risto on 7/7/2016.
 */
public class ResponseCheckerTask extends TimerTask {

    private String url;
    private String content;

    public ResponseCheckerTask(String url, String content) {
        this.url = url;
        this.content = content;
    }

    @Override
    public void run() {
        WebChecker wc = new WebChecker();
        try {
            wc.getResponse(url, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
