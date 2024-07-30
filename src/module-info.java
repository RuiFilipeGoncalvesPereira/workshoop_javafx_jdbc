module Work_javafx_jdbc {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	opens gui to javafx.graphics,  javafx.fxml;
	opens model.entities to javafx.graphics,  javafx.fxml, javafx.base, javafx.controls;
	opens model.services to javafx.graphics,  javafx.fxml, javafx.base, javafx.controls;
	opens gui.util to javafx.graphics,  javafx.fxml, javafx.base, javafx.controls;
	
    exports gui;
    exports model.entities;
    exports model.services;
    exports gui.util;
}
