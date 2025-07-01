import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.bson.Document;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class App extends Application {
   private Stage primaryStage;
   private VBox rootLayout;
   private boolean isSignedUp = false;
   String uri = "mongodb+srv://fostericetwo:TwcEP3um0ycpfhTe@attendanceappcluster.xvh3e.mongodb.net/?retryWrites=true&w=majority&appName=AttendanceAppCluster";
   MongoClient mongoClient;
   MongoDatabase database;
   MongoCollection<Document> usersCollection;
   private List<ComboBox<String>> answerDropdowns;

   public App() {
      this.mongoClient = MongoClients.create(this.uri);
      this.database = this.mongoClient.getDatabase("EyeClear");
      this.usersCollection = this.database.getCollection("Patient_credentials");
      this.answerDropdowns = new ArrayList();
   }

   public void start(Stage stage) {
      this.primaryStage = stage;
      this.rootLayout = new VBox();
      this.primaryStage.setScene(new Scene(this.rootLayout, 400.0, 500.0));
      this.showSignUpScreen();
      this.primaryStage.show();
   }

   private void showSignUpScreen() {
      this.primaryStage.setTitle("Sign Up");
      Image backgroundImage = new Image("file:src/Images/bg1.png");
      BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100.0, 100.0, true, true, true, true));
      VBox signUpLayout = new VBox(10.0);
      signUpLayout.setPadding(new Insets(20.0));
      signUpLayout.setAlignment(Pos.CENTER);
      this.rootLayout.setBackground(new Background(new BackgroundImage[]{background}));
      Label title = new Label("Sign Up");
      title.setFont(Font.font("Arial", FontWeight.BOLD, 18.0));
      title.setStyle("-fx-text-fill: white;");
      TextField usernameField = new TextField();
      usernameField.setPromptText("Enter username");
      PasswordField passwordField = new PasswordField();
      passwordField.setPromptText("Enter password");
      String password = passwordField.getText();
      Image signUpImage = new Image("file:src/Images/sign_up.png");
      ImageView imageView = new ImageView(signUpImage);
      imageView.setFitWidth(150.0);
      imageView.setFitHeight(30.0);
      Button signUpButton = new Button("", imageView);
      signUpButton.setContentDisplay(ContentDisplay.LEFT);
      signUpButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
      signUpButton.setOnAction((e) -> {
         String username = usernameField.getText();
         Document query1 = new Document("username", username);
         Document user = (Document)this.usersCollection.find(query1).first();
         if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
            System.out.println(username);
            if (user != null) {
               this.isSignedUp = true;
               this.showHomeScreen();
            } else {
               this.showAlert("Sign-Up Error", "Incorrect username or password");
            }
         } else {
            this.showAlert("Sign-Up Error", "Please fill all fields!");
         }

      });
      signUpLayout.getChildren().setAll(new Node[]{title, usernameField, passwordField, signUpButton});
      this.rootLayout.getChildren().setAll(new Node[]{signUpLayout});
   }

   private void showHomeScreen() {
      this.primaryStage.setTitle("Vertical Buttons");
      HBox titleBar = new HBox();
      titleBar.setStyle("-fx-background-color: blue; -fx-padding: 10;");
      Label titleLabel = new Label("EyeClear");
      titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
      titleBar.getChildren().add(titleLabel);
      VBox buttonPanel = new VBox(10.0);
      buttonPanel.setPadding(new Insets(5.0));
      buttonPanel.setAlignment(Pos.CENTER);
      Image check = new Image("file:src/Images/check.png");
      ImageView imageView2 = new ImageView(check);
      imageView2.setFitWidth(250.0);
      imageView2.setFitHeight(120.0);
      imageView2.setPreserveRatio(true);
      Button btn1 = new Button("", imageView2);
      btn1.setBackground((Background)null);
      btn1.setBorder((Border)null);
      Image consult = new Image("file:src/Images/consult.png");
      ImageView imageView3 = new ImageView(consult);
      imageView3.setFitWidth(250.0);
      imageView3.setFitHeight(120.0);
      imageView3.setPreserveRatio(true);
      Button btn2 = new Button("", imageView3);
      btn2.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
      Image glasses = new Image("file:src/Images/glasses.png");
      ImageView imageView4 = new ImageView(glasses);
      imageView4.setFitWidth(250.0);
      imageView4.setFitHeight(120.0);
      imageView2.setPreserveRatio(true);
      Button btn3 = new Button("", imageView4);
      btn3.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
      btn1.setMinSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
      btn1.setPrefSize(250.0, 120.0);
      btn1.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
      btn2.setMinSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
      btn2.setPrefSize(120.0, 120.0);
      btn2.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
      btn3.setMinSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
      btn3.setPrefSize(120.0, 120.0);
      btn3.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
      btn1.setOnAction((e) -> {
         this.showFormScreen();
      });
      btn2.setOnAction((e) -> {
         this.showScreenTwo();
      });
      btn3.setOnAction((e) -> {
         this.showScreenThree();
      });
      buttonPanel.getChildren().addAll(new Node[]{btn1, btn2, btn3});
      Button chatBotButton = new Button("\ud83d\udcac Chatbot");
      chatBotButton.setPrefSize(100.0, 50.0);
      chatBotButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");
      chatBotButton.setOnAction((e) -> {
         this.showChatBot();
      });
      StackPane chatBotPane = new StackPane(new Node[]{chatBotButton});
      chatBotPane.setPadding(new Insets(0.0, 20.0, 20.0, 0.0));
      StackPane.setAlignment(chatBotButton, Pos.BOTTOM_RIGHT);
      BorderPane homeLayout = new BorderPane();
      homeLayout.setTop(titleBar);
      homeLayout.setCenter(buttonPanel);
      homeLayout.setBottom(chatBotPane);
      this.rootLayout.getChildren().setAll(new Node[]{homeLayout});
   }

   private void showFormScreen() {
      Button backButton = new Button("←");
      backButton.setFont(Font.font("Arial", FontWeight.BOLD, 16.0));
      backButton.setOnAction((e) -> {
         this.showHomeScreen();
      });
      VBox formLayout = new VBox(20.0);
      formLayout.setPadding(new Insets(20.0));
      formLayout.getChildren().add(backButton);
      formLayout.setStyle("-fx-background-image: url('/Images/clip.png'); -fx-background-size: cover;");
      this.answerDropdowns.clear();
      formLayout.getChildren().addAll(new Node[]{this.createQuestion("Do you experience blurry vision?", "Yes, when looking at near objects (Hyperopia – Long Sightedness)", "Yes, when looking at distant objects (Myopia – Short Sightedness)", "Yes, vision is cloudy or foggy (Possible Cataracts)", "No"), this.createQuestion("Do you have difficulty seeing at night?", "Yes, very often (Possible Cataracts or Diabetic Eye Disease)", "Sometimes", "No"), this.createQuestion("Do you experience loss of peripheral (side) vision?", "Yes (Possible Glaucoma)", "No"), this.createQuestion("Do you see dark spots or sudden vision loss?", "Yes, I see dark spots or floaters (Possible Diabetic Eye Disease or Retinal Detachment)", "Yes, a shadow or curtain-like vision loss (Possible Retinal Detachment)", "No"), this.createQuestion("Do straight lines appear wavy or distorted?", "Yes (Possible Macular Degeneration)", "No"), this.createQuestion("Do you feel eye pain or pressure?", "Yes, severe pain (Possible Glaucoma or Corneal Ulcer)", "No"), this.createQuestion("Do you have redness, swelling, or discharge from your eye?", "Yes, with sticky or thick discharge (Possible Eye Infection or Conjunctivitis)", "Yes, with pain (Possible Corneal Ulcer)", "No")});
      Button submitButton = new Button("Submit");
      submitButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
      submitButton.setOnAction((e) -> {
         String diagnosis = this.determineDiagnosis();
         this.showCameraScreen(diagnosis);
      });
      VBox buttonBox = new VBox(new Node[]{submitButton});
      buttonBox.setAlignment(Pos.CENTER);
      formLayout.getChildren().add(buttonBox);
      ScrollPane scrollPane = new ScrollPane(formLayout);
      scrollPane.setFitToWidth(true);
      scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
      this.rootLayout.getChildren().setAll(new Node[]{scrollPane});
   }

   private VBox createQuestion(String question, String... options) {
      Label label = new Label(question);
      label.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: 'Arial';");
      ComboBox<String> dropdown = new ComboBox();
      dropdown.getItems().addAll(options);
      dropdown.setPromptText("Select an option");
      this.answerDropdowns.add(dropdown);
      return new VBox(10.0, new Node[]{label, dropdown});
   }

   private String determineDiagnosis() {
      Map<String, Integer> diagnosisCount = new HashMap();
      Iterator var3 = this.answerDropdowns.iterator();

      while(true) {
         while(true) {
            String answer;
            do {
               do {
                  if (!var3.hasNext()) {
                     return (String)diagnosisCount.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).orElse("Unknown Condition");
                  }

                  ComboBox<String> dropdown = (ComboBox)var3.next();
                  answer = (String)dropdown.getValue();
               } while(answer == null);
            } while(answer.equals("No"));

            int start = answer.indexOf("(");
            int end = answer.indexOf(")");
            if (start != -1 && end != -1 && start < end) {
               String diagnosis = answer.substring(start + 1, end);
               diagnosisCount.put(diagnosis, (Integer)diagnosisCount.getOrDefault(diagnosis, 0) + 1);
            } else {
               System.out.println("Warning: Invalid answer format -> " + answer);
            }
         }
      }
   }

   private void showCameraScreen(String diagnosis) {
      Button backButton = new Button("←");
      backButton.setFont(Font.font("Arial", FontWeight.BOLD, 16.0));
      backButton.setOnAction((e) -> {
         this.showHomeScreen();
      });
      VBox cameraLayout = new VBox(20.0);
      cameraLayout.setPadding(new Insets(20.0));
      cameraLayout.getChildren().add(backButton);
      Label diagnosisLabel = new Label("You have been diagnosed with " + diagnosis + ".\nScan your face for your glasses:");
      diagnosisLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
      Button cameraButton = new Button("Open Camera");
      cameraButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
      cameraButton.setOnAction((e) -> {
         this.openCamera();
      });
      cameraLayout.getChildren().addAll(new Node[]{diagnosisLabel, cameraButton});
      this.rootLayout.getChildren().setAll(new Node[]{cameraLayout});
   }

   private void openCamera() {
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      VideoCapture camera = new VideoCapture(0);
      if (!camera.isOpened()) {
         System.out.println("Error: Unable to access camera.");
      } else {
         ImageView cameraImageView = new ImageView();
         StackPane cameraPane = new StackPane(new Node[]{cameraImageView});
         Line laserLine = new Line(0.0, 0.0, 640.0, 0.0);
         laserLine.setStroke(Color.RED);
         laserLine.setStrokeWidth(3.0);
         TranslateTransition laserAnimation = new TranslateTransition(Duration.seconds(2.0), laserLine);
         laserAnimation.setFromY(-240.0);
         laserAnimation.setToY(240.0);
         laserAnimation.setCycleCount(-1);
         laserAnimation.setAutoReverse(true);
         laserAnimation.play();
         Label faceLabel = new Label();
         faceLabel.setTextFill(Color.LIMEGREEN);
         faceLabel.setFont(new Font("Arial", 24.0));
         faceLabel.setVisible(false);
         cameraPane.getChildren().addAll(new Node[]{laserLine, faceLabel});
         Scene cameraScene = new Scene(cameraPane, 640.0, 480.0);
         this.primaryStage.setScene(cameraScene);
         CascadeClassifier faceDetector = new CascadeClassifier("C:\\Users\\great\\OneDrive\\Desktop\\xml\\haarcascade_frontalface_default.xml");
         if (faceDetector.empty()) {
            System.out.println("Error: Haar Cascade XML file could not be loaded.");
         } else {
            Mat frame = new Mat();
            MatOfByte matOfByte = new MatOfByte();
            long[] faceStartTime = new long[1];
            (new Thread(() -> {
               while(true) {
                  if (camera.read(frame)) {
                     Imgcodecs.imencode(".bmp", frame, matOfByte);
                     byte[] byteArray = matOfByte.toArray();
                     Image image = new Image(new ByteArrayInputStream(byteArray));
                     MatOfRect faceDetections = new MatOfRect();
                     faceDetector.detectMultiScale(frame, faceDetections);
                     boolean faceDetected = faceDetections.toArray().length > 0;
                     if (faceDetected) {
                        if (faceStartTime[0] == 0L) {
                           faceStartTime[0] = System.currentTimeMillis();
                        }

                        long elapsedTime = System.currentTimeMillis() - faceStartTime[0];
                        Platform.runLater(() -> {
                           cameraImageView.setImage(image);
                           faceLabel.setText("Face Detected");
                           faceLabel.setVisible(true);
                        });
                        if (elapsedTime > 5000L) {
                           Platform.runLater(() -> {
                              this.showVideoScreen();
                           });
                           return;
                        }
                     } else {
                        faceStartTime[0] = 0L;
                        Platform.runLater(() -> {
                           cameraImageView.setImage(image);
                           faceLabel.setVisible(false);
                        });
                     }
                  }

                  try {
                     Thread.sleep(10L);
                  } catch (InterruptedException var14) {
                     var14.printStackTrace();
                  }
               }
            })).start();
         }
      }
   }

   private void showVideoScreen() {
      String[] videoFiles = new String[]{(new File("src/Videos/a.mp4")).toURI().toString(), (new File("src/Videos/b.mp4")).toURI().toString(), (new File("src/Videos/c.mp4")).toURI().toString()};
      Random random = new Random();
      String selectedVideo = videoFiles[random.nextInt(videoFiles.length)];

      try {
         Media media = new Media(selectedVideo);
         MediaPlayer mediaPlayer = new MediaPlayer(media);
         MediaView mediaView = new MediaView(mediaPlayer);
         mediaView.setPreserveRatio(false);
         StackPane videoLayout = new StackPane(new Node[]{mediaView});
         Scene videoScene = new Scene(videoLayout, this.primaryStage.getWidth(), this.primaryStage.getHeight());
         mediaView.fitWidthProperty().bind(videoScene.widthProperty());
         mediaView.fitHeightProperty().bind(videoScene.heightProperty());
         mediaPlayer.setAutoPlay(true);
         Button backButton = new Button("Back");
         backButton.setOnAction((e) -> {
            this.showHomeScreen();
         });
         videoLayout.getChildren().add(backButton);
         StackPane.setAlignment(backButton, Pos.TOP_LEFT);
         this.primaryStage.setScene(videoScene);
         this.primaryStage.setFullScreen(true);
         this.primaryStage.setResizable(true);
      } catch (Exception var10) {
         var10.printStackTrace();
         System.out.println("Error loading media: " + selectedVideo);
      }

   }

   private void showScreenTwo() {
      Button backButton = new Button("←");
      backButton.setFont(Font.font("Arial", FontWeight.BOLD, 16.0));
      backButton.setOnAction((e) -> {
         this.showHomeScreen();
      });
      GridPane grid = new GridPane();
      grid.setHgap(20.0);
      grid.setVgap(20.0);
      grid.setPadding(new Insets(20.0));
      ColumnConstraints col1 = new ColumnConstraints();
      ColumnConstraints col2 = new ColumnConstraints();
      col1.setHgrow(Priority.ALWAYS);
      col2.setHgrow(Priority.ALWAYS);
      grid.getColumnConstraints().addAll(new ColumnConstraints[]{col1, col2});
      String[] imgs = new String[]{"file:src/Doctors/denzel.jpg", "file:src/Doctors/chi.jpg", "file:src/Doctors/hi.jpeg", "file:src/Doctors/ken.jpg", "file:src/Doctors/blonde.jpg", "file:src/Doctors/hello.jpg"};
      String[] names = new String[]{"Denzel W.", "Natalie Grace", "Stella May", "Yun Chi", "Precious Bills", "Lisa Rice"};

      VBox frame;
      for(int i = 0; i < 6; ++i) {
         frame = new VBox(10.0);
         frame.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-padding: 10px; -fx-alignment: center;");
         frame.setPrefSize(150.0, 200.0);
         ImageView imageView = new ImageView(new Image(imgs[i % imgs.length]));
         imageView.setFitWidth(100.0);
         imageView.setFitHeight(100.0);
         Label nameLabel = new Label(names[i]);
         nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14.0));
         HBox stars = new HBox(5.0);

         for(int j = 0; j < 5; ++j) {
            Label star = new Label("★");
            star.setFont(Font.font("Arial", FontWeight.BOLD, 14.0));
            star.setTextFill(Color.GOLD);
            stars.getChildren().add(star);
         }

         HBox bottomLayout = new HBox(10.0, new Node[]{nameLabel, stars});
         bottomLayout.setAlignment(Pos.CENTER);
         HBox.setHgrow(nameLabel, Priority.ALWAYS);
         frame.getChildren().addAll(new Node[]{imageView, bottomLayout});
         GridPane.setHgrow(frame, Priority.ALWAYS);
         GridPane.setVgrow(frame, Priority.ALWAYS);
         grid.add(frame, i % 2, i / 2);
      }

      ScrollPane scrollPane = new ScrollPane(grid);
      scrollPane.setFitToWidth(true);
      scrollPane.setFitToHeight(true);
      frame = new VBox(20.0, new Node[]{backButton, scrollPane});
      frame.setPadding(new Insets(20.0));
      frame.setAlignment(Pos.CENTER);
      VBox.setVgrow(scrollPane, Priority.ALWAYS);
      this.rootLayout.getChildren().setAll(new Node[]{frame});
   }

   private void showScreenThree() {
      Button backButton = new Button("←");
      backButton.setFont(Font.font("Arial", FontWeight.BOLD, 16.0));
      backButton.setOnAction((e) -> {
         this.showHomeScreen();
      });
      VBox screenLayout = new VBox(20.0);
      screenLayout.setPadding(new Insets(20.0));
      screenLayout.getChildren().add(backButton);
      VBox boxesContainer = new VBox(15.0);
      boxesContainer.setAlignment(Pos.CENTER);
      String[] imgs = new String[]{"file:src/Glasses/a.jpg", "file:src/Glasses/b.jpg", "file:src/Glasses/c.jpg", "file:src/Glasses/d.jpg", "file:src/Glasses/e.jpg", "file:src/Glasses/f.jpg"};
      String[] names = new String[]{"Acuvue", "Cooper Vision", "Alcon", "Zeis", "Paragon", "Lomb"};

      for(int i = 0; i < 5; ++i) {
         StackPane box = new StackPane();
         box.setMinSize(250.0, 200.0);
         box.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: #f0f0f0;");
         ImageView imageView = new ImageView(new Image(imgs[i % imgs.length]));
         imageView.setFitWidth(350.0);
         imageView.setFitHeight(300.0);
         imageView.setPreserveRatio(true);
         Label nameLabel = new Label(names[i]);
         nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14.0));
         HBox starsBox = new HBox(5.0);
         starsBox.setAlignment(Pos.BOTTOM_RIGHT);

         for(int j = 0; j < 5; ++j) {
            Label star = new Label("★");
            star.setFont(Font.font("Arial", FontWeight.BOLD, 16.0));
            star.setTextFill(Color.GOLD);
            starsBox.getChildren().add(star);
         }

         HBox bottomLayout = new HBox(10.0);
         bottomLayout.setPadding(new Insets(10.0));
         bottomLayout.setAlignment(Pos.CENTER_LEFT);
         HBox.setHgrow(nameLabel, Priority.ALWAYS);
         HBox.setHgrow(starsBox, Priority.ALWAYS);
         bottomLayout.getChildren().addAll(new Node[]{nameLabel, starsBox});
         VBox boxContent = new VBox();
         boxContent.getChildren().addAll(new Node[]{imageView, bottomLayout});
         VBox.setVgrow((Node)boxContent.getChildren().get(0), Priority.ALWAYS);
         box.getChildren().add(boxContent);
         boxesContainer.getChildren().add(box);
      }

      ScrollPane scrollPane = new ScrollPane();
      scrollPane.setContent(boxesContainer);
      scrollPane.setFitToWidth(true);
      scrollPane.setPannable(true);
      scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
      screenLayout.getChildren().add(scrollPane);
      this.rootLayout.getChildren().setAll(new Node[]{screenLayout});
   }

   private void showChatBot() {
      Stage chatStage = new Stage();
      chatStage.initModality(Modality.APPLICATION_MODAL);
      chatStage.setTitle("Chatbot");
      VBox chatBox = new VBox(10.0);
      chatBox.setPadding(new Insets(20.0));
      Label chatTitle = new Label("Chatbot Assistant");
      chatTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16.0));
      TextArea chatArea = new TextArea();
      chatArea.setEditable(false);
      chatArea.setPrefHeight(200.0);
      TextField chatInput = new TextField();
      chatInput.setPromptText("Type a message...");
      Button sendButton = new Button("Send");
      sendButton.setOnAction((e) -> {
         String message = chatInput.getText();
         if (message.toLowerCase().contains("symptoms")) {
            chatArea.appendText("You: " + message + "\n");
            chatArea.appendText("Chatbot: Please click on the 'Check your symptoms' \nbutton to check for symptoms.\n");
         } else if (message.toLowerCase().contains("doctor")) {
            chatArea.appendText("You: " + message + "\n");
            chatArea.appendText("Chatbot: Please click on the 'Consult an optomitrist' \nbutton to contact our doctors.\n");
         } else if (message.toLowerCase().contains("glasses")) {
            chatArea.appendText("You: " + message + "\n");
            chatArea.appendText("Chatbot: Please click on the 'Purchase eye glasses' \nbutton to purchase glasses\n");
         } else if (!message.isEmpty()) {
            chatArea.appendText("You: " + message + "\n");
         } else {
            chatArea.appendText("Chatbot: Please type something...\n");
         }

         chatArea.setScrollTop(Double.MAX_VALUE);
         chatInput.clear();
      });
      HBox inputBox = new HBox(10.0, new Node[]{chatInput, sendButton});
      chatBox.getChildren().addAll(new Node[]{chatTitle, chatArea, inputBox});
      Scene chatScene = new Scene(chatBox, 400.0, 400.0);
      chatStage.setScene(chatScene);
      chatStage.show();
   }

   private void showAlert(String title, String message) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle(title);
      alert.setHeaderText((String)null);
      alert.setContentText(message);
      alert.showAndWait();
   }

   public static void main(String[] args) {
      launch(args);
   }
}
