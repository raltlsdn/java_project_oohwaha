package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Connector;
import application.Main;
import application.SceneLoader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import network_dto.NetworkData;
import vo.Member;

public class JoinController implements Initializable {

	@FXML
	private AnchorPane Anchor;
	@FXML
	private ToggleGroup group;
	@FXML
	private TextField id, name, age;
	@FXML
	private Button idcheck, back, btnreg;
	@FXML
	private PasswordField password, passwordchk;
	@FXML
	private ComboBox<String> comboPublic1, comboPublic2;
	@FXML
	private BorderPane borderPane;

	boolean isOwner = false;
	public static Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				RadioButton value = (RadioButton) newValue;
				String text = value.getText();
				System.out.println(text);
				if (text.equals("μΌλ°νμ")) {
					isOwner = false;
				} else {
					isOwner = true;
				}
			}
		}); // END ToggleGroup

		btnreg.setOnAction(e -> {

			// TextField
			String ID = id.getText();
			String pass = password.getText();
			String passchk = passwordchk.getText();
			String namee = name.getText();
			String agee = age.getText();

			// combobox
			String comboArea = comboPublic1.getValue();
			String comboArea2 = comboPublic2.getValue();

			// μμ΄λ μλ ₯ κ²μ¬
			if (ID.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("μμ΄λλ₯Ό μλ ₯ν΄ μ£ΌμΈμ.");
				alert.show();
				return;
			}
			// λΉλ°λ²νΈ μλ ₯ κ²μ¬
			if (pass.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("λΉλ°λ²νΈλ₯Ό μλ ₯ν΄ μ£ΌμΈμ.");
				alert.show();
				return;
			} else if (passchk.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("λΉλ°λ²νΈ νμΈλμ μλ ₯ν΄ μ£ΌμΈμ.");
				alert.show();
				return;
			} else if (!pass.equals(passchk)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("λΉλ°λ²νΈκ° μΌμΉνμ§μμ΅λλ€.");
				alert.show();
				return;
			}
			// μ΄λ¦ μλ ₯ κ²μ¬
			if (namee.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("μ΄λ¦μ μλ ₯ν΄ μ£ΌμΈμ.");
				alert.show();
				return;
			}
			// λμ΄ μλ ₯ κ²μ¬
			if (agee.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("λμ΄λ₯Ό μλ ₯ν΄ μ£ΌμΈμ.");
				alert.show();
				return;
			}
			// κ±°μ£Όμ§μ­ μλ ₯ κ²μ¬
			if (comboPublic2.getValue() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("κ±°μ£Όμ§μ­μ μ νν΄ μ£ΌμΈμ.");
				alert.show();
				return;
			}

			Connector.send(new NetworkData<Member>("member/join",
					new Member(ID, pass, namee, Integer.parseInt(agee), comboArea + " " + comboArea2, isOwner)));

			Platform.runLater(() -> {
				Scene scene = new Scene((BorderPane) Main.sceneLoader.load(SceneLoader.MAIN_PATH));
				System.out.println(stage);
				stage = MainController.stage;
				stage.setScene(scene);
				stage.show();
			});
		});

		// μμ΄λ μ€λ³΅ μ²΄ν¬
		idcheck.setOnAction(event -> {
			handlePopup();
		});

		// μ΄μ  λ²νΌ μ€ν
		back.setOnAction(e -> {
			Scene scene = new Scene((BorderPane) Main.sceneLoader.load(SceneLoader.MAIN_PATH));
			System.out.println(MainController.stage);
			MainController.stage.setScene(scene);
		});

		comboPublic1.getSelectionModel().selectFirst();

	}// initialize END

	public void idcheck() {

		Connector.send(new NetworkData<Member>("member/find", new Member(id.getText())));

	}

	public void handlePopup() {

		Alert alert = new Alert(AlertType.ERROR);

		if (alert.isShowing()) {
			alert.hide();
		} else if (id.getText() == null || id.getText().equals("")) {

//        	final Window window = idcheck.getScene().getWindow();
			alert.setHeaderText("μμ΄λλ₯Ό μλ ₯ν΄μ£ΌμΈμ.");
			alert.show();
		} else {
			idcheck();
		}
	}

}
