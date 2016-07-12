import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class Controller {
    @FXML private TextField URLField;
    @FXML private TextField ContentField;
    @FXML private TextField IntervalField;
    @FXML private TextField MaxResponseTimeField;
    @FXML private TextField MessageSendOutTimesField;
    @FXML private TextField CellNumberField;

    @FXML private Button StartButton;

    @FXML private TextArea logArea;

    private boolean checking = false;
    private ArrayList<Integer> alertConditions = new ArrayList<>();
    private Timer timer;

    @FXML
    private void startChecking() {
        if (!checking) {
            if (validateFields()){
                timer = new Timer();

                // Getting information from fields
                String url = URLField.getText();
                String content = ContentField.getText();
                Long period = Long.parseLong(IntervalField.getText()) * 1000;
                Double timeout = Double.parseDouble(MaxResponseTimeField.getText()) * 1000;
                String phoneNumber = CellNumberField.getText();

                // Start scheduled task
                timer.scheduleAtFixedRate(new ResponseCheckerTask(url, content, timeout.intValue(), alertConditions, phoneNumber, logArea), 0, period);

                // Reset variables
                StartButton.setText("Stop");
                checking = true;
            }
        } else { // Stop Checking
            timer.cancel();
            timer.purge();

            // Reset variables
            checking = false;
            StartButton.setText("Start");
        }
    }

    private boolean validateFields() {
        boolean validated = true;

        // URL validation
        if (!URLField.getText().startsWith("http://")) {
            URLField.setStyle("-fx-border-color: red;");
            validated = false;
        }else {
            URLField.setStyle("-fx-border-color: gray;");
        }

        // Interval. If the interval is not set, set it to 2
        if (!IntervalField.getText().equals("")){
            try {
                Long.parseLong(IntervalField.getText());
                IntervalField.setStyle("-fx-border-color: gray;");
            }catch (Exception e){
                IntervalField.setStyle("-fx-border-color: red;");
                validated = false;
            }
        } else {
            IntervalField.setText("2");
        }

        // Response time validation. If the time is not set, set it to 0.5
        if (!MaxResponseTimeField.getText().equals("")){
            try {
                Double.parseDouble(MaxResponseTimeField.getText());
                MaxResponseTimeField.setStyle("-fx-border-color: gray;");
            }catch (Exception e){
                MaxResponseTimeField.setStyle("-fx-border-color: red;");
                validated = false;
            }
        } else {
            MaxResponseTimeField.setText("0.5");
        }

        // Alert message times validation. If the field is not set, set it to [4, 10]
        if (!MessageSendOutTimesField.getText().equals("")){
            try{
                String messageSendOutTimesString = MessageSendOutTimesField.getText().replace(" ", "");
                String[] split = messageSendOutTimesString.split(",");
                List<String> times = new ArrayList<>(Arrays.asList(split));

                alertConditions.addAll(times.stream().map(Integer::parseInt).collect(Collectors.toList()));

                MessageSendOutTimesField.setStyle("-fx-border-color: gray;");
            } catch (Exception e){
                MessageSendOutTimesField.setStyle("-fx-border-color: red;");
            }
        } else {
            MessageSendOutTimesField.setText("4, 10");
            alertConditions.add(4);
            alertConditions.add(10);
        }

        return validated;
    }

}
