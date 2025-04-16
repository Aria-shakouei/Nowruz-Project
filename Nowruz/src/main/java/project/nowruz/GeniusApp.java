package project.nowruz;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GeniusApp extends Application {

    public Stage primaryStage;
    public List<Account> accounts = new ArrayList<>();
    public List<Song> songs = new ArrayList<>();
    public List<ArtistRequest> artistRequests = new ArrayList<>();
    public Account currentUser;
    public int nextSongId = 4;
    public MediaPlayer mediaPlayer;
    public ByteArrayOutputStream recordedBytes;
    public TargetDataLine microphone;
    public boolean isRecording = false;

    // Emoji constants
    private final String MUSIC_EMOJI = "🎵";
    private final String USER_EMOJI = "👤";
    private final String ARTIST_EMOJI = "🎤";
    private final String HEART_EMOJI = "❤️";
    private final String SEARCH_EMOJI = "🔍";
    private final String BACK_EMOJI = "⬅️";
    private final String LOGOUT_EMOJI = "🚪";
    private final String STAR_EMOJI = "⭐";
    private final String ALBUM_EMOJI = "💿";
    private final String LINK_EMOJI = "🔗";
    private final String PLUS_EMOJI = "➕";
    private final String FOLLOW_EMOJI = "✨";
    private final String FOLLOWING_EMOJI = "✅";
    private final String EDIT_EMOJI = "✏️";
    private final String MIC_EMOJI = "🎤";
    private final String PLAY_EMOJI = "▶️";
    private final String PAUSE_EMOJI = "⏸️";
    private final String STOP_EMOJI = "⏹️";
    private final String RECORD_EMOJI = "🔴";
    private final String UPLOAD_EMOJI = "📤";
    private final String DOWNLOAD_EMOJI = "📥";
    private final String COMMENT_EMOJI = "💬";
    private final String LYRICS_EMOJI = "📝";
    private final String ADMIN_EMOJI = "👑";
    private final String REQUEST_EMOJI = "📋";
    private final String IMAGE_EMOJI = "🖼️";

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        initializeSampleData();
        showSignUpPage();
        primaryStage.setTitle("Genius Music App " + MUSIC_EMOJI);
        primaryStage.show();
    }

    private void initializeSampleData() {
        accounts.clear();
        songs.clear();
        artistRequests.clear();
        // Admin account
        Admin admin = new Admin("Admin", 18, "admin@gmail.com", "admin", "admin123", "admin1");
        accounts.add(admin);

        // Sample artist - Moein
        Artist artist1 = new Artist("Moein", 73, "n.moein@gmail.com", "moein", "1234" , "i'm iranian singer");
        artist1.addSocialLink("https://www.moeinmusic.com");

        // Sample user
        User user1 = new User("Aria", 18, "arya.shakooee@gmail.com", "Aria-ss", "123");

        accounts.add(artist1);
        accounts.add(user1);

        // Sample song with lyrics
        String lyrics = "داری میری از خونه ی آرزو جدا میشم از تو چه آواره و کنارت نمیزارم از زندگیم\n" +
                "برو زندگی کن بزارم کنار پی آرزو های بعد از منی منم غصه هامو به دوش میکشم\n" +
                "بتونم از عشقت بمیرم ولی نمیتونم عشق یکی دیگه شم\n" +
                "واست بهترین هارو میخوام چون واسه اولین بار فهمیدمت\n" +
                "واسه آخرین بار عاشق شدم واسه اولین بار بخشیدمت\n" +
                "به امید رویای بوسیدنت به عشق تو چشمامو خواب میکنم\n" +
                "اگه صد دفعه باز به دنیا بیام میدونم تو رو انتخاب میکنم\n" +
                "اگه بعضی وقتا دلت تنگ شد یه گوشه مثل من فقط گریه کن\n" +
                "رو اون نامه که تشنه حرفته به جای نوشتن فقط گریه کن\n" +
                "همینکه دلم با توئه کافیه نمیخوام بدونم دلت با کیه\n" +
                "من آلوده ام اما نجاتم نده که آلوده بودن به تو پاکیه\n" +
                "واست بهترین هارو میخوام چون واسه اولین بار فهمیدمت\n" +
                "واسه آخرین بار عاشق شدم واسه اولین بار بخشیدمت\n" +
                "به امید رویای بوسیدنت به عشق تو چشمامو خواب میکنم\n" +
                "اگه صد دفعه باز به دنیا بیام میدونم تو رو انتخاب میکنم";

        Song song1 = new Song(1, "bibi gol", "pop", "none", artist1, 500, new Date(), 1000);
        song1.setLyrics(lyrics);

        Song song2 = new Song(2, "sobhet bekheir", "Pop", "none", artist1, 214, new Date(), 1500);
        Song song3 = new Song(3, "khooneye Arezoo", "pop", "none", artist1, 239, new Date(), 1200);

        songs.add(song1);
        songs.add(song2);
        songs.add(song3);

        List<String> contributors = new ArrayList<>();
        contributors.add("Moein");
        List<Song> albumSongs = new ArrayList<>();
        albumSongs.add(song1);
    }

    private void showSignUpPage() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Text title = new Text(MUSIC_EMOJI + " Genius Sign Up " + MUSIC_EMOJI);
        title.setFont(Font.font(24));

        GridPane form = new GridPane();
        form.setAlignment(Pos.CENTER);
        form.setHgap(10);
        form.setVgap(10);

        Label nameLabel = new Label(USER_EMOJI + " Name:");
        TextField nameField = new TextField();

        Label ageLabel = new Label("🔢 Age:");
        TextField ageField = new TextField();

        Label emailLabel = new Label("📧 Email:");
        TextField emailField = new TextField();

        Label usernameLabel = new Label(USER_EMOJI + " Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("🔑 Password:");
        PasswordField passwordField = new PasswordField();

        Label accountTypeLabel = new Label("🎚️ Account Type:");
        ToggleGroup accountTypeGroup = new ToggleGroup();
        RadioButton userRadio = new RadioButton(USER_EMOJI + " User");
        userRadio.setToggleGroup(accountTypeGroup);
        userRadio.setUserData("User");
        RadioButton artistRadio = new RadioButton(ARTIST_EMOJI + " Artist");
        artistRadio.setToggleGroup(accountTypeGroup);
        artistRadio.setUserData("Artist");

        Label biographyLabel = new Label("📝 Biography:");
        TextField biographyField = new TextField();
        biographyLabel.setVisible(false);
        biographyField.setVisible(false);

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(ageLabel, 0, 1);
        form.add(ageField, 1, 1);
        form.add(emailLabel, 0, 2);
        form.add(emailField, 1, 2);
        form.add(usernameLabel, 0, 3);
        form.add(usernameField, 1, 3);
        form.add(passwordLabel, 0, 4);
        form.add(passwordField, 1, 4);
        form.add(accountTypeLabel, 0, 5);
        form.add(userRadio, 1, 5);
        form.add(artistRadio, 1, 6);
        form.add(biographyLabel, 0, 7);
        form.add(biographyField, 1, 7);

        Button signUpButton = new Button(PLUS_EMOJI + " Sign Up");
        Button loginButton = new Button("🔐 Already have an account? Login");

        HBox buttonBox = new HBox(10, signUpButton, loginButton);
        buttonBox.setAlignment(Pos.CENTER);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        root.getChildren().addAll(title, form, buttonBox, errorLabel);

        accountTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                boolean isArtist = newValue.getUserData().equals("Artist");
                biographyLabel.setVisible(isArtist);
                biographyField.setVisible(isArtist);
            }
        });

        signUpButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String email = emailField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();

                if (accountTypeGroup.getSelectedToggle() == null) {
                    errorLabel.setText("Please select an account type");
                    return;
                }

                String accountType = accountTypeGroup.getSelectedToggle().getUserData().toString();

                for (Account account : accounts) {
                    if (account.getUsername().equals(username)) {
                        errorLabel.setText("Username already exists");
                        return;
                    }
                }

                if (accountType.equals("User")) {
                    String userId = "user" + (accounts.size() + 1);
                    User newUser = new User(name, age, email, username, password);
                    accounts.add(newUser);
                    currentUser = newUser;
                    showUserHomePage(newUser);
                } else {
                    String biography = biographyField.getText();
                    if (biography.isEmpty()) {
                        errorLabel.setText("Biography is required for artists");
                        return;
                    }

                    // For artist signup, create a request that needs admin approval
                    ArtistRequest request = new ArtistRequest(name, age, email, username, password, biography);
                    artistRequests.add(request);

                    showAlert("Request Submitted", "Your artist account request has been submitted for admin approval.");
                    showLoginPage();
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid age");
            }
        });

        loginButton.setOnAction(e -> showLoginPage());

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    private void showLoginPage() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Text title = new Text(MUSIC_EMOJI + " Genius Login " + MUSIC_EMOJI);
        title.setFont(Font.font(24));

        GridPane form = new GridPane();
        form.setAlignment(Pos.CENTER);
        form.setHgap(10);
        form.setVgap(10);

        Label usernameLabel = new Label(USER_EMOJI + " Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("🔑 Password:");
        PasswordField passwordField = new PasswordField();

        form.add(usernameLabel, 0, 0);
        form.add(usernameField, 1, 0);
        form.add(passwordLabel, 0, 1);
        form.add(passwordField, 1, 1);

        Button loginButton = new Button("🔐 Login");
        Button signUpButton = new Button(PLUS_EMOJI + " Sign Up");
        Button adminLoginButton = new Button(ADMIN_EMOJI + " Admin Login");

        HBox buttonBox = new HBox(10, loginButton, signUpButton, adminLoginButton);
        buttonBox.setAlignment(Pos.CENTER);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        root.getChildren().addAll(title, form, buttonBox, errorLabel);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            for (Account account : accounts) {
                if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                    currentUser = account;
                    if (account instanceof Admin) {
                        showAdminDashboard((Admin) account);
                    }
                    else if (account instanceof Artist) {
                        showArtistPage((Artist) account);  // This should show artist dashboard
                    }
                    else if (account instanceof User) {
                        showUserHomePage((User) account);
                    }
                    return;
                }
            }

            errorLabel.setText("Invalid username or password");
        });

        signUpButton.setOnAction(e -> showSignUpPage());

        adminLoginButton.setOnAction(e -> {
            usernameField.setText("admin");
            passwordField.setText("admin123");
        });

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    private void showUserHomePage(User user) {
        BorderPane root = new BorderPane();

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setStyle("-fx-background-color: #336699; -fx-padding: 10;");

        Button profileButton = new Button(USER_EMOJI + " Profile");
        profileButton.setOnAction(e -> showProfilePage(user));

        Text title = new Text(MUSIC_EMOJI + " Genius Music App " + USER_EMOJI);
        title.setStyle("-fx-font-size: 20; -fx-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button(LOGOUT_EMOJI + " Logout");

        topBar.getChildren().addAll(profileButton, title, spacer, logoutButton);
        root.setTop(topBar);

        VBox center = new VBox(10);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(20));

        if (!User.getFollowedArtists().isEmpty()) {
            Text followedTitle = new Text(FOLLOWING_EMOJI + " Followed Artists " + FOLLOWING_EMOJI);
            followedTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

            ListView<Artist> followedListView = new ListView<>();
            followedListView.setPrefHeight(100);
            ObservableList<Artist> followedArtists = FXCollections.observableArrayList((Artist) user.getFollowedArtists());
            followedListView.setItems(followedArtists);
            followedListView.setCellFactory(lv -> new ListCell<Artist>() {
                @Override
                protected void updateItem(Artist artist, boolean empty) {
                    super.updateItem(artist, empty);
                    if (empty || artist == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Hyperlink artistLink = new Hyperlink(ARTIST_EMOJI + " " + artist.getName());
                        artistLink.setOnAction(e -> showArtistProfilePage(artist));
                        setGraphic(artistLink);
                        setText(ARTIST_EMOJI + " " + artist.getName());
                    }
                }
            });

            center.getChildren().addAll(followedTitle, followedListView);
        }

        Text songsTitle = new Text(STAR_EMOJI + " Popular Songs " + STAR_EMOJI);
        songsTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        ListView<Song> songsListView = new ListView<>();
        songsListView.setPrefSize(600, 400);

        ObservableList<Song> songItems = FXCollections.observableArrayList(songs);
        songsListView.setItems(songItems);

        songsListView.setCellFactory(lv -> new ListCell<Song>() {
            @Override
            protected void updateItem(Song song, boolean empty) {
                super.updateItem(song, empty);
                if (empty || song == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    VBox box = new VBox(5);

                    // Create an ImageView for the song cover (if available)
                    ImageView coverView = new ImageView();
                    coverView.setFitWidth(50);
                    coverView.setFitHeight(50);
                    coverView.setPreserveRatio(true);

                    if (song.getCoverImage() != null) {
                        coverView.setImage(song.getCoverImage());
                    } else {
                        // Default music icon if no cover
                        coverView.setImage(new Image(getClass().getResourceAsStream("/music-icon.png")));
                    }

                    Label titleLabel = new Label(MUSIC_EMOJI + " " + song.getTitle() + " by " + song.getArtist().getName());
                    titleLabel.setStyle("-fx-font-weight: bold;");

                    HBox infoBox = new HBox(10, coverView, titleLabel);
                    box.getChildren().add(infoBox);

                    setGraphic(box);
                }
            }
        });

        songsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showSongDetailPage(newVal);
            }
        });

        center.getChildren().addAll(songsTitle, songsListView);
        root.setCenter(center);

        logoutButton.setOnAction(e -> showLoginPage());

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    public void showArtistProfilePage(Artist artist){
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Text title = new Text(ARTIST_EMOJI + " " + artist.getName() + " Profile " + ARTIST_EMOJI);
        title.setFont(Font.font(24));

        Label bioLabel = new Label("📝 Biography: " + artist.getBiography());
        bioLabel.setWrapText(true);

        Text socialTitle = new Text(LINK_EMOJI + " Social Links:");
        socialTitle.setStyle("-fx-font-weight: bold;");

        ListView<String> socialLinks = new ListView<>();
        socialLinks.setItems(FXCollections.observableArrayList(artist.getSocialLinks()));
        socialLinks.setPrefHeight(100);

        Text albumsTitle = new Text(ALBUM_EMOJI + " Albums:");
        albumsTitle.setStyle("-fx-font-weight: bold;");

        ListView<Album> albumsList = new ListView<>();
        albumsList.setItems(FXCollections.observableArrayList(artist.getAlbums()));
        albumsList.setCellFactory(lv -> new ListCell<Album>() {
            @Override
            protected void updateItem(Album album, boolean empty) {
                super.updateItem(album, empty);
                if (empty || album == null) {
                    setText(null);
                } else {
                    setText(ALBUM_EMOJI + " " + album.getTitle() + " (" + album.getSongs().size() + " songs)");
                }
            }
        });

        Button backButton = new Button(BACK_EMOJI + " Back");
        backButton.setOnAction(e -> showUserHomePage((User)currentUser));

        root.getChildren().addAll(title, bioLabel, socialTitle, socialLinks, albumsTitle, albumsList, backButton);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

    }

    private void showArtistPage(Artist artist) {
        BorderPane root = new BorderPane();

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setStyle("-fx-background-color: #336699; -fx-padding: 10;");

        if (currentUser instanceof Artist && currentUser.equals(artist)) {
            Button profileButton = new Button(ARTIST_EMOJI + " Profile");
            profileButton.setOnAction(e -> showProfilePage(artist));
            topBar.getChildren().add(profileButton);
        } else {
            Button backButton = new Button(BACK_EMOJI + " Back");
            backButton.setOnAction(e -> {
                if (currentUser.getRule().equals("User")) {
                    showUserHomePage((User)currentUser);
                } else if (currentUser.getRule().equals("Artist")) {
                    showArtistPage((Artist)currentUser);
                }
            });
            topBar.getChildren().add(backButton);
        }

        Text title = new Text(MUSIC_EMOJI + " " + artist.getName() + " " + ARTIST_EMOJI);
        title.setStyle("-fx-font-size: 20; -fx-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button(LOGOUT_EMOJI + " Logout");

        topBar.getChildren().addAll(title, spacer, logoutButton);
        root.setTop(topBar);

        Button profileButton = new Button(ARTIST_EMOJI + " Profile");
        profileButton.setOnAction(e -> showProfilePage(artist));

        topBar.getChildren().addAll(profileButton, title, spacer, logoutButton);
        root.setTop(topBar);

        VBox leftSidebar = new VBox(10);
        leftSidebar.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
        leftSidebar.setPrefWidth(200);

        Text profileTitle = new Text(ARTIST_EMOJI + " Artist Profile");
        profileTitle.setStyle("-fx-font-weight: bold;");

        Label artistNameLabel = new Label(USER_EMOJI + " " + artist.getName());
        Label biographyLabel = new Label("📝 " + artist.getBiography());
        biographyLabel.setWrapText(true);

        Text socialLinksTitle = new Text(LINK_EMOJI + " Social Links:");
        socialLinksTitle.setStyle("-fx-font-weight: bold;");

        ListView<String> socialLinksListView = new ListView<>();
        socialLinksListView.setPrefHeight(100);
        ObservableList<String> socialLinks = FXCollections.observableArrayList(artist.getSocialLinks());
        socialLinksListView.setItems(socialLinks);
        socialLinksListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String link, boolean empty) {
                super.updateItem(link, empty);
                if (empty || link == null) {
                    setText(null);
                } else {
                    setText(LINK_EMOJI + " " + link);
                }
            }
        });

        TextField newLinkField = new TextField();
        newLinkField.setPromptText("Enter new social link");
        Button addLinkButton = new Button(PLUS_EMOJI + " Add Link");

        addLinkButton.setOnAction(e -> {
            String newLink = newLinkField.getText();
            if (!newLink.isEmpty()) {
                artist.addSocialLink(newLink);
                socialLinks.add(newLink);
                newLinkField.clear();
            }
        });

        Button removeLinkButton = new Button("🗑️ Remove Selected");
        removeLinkButton.setOnAction(e -> {
            String selected = socialLinksListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                artist.getSocialLinks().remove(selected);
                socialLinks.remove(selected);
            }
        });

        Text albumsTitle = new Text(ALBUM_EMOJI + " Albums:");
        albumsTitle.setStyle("-fx-font-weight: bold;");

        ListView<Album> albumsListView = new ListView<>();
        albumsListView.setPrefHeight(150);
        ObservableList<Album> albums = FXCollections.observableArrayList(artist.getAlbums());
        albumsListView.setItems(albums);
        albumsListView.setCellFactory(lv -> new ListCell<Album>() {
            @Override
            protected void updateItem(Album album, boolean empty) {
                super.updateItem(album, empty);
                if (empty || album == null) {
                    setText(null);
                } else {
                    setText(ALBUM_EMOJI + " " + album.getTitle() + " (" + album.getSongs().size() + " songs)");
                }
            }
        });

        Button createSongButton = new Button(PLUS_EMOJI + " Create New Song");

        leftSidebar.getChildren().addAll(profileTitle, artistNameLabel, biographyLabel,
                socialLinksTitle, socialLinksListView, newLinkField, addLinkButton, removeLinkButton,
                albumsTitle, albumsListView, createSongButton);
        root.setLeft(leftSidebar);

        VBox center = new VBox(10);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(20));

        Text songsTitle = new Text(MUSIC_EMOJI + " Your Songs " + MUSIC_EMOJI);
        songsTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        ListView<Song> songsListView = new ListView<>();
        songsListView.setPrefSize(500, 400);

        List<Song> artistSongs = songs.stream()
                .filter(song -> song.getArtist().equals(artist))
                .collect(Collectors.toList());
        ObservableList<Song> songItems = FXCollections.observableArrayList(artistSongs);
        songsListView.setItems(songItems);

        songsListView.setCellFactory(lv -> new ListCell<Song>() {
            @Override
            protected void updateItem(Song song, boolean empty) {
                super.updateItem(song, empty);
                if (empty || song == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    VBox box = new VBox(5);

                    // Create an ImageView for the song cover (if available)
                    ImageView coverView = new ImageView();
                    coverView.setFitWidth(50);
                    coverView.setFitHeight(50);
                    coverView.setPreserveRatio(true);

                    if (song.getCoverImage() != null) {
                        coverView.setImage(song.getCoverImage());
                    } else {
                        // Default music icon if no cover
                        coverView.setImage(new Image(getClass().getResourceAsStream("/music-icon.png")));
                    }

                    HBox songBox = new HBox(10);

                    Label titleLabel = new Label(MUSIC_EMOJI + " " + song.getTitle() + " (" + song.getAlbum() + ")");
                    titleLabel.setStyle("-fx-font-weight: bold;");

                    Button editButton = new Button(EDIT_EMOJI);
                    editButton.setOnAction(e -> showEditSongDialog(artist, song));

                    songBox.getChildren().addAll(coverView, titleLabel, editButton);
                    box.getChildren().add(songBox);

                    setGraphic(box);
                }
            }
        });

        songsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showSongDetailPage(newVal);
            }
        });

        center.getChildren().addAll(songsTitle, songsListView);
        root.setCenter(center);

        HBox bottomBar = new HBox(10);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10));

        TextField searchField = new TextField();
        searchField.setPromptText(SEARCH_EMOJI + " Search songs...");
        Button searchButton = new Button(SEARCH_EMOJI + " Search");

        bottomBar.getChildren().addAll(searchField, searchButton);
        root.setBottom(bottomBar);

        logoutButton.setOnAction(e -> showLoginPage());
        createSongButton.setOnAction(e -> showCreateSongDialog(artist));
        searchButton.setOnAction(e -> {
            String query = searchField.getText().toLowerCase();
            List<Song> filteredSongs = songs.stream()
                    .filter(song -> song.getArtist().equals(artist))
                    .filter(song -> song.getTitle().toLowerCase().contains(query) ||
                            song.getAlbum().toLowerCase().contains(query) ||
                            song.getGenre().toLowerCase().contains(query))
                    .collect(Collectors.toList());
            songsListView.setItems(FXCollections.observableArrayList(filteredSongs));
        });

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    private void showAdminDashboard(Admin admin) {
        BorderPane root = new BorderPane();

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setStyle("-fx-background-color: #336699; -fx-padding: 10;");

        Text title = new Text(ADMIN_EMOJI + " Admin Dashboard " + ADMIN_EMOJI);
        title.setStyle("-fx-font-size: 20; -fx-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button(LOGOUT_EMOJI + " Logout");

        topBar.getChildren().addAll(title, spacer, logoutButton);
        root.setTop(topBar);

        TabPane tabPane = new TabPane();

        // Artist Requests Tab
        Tab requestsTab = new Tab(REQUEST_EMOJI + " Artist Requests");
        VBox requestsBox = new VBox(10);
        requestsBox.setPadding(new Insets(10));

        ListView<ArtistRequest> requestsListView = new ListView<>();
        ObservableList<ArtistRequest> requests = FXCollections.observableArrayList(artistRequests);
        requestsListView.setItems(requests);
        requestsListView.setCellFactory(lv -> new ListCell<ArtistRequest>() {
            @Override
            protected void updateItem(ArtistRequest request, boolean empty) {
                super.updateItem(request, empty);
                if (empty || request == null) {
                    setText(null);
                } else {
                    setText(ARTIST_EMOJI + " " + request.getName() + " (" + request.getUsername() + ")");
                }
            }
        });

        HBox requestButtons = new HBox(10);
        Button approveButton = new Button("✅ Approve");
        Button rejectButton = new Button("❌ Reject");
        requestButtons.getChildren().addAll(approveButton, rejectButton);
        requestButtons.setAlignment(Pos.CENTER);

        TextArea requestDetails = new TextArea();
        requestDetails.setEditable(false);
        requestDetails.setWrapText(true);

        requestsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                requestDetails.setText(
                        "Name: " + newVal.getName() + "\n" +
                                "Age: " + newVal.getAge() + "\n" +
                                "Email: " + newVal.getEmail() + "\n" +
                                "Username: " + newVal.getUsername() + "\n" +
                                "Biography: " + newVal.getBiography()
                );
            }
        });

        approveButton.setOnAction(e -> {
            ArtistRequest selected = requestsListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String userId = "artist" + (accounts.size() + 1);
                Artist newArtist = new Artist(
                        selected.getName(),
                        selected.getAge(),
                        selected.getEmail(),
                        selected.getUsername(),
                        selected.getPassword(),
                        selected.getBiography()
                );

                accounts.add(newArtist);
                artistRequests.remove(selected);
                requests.remove(selected);

                showAlert("Approved", "Artist account has been created for " + newArtist.getName());
            }
        });

        rejectButton.setOnAction(e -> {
            ArtistRequest selected = requestsListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                artistRequests.remove(selected);
                requests.remove(selected);
                showAlert("Rejected", "Artist request has been rejected");
            }
        });

        requestsBox.getChildren().addAll(
                new Label("Pending Artist Requests:"),
                requestsListView,
                requestDetails,
                requestButtons
        );
        requestsTab.setContent(requestsBox);

        // All Artists Tab
        Tab artistsTab = new Tab(ARTIST_EMOJI + " All Artists");
        VBox artistsBox = new VBox(10);
        artistsBox.setPadding(new Insets(10));

        ListView<Artist> artistsListView = new ListView<>();
        ObservableList<Artist> artists = FXCollections.observableArrayList(
                accounts.stream()
                        .filter(a -> a instanceof Artist)
                        .map(a -> (Artist) a)
                        .collect(Collectors.toList())
        );
        artistsListView.setItems(artists);
        artistsListView.setCellFactory(lv -> new ListCell<Artist>() {
            @Override
            protected void updateItem(Artist artist, boolean empty) {
                super.updateItem(artist, empty);
                if (empty || artist == null) {
                    setText(null);
                } else {
                    setText(ARTIST_EMOJI + " " + artist.getName() + " (" + artist.getUsername() + ")");
                }
            }
        });

        artistsBox.getChildren().addAll(
                new Label("Registered Artists:"),
                artistsListView
        );
        artistsTab.setContent(artistsBox);

        tabPane.getTabs().addAll(requestsTab, artistsTab);
        root.setCenter(tabPane);

        logoutButton.setOnAction(e -> showLoginPage());

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    private void showProfilePage(Account account) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Text title = new Text(USER_EMOJI + " Profile Details " + USER_EMOJI);
        title.setFont(Font.font(24));

        GridPane form = new GridPane();
        form.setAlignment(Pos.CENTER);
        form.setHgap(10);
        form.setVgap(10);

        Label nameLabel = new Label(USER_EMOJI + " Name:");
        TextField nameField = new TextField(account.getName());

        Label ageLabel = new Label("🔢 Age:");
        TextField ageField = new TextField(String.valueOf(account.getAge()));

        Label emailLabel = new Label("📧 Email:");
        TextField emailField = new TextField(account.getEmail());

        Label usernameLabel = new Label(USER_EMOJI + " Username:");
        TextField usernameField = new TextField(account.getUsername());

        Label passwordLabel = new Label("🔑 Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password");

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(ageLabel, 0, 1);
        form.add(ageField, 1, 1);
        form.add(emailLabel, 0, 2);
        form.add(emailField, 1, 2);
        form.add(usernameLabel, 0, 3);
        form.add(usernameField, 1, 3);
        form.add(passwordLabel, 0, 4);
        form.add(passwordField, 1, 4);

        if (account instanceof Artist) {
            Artist artist = (Artist) account;
            Label biographyLabel = new Label("📝 Biography:");
            TextField biographyField = new TextField(artist.getBiography());
            form.add(biographyLabel, 0, 5);
            form.add(biographyField, 1, 5);
        }

        Button saveButton = new Button("💾 Save Changes");
        Button backButton = new Button(BACK_EMOJI + " Back");

        HBox buttonBox = new HBox(10, saveButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        root.getChildren().addAll(title, form, buttonBox, errorLabel);

        saveButton.setOnAction(e -> {
            try {
                account.setName(nameField.getText());
                account.setAge(Integer.parseInt(ageField.getText()));
                account.setEmail(emailField.getText());
                account.setUsername(usernameField.getText());

                if (!passwordField.getText().isEmpty()) {
                    account.setPassword(passwordField.getText());
                }

                if (account instanceof Artist) {
                    Artist artist = (Artist) account;
                    TextField biographyField = (TextField) form.getChildren().get(form.getChildren().size() - 1);
                    artist.setBiography(biographyField.getText());
                }

                if (account.getRule().equals("User")) {
                    showUserHomePage((User) account);
                } else if (account.getRule().equals("Artist")) {
                    showArtistPage((Artist) account);
                } else if (account.getRule().equals("Admin")) {
                    showAdminDashboard((Admin) account);
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid age");
            }
        });

        backButton.setOnAction(e -> {
            if (account.getRule().equals("User")) {
                showUserHomePage((User) account);
            } else if (account.getRule().equals("Artist")) {
                showArtistPage((Artist) account);
            } else if (account.getRule().equals("Admin")) {
                showAdminDashboard((Admin) account);
            }
        });

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    private void showCreateSongDialog(Artist artist) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Create New Song");

        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(15));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        // Song cover image
        Label coverLabel = new Label(IMAGE_EMOJI + " Song Cover:");
        ImageView coverImageView = new ImageView();
        coverImageView.setFitWidth(100);
        coverImageView.setFitHeight(100);
        coverImageView.setPreserveRatio(true);
        Button uploadCoverButton = new Button(UPLOAD_EMOJI + " Upload Cover");

        // Song basic info
        Label titleLabel = new Label(MUSIC_EMOJI + " Title:");
        TextField titleField = new TextField();

        Label genreLabel = new Label("🎼 Genre:");
        TextField genreField = new TextField();

        Label albumLabel = new Label(ALBUM_EMOJI + " Album:");
        TextField albumField = new TextField();

        Label durationLabel = new Label("⏱️ Duration (seconds):");
        TextField durationField = new TextField();

        Label lyricsLabel = new Label(LYRICS_EMOJI + " Lyrics:");
        TextArea lyricsArea = new TextArea();
        lyricsArea.setPrefRowCount(5);

        // Audio source selection
        Label voiceLabel = new Label(MIC_EMOJI + " Audio Source:");
        ToggleGroup audioSourceGroup = new ToggleGroup();

        RadioButton micOption = new RadioButton(MIC_EMOJI + " Record from Microphone");
        micOption.setToggleGroup(audioSourceGroup);
        micOption.setUserData("mic");

        RadioButton fileOption = new RadioButton(UPLOAD_EMOJI + " Upload Audio File");
        fileOption.setToggleGroup(audioSourceGroup);
        fileOption.setUserData("file");

        RadioButton urlOption = new RadioButton(DOWNLOAD_EMOJI + " Download from URL");
        urlOption.setToggleGroup(audioSourceGroup);
        urlOption.setUserData("url");

        VBox audioSourceControls = new VBox(10);

        // Microphone controls
        HBox micControls = new HBox(10);
        Button recordButton = new Button(RECORD_EMOJI + " Record");
        Button stopButton = new Button(STOP_EMOJI + " Stop");
        Button playButton = new Button(PLAY_EMOJI + " Play");
        Button pauseButton = new Button(PAUSE_EMOJI + " Pause");

        stopButton.setDisable(true);
        playButton.setDisable(true);
        pauseButton.setDisable(true);

        micControls.getChildren().addAll(recordButton, stopButton, playButton, pauseButton);
        micControls.setVisible(false);

        // File upload controls
        HBox fileControls = new HBox(10);
        Button browseButton = new Button("📂 Browse");
        Label fileLabel = new Label("No file selected");
        fileControls.getChildren().addAll(browseButton, fileLabel);
        fileControls.setVisible(false);

        // URL download controls
        HBox urlControls = new HBox(10);
        TextField urlField = new TextField();
        urlField.setPromptText("Enter audio URL");
        Button downloadButton = new Button(DOWNLOAD_EMOJI + " Download");
        urlControls.getChildren().addAll(urlField, downloadButton);
        urlControls.setVisible(false);

        AtomicReference<File> selectedAudioFile = new AtomicReference<>();
        AtomicReference<ByteArrayOutputStream> recordedBytes = new AtomicReference<>();
        AtomicReference<File> selectedCoverFile = new AtomicReference<>();

        // Layout the form
        HBox coverBox = new HBox(10, coverLabel, coverImageView, uploadCoverButton);
        coverBox.setAlignment(Pos.CENTER_LEFT);

        form.add(coverBox, 0, 0, 2, 1);
        form.add(titleLabel, 0, 1);
        form.add(titleField, 1, 1);
        form.add(genreLabel, 0, 2);
        form.add(genreField, 1, 2);
        form.add(albumLabel, 0, 3);
        form.add(albumField, 1, 3);
        form.add(durationLabel, 0, 4);
        form.add(durationField, 1, 4);
        form.add(lyricsLabel, 0, 5);
        form.add(lyricsArea, 1, 5);
        form.add(voiceLabel, 0, 6);
        form.add(audioSourceControls, 1, 6);

        audioSourceControls.getChildren().addAll(
                micOption, micControls,
                fileOption, fileControls,
                urlOption, urlControls
        );

        // Upload cover image button action
        uploadCoverButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Cover Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            selectedCoverFile.set(fileChooser.showOpenDialog(dialog));
            if (selectedCoverFile.get() != null) {
                try {
                    Image image = new Image(selectedCoverFile.get().toURI().toString());
                    coverImageView.setImage(image);
                } catch (Exception ex) {
                    showAlert("Error", "Could not load image: " + ex.getMessage());
                }
            }
        });

        // Audio source selection listener
        audioSourceGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            micControls.setVisible("mic".equals(newVal.getUserData()));
            fileControls.setVisible("file".equals(newVal.getUserData()));
            urlControls.setVisible("url".equals(newVal.getUserData()));

            selectedAudioFile.set(null);
            recordedBytes.set(null);
            fileLabel.setText("No file selected");
            urlField.clear();
        });

        // Recording controls
        recordButton.setOnAction(e -> {
            try {
                startRecording();
                recordButton.setDisable(true);
                stopButton.setDisable(false);
                playButton.setDisable(true);
                pauseButton.setDisable(true);
            } catch (LineUnavailableException ex) {
                showAlert("Recording Error", "Could not start recording: " + ex.getMessage());
            }
        });

        stopButton.setOnAction(e -> {
            recordedBytes.set(stopRecording());
            recordButton.setDisable(false);
            stopButton.setDisable(true);
            if (recordedBytes.get() != null && recordedBytes.get().size() > 0) {
                playButton.setDisable(false);
            }
        });

        playButton.setOnAction(e -> {
            playRecording(recordedBytes.get());
            playButton.setDisable(true);
            pauseButton.setDisable(false);
        });

        pauseButton.setOnAction(e -> {
            pauseRecording();
            pauseButton.setDisable(true);
            playButton.setDisable(false);
        });

        // File browse button
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Audio File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            selectedAudioFile.set(fileChooser.showOpenDialog(dialog));
            if (selectedAudioFile.get() != null) {
                fileLabel.setText(selectedAudioFile.get().getName());
            }
        });

        // URL download button
        downloadButton.setOnAction(e -> {
            String audioUrl = urlField.getText();
            if (audioUrl == null || audioUrl.isEmpty()) {
                showAlert("Error", "Please enter a valid URL");
                return;
            }

            ProgressIndicator progress = new ProgressIndicator();
            dialogVBox.getChildren().add(progress);

            new Thread(() -> {
                try {
                    File tempFile = File.createTempFile("downloaded_audio", ".tmp");

                    try (InputStream in = new URL(audioUrl).openStream()) {
                        Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    selectedAudioFile.set(tempFile);
                    javafx.application.Platform.runLater(() -> {
                        dialogVBox.getChildren().remove(progress);
                        showAlert("Success", "Audio downloaded successfully!");
                    });
                } catch (IOException ex) {
                    javafx.application.Platform.runLater(() -> {
                        dialogVBox.getChildren().remove(progress);
                        showAlert("Download Failed", "Error downloading audio: " + ex.getMessage());
                    });
                }
            }).start();
        });

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button createButton = new Button(PLUS_EMOJI + " Create");
        Button cancelButton = new Button("Cancel");

        HBox buttonBox = new HBox(10, createButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        dialogVBox.getChildren().addAll(form, errorLabel, buttonBox);

        createButton.setOnAction(e -> {
            try {
                String title = titleField.getText();
                String genre = genreField.getText();
                String album = albumField.getText();
                int duration = Integer.parseInt(durationField.getText());
                String lyrics = lyricsArea.getText();

                if (title.isEmpty() || genre.isEmpty() || album.isEmpty()) {
                    errorLabel.setText("Please fill in all fields");
                    return;
                }

                if (duration <= 0) {
                    errorLabel.setText("Duration must be positive");
                    return;
                }

                Song newSong = new Song(nextSongId++, title, genre, album, artist, duration, new Date(), 0);
                newSong.setLyrics(lyrics);

                // Set cover image if available
                if (selectedCoverFile.get() != null) {
                    try {
                        byte[] imageBytes = Files.readAllBytes(selectedCoverFile.get().toPath());
                        newSong.setCoverImageData(imageBytes);
                    } catch (IOException ex) {
                        errorLabel.setText("Error reading cover image: " + ex.getMessage());
                        return;
                    }
                }

                if (audioSourceGroup.getSelectedToggle() != null) {
                    String sourceType = audioSourceGroup.getSelectedToggle().getUserData().toString();
                    newSong.setAudioSourceType(sourceType);

                    switch (sourceType) {
                        case "mic":
                            if (recordedBytes.get() != null && recordedBytes.get().size() > 0) {
                                newSong.setAudioData(recordedBytes.get().toByteArray());
                            }
                            break;

                        case "file":
                            if (selectedAudioFile.get() != null) {
                                try {
                                    byte[] fileBytes = Files.readAllBytes(selectedAudioFile.get().toPath());
                                    newSong.setAudioData(fileBytes);
                                    newSong.setOriginalFileName(selectedAudioFile.get().getName());
                                } catch (IOException ex) {
                                    errorLabel.setText("Error reading audio file: " + ex.getMessage());
                                    return;
                                }
                            }
                            break;

                        case "url":
                            if (selectedAudioFile.get() != null) {
                                try {
                                    byte[] fileBytes = Files.readAllBytes(selectedAudioFile.get().toPath());
                                    newSong.setAudioData(fileBytes);
                                    newSong.setOriginalFileName(urlField.getText());
                                } catch (IOException ex) {
                                    errorLabel.setText("Error processing downloaded audio: " + ex.getMessage());
                                    return;
                                }
                            }
                            break;
                    }
                }

                songs.add(newSong);

                // Add to album
                boolean albumExists = false;
                for (Album alb : artist.getAlbums()) {
                    if (alb.getTitle().equals(album)) {
                        alb.addSong(newSong);
                        albumExists = true;
                        break;
                    }
                }

                if (!albumExists) {
                    List<String> contributors = new ArrayList<>();
                    contributors.add(artist.getName());
                    List<Song> albumSongs = new ArrayList<>();
                    albumSongs.add(newSong);
                    Album newAlbum = new Album(artist.getAlbums().size() + 1, album, contributors,
                            duration, "Album by " + artist.getName(), 0, albumSongs);
                    artist.addAlbum(newAlbum);
                }

                dialog.close();
                showArtistPage(artist);
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid duration");
            }
        });

        cancelButton.setOnAction(e -> {
            stopRecording();
            dialog.close();
        });

        Scene dialogScene = new Scene(dialogVBox, 500, 600);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private void showEditSongDialog(Artist artist, Song song) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Song");

        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(15));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        // Song cover image
        Label coverLabel = new Label(IMAGE_EMOJI + " Song Cover:");
        ImageView coverImageView = new ImageView();
        coverImageView.setFitWidth(100);
        coverImageView.setFitHeight(100);
        coverImageView.setPreserveRatio(true);

        if (song.getCoverImage() != null) {
            coverImageView.setImage(song.getCoverImage());
        }

        Button uploadCoverButton = new Button(UPLOAD_EMOJI + " Upload Cover");

        // Song basic info
        Label titleLabel = new Label(MUSIC_EMOJI + " Title:");
        TextField titleField = new TextField(song.getTitle());

        Label genreLabel = new Label("🎼 Genre:");
        TextField genreField = new TextField(song.getGenre());

        Label albumLabel = new Label(ALBUM_EMOJI + " Album:");
        TextField albumField = new TextField(song.getAlbum());

        Label durationLabel = new Label("⏱️ Duration (seconds):");
        TextField durationField = new TextField(String.valueOf(song.getDuration()));

        Label lyricsLabel = new Label(LYRICS_EMOJI + " Lyrics:");
        TextArea lyricsArea = new TextArea(song.getLyrics());
        lyricsArea.setPrefRowCount(5);

        // Layout the form
        HBox coverBox = new HBox(10, coverLabel, coverImageView, uploadCoverButton);
        coverBox.setAlignment(Pos.CENTER_LEFT);

        form.add(coverBox, 0, 0, 2, 1);
        form.add(titleLabel, 0, 1);
        form.add(titleField, 1, 1);
        form.add(genreLabel, 0, 2);
        form.add(genreField, 1, 2);
        form.add(albumLabel, 0, 3);
        form.add(albumField, 1, 3);
        form.add(durationLabel, 0, 4);
        form.add(durationField, 1, 4);
        form.add(lyricsLabel, 0, 5);
        form.add(lyricsArea, 1, 5);

        AtomicReference<File> selectedCoverFile = new AtomicReference<>();

        // Upload cover image button action
        uploadCoverButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Cover Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            selectedCoverFile.set(fileChooser.showOpenDialog(dialog));
            if (selectedCoverFile.get() != null) {
                try {
                    Image image = new Image(selectedCoverFile.get().toURI().toString());
                    coverImageView.setImage(image);
                } catch (Exception ex) {
                    showAlert("Error", "Could not load image: " + ex.getMessage());
                }
            }
        });

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button saveButton = new Button("💾 Save Changes");
        Button cancelButton = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        dialogVBox.getChildren().addAll(form, errorLabel, buttonBox);

        saveButton.setOnAction(e -> {
            try {
                song.setTitle(titleField.getText());
                song.setGenre(genreField.getText());
                song.setAlbum(albumField.getText());
                song.setDuration(Integer.parseInt(durationField.getText()));
                song.setLyrics(lyricsArea.getText());

                // Update cover image if changed
                if (selectedCoverFile.get() != null) {
                    try {
                        byte[] imageBytes = Files.readAllBytes(selectedCoverFile.get().toPath());
                        song.setCoverImageData(imageBytes);
                    } catch (IOException ex) {
                        errorLabel.setText("Error reading cover image: " + ex.getMessage());
                        return;
                    }
                }

                // Update album if changed
                if (!song.getAlbum().equals(albumField.getText())) {
                    // Remove from old album
                    for (Album alb : artist.getAlbums()) {
                        if (alb.getSongs().contains(song)) {
                            alb.removeSong(song);
                            break;
                        }
                    }

                    // Add to new album
                    boolean albumExists = false;
                    for (Album alb : artist.getAlbums()) {
                        if (alb.getTitle().equals(albumField.getText())) {
                            alb.addSong(song);
                            albumExists = true;
                            break;
                        }
                    }

                    if (!albumExists) {
                        List<String> contributors = new ArrayList<>();
                        contributors.add(artist.getName());
                        List<Song> albumSongs = new ArrayList<>();
                        albumSongs.add(song);
                        Album newAlbum = new Album(artist.getAlbums().size() + 1,
                                albumField.getText(), contributors,
                                song.getDuration(), "Album by " + artist.getName(), 0, albumSongs);
                        artist.addAlbum(newAlbum);
                    }
                }

                dialog.close();
                showArtistPage(artist);
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid duration");
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        Scene dialogScene = new Scene(dialogVBox, 500, 500);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private void startRecording() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Line not supported");
        }

        microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
        recordedBytes = new ByteArrayOutputStream();
        isRecording = true;

        new Thread(() -> {
            microphone.start();
            byte[] buffer = new byte[1024];
            while (isRecording) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    recordedBytes.write(buffer, 0, bytesRead);
                }
            }
        }).start();
    }

    private ByteArrayOutputStream stopRecording() {
        isRecording = false;
        if (microphone != null) {
            microphone.stop();
            microphone.close();
        }
        return recordedBytes;
    }

    private void playRecording(ByteArrayOutputStream audioData) {
        if (audioData != null && audioData.size() > 0) {
            try {
                File tempFile = File.createTempFile("playback", ".wav");
                Files.write(tempFile.toPath(), audioData.toByteArray());
                playAudioFile(tempFile);
            } catch (IOException ex) {
                showAlert("Playback Error", "Could not play recording: " + ex.getMessage());
            }
        }
    }

    private void playAudioFile(File audioFile) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Media sound = new Media(audioFile.toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
        });
    }

    private void pauseRecording() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    private void showSongDetailPage(Song song) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #336699; -fx-padding: 10;");

        Button backButton = new Button(BACK_EMOJI + " Back");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label songTitleLabel = new Label(MUSIC_EMOJI + " " + song.getTitle());
        songTitleLabel.setStyle("-fx-font-size: 20; -fx-text-fill: white;");

        topBar.getChildren().addAll(backButton, spacer, songTitleLabel);

        // Song cover image
        ImageView coverImageView = new ImageView();
        coverImageView.setFitWidth(150);
        coverImageView.setFitHeight(150);
        coverImageView.setPreserveRatio(true);

        if (song.getCoverImage() != null) {
            coverImageView.setImage(song.getCoverImage());
        } else {
            // Default music icon if no cover
            coverImageView.setImage(new Image(getClass().getResourceAsStream("/music-icon.png")));
        }

        HBox coverBox = new HBox(coverImageView);
        coverBox.setAlignment(Pos.CENTER);

        GridPane detailsGrid = new GridPane();
        detailsGrid.setAlignment(Pos.CENTER);
        detailsGrid.setHgap(10);
        detailsGrid.setVgap(10);

        Label artistLabel = createDetailLabel(ARTIST_EMOJI + " Artist:", song.getArtist().getName());
        Label albumLabel = createDetailLabel(ALBUM_EMOJI + " Album:", song.getAlbum());
        Label genreLabel = createDetailLabel("🎼 Genre:", song.getGenre());

        int minutes = song.getDuration() / 60;
        int seconds = song.getDuration() % 60;
        String duration = String.format("%d:%02d", minutes, seconds);
        Label durationLabel = createDetailLabel("⏱️ Duration:", duration);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String releaseYear = dateFormat.format(song.getReleaseYear());
        Label releaseYearLabel = createDetailLabel("📅 Release Year:", releaseYear);

        Label likeCountLabel = createDetailLabel(HEART_EMOJI + " Likes:", String.valueOf(song.getLikeCount()));

        addDetailRow(detailsGrid, ARTIST_EMOJI + " Artist:", artistLabel, 0);
        addDetailRow(detailsGrid, ALBUM_EMOJI + " Album:", albumLabel, 1);
        addDetailRow(detailsGrid, "🎼 Genre:", genreLabel, 2);
        addDetailRow(detailsGrid, "⏱️ Duration:", durationLabel, 3);
        addDetailRow(detailsGrid, "📅 Release Year:", releaseYearLabel, 4);
        addDetailRow(detailsGrid, HEART_EMOJI + " Likes:", likeCountLabel, 5);

        // Playback controls
        if (song.hasAudio()) {
            HBox playbackControls = new HBox(10);
            playbackControls.setAlignment(Pos.CENTER);

            Button playButton = new Button(PLAY_EMOJI + " Play");
            Button pauseButton = new Button(PAUSE_EMOJI + " Pause");
            Button stopButton = new Button(STOP_EMOJI + " Stop");

            playButton.setOnAction(e -> {
                try {
                    File tempFile = File.createTempFile("playback", ".wav");
                    Files.write(tempFile.toPath(), song.getAudioData());
                    playAudioFile(tempFile);

                    playButton.setDisable(true);
                    pauseButton.setDisable(false);
                    stopButton.setDisable(false);
                } catch (IOException ex) {
                    showAlert("Playback Error", "Could not play audio: " + ex.getMessage());
                }
            });

            pauseButton.setOnAction(e -> {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    pauseButton.setDisable(true);
                    playButton.setDisable(false);
                }
            });

            stopButton.setOnAction(e -> {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    playButton.setDisable(false);
                    pauseButton.setDisable(true);
                    stopButton.setDisable(true);
                }
            });

            pauseButton.setDisable(true);
            stopButton.setDisable(true);

            playbackControls.getChildren().addAll(playButton, pauseButton, stopButton);
            root.getChildren().add(playbackControls);

            Label sourceLabel = new Label();
            switch (song.getAudioSourceType()) {
                case "mic":
                    sourceLabel.setText("Source: Microphone Recording");
                    break;
                case "file":
                    sourceLabel.setText("Source: File Upload (" + song.getOriginalFileName() + ")");
                    break;
                case "url":
                    sourceLabel.setText("Source: URL Download (" + song.getOriginalFileName() + ")");
                    break;
                default:
                    sourceLabel.setText("Source: Unknown");
            }
            root.getChildren().add(sourceLabel);
        }

        // Lyrics section
        if (song.getLyrics() != null && !song.getLyrics().isEmpty()) {
            Text lyricsTitle = new Text(LYRICS_EMOJI + " Lyrics " + LYRICS_EMOJI);
            lyricsTitle.setStyle("-fx-font-weight: bold;");

            TextArea lyricsArea = new TextArea(song.getLyrics());
            lyricsArea.setEditable(false);
            lyricsArea.setWrapText(true);
            lyricsArea.setPrefRowCount(8);

            root.getChildren().addAll(lyricsTitle, lyricsArea);
        }

        // Comments section
        if (currentUser instanceof User) {
            Text commentsTitle = new Text(COMMENT_EMOJI + " Comments " + COMMENT_EMOJI);
            commentsTitle.setStyle("-fx-font-weight: bold;");

            ListView<Comment> commentsListView = new ListView<>();
            commentsListView.setPrefHeight(150);
            ObservableList<Comment> comments = FXCollections.observableArrayList(song.getComments());
            commentsListView.setItems(comments);
            commentsListView.setCellFactory(lv -> new ListCell<Comment>() {
                @Override
                protected void updateItem(Comment comment, boolean empty) {
                    super.updateItem(comment, empty);
                    if (empty || comment == null) {
                        setText(null);
                    } else {
                        setText(USER_EMOJI + " " + comment.getUser().getUsername() + ": " + comment.getText());
                    }
                }
            });

            TextField newCommentField = new TextField();
            newCommentField.setPromptText("Add a comment...");
            Button addCommentButton = new Button(PLUS_EMOJI + " Add Comment");

            addCommentButton.setOnAction(e -> {
                String commentText = newCommentField.getText();
                if (!commentText.isEmpty()) {
                    Comment newComment = new Comment((User) currentUser, commentText, new Date());
                    song.addComment(newComment);
                    comments.add(newComment);
                    newCommentField.clear();
                }
            });

            HBox commentBox = new HBox(10, newCommentField, addCommentButton);
            commentBox.setAlignment(Pos.CENTER);

            root.getChildren().addAll(commentsTitle, commentsListView, commentBox);
        }

        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);

        Button likeButton = new Button(HEART_EMOJI + " Like");

        if (currentUser instanceof User) {
            User user = (User) currentUser;
            Button followButton = new Button();

            if (user.getFollowedArtists().containsKey(song.getArtist())) {
                followButton.setText(FOLLOWING_EMOJI + " Following");
            } else {
                followButton.setText(FOLLOW_EMOJI + " Follow Artist");
            }

            followButton.setOnAction(e -> {
                if (user.getFollowedArtists().containsKey(song.getArtist())) {
                    user.unfollowArtist(song.getArtist());
                    followButton.setText(FOLLOW_EMOJI + " Follow Artist");
                } else {
                    user.followArtist(song.getArtist());
                    followButton.setText(FOLLOWING_EMOJI + " Following");
                }
            });

            actionButtons.getChildren().add(followButton);
        }

        actionButtons.getChildren().add(likeButton);

        root.getChildren().addAll(topBar, coverBox, detailsGrid, actionButtons);

        backButton.setOnAction(e -> {
            if (currentUser.getRule().equals("User")) {
                showUserHomePage((User) currentUser);
            } else if (currentUser.getRule().equals("Artist")) {
                showArtistPage((Artist) currentUser);
            } else if (currentUser.getRule().equals("Admin")) {
                showAdminDashboard((Admin) currentUser);
            }
        });

        likeButton.setOnAction(e -> {
            song.setLikeCount(song.getLikeCount() + 1);
            likeCountLabel.setText(String.valueOf(song.getLikeCount()));
        });

        Scene scene = new Scene(root, 800, 700);
        primaryStage.setScene(scene);
    }

    private Label createDetailLabel(String title, String value) {
        Label label = new Label(value);
        return label;
    }

    private void addDetailRow(GridPane grid, String title, Label valueLabel, int row) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold;");
        grid.add(titleLabel, 0, row);
        grid.add(valueLabel, 1, row);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}