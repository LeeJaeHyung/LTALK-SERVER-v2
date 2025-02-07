package com.ltalk.server.view.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

import static com.ltalk.server.controller.ServerController.startServer;

public class MainViewController implements Initializable {

    @FXML
    TextArea textArea;
    @FXML
    Button button;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)  {

    }

    public void buttonClick() {
        if(button.getText().equals("Start")) {
            try {
                startServer();
                button.setText("Stop");
                textArea.appendText("[Server]-> Start\n");
            }catch(Exception e) {
                e.printStackTrace();
            }
        }else {
            try {
                button.setText("Start");
                textArea.appendText("[Server]-> Stop\n");
            }catch(Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void addText(String text) {
        Platform.runLater(()->{
            textArea.appendText(text+"\n");
        });
    }

}