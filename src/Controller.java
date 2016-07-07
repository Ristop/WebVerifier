import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Timer;

public class Controller {
    @FXML private TextField URLField;
    @FXML private TextField ContentField;
    @FXML private TextField IntervalField;
    @FXML private TextField MaxResponseTimeField;
    @FXML private TextField MessageSendOutTimesField;
    @FXML private TextField CellNumberField;

    @FXML private Button StartButton;

    @FXML private void startChecking(){
        Timer timer = new Timer();

        String url = URLField.getText();
        String content = ContentField.getText();
        Long period = Long.parseLong(IntervalField.getText())*1000;
        Double timeout = Double.parseDouble(MaxResponseTimeField.getText())*1000;

        timer.scheduleAtFixedRate(new ResponseCheckerTask(url, content, timeout.intValue()), 0, period);
    }

}
