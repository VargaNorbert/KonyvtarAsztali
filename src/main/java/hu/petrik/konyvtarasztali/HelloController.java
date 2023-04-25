package hu.petrik.konyvtarasztali;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Optional;

public class HelloController {

    @FXML
    private TableColumn<Konyv, String> titleCol;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Konyv> booksTable;
    @FXML
    private TableColumn<Konyv, String> authorCol;
    @FXML
    private TableColumn<Konyv, Integer> pageCountCol;
    @FXML
    private TableColumn<Konyv, Integer>publishYearCol;

    private DBHelper db;

    public void initialize(){
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publishYearCol.setCellValueFactory(new PropertyValueFactory<>("publish_year"));
        pageCountCol.setCellValueFactory(new PropertyValueFactory<>("page_count"));
        try {
            db = new DBHelper();
            refreshTableData();
        } catch (SQLException e) {
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Hiba az adatbázissal");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                Platform.exit();
            });

        }
    }

    private void refreshTableData() throws SQLException {
        booksTable.getItems().clear();
        booksTable.getItems().addAll(db.readBooks());
    }

    @FXML
    public void deleteOnClick(ActionEvent actionEvent) {
        Konyv selected = booksTable.getSelectionModel().getSelectedItem();

        if (selected== null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Válasszon ki könyvet");
            alert.showAndWait();
            return;
        }{
            Alert confirm= new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setHeaderText("Biztos vagy benne?");
            Optional<ButtonType> optiolanButtonType=confirm.showAndWait();
            if (optiolanButtonType.isPresent() && optiolanButtonType.get().equals(ButtonType.OK)){
                deleteBook(selected);
            }
        }
    }

    private void deleteBook(Konyv selected) {
        try {
            if (db.deleteBook(selected)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Sikeres törlés");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Törlés nem sikerült");
                alert.showAndWait();
            }
            refreshTableData();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Hiba a törléssel");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            Platform.exit();
        }
    }
}