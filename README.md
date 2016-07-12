# WebVerifier

Simple program with a graphical user interface to check the state of a web page.

## How to
User must enter a link to the web page (first text box), enter a phrase that the page must include (second text box).
Then the User can enter the interval (third text box in middle left), how often the page is checked and maximum response time (fourth text box in middle right). Default times are (2 and 0.5)
Next, user can set the message send out times separated with coma (fourth text box). The numbers show how many times in row the request must fail in order to send out the alert message.
Last text box is for the phone number. Right now the phone number needs to be whitelisted in order to work.
Now when the user clicks Start button, the program runs the script and starts checking the page until user click Stop.

