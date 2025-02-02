package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;


import db.DbException;
import gui.listeners.DataChangedListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils_Java;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartementService;
import model.services.SellerService;

public class SellerFormController  implements Initializable{
	
	 private Seller entity;
	 
	 private SellerService service;
	 
	 private DepartementService departmentService;
	 
	 private List<DataChangedListener> dataChangeListeners = new ArrayList<>();

	 @FXML
	 private TextField txtId;
	 
	 @FXML
	 private TextField txtName;
	 
	 @FXML
	 private TextField txtEmail;
	 
	 @FXML
	 private DatePicker dpBirthDate;
	 
	 @FXML
	 private TextField txtbaseSalary;
	 
	 @FXML
	 private ComboBox<Department> comboBoxDepartment;
	 
	 @FXML
	 private Label labelErrorName;
	 
	 @FXML
	 private Label labelErrorEmail;
	 
	 @FXML
	 private Label labelErrorBirthDate;
	 
	 @FXML
	 private Label labelErrorBaseSalary;
	 
	 @FXML
	 private Button btSave;
	 
	 @FXML
	 private Button btCancel;
	 
	 @FXML
	 private ObservableList<Department> obsList;
	 
	 public void setSeller(Seller entity)
	 {
		 this.entity = entity;
	 }
	 
	 public void setServices(SellerService service, DepartementService departmentSeervice)
	 {
		 this.service = service;
		 this.departmentService = departmentSeervice;
	 }
	 
	 public void subscribeDataChangListener(@SuppressWarnings("exports") DataChangedListener listener)
	 {
		 dataChangeListeners.add(listener);
	 }
	 
	 @FXML
	 public void onBtSaveAction(@SuppressWarnings("exports") ActionEvent event)
	 {
		 if(entity == null)
		 {
			 throw new IllegalStateException("Entity was null");
		 }
		 if(service == null)
		 {
			 throw new IllegalStateException("Service was null");
		 }
		 try {
			 entity = getFormData();
			 service.saveOrUpdate(entity);
			 notifyDataChangeListeners();
			 Utils_Java.currentStage(event).close();
		 }
		 catch (ValidationException e)
		 {
			 setErrorMessages(e.getErrors());
		 }
		 catch(DbException e)
		 {
			 Alerts.showAlert("Error saving object",null, e.getMessage(), AlertType.ERROR);
		 }
	 }
	 
	 private void notifyDataChangeListeners() {
		// TODO Auto-generated method stub
		for(DataChangedListener listener : dataChangeListeners)
		{
			listener.onDataChanged();
		}
	}

	private Seller getFormData() {
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation eror!");
				 
		obj.setId(Utils_Java.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals(""))
		{
			exception.addError("name", "field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if(txtEmail.getText() == null || txtEmail.getText().trim().equals(""))
		{
			exception.addError("email", "field can't be empty");
		}
		obj.setEmail(txtEmail.getText());
        
		if (dpBirthDate.getValue() == null)
		{
			exception.addError("birthDate","field can't be empty");
		}
		else
		{
        Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
        obj.setBirthDate(Date.from(instant));
		}
        
    	if(txtbaseSalary.getText() == null || txtbaseSalary.getText().trim().equals(""))
		{
			exception.addError("baseSalary", "field can't be empty");
		}
    	obj.setBaseSalary(Utils_Java.tryParseToDouble(txtbaseSalary.getText()));
    	
    	obj.setDepartment(comboBoxDepartment.getValue());
		
		if (exception.getErrors().size() > 0 )
		{		
		  throw exception;	
		}
		
		return obj;
	}

	@FXML
	 public void onBtCancelAction(@SuppressWarnings("exports") ActionEvent event)
	 {
		Utils_Java.currentStage(event).close();
	 }
	 
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializaNodes();
	}	 
	
	private void initializaNodes()
	{
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtbaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail,60);
		Utils_Java.formatDatePicker(dpBirthDate,"dd/MM/yyyy");
		initializeComboBoxDepartment();
	}
	
	public void updateFormData()
	{
		if(entity == null)
		{
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtbaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if(entity.getBirthDate() != null)
		{
		  dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(),ZoneId.systemDefault()));
		}
		if(entity.getDepartment() == null)
		{
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else
		{
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}
	
	public void loadAssociateObject()
	{
		if(departmentService == null)
		{
			throw new IllegalStateException("DepartmentService was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}
	
	private void setErrorMessages(Map<String, String> errors)
	{
		Set<String> fields = errors.keySet();
			
		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
		labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));
		
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
	
}
