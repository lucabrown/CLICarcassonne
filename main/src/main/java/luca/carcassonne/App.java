// package luca.carcassonne;

// import javafx.application.Application;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.input.MouseButton;
// import javafx.scene.layout.GridPane;
// import javafx.scene.layout.StackPane;
// import javafx.scene.paint.ImagePattern;
// import javafx.scene.shape.Rectangle;
// import javafx.scene.transform.Rotate;
// import javafx.stage.Stage;
// import javafx.event.ActionEvent;
// import javafx.event.EventHandler;
// import java.io.IOException;

// /**
// * JavaFX App
// */
// public class App extends Application {

// private static Scene scene;
// private static Game game;
// private GridPane root;
// private ImageView image;

// @Override
// public void start(Stage primaryStage) {
// primaryStage.setTitle("Hello World!");
// Button btn = new Button();
// btn.setText("Say 'Hello World'");
// btn.setOnAction(new EventHandler<ActionEvent>() {

// @Override
// public void handle(ActionEvent event) {
// System.out.println("Hello World!");
// }
// });

// StackPane root = new StackPane();
// root.getChildren().add(btn);
// primaryStage.setScene(new Scene(root, 300, 250));
// primaryStage.show();
// }

// public static void main(String[] args) {
// game = new Game(new Board());
// launch();
// }

// public void rotateImage() {
// image.setRotate(90);
// }
// }