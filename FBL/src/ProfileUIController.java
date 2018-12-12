import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.bson.Document;

import java.util.Date;
import java.util.Optional;

public class ProfileUIController {
    GUIManager gui;
    FBLManager fbl;
    private String userName;
    private UserDoc doc;

    @FXML
    Label name;
    @FXML
    Label age;
    @FXML
    Label status;
    @FXML
    Button addRemoveFriend;
    @FXML
    ScrollPane panePosts;
    @FXML
    ScrollPane paneFriends;

    public void initialize(GUIManager gui, FBLManager fbl){
        this.gui = gui;
        this.fbl = fbl;
        userName = fbl.getFriend();
        doc = fbl.getUser(userName);

        buildSceneLayout();

        loadUserData();
        buildUserPost();
        buildFriendsList();

        if(userName.equals(fbl.getUsername()))
            addRemoveFriend.setVisible(false);
        else if (fbl.isFriend) {
            addRemoveFriend.setVisible(true);
            addRemoveFriend.setText("Remove friend");
        }
        else {
            addRemoveFriend.setVisible(true);
            addRemoveFriend.setText("Add friend");
        }
    }

    private void buildSceneLayout() {
        name.setWrapText(true);
        panePosts.setPadding(new Insets(5));
        paneFriends.setPadding(new Insets(5));
    }

    private void buildUserPost() {
        fbl.getPosts(userName);

        VBox postsVBox = new VBox(5);

        for (Document postDocs: fbl.userPost.postDocs) {

            AnchorPane postPanel = new AnchorPane();

            Label postLabel = new Label(
                    "@" + postDocs.getString("username") + "\n" +
                            new Date(postDocs.getLong("date")).toString() + "\n" +
                            postDocs.getString("post") + "\n" +
                            "--------------------"
            );
            postLabel.setWrapText(true);
            postPanel.getChildren().add(postLabel);

            postsVBox.getChildren().add(postPanel);
        }

        // add VBox to scrollPanel
        panePosts.setContent(postsVBox);
    }

    private void buildFriendsList() {
        fbl.getOtherFriendsList();

        // VBox of holding all friends' username
        VBox friendsVBox = new VBox(5);

        if (!fbl.friendList.friendsList.isEmpty()) {
            for (String name : fbl.friendList.friendsList) {

                // TODO design decision pending
                Button friendButton = new Button("@" + name);
                friendButton.setPrefWidth(190);
                friendButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Jumping to " + friendButton.getText() + " profile..");
                        System.out.println(name);
                        try {
                            viewFriendProfile(name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                friendsVBox.getChildren().add(friendButton);
            }

            paneFriends.setContent(friendsVBox);
        }
        else {
            Label noFriend = new Label("This user have not followed anyone yet.");
            //friendsVBox.getChildren().addListener(noFriend);
            paneFriends.setContent(noFriend);
        }
    }

    public void viewFriendProfile(String other) throws Exception{
        //View the profile of a friend
        fbl.setFriend(other);
        gui.loadProfileUIPage();
    }

    public void back() throws Exception {
        gui.loadHomePage();
    }

    private void loadUserData(){
        if(!doc.getHideAge())
            age.setText("" + doc.getAge());
        else
            age.setText("");

        if(!doc.getHideStatus())
            status.setText(doc.getStatus());
        else
            status.setText("");

        name.setText(doc.getFirstName() + " " + doc.getLastName());
    }

}
