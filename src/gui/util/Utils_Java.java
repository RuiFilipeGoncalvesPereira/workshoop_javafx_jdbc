package gui.util;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Utils_Java {
	
	public static Stage currentStage(ActionEvent event)
	{
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

}
