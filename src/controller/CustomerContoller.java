package controller;

import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.CustomerTM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class CustomerContoller {
    public TableView<CustomerTM> tblCustomerDetails;
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public Button btnSave;
    public Button btnDelete;
    public Button btnAddNewCustomer;
    public Button btnCusHOME;
    public AnchorPane root;
    static ArrayList<CustomerTM> customerDB = new ArrayList();

    static{
        customerDB.add(new CustomerTM("C001","Asanka","panadura"));
        customerDB.add(new CustomerTM("C002","Nimal","Colombo"));
        customerDB.add(new CustomerTM("C003","kamal","Kandy"));
    }


    public void initialize() {


        ObservableList<CustomerTM> customers = FXCollections.observableList(customerDB);
        tblCustomerDetails.setItems(customers);

        tblCustomerDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomerDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomerDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));


        btnDelete.setDisable(true);
        btnSave.setDisable(true);
        txtCustomerAddress.setDisable(true);
        txtCustomerName.setDisable(true);
        txtCustomerId.setEditable(false);

//        ObservableList<CustomerTM> customers = tblCustomerDetails.getItems();
        //customers.add(new CustomerTM("C001","Gaka","Kurunagala"));

        // Let's handle selection



        tblCustomerDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomerTM>() {
            @Override
            public void changed(ObservableValue<? extends CustomerTM> observable, CustomerTM oldValue, CustomerTM newValue) {
                int selectedIndex = tblCustomerDetails.getSelectionModel().getSelectedIndex();
                if (selectedIndex == -1) {
                    txtCustomerName.requestFocus();
                    txtCustomerAddress.clear();
                    txtCustomerName.clear();
                    btnDelete.setDisable(true);
                    return;
                }
                CustomerTM selectedCustomer = tblCustomerDetails.getSelectionModel().getSelectedItem();
                txtCustomerId.setText(selectedCustomer.getId());
                txtCustomerName.setText(selectedCustomer.getName());
                txtCustomerAddress.setText(selectedCustomer.getAddress());

                btnSave.setText("Update");
                btnSave.setDisable(false);
                btnDelete.setDisable(false);
            }
        });





    }

    public void btnSaveOnAction(ActionEvent event) {

        if (btnSave.getText().equals("Save")) {
            if (txtCustomerName.getText().trim().length() == 0 || txtCustomerAddress.getText().trim().length() == 0) {
                new Alert(Alert.AlertType.ERROR, "Fields can not be empty !").showAndWait();
            } else {
                ObservableList<CustomerTM> customers = tblCustomerDetails.getItems();
                customers.add(new CustomerTM(txtCustomerId.getText(), txtCustomerName.getText(), txtCustomerAddress.getText()));
            }
        } else {
            ObservableList<CustomerTM> customers = tblCustomerDetails.getItems();
            int selectedIndex = tblCustomerDetails.getSelectionModel().getSelectedIndex();
            customers.get(selectedIndex).setName(txtCustomerName.getText());
            customers.get(selectedIndex).setAddress(txtCustomerAddress.getText());
            tblCustomerDetails.getSelectionModel().clearSelection();
            tblCustomerDetails.refresh();
            txtCustomerAddress.setDisable(false);
            txtCustomerName.setDisable(false);
            btnSave.setText("Save");
            btnSave.setDisable(true);
            btnAddNewCustomer.requestFocus();
        }

        txtCustomerAddress.clear();
        txtCustomerId.clear();
        txtCustomerName.clear();
        btnAddNewCustomer.requestFocus();

    }

    public void btnDeleteOnAction(ActionEvent event) {

        int selectedIndex = tblCustomerDetails.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            new Alert(Alert.AlertType.ERROR, "Please Select Row").showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete..", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = confirm.showAndWait();
        if (buttonType.get() == ButtonType.YES) {

            ObservableList<CustomerTM> customers = tblCustomerDetails.getItems();
            customers.remove(selectedIndex);
            if (customers.size() == 0) {
                txtCustomerName.setDisable(true);
                txtCustomerAddress.setDisable(true);
                btnSave.setDisable(true);
                btnDelete.setDisable(true);
                btnAddNewCustomer.requestFocus();
            }
            txtCustomerId.clear();
            txtCustomerName.clear();
            txtCustomerAddress.clear();
            btnSave.setText("Save");
            tblCustomerDetails.getSelectionModel().clearSelection();
        }

    }

    public void btnAddOnAction(ActionEvent event) {
        tblCustomerDetails.getSelectionModel().clearSelection();
        btnSave.setText("Save");
        // Todo: Generate ID
        ObservableList<CustomerTM> customers = tblCustomerDetails.getItems();

        if (customers.size() == 0) {
            txtCustomerId.setText("C001");
        } else {
            // Todo: Generate ID
            CustomerTM lastCustomer = customers.get(customers.size() - 1);
            String lastId = lastCustomer.getId();
            String number = lastId.substring(1, 4);
            int newID = Integer.parseInt(number) + 1;
            if (newID < 10) {
                txtCustomerId.setText("C00" + newID);
            } else if (newID < 100) {
                txtCustomerId.setText("C0" + newID);
            } else {
                txtCustomerId.setText("C" + newID);
            }
        }

        txtCustomerName.setDisable(false);
        txtCustomerAddress.setDisable(false);
        btnSave.setDisable(false);

    }

    public void btnCusHOME_OnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/WelcomeMenu.fxml"));
        Scene itemScene = new Scene(root);
        Stage newStage = (Stage) (this.root.getScene().getWindow());
        newStage.setScene(itemScene);
        newStage.centerOnScreen();
        newStage.show();
    }
}
