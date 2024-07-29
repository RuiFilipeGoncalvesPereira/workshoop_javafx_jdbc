package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MainView_Controller {

    @SuppressWarnings("exports")
	@FXML
    public MenuItem menuItemAbout;

    @SuppressWarnings("exports")
	@FXML
    public MenuItem menuItemDepartment;

    @SuppressWarnings("exports")
	@FXML
    public MenuItem menuItemSeller;

    @FXML
    public void onMenuItemAboutAction(@SuppressWarnings("exports") ActionEvent event) {
          System.out.println("onMenuItemAboutAction");
    }

    @FXML
    public void onMenuItemDepartmmentAction(@SuppressWarnings("exports") ActionEvent event) {
    	System.out.println("onMenuItemDepartmmentAction");
    }

    @FXML
    public void onMenuItemSellerAction(@SuppressWarnings("exports") ActionEvent event) {
    	System.out.println("onMenuItemSellerAction");
    }

}
