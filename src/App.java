import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class App extends Application {

        private int currentIndex = 0;

        MovieCatalog movieCatalog = new MovieCatalog(101);
        TableView<Movie> movieTable = new TableView<>();

        Label avlHeight=new Label();
        Label hashCell=new Label();

        ObservableList<Movie> hashCellMovies = FXCollections.observableArrayList();

        int releaseyear = 0;

        public static void main(String[] args) throws Exception {
                launch(args);
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
                TabPane tabPane = new TabPane();
                tabPane.setPrefSize(731, 510);
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

                tabPane.getTabs().addAll(createFileTap(primaryStage), createMovieTab());

                Scene scene = new Scene(tabPane, 731, 510);
                primaryStage.setTitle("Movie Catalog Management System");
                primaryStage.setScene(scene);
                primaryStage.show();
        }

        private Tab createFileTap(Stage primaryStage) {
                Tab fileTab = new Tab("File Tab");
                Pane fileTabContent = new Pane();
                fileTabContent.setStyle("-fx-background-color: E5E1DA;");

                Button exitButton = new Button("Exit ");
                exitButton.setLayoutX(492);
                exitButton.setLayoutY(277);
                exitButton.setPrefSize(160, 80);
                exitButton.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                exitButton.setFont(new Font("System Bold", 25));
                ImageView exitImageView = new ImageView(new Image("exit.png"));
                exitImageView.setFitHeight(40);
                exitImageView.setFitWidth(40);
                exitButton.setGraphic(exitImageView);
                addButtonEffect(exitButton);
                exitButton.setOnAction(e -> { System.out.println(movieCatalog.getAverageHeight());
                        System.out.println(movieCatalog.size);
                        primaryStage.close();});

                Button saveButton = new Button("Save ");
                saveButton.setLayoutX(286);
                saveButton.setLayoutY(277);
                saveButton.setPrefSize(160, 80);
                saveButton.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                saveButton.setFont(new Font("System Bold", 25));
                ImageView saveImageView = new ImageView(new Image("diskette.png"));
                saveImageView.setFitHeight(40);
                saveImageView.setFitWidth(40);
                saveButton.setGraphic(saveImageView);
                addButtonEffect(saveButton);
                saveButton.setOnAction(e -> {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Open Data File");

                        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
                        fileChooser.getExtensionFilters().add(extFilter);

                        File selectedFile = fileChooser.showSaveDialog(primaryStage);
                        if (selectedFile != null) {
                                try {
                                        movieCatalog.saveMoviesToFile(selectedFile.getName());
                                } catch (IOException e1) {
                                        e1.printStackTrace();
                                }
                        }
                });

                Button openButton = new Button("Open ");
                openButton.setLayoutX(75);
                openButton.setLayoutY(277);
                openButton.setPrefSize(160, 80);
                openButton.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                openButton.setFont(new Font("System Bold", 25));
                ImageView openImageView = new ImageView(new Image("folder.png"));
                openImageView.setFitHeight(41);
                openImageView.setFitWidth(41);
                openButton.setGraphic(openImageView);
                addButtonEffect(openButton);
                openButton.setOnAction(e -> {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Open Data File");

                        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
                        fileChooser.getExtensionFilters().add(extFilter);

                        File selectedFile = fileChooser.showOpenDialog(primaryStage);
                        if (selectedFile != null) {
                                try {
                                        movieCatalog.loadMoviesFromFile(selectedFile.getName());
                                } catch (IOException e1) {
                                        e1.printStackTrace();
                                }
                        }
                });

                Pane titlePane = new Pane();
                titlePane.setLayoutX(59);
                titlePane.setLayoutY(66);
                titlePane.setPrefSize(613, 90);
                titlePane.setStyle("-fx-background-color: F6F5F2; -fx-background-radius: 40;");

                Label titleLabel = new Label("Movie Catalog Management System");
                titleLabel.setFont(new Font("System Bold", 28));
                titleLabel.setLayoutX(68);
                titleLabel.setLayoutY(25);

                titlePane.getChildren().add(titleLabel);
                fileTabContent.getChildren().addAll(exitButton, saveButton, openButton, titlePane);
                fileTab.setContent(fileTabContent);
                return fileTab;
        }

        private Tab createMovieTab() {
                Tab movieTab = new Tab("Movie Tab");
                Pane movieTabPane = new Pane();
                movieTabPane.setStyle("-fx-background-color: #E5E1DA;");

                movieTable.setLayoutX(64);
                movieTable.setLayoutY(52);
                movieTable.setPrefSize(602, 229);

                TableColumn<Movie, String> titleColumn = new TableColumn<>("Title");
                titleColumn.setPrefWidth(117);
                titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

                TableColumn<Movie, String> descriptionColumn = new TableColumn<>("Description");
                descriptionColumn.setPrefWidth(342);
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

                TableColumn<Movie, String> yearColumn = new TableColumn<>("Release Year");
                yearColumn.setPrefWidth(83);
                yearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

                TableColumn<Movie, String> ratingColumn = new TableColumn<>("Rating");
                ratingColumn.setPrefWidth(59);
                ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

                movieTable.getColumns().addAll(titleColumn, descriptionColumn, yearColumn, ratingColumn);

                movieTab.setOnSelectionChanged(e -> movieTable.setItems(movieCatalog.getAllMovies()));

                ComboBox<String> modeComboBox = new ComboBox<>();
                modeComboBox.setLayoutX(460);
                modeComboBox.setLayoutY(292);
                modeComboBox.setPrefSize(100, 40);
                modeComboBox.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                modeComboBox.setPromptText("Mode: ");
                modeComboBox.getItems().addAll("All movies", "Hash cells");
                modeComboBox.setOnAction(e -> {
                        if(modeComboBox.getValue().equals("Hash cells")){
                                updateHashCellsMoviesDetails();
                        }
                        else if(modeComboBox.getValue().equals("All movies")){
                                hashCell.setText("");
                                avlHeight.setText("");
                                movieTable.setItems(movieCatalog.getAllMovies());
                        }
                });

                Button topRatedButton = new Button("Top Ranked");
                topRatedButton.setLayoutX(460);
                topRatedButton.setLayoutY(351);
                topRatedButton.setPrefSize(100, 40);
                topRatedButton.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                topRatedButton.setFont(new Font("System Bold", 12));
                addButtonEffect(topRatedButton);
                topRatedButton.setOnAction(e -> {
                        Movie movie = movieCatalog.getTopRatedMovie();
                        if (movie != null) {
                                showAlert(AlertType.INFORMATION, "Top rated movie", "Title: " + movie.getTitle() + "\n"
                                                + "Description: " + movie.getDescription() + "\n"
                                                + "Release Year: " + movie.getReleaseYear() + "\n"
                                                + "Rating: " + movie.getRating());
                        }
                });

                Button leastRatedButton = new Button("Least Ranked");
                leastRatedButton.setLayoutX(460);
                leastRatedButton.setLayoutY(407);
                leastRatedButton.setPrefSize(100, 40);
                leastRatedButton.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                leastRatedButton.setFont(new Font("System Bold", 12));
                addButtonEffect(leastRatedButton);
                leastRatedButton.setOnAction(e -> {
                        Movie movie = movieCatalog.getLeastRatedMovie();
                        if (movie != null) {
                                showAlert(AlertType.INFORMATION, "Top rated movie", "Title: " + movie.getTitle() + "\n"
                                                + "Description: " + movie.getDescription() + "\n"
                                                + "Release Year: " + movie.getReleaseYear() + "\n"
                                                + "Rating: " + movie.getRating());
                        }
                });

                ComboBox<String> sortComboBox = new ComboBox<>();
                sortComboBox.setLayoutX(482);
                sortComboBox.setLayoutY(22);
                sortComboBox.setPrefSize(117, 29);
                sortComboBox.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20;");
                sortComboBox.getItems().addAll("Ascending", "Descending");

                Button sortButton = new Button("Sort");
                sortButton.setLayoutX(607);
                sortButton.setLayoutY(21);
                sortButton.setPrefSize(59, 30);
                sortButton.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                sortButton.setFont(new Font("System Bold", 13));
                addButtonEffect(sortButton);
                sortButton.setOnAction(e -> {
                        if (sortComboBox.getValue().equals("Ascending")) {
                                try{
                                if(modeComboBox.getValue().equals("Hash cells"))
                                movieTable.setItems(sortMoviesByTitleAsc());
                                else
                                movieTable.setItems(movieCatalog.sortMoviesByTitleAsc());
                                }catch(NullPointerException ex){
                                        movieTable.setItems(movieCatalog.sortMoviesByTitleAsc());
                                }
                        } else if (sortComboBox.getValue().equals("Descending")) {
                                try{
                                if(modeComboBox.getValue().equals("Hash cells"))
                                movieTable.setItems(sortMoviesByTitleDesc());
                                else
                                movieTable.setItems(movieCatalog.sortMoviesByTitleDesc());
                                }catch(NullPointerException ex){
                                        movieTable.setItems(movieCatalog.sortMoviesByTitleDesc());
                                }
                        }
                });

                avlHeight.setLayoutX(307);
                avlHeight.setLayoutY(28);
                avlHeight.setFont(new Font("System Bold", 12));

                hashCell.setLayoutX(337);
                hashCell.setLayoutY(13);
                hashCell.setFont(new Font("System Bold", 12));

                ImageView searchIcon = new ImageView(new Image("searchsmall.png"));
                searchIcon.setLayoutX(64);
                searchIcon.setLayoutY(22);

                TextField searchField = new TextField();
                searchField.setLayoutX(94);
                searchField.setLayoutY(21);
                searchField.setPrefSize(148, 31);
                searchField.setPromptText("Search. . .");
                searchField.setStyle("-fx-background-color: transparent;");

                searchField.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ENTER) {
                                try{
                                if(modeComboBox.getValue().equals("Hash cells"))
                                movieTable.setItems(searchHashCellMovies(searchField.getText()));
                                else
                                movieTable.setItems(movieCatalog.searchMovies(searchField.getText()));
                                }catch(NullPointerException E){
                                        movieTable.setItems(movieCatalog.searchMovies(searchField.getText()));
                                }
                        }
                });

                ImageView leftImageView = new ImageView(new Image("left.png"));
                leftImageView.setFitWidth(50);
                leftImageView.setFitHeight(50);
                ImageView greyLeftImageView = new ImageView(new Image("leftGrey.png"));
                greyLeftImageView.setFitWidth(50);
                greyLeftImageView.setFitHeight(50);
                Button leftButton = new Button();
                leftButton.setLayoutX(-3);
                leftButton.setLayoutY(226);
                leftButton.setStyle("-fx-background-color: transparent;");
                leftButton.setGraphic(leftImageView);
                leftButton.setOnMouseEntered(e -> leftButton.setGraphic(greyLeftImageView));
                leftButton.setOnMouseExited(e -> leftButton.setGraphic(leftImageView));

                ImageView rightImageView = new ImageView(new Image("right.png"));
                rightImageView.setFitWidth(50);
                rightImageView.setFitHeight(50);
                ImageView greyRightImageView = new ImageView(new Image("rightGrey.png"));
                greyRightImageView.setFitWidth(50);
                greyRightImageView.setFitHeight(50);
                Button rightButton = new Button();
                rightButton.setLayoutX(670);
                rightButton.setLayoutY(226);
                rightButton.setStyle("-fx-background-color: transparent;");
                rightButton.setGraphic(rightImageView);
                rightButton.setOnMouseEntered(e -> rightButton.setGraphic(greyRightImageView));
                rightButton.setOnMouseExited(e -> rightButton.setGraphic(rightImageView));

                leftButton.setOnAction(e -> {
                        try{
                        if (movieCatalog != null) {
                                if(modeComboBox.getValue().equals("Hash cells")){
                                currentIndex = (currentIndex - 1 + movieCatalog.size) % movieCatalog.size;
                                updateHashCellsMoviesDetails();
                                }
                                else{
                                        showAlert(AlertType.INFORMATION, "Mode not selected", "Please select hash cells mode first");
                                }
                        }
                        }catch(NullPointerException ex){
                                showAlert(AlertType.INFORMATION, "Mode not selected", "Please select hash cells mode first");
                        }
                });

                rightButton.setOnAction(e -> {
                        try{
                        if (movieCatalog != null) {
                                if(modeComboBox.getValue().equals("Hash cells")){
                                        currentIndex = (currentIndex + 1) % movieCatalog.size;
                                updateHashCellsMoviesDetails();
                                }
                                else{
                                        showAlert(AlertType.INFORMATION, "Mode not selected", "Please select hash cells mode first");
                                }
                        }
                        }catch(NullPointerException ex){
                                showAlert(AlertType.INFORMATION, "Mode not selected", "Please select hash cells mode first");
                        }
                });

                Button addMovieButton = new Button("Add Movie");
                addMovieButton.setLayoutX(168);
                addMovieButton.setLayoutY(292);
                addMovieButton.setPrefSize(100, 40);
                addMovieButton.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                addMovieButton.setFont(new Font("System Bold", 12));
                addButtonEffect(addMovieButton);
                addMovieButton.setOnAction(e -> createAddMovieForm());

                Button updateMovieButton = new Button("Update Movie");
                updateMovieButton.setLayoutX(168);
                updateMovieButton.setLayoutY(351);
                updateMovieButton.setPrefSize(100, 40);
                updateMovieButton.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                updateMovieButton.setFont(new Font("System Bold", 12));
                addButtonEffect(updateMovieButton);
                updateMovieButton.setOnAction(e -> createUpdateMovieForm());

                Button deleteMovieButton = new Button("Delete Movie");
                deleteMovieButton.setLayoutX(168);
                deleteMovieButton.setLayoutY(407);
                deleteMovieButton.setPrefSize(100, 40);
                deleteMovieButton.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                deleteMovieButton.setFont(new Font("System Bold", 12));
                addButtonEffect(deleteMovieButton);
                deleteMovieButton.setOnAction(e -> {
                        Movie selected = movieTable.getSelectionModel().getSelectedItem();
                        if (selected != null) {
                                movieCatalog.erase(selected.getTitle());
                                movieTable.getItems().remove(selected);
                        } else {
                                showAlert(AlertType.WARNING, "No selected movie", "Please select movie to delete");
                        }
                });

                movieTabPane.getChildren().addAll(movieTable, topRatedButton, leastRatedButton, sortComboBox,
                                sortButton,
                                searchIcon, searchField, addMovieButton, updateMovieButton,
                                deleteMovieButton, leftButton, rightButton, modeComboBox, avlHeight, hashCell);
                movieTab.setContent(movieTabPane);

                return movieTab;
        }

        private void addButtonEffect(Button button) {
                button.setOnMouseEntered(e -> button.setStyle(
                                "-fx-background-color:  #F6F5F2; -fx-background-radius: 20; -fx-border-width: 2; -fx-border-color: black; -fx-border-radius: 20;"));
                button.setOnMouseExited(e -> button.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-width: 2; -fx-border-color: black; -fx-border-radius: 20;"));
        }

        private void createAddMovieForm() {
                Stage stage = new Stage();
                Pane pane = new Pane();
                pane.setStyle("-fx-background-color: #E5E1DA;");
                pane.setPrefSize(271, 400);

                Label addMovieLabel = new Label("Add Movie");
                addMovieLabel.setLayoutX(74);
                addMovieLabel.setLayoutY(40);
                addMovieLabel.setFont(new Font("System Bold", 24));

                Label titleLabel = new Label("Title");
                titleLabel.setLayoutX(26);
                titleLabel.setLayoutY(107);
                titleLabel.setFont(new Font("System Bold", 16));

                Label descriptionLabel = new Label("Description");
                descriptionLabel.setLayoutX(26);
                descriptionLabel.setLayoutY(164);
                descriptionLabel.setFont(new Font("System Bold", 16));

                Label releaseYearLabel = new Label("Release Year");
                releaseYearLabel.setLayoutX(26);
                releaseYearLabel.setLayoutY(228);
                releaseYearLabel.setFont(new Font("System Bold", 16));

                Label ratingLabel = new Label("Rating");
                ratingLabel.setLayoutX(26);
                ratingLabel.setLayoutY(289);
                ratingLabel.setFont(new Font("System Bold", 16));

                TextField titleField = new TextField();
                titleField.setLayoutX(136);
                titleField.setLayoutY(107);
                titleField.setPrefHeight(24);
                titleField.setPrefWidth(124);

                TextField descriptionField = new TextField();
                descriptionField.setLayoutX(136);
                descriptionField.setLayoutY(164);
                descriptionField.setPrefHeight(24);
                descriptionField.setPrefWidth(124);

                DatePicker releaseYearField = new DatePicker();
                releaseYearField.setLayoutX(136);
                releaseYearField.setLayoutY(228);
                releaseYearField.setPrefHeight(24);
                releaseYearField.setPrefWidth(124);

                releaseYearField.valueProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                                int year = newValue.getYear();
                                releaseyear = year;
                        }
                });

                TextField ratingField = new TextField();
                ratingField.setLayoutX(136);
                ratingField.setLayoutY(289);
                ratingField.setPrefHeight(24);
                ratingField.setPrefWidth(124);

                Button addButton = new Button("Add");
                addButton.setLayoutX(99);
                addButton.setLayoutY(340);
                addButton.setPrefHeight(35);
                addButton.setPrefWidth(73);
                addButton.setStyle(
                                "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                addButton.setFont(new Font("System Bold", 16));
                addButtonEffect(addButton);
                addButton.setOnAction(e -> {
                        try {
                                String title = titleField.getText();
                                String description = descriptionField.getText();
                                int releaseYear = releaseyear;
                                double rating = Double.parseDouble(ratingField.getText());
                                Movie movie = new Movie(title, description, releaseYear, rating);
                                movieCatalog.put(movie);
                                movieTable.setItems(movieCatalog.getAllMovies());
                                stage.close();
                        } catch (NumberFormatException E) {
                                showAlert(AlertType.ERROR, "Error",
                                                "Please enter valid year in release year or a valid number for rating");
                        }
                });

                pane.getChildren().addAll(addMovieLabel, titleLabel, descriptionLabel, releaseYearLabel, ratingLabel,
                                titleField, descriptionField, releaseYearField, ratingField, addButton);

                Scene scene = new Scene(pane);
                stage.setTitle("Add Movie");
                stage.setScene(scene);
                stage.show();
        }

        private void createUpdateMovieForm() {

                Movie selected = movieTable.getSelectionModel().getSelectedItem();
                Stage stage = new Stage();

                Pane pane = new Pane();
                pane.setStyle("-fx-background-color: #E5E1DA;");
                pane.setPrefSize(271, 400);

                Label updateMovieLabel = new Label("Update Movie");
                updateMovieLabel.setLayoutX(56);
                updateMovieLabel.setLayoutY(40);
                updateMovieLabel.setFont(new Font("System Bold", 24));

                Label titleLabel = new Label("Title");
                titleLabel.setLayoutX(26);
                titleLabel.setLayoutY(107);
                titleLabel.setFont(new Font("System Bold", 16));

                Label descriptionLabel = new Label("Description");
                descriptionLabel.setLayoutX(26);
                descriptionLabel.setLayoutY(164);
                descriptionLabel.setFont(new Font("System Bold", 16));

                Label releaseYearLabel = new Label("Release Year");
                releaseYearLabel.setLayoutX(26);
                releaseYearLabel.setLayoutY(228);
                releaseYearLabel.setFont(new Font("System Bold", 16));

                Label ratingLabel = new Label("Rating");
                ratingLabel.setLayoutX(26);
                ratingLabel.setLayoutY(289);
                ratingLabel.setFont(new Font("System Bold", 16));

                if (selected != null) {
                        TextField titleField = new TextField(selected.getTitle());
                        titleField.setLayoutX(136);
                        titleField.setLayoutY(107);
                        titleField.setPrefHeight(24);
                        titleField.setPrefWidth(124);

                        TextField descriptionField = new TextField(selected.getDescription());
                        descriptionField.setLayoutX(136);
                        descriptionField.setLayoutY(164);
                        descriptionField.setPrefHeight(24);
                        descriptionField.setPrefWidth(124);

                        DatePicker releaseYearField = new DatePicker();

                        releaseYearField.setPromptText(Integer.toString(selected.getReleaseYear()));
                        ;
                        releaseYearField.setLayoutX(136);
                        releaseYearField.setLayoutY(228);
                        releaseYearField.setPrefHeight(24);
                        releaseYearField.setPrefWidth(124);

                        releaseYearField.valueProperty().addListener((observable, oldValue, newValue) -> {
                                if (newValue != null) {
                                        int year = newValue.getYear();
                                        releaseyear = year;
                                }
                        });

                        TextField ratingField = new TextField(Double.toString(selected.getRating()));
                        ratingField.setLayoutX(136);
                        ratingField.setLayoutY(289);
                        ratingField.setPrefHeight(24);
                        ratingField.setPrefWidth(124);

                        Button updateButton = new Button("Update");
                        updateButton.setLayoutX(92);
                        updateButton.setLayoutY(339);
                        updateButton.setPrefHeight(40);
                        updateButton.setPrefWidth(86);
                        updateButton.setStyle(
                                        "-fx-background-color: transparent; -fx-background-radius: 20; -fx-border-color: black; -fx-border-radius: 20; -fx-border-width: 2;");
                        updateButton.setFont(new Font("System Bold", 16));
                        addButtonEffect(updateButton);
                        updateButton.setOnAction(e -> {
                                try {
                                        selected.setTitle(titleField.getText());
                                        selected.setDescription(descriptionField.getText());
                                        selected.setReleaseYear(releaseyear);
                                        selected.setRating(Double.parseDouble(ratingField.getText()));
                                        stage.close();
                                        movieTable.setItems(movieCatalog.getAllMovies());
                                } catch (NumberFormatException E) {
                                        showAlert(AlertType.ERROR, "Error",
                                                        "Please enter valid year in release year or a valid number for rating");
                                }
                        });

                        pane.getChildren().addAll(updateMovieLabel, titleLabel, descriptionLabel, releaseYearLabel,
                                        ratingLabel,
                                        titleField, descriptionField, releaseYearField, ratingField, updateButton);

                        Scene scene = new Scene(pane);
                        stage.setTitle("Update Movie");
                        stage.setScene(scene);
                        stage.show();
                } else {
                        showAlert(AlertType.WARNING, "No selected movie", "Please select movie to update");
                }

        }

        private void showAlert(AlertType type, String title, String message) {
                Alert alert = new Alert(type);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }

        private void updateHashCellsMoviesDetails() {
                if (movieCatalog != null) {
                        AVLTree currentTree = movieCatalog.table[currentIndex];

                        if (currentTree == null || currentTree.root == null) {
                                movieTable.getItems().clear();
                                avlHeight.setText("Avl tree height: "+currentTree.getHeight(currentTree.root));
                                hashCell.setText("Hash cell: "+currentIndex);
                                
                        } else {
                                movieTable.getItems().clear();
                                currentTree.inorderTraversal(currentTree.root, hashCellMovies);
                                movieTable.setItems(hashCellMovies);
                                avlHeight.setText("Avl tree height: "+currentTree.getHeight(currentTree.root));
                                hashCell.setText("Hash cell: "+currentIndex);
                        }
                }
        }

        public ObservableList<Movie> searchHashCellMovies(String title) {
                ObservableList<Movie> allMovies = hashCellMovies;
                ObservableList<Movie> result = FXCollections.observableArrayList();
        
                if (title == null || title.trim().isEmpty()) {
                    return allMovies;
                }
        
                for (int i = 0; i < allMovies.size(); i++) {
                    if (allMovies.get(i).getTitle().toLowerCase().contains(title.toLowerCase())) {
                        result.add(allMovies.get(i));
                    }
                }
                return result;
        }

        public ObservableList<Movie> sortMoviesByTitleAsc() {
                ObservableList<Movie> movies = hashCellMovies;
                FXCollections.sort(movies, (m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()));
                return movies;
        }
            
        public ObservableList<Movie> sortMoviesByTitleDesc() {
                ObservableList<Movie> movies = hashCellMovies;
                FXCollections.sort(movies, (m1, m2) ->  m2.getTitle().compareToIgnoreCase(m1.getTitle()));
                return movies;
        }

}
