package yourpackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.net.URL;

public class LoginController {

    @FXML
    private void handleMemberLogin(ActionEvent event) {
        try {
            // چک کردن مسیر فایل FXML برای LibraryMember
            URL fxmlLocation = getClass().getResource("/LibraryMember.fxml");
            if (fxmlLocation == null) {
                System.out.println("LibraryMember.fxml not found!");
            } else {
                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("ورود عضو - سیستم کتابخانه");
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLibrarianLogin(ActionEvent event) {
        try {
            // چک کردن مسیر فایل FXML برای LibraryAdmin
            URL fxmlLocation = getClass().getResource("/LibraryAdmin.fxml");
            if (fxmlLocation == null) {
                System.out.println("LibraryAdmin.fxml not found!");
            } else {
                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("ورود کتابدار - سیستم کتابخانه");
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBackToLogin(ActionEvent event) {
        try {
            // چک کردن مسیر فایل FXML برای صفحه ورود
            URL fxmlLocation = getClass().getResource("/Login.fxml");
            if (fxmlLocation == null) {
                System.out.println("Login.fxml not found!");
            } else {
                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("ورود - سیستم کتابخانه");
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
