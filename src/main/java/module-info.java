module com.exercise1.luzadrianaariza_comp228lab4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires ojdbc7;


    opens com.exercise1.luzadrianaariza_comp228lab4 to javafx.fxml;
    exports com.exercise1.luzadrianaariza_comp228lab4;
}