
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class Controller {
    @FXML
    private TextField URLField;
    @FXML
    private TextField ContentField;
    @FXML
    private TextField IntervalField;
    @FXML
    private TextField MaxResponseTimeField;
    @FXML
    private TextField MessageSendOutTimesField;
    @FXML
    private TextField CellNumberField;

    @FXML
    private Button StartButton;

    @FXML
    private TextArea logArea;

    private boolean checking = false;
    private ArrayList<Integer> alertConditions = new ArrayList<>();
    private Timer timer;

    @FXML
    private void startChecking() {
        if (!checking) {
            if (validateFields()){
                timer = new Timer();
                String url = URLField.getText();
                String content = ContentField.getText();
                Long period = Long.parseLong(IntervalField.getText()) * 1000;
                Double timeout = Double.parseDouble(MaxResponseTimeField.getText()) * 1000;
                String cellnumber = CellNumberField.getText();

                timer.scheduleAtFixedRate(new ResponseCheckerTask(url, content, timeout.intValue(), alertConditions, cellnumber, logArea), 0, period);

                StartButton.setText("Stop");
                checking = true;
            }
        } else {
            timer.cancel();
            timer.purge();
            checking = false;
            StartButton.setText("Start");
        }
    }

    private boolean validateFields() {
        boolean validated = true;
        if (!URLField.getText().startsWith("http://")) {
            URLField.setStyle("-fx-border-color: red;");
            validated = false;
        }else {
            URLField.setStyle("-fx-border-color: gray;");
        }

        try {
            Long.parseLong(IntervalField.getText());
            IntervalField.setStyle("-fx-border-color: gray;");
        }catch (Exception e){
            IntervalField.setStyle("-fx-border-color: red;");
            validated = false;
        }

        try {
            Double.parseDouble(MaxResponseTimeField.getText());
            MaxResponseTimeField.setStyle("-fx-border-color: gray;");
        }catch (Exception e){
            MaxResponseTimeField.setStyle("-fx-border-color: red;");
            validated = false;
        }

        try{
            String messageSendOutTimesString = MessageSendOutTimesField.getText().replace(" ", "");
            String[] split = messageSendOutTimesString.split(",");
            List<String> times = new ArrayList<>(Arrays.asList(split));
            for (String time : times) {
                alertConditions.add(Integer.parseInt(time));
            }
            MessageSendOutTimesField.setStyle("-fx-border-color: gray;");
        } catch (Exception e){
            MessageSendOutTimesField.setStyle("-fx-border-color: red;");
        }

        return validated;
    }

}
