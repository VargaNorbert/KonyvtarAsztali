module hu.petrik.konyvtarasztali {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    exports hu.petrik.konyvtarasztali;
    opens hu.petrik.konyvtarasztali to javafx.fxml;
}