// package luca.carcassonne;

// import javafx.application.Application;
// import javafx.scene.Scene;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.input.MouseButton;
// import javafx.scene.layout.GridPane;
// import javafx.scene.paint.ImagePattern;
// import javafx.scene.shape.Rectangle;
// import javafx.scene.transform.Rotate;
// import javafx.stage.Stage;

// import java.io.IOException;

// /**
//  * JavaFX App
//  */
// public class App extends Application {

//     private static Scene scene;
//     private static Game game;
//     private GridPane root;
//     private ImageView image;

//     @Override
//     public void start(Stage stage) throws IOException {
//         Tile tile = new Tile(0, 0);
//         // image = new ImageView(new Image(tile.getImageUrl()));
//         image.setRotate(90);
//         root = new GridPane();
//         scene = new Scene(root, 1000, 1000);

//         for (int row = 0; row < 20; row++) {
//             for (int col = 0; col < 20; col++) {
//                 Rectangle rec = new Rectangle();
//                 rec.setWidth(50);
//                 rec.setHeight(50);
//                 rec.setOnMouseClicked(e -> {
//                     if (e.getButton() == MouseButton.PRIMARY) {
//                         rec.setFill(new ImagePattern(image.getImage()));
//                     }
//                 });
//                 GridPane.setRowIndex(rec, row);
//                 GridPane.setColumnIndex(rec, col);
//                 root.add(rec, row * 100, col * 100);
//             }
//         }
//         scene.setOnMouseClicked(e -> {
//             if (e.getButton() == MouseButton.SECONDARY) {
//                 System.out.println("Right click");

//                 rotateImage();
//             }
//         });
//         stage.setScene(scene);
//         stage.show();
//     }

//     public static void main(String[] args) {
//         game = new Game(new Board());
//         launch();
//     }

//     public void rotateImage() {
//         image.setRotate(90);
//     }
// }