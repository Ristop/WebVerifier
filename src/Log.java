import javafx.scene.control.TextArea;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Risto on 7/8/2016.
 */
public class Log {

    private TextArea logArea;

    public Log(TextArea logArea) {
        this.logArea = logArea;
    }

    public void write(String text){

        logArea.appendText("[" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + "] " + text);
    }
}
