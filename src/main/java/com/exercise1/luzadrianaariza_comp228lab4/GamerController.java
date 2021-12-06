package com.exercise1.luzadrianaariza_comp228lab4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import oracle.jdbc.driver.OracleDriver;

import static java.lang.System.out;

public class GamerController {

//    public static final String DBURL = "jdbc:oracle:thin:@localhost:1521:ORCLCDB";
//    public static final String DBUSER = "adriana";
//    public static final String DBPASS = "adriana";

//    public static final String DBURL = "jdbc:oracle:thin:@localhost:1522:oracle";
//    public static final String DBUSER = "imoskgn";
//    public static final String DBPASS = "imoskgn";

    public static final String DBURL = "jdbc:oracle:thin:@localhost:1521:xe";
    public static final String DBUSER = "SYSTEM";
    public static final String DBPASS = "oracle";

    @FXML
    private TextField playerId;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField address;
    @FXML
    private TextField postalCode;
    @FXML
    private TextField province;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField gameId;
    @FXML
    private TextField gameTitle;
    @FXML
    private DatePicker playingDate;
    @FXML
    private TextField score;
    @FXML
    private ComboBox<String> playerList;
    @FXML
    private TextArea output;

    @FXML
    private void initialize() throws SQLException {
        loadPlayerList();
    }

    public static final Alert alertUserExists = new Alert(Alert.AlertType.WARNING,"User already exists!\n We will only add the game");
    public static final Alert registerCreated = new Alert(Alert.AlertType.INFORMATION,"New register created!\nFind your user in the Combo Box and press display");
    public static final Alert userUpdated = new Alert(Alert.AlertType.INFORMATION,"User Updated!");
    public static final String[] playerFields = {"PLAYER_ID", "FIRST_NAME", "LAST_NAME", "ADDRESS", "POSTAL_CODE", "PROVINCE", "PHONE_NUMBER"};

    public void createRegister() throws SQLException {
        Boolean userCreated = false;
        userCreated = createPlayer();
        createGame();
        createPlayerAndGame();
        loadPlayerList();
        clearFields();
        if (userCreated) {
            registerCreated.show();
        }
    }

    public void loadPlayerList() throws SQLException {
        DriverManager.registerDriver(new OracleDriver());
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
        List<String> players = new ArrayList<String>();
        String query = "SELECT PLAYER_ID FROM PLAYER";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
//            System.out.println("PlayerId: "+rs.getString("PLAYER_ID")+", ");
            players.add(rs.getString("PLAYER_ID"));
        }
        out.println(players.size());
        if(players.size() > 0){
            ObservableList<String> listOfPlayers = FXCollections.observableArrayList(players);
            playerList.setItems(listOfPlayers);
        }
        con.close();

    }

    public Boolean createPlayer() throws SQLException {
        Integer userExist = 0;
        Boolean userCreated = false;
        try {
            System.out.println("Create player");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            System.out.println("DB connected");

            Statement statement = con.createStatement();

            String queryUserExist = "SELECT COUNT(*) AS \"USER_NUMBER\" FROM PLAYER WHERE PLAYER_ID =" + playerId.getText();
            System.out.println(queryUserExist);

            ResultSet rs = statement.executeQuery(queryUserExist);
            while(rs.next()) {
                String response = rs.getString("USER_NUMBER");
                userExist = Integer.parseInt(response);
            }

            if (userExist == 0) {
                userCreated = true;
                String query = "INSERT INTO PLAYER (player_id, first_name, last_name, phone_number, address, postal_code, province) " +
                        "VALUES ("
                        + "'" + playerId.getText() + "', "
                        + "'" + firstName.getText() + "', "
                        + "'" + lastName.getText() + "', "
                        + "'" + phoneNumber.getText() + "', "
                        + "'" + address.getText() + "', "
                        + "'" + postalCode.getText() + "', "
                        + "'" + province.getText() + "')";
                System.out.println(query);

                statement.executeQuery(query);
                System.out.println("Query executed");
                statement.close();
                con.close();
            } else {
                alertUserExists.show();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return userCreated;
    }
    public void createGame() throws SQLException {
        DriverManager.registerDriver(new OracleDriver());
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
        Statement statement = con.createStatement();



        String query = "INSERT INTO GAME (game_id, game_title) " +
                "VALUES ("
                + "'" + gameId.getText() + "', "
                + "'" + gameTitle.getText() + "')";
        out.println(query);
        statement.executeQuery(query);
        out.println("query create game executed");
        statement.close();
        con.close();
    }
    public void createPlayerAndGame() throws SQLException {
        Random numRandom = new Random();
        int n = numRandom.nextInt(75-25+1) + 25;
        String playerGameId = Integer.toString(n);

        DriverManager.registerDriver(new OracleDriver());
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
        Statement statement = con.createStatement();
        String query = "INSERT INTO PLAYERANDGAME (player_game_id, game_id, player_id, playing_date, score) " +
                "VALUES ("
                + "'" + playerGameId + "', "
                + "'" + gameId.getText() + "', "
                + "'" + playerId.getText() + "', "
                + "'" + playingDate.getValue() + "', "
                + "'" + score.getText() + "')";
        System.out.println(query);

        statement.executeQuery(query);

        statement.close();
        con.close();
    }
    public void clearFields(){
        playerId.setText("");
        firstName.setText("");
        lastName.setText("");
        phoneNumber.setText("");
        address.setText("");
        postalCode.setText("");
        province.setText("");
        gameId.setText("");
        gameTitle.setText("");
        playingDate.setValue(LocalDate.now());
        score.setText("");
    }
    public void displayAction() throws SQLException {
        String playerID = playerList.getSelectionModel().getSelectedItem();

        if (playerId != null) {
            DriverManager.registerDriver(new OracleDriver());
            Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            String query = "SELECT * FROM PLAYER WHERE PLAYER_ID ="+ playerID;
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                playerId.setText(rs.getString("PLAYER_ID"));
                firstName.setText(rs.getString("FIRST_NAME"));
                lastName.setText(rs.getString("LAST_NAME"));
                address.setText(rs.getString("ADDRESS"));
                postalCode.setText(rs.getString("POSTAL_CODE"));
                province.setText(rs.getString("PROVINCE"));
                phoneNumber.setText(rs.getString("PHONE_NUMBER"));
            }
            rs.close();
            displayInPrompt(playerID, con);
            con.close();
        }
    }

    public void displayInPrompt(String playerId, Connection con) throws SQLException {
        String msgPrompt = "No info";

        String queryUser = "SELECT * FROM PLAYER WHERE PLAYER_ID = " + playerId;
        PreparedStatement ps = con.prepareStatement(queryUser);
        ResultSet rsUser = ps.executeQuery();

        if (rsUser.next()) {
            msgPrompt = "USER INFO\n";
            msgPrompt += "=========\n";
            msgPrompt += "\n";
            for (int i = 0; i < playerFields.length; i++) {
                msgPrompt += playerFields[i] + ": " + rsUser.getString(playerFields[i]) + "\n";
            }
            msgPrompt += "\n";
        }
        rsUser.close();
        String queryGames = "SELECT " +
                "PLAYERANDGAME.game_id, PLAYERANDGAME.score, GAME.game_title " +
                "FROM " +
                "PLAYERANDGAME " +
                "INNER JOIN " +
                "PLAYER " +
                "ON PLAYERANDGAME.player_id = PLAYER.player_id " +
                "INNER JOIN " +
                "GAME " +
                "ON PLAYERANDGAME.game_id = GAME.game_id " +
                "WHERE PLAYER.player_id= " + playerId;

        msgPrompt += "GAMES INFO\n";
        msgPrompt += "=========\n";

        ps = con.prepareStatement(queryGames);
        ResultSet rsGames = ps.executeQuery();

        while (rsGames.next()) {
                msgPrompt += "GAME_ID: " + rsGames.getString("GAME_ID") +"\n";
                msgPrompt += "GAME_TITLE: " + rsGames.getString("GAME_TITLE") +"\n";
                msgPrompt += "SCORE: " + rsGames.getString("SCORE") +"\n";
                msgPrompt += "\n";
        }
        rsGames.close();
        output.setText(msgPrompt);
    }

    public void updatePlayerInfo() throws SQLException {
        String changes = "";
        if (firstName.getText() != null && firstName.getText() != ""){
            changes += "FIRST_NAME = \'"+firstName.getText()+"\',";
        }
        if (lastName.getText() != "" && lastName.getText() != ""){
            changes += "LAST_NAME = \'"+lastName.getText()+"\',";
        }
        if (address.getText() != "" && address.getText() != ""){
            changes += "ADDRESS = \'"+address.getText()+"\',";
        }
        if (postalCode.getText() != "" && postalCode.getText() != ""){
            changes += "POSTAL_CODE = \'"+postalCode.getText()+"\',";
        }
        if (province.getText() != "" && province.getText() != ""){
            changes += "PROVINCE = \'"+province.getText()+"\',";
        }
        if (phoneNumber.getText() != "" && phoneNumber.getText() != ""){
            changes += "PHONE_NUMBER = \'"+phoneNumber.getText()+"\',";
        }

        if (changes!=""){
            if (changes.charAt(changes.length() - 1) == ','){
                changes = changes.substring(0,changes.length() - 1);
            }
            out.println("fue: \""+phoneNumber.getText()+"\"");
            String query = "UPDATE PLAYER SET "+ changes +" WHERE PLAYER_ID = "+
                    playerList.getSelectionModel().getSelectedItem();
            DriverManager.registerDriver(new OracleDriver());
            Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            PreparedStatement ps = con.prepareStatement(query);
            userUpdated.show();
            ps.execute();
            con.close();
        }
    }


}