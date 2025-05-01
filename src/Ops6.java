import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Library; // اضافه کردن کلاس Library برای استفاده از متدهای ذخیره و بارگذاری کتاب‌ها و اعضا

import java.net.URL;

public class Ops6 extends Application {

    // ایجاد شیء از کلاس Library برای بارگذاری و ذخیره داده‌ها
    private Library library = new Library();

    @Override
    public void start(Stage stage) throws Exception {
        // چک کردن مسیر فایل FXML برای Login.fxml
        URL fxmlLocation = getClass().getResource("/login.fxml");
        if (fxmlLocation == null) {
            System.out.println("Login.fxml not found!");
        } else {
            Parent root = FXMLLoader.load(fxmlLocation);  // بارگذاری فایل FXML

            // بارگذاری داده‌ها از فایل‌های txt
            library.loadBooksFromFile("books.txt");
            library.loadMembersFromFile("members.txt");

            Scene scene = new Scene(root);
            stage.setTitle("ورود به سیستم مدیریت کتابخانه");
            stage.setScene(scene);
            stage.show();

            // ذخیره داده‌ها به فایل‌های txt هنگام بسته شدن برنامه
//            stage.setOnCloseRequest(event -> {   
//                library.saveBooksToFile("books.txt");
//                library.saveMembersToFile("members.txt");
//            });
        }
    }

    // متد main که به طور خودکار برنامه را راه‌اندازی می‌کند
    public static void main(String[] args) {
        launch(args);
    }
}
