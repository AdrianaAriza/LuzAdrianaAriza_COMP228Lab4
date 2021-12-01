package com.exercise1.luzadrianaariza_comp228lab4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.control.TextField;
import oracle.jdbc.driver.OracleDriver;

public class GamerController {

    public static final String DBURL = "jdbc:oracle:thin:@localhost:1521:ORCLCDB";
    public static final String DBUSER = "adriana";
    public static final String DBPASS = "adriana";

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
    private void initialize() throws SQLException {
        DriverManager.registerDriver(new OracleDriver());
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
        List<String> players = new ArrayList<String>();
        String query = "SELECT PLAYER_ID FROM PLAYER";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        System.out.println(rs);
        while(rs.next()) {
            System.out.println("PlayerId: "+rs.getString("PLAYER_ID")+", ");
            players.add(rs.getString("PLAYER_ID"));
        }
        ObservableList<String> listOfPlayers = FXCollections.observableArrayList(players);
        playerList.setItems(listOfPlayers);
    }

    public void createRegister() throws SQLException {
        System.out.println("CREATING USER");
        createPlayer();
        createGame();
        createPlayerAndGame();
        clearFields();
    }

    public void createPlayer() throws SQLException {

        DriverManager.registerDriver(new OracleDriver());
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
        Statement statement = con.createStatement();
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
        statement.close();
        con.close();
    }
    public void createGame() throws SQLException {
        DriverManager.registerDriver(new OracleDriver());
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
        Statement statement = con.createStatement();

        String query = "INSERT INTO GAME (game_id, game_title) " +
                "VALUES ("
                + "'" + gameId.getText() + "', "
                + "'" + gameTitle.getText() + "')";
        System.out.println(query);
        statement.executeQuery(query);
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
}