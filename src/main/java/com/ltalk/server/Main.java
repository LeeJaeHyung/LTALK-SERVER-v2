package com.ltalk.server;

import com.ltalk.server.controller.ServerController;
import com.ltalk.server.view.controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static com.ltalk.server.controller.ServerController.shutdownServer;

public class Main extends Application {

    public static MainViewController control;
    public static ServerController server;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("[" + Thread.currentThread().getName() + "] on start()");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ServerMain.fxml"));
        Parent parent = loader.load();
        control = loader.getController();
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LTalk-Sever");
        primaryStage.getIcons().add(new Image("file:src/images/icon.png"));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("종료 됨");
            shutdownServer(); // 서버 종료 처리
        });
        server = new ServerController();
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        shutdownServer(); // 서버 종료
        super.stop();
    }


}
