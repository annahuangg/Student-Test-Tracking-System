package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Login {
	final int MAX_LOGIN = 3;
	String LoginMsg;
	String pwdToMatch = "admin";
	int loginCounter;
	boolean passwordMatched;

	public Login(String message) {
		LoginMsg = message;
		loginCounter = 0;
		passwordMatched = false;
	}

	public int show() {
		Stage popupwindow = new Stage();
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Login");
		Label lblMessage = new Label(LoginMsg);
		lblMessage.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
		lblMessage.setMinWidth(350);
		lblMessage.setMaxWidth(350);
		lblMessage.setTranslateX(70);
		lblMessage.setAlignment(Pos.CENTER);

		VBox layout = new VBox(30);
		GridPane pane = new GridPane();
		pane.setVgap(20);
		layout.getChildren().add(pane);

		Label msg = new Label("3 password try");
		msg.setMinWidth(130);
		msg.setMaxWidth(130);
		msg.setTranslateX(220);
		msg.setTranslateY(-20);

		Label lblName = new Label("Name ");
		Label lblPswd = new Label("Password ");
		TextField txtName = new TextField();
		PasswordField txtPswd = new PasswordField();
		txtPswd.setOnAction(e -> {
			if (txtName.getText().trim().length() == 0) {
				txtName.setText("Please enter user name");
			} else {
				passwordCheck(txtPswd.getText());
				if (passwordMatched || loginCounter > 2)
					popupwindow.close();
				else
					msg.setText(Integer.toString(MAX_LOGIN - loginCounter) + " password try left");
			}
		});
		Button btnLogin = new Button("Login");
		btnLogin.setTranslateX(230);
		btnLogin.setTranslateY(-30);
		btnLogin.setOnAction(e -> {
			if (txtName.getText().trim().length() == 0) {
				txtName.setText("Please enter user name");
			} else {
				passwordCheck(txtPswd.getText());
				if (passwordMatched || loginCounter > 2)
					popupwindow.close();
				else
					msg.setText(Integer.toString(MAX_LOGIN - loginCounter) + " password try left");
			}
		});

		lblName.setMinWidth(130);
		lblName.setMaxWidth(130);
		lblName.setTranslateX(60);
		lblName.setAlignment(Pos.CENTER_RIGHT);
		lblPswd.setMinWidth(130);
		lblPswd.setMaxWidth(130);
		lblPswd.setTranslateX(60);
		lblPswd.setAlignment(Pos.CENTER_RIGHT);

		txtName.setMinWidth(150);
		txtName.setMaxWidth(150);
		txtName.setTranslateX(-150);
		txtPswd.setMinWidth(150);
		txtPswd.setMaxWidth(150);
		txtPswd.setTranslateX(-150);

		lblPswd.setTranslateY(-10);
		txtPswd.setTranslateY(-10);

		pane.addRow(0, lblMessage);
		pane.addRow(1, lblName, txtName);
		pane.addRow(2, lblPswd, txtPswd);
		pane.addRow(3, msg);
		pane.addRow(4, btnLogin);

		layout.setAlignment(Pos.CENTER);

		Scene scene1 = new Scene(layout, 350, 200);
		popupwindow.setScene(scene1);
		popupwindow.showAndWait();
		return loginCounter;
	}

	private int passwordCheck(String pwd) {
		if (pwd.compareTo(pwdToMatch) != 0)
			loginCounter++;
		else
			passwordMatched = true;

		return loginCounter;
	}

	public boolean isPasswordmatched() {
		return passwordMatched;
	}
}
