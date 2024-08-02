package gui;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import model.services.DepartementService;
import model.services.SellerService;

public class MainView_Controller implements Serializable {

	private static final long serialVersionUID = 1L;

	@FXML
    private MenuItem menuItemAbout;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemSeller;

    @FXML
    public void onMenuItemAboutAction(@SuppressWarnings("exports") ActionEvent event) {
         loadView("/gui/About.fxml", x -> {});
    }

    @FXML
    public void onMenuItemDepartmmentAction(@SuppressWarnings("exports") ActionEvent event) {
    	loadView("/gui/DepartmentList.fxml",(DepartmentListController controller) -> {	
    		controller.setDepartmentService(new DepartementService());
    		controller.updateTableView(); 	
    	});
    }

    @FXML
    public void onMenuItemSellerAction(@SuppressWarnings("exports") ActionEvent event) {
    	loadView("/gui/SellerList.fxml",(SellerListController controller) -> {	
    		controller.setSellerService(new SellerService());
    		controller.updateTableView(); 	
    	});
    }
    
    public void initialize(URL uri, ResourceBundle rb)
    {
    	
    }
    
    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction)
    {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
    		VBox newVBox = loader.load();
    		Scene mainScene = Main.getMainScene();
    		VBox  mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
    		
    		Node mainMenu = mainVBox.getChildren().get(0);
    		mainVBox.getChildren().clear();
    		mainVBox.getChildren().add(mainMenu);
    		mainVBox.getChildren().addAll(newVBox.getChildren());
    		
    		T controller  = loader.getController();
    		initializingAction.accept(controller);
 
    	}
    	
        catch(IOException e)
    	{
        	Alerts.showAlert("IO Exception" , "Error loading view", e.getMessage(), AlertType.ERROR);
    	}
    }
 }
