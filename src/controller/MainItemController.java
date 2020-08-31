package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import util.ItemTM;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;

public class MainItemController {
    public TableView<ItemTM> tblItemDetails;
    public TextField txtItemCode;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public Button btnSaveItem;
    public Button btnDeleteItem;
    public Button btnAddNewItem;
    public TextField txtUnitPrice;
    public Label lblQtyDigit;
    public Label lblUnitDigit;
    public Button btnCusHome;
    public AnchorPane root;
static ArrayList<ItemTM> itemDB=new ArrayList<>();
static {
    itemDB.add(new ItemTM("S001","Soap",3,40));
    itemDB.add(new ItemTM("S002","pen",8,50));
    itemDB.add(new ItemTM("S003","Soap2",6,70));
}

    public void initialize(){
        ObservableList<ItemTM> items = FXCollections.observableList(itemDB);
        tblItemDetails.setItems(items);

        tblItemDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblItemDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItemDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        tblItemDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));


        btnDeleteItem.setDisable(true);
        btnSaveItem.setDisable(true);
        txtDescription.setDisable(true);
        txtQtyOnHand.setDisable(true);
        txtUnitPrice.setDisable(true);
        txtItemCode.setEditable(false);
        lblQtyDigit.setVisible(false);
        lblUnitDigit.setVisible(false);

        tblItemDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemTM>() {
            @Override
            public void changed(ObservableValue<? extends ItemTM> observable, ItemTM oldValue, ItemTM newValue) {
                int selectedIndex = tblItemDetails.getSelectionModel().getSelectedIndex();
                if(selectedIndex == -1){
                    return;
                }
                ItemTM selectedItem = tblItemDetails.getSelectionModel().getSelectedItem();
                txtItemCode.setText(selectedItem.getItemCode());
                txtDescription.setText(selectedItem.getDescription());
                txtQtyOnHand.setText(selectedItem.getQtyOnHand()+"");
                txtUnitPrice.setText(selectedItem.getUnitPrice()+"");
                btnSaveItem.setText("Update");
                btnSaveItem.setDisable(false);
                btnDeleteItem.setDisable(false);
            }
        });
    }
    public void btnSaveItemOnAction(ActionEvent event) {
        String qty = txtQtyOnHand.getText();
        String price = txtUnitPrice.getText();

        if((qty.matches("^[0-9]*$") && qty.length() > 0)){
            lblQtyDigit.setVisible(false);
            txtQtyOnHand.setStyle("-fx-border-color: default");
            if((price.matches("^[0-9]*$") && price.length() > 0)){
                txtUnitPrice.setStyle("-fx-border-color: default");
                lblUnitDigit.setVisible(false);
                if(btnSaveItem.getText().equals("Save")) {
                    if (txtDescription.getText().trim().length() == 0 || txtQtyOnHand.getText().trim().length() == 0 || txtUnitPrice.getText().trim().length() == 0) {
                        new Alert(Alert.AlertType.ERROR, "Fields Can Not Be Empty...").showAndWait();
                    } else {
                        ObservableList<ItemTM> items = tblItemDetails.getItems();
                        items.add(new ItemTM(txtItemCode.getText(), txtDescription.getText(), Integer.parseInt(txtQtyOnHand.getText()), Double.parseDouble(txtUnitPrice.getText())));
                    }

                }else{
                    ObservableList<ItemTM> items = tblItemDetails.getItems();
                    int selectedIndex = tblItemDetails.getSelectionModel().getSelectedIndex();
                    items.get(selectedIndex).setDescription(txtDescription.getText());
                    items.get(selectedIndex).setQtyOnHand(Integer.parseInt(txtQtyOnHand.getText()));
                    items.get(selectedIndex).setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));
                    tblItemDetails.refresh();
                    tblItemDetails.getSelectionModel().clearSelection();
                }
                txtUnitPrice.clear();
                txtUnitPrice.setDisable(false);
                txtQtyOnHand.clear();
                txtQtyOnHand.setDisable(false);
                txtDescription.clear();
                txtDescription.setDisable(false);
                txtItemCode.clear();
                btnAddNewItem.requestFocus();
                btnSaveItem.setDisable(true);
            }else{
                new Alert(Alert.AlertType.ERROR,"Something Went Wrong...",ButtonType.OK).showAndWait();
                txtUnitPrice.setStyle("-fx-border-color: #ff0000");
                lblUnitDigit.setVisible(true);
                txtUnitPrice.clear();
                txtUnitPrice.requestFocus();
            }
        }else {
            new Alert(Alert.AlertType.ERROR,"Something Went Wrong...",ButtonType.OK).showAndWait();
            txtQtyOnHand.setStyle("-fx-border-color: #ff0000");
            lblQtyDigit.setVisible(true);
            txtQtyOnHand.clear();
            txtQtyOnHand.requestFocus();
        }


    }

    public void btnDeleteItemOnAction(ActionEvent event) {
        ObservableList<ItemTM> items = tblItemDetails.getItems();
        int selectedIndex = tblItemDetails.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1){
            new Alert(Alert.AlertType.ERROR,"Select Row First...").showAndWait();
            btnSaveItem.setDisable(true);
            btnDeleteItem.setDisable(true);
            return;
        }
        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.ERROR, "Are you sure...", ButtonType.YES, ButtonType.NO).showAndWait();

        if(buttonType.get() == ButtonType.YES){
            items.remove(selectedIndex);
            tblItemDetails.getSelectionModel().clearSelection();
            txtItemCode.clear();
            txtUnitPrice.clear();
            txtQtyOnHand.clear();
            txtDescription.clear();
            btnSaveItem.setDisable(true);
        }


    }

    public void btnAddItemOnAction(ActionEvent event) {


        ObservableList<ItemTM> items = tblItemDetails.getItems();

        if(items.size()==0){
            txtItemCode.setText("I001");
        }else{
            ItemTM lastItem = items.get(items.size() - 1);
            String lastItemCode = lastItem.getItemCode();
            String last = lastItemCode.substring(1,4);
            int number = Integer.parseInt(last) + 1;
            if(number < 10){
                txtItemCode.setText("I00"+number);
            }else if(number < 100){
                txtItemCode.setText("I0"+number);
            }else{
                txtItemCode.setText("I"+number);
            }

        }
        txtUnitPrice.setDisable(false);
        txtQtyOnHand.setDisable(false);
        txtDescription.setDisable(false);
        txtDescription.requestFocus();
        btnSaveItem.setDisable(false);
        btnDeleteItem.setDisable(true);
        btnSaveItem.setText("Save");
    }

    public void txtUnitOnPressed(KeyEvent keyEvent) {

    }


    public void txtQtyOnPressed(KeyEvent keyEvent) {

    }

    public void btnAddOnMouseEnterd(MouseEvent mouseEvent) {
        btnAddNewItem.setStyle("-fx-background-color: #3679ff");
    }

    public void btnAddOnMouseExit(MouseEvent mouseEvent) {
        btnAddNewItem.setStyle("-fx-background-color:  #93d4fa");
    }

    public void btnSaveOnMouseEnterd(MouseEvent mouseEvent) {
        btnSaveItem.setStyle("-fx-background-color: #65ff36");
    }

    public void btnSaveOnMouseExit(MouseEvent mouseEvent) {
        btnSaveItem.setStyle("-fx-background-color:  #84fa9a");
    }

    public void btnDeleteOnMouseEnterd(MouseEvent mouseEvent) {
        btnDeleteItem.setStyle("-fx-background-color: #bf0000");
    }

    public void btnDeleteOnMouseExit(MouseEvent mouseEvent) {
        btnDeleteItem.setStyle("-fx-background-color:  #fa8484");
    }

    public void btnAddOnMouseClicked(MouseEvent mouseEvent) {
        btnAddNewItem.setStyle("-fx-background-color: #c2c6cf");
    }

    public void btnAddOnMouseRelesed(MouseEvent mouseEvent) {
        btnAddNewItem.setStyle("-fx-background-color:  #3679ff");
    }

    public void btnCusHome_OnAction(ActionEvent actionEvent) throws IOException {
        Parent root= FXMLLoader.load(this.getClass().getResource("/view/WelcomeMenu.fxml"));
        Scene itemScene = new Scene(root);
        Stage newStage = (Stage)(this.root.getScene().getWindow());
        newStage.setScene(itemScene);
        newStage.centerOnScreen();
        newStage.show();
    }
}
