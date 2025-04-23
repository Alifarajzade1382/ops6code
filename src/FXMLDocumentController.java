import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

import model.Book;
import model.Member;
import model.Library;

public class FXMLDocumentController implements Initializable {

    private Library library = new Library();

    // بخش افزودن کتاب
    @FXML private TextField bookIdField;
    @FXML private TextField bookTitleField;
    @FXML private TextField bookAuthorField;
    @FXML private Label bookMessageLabel;

    // بخش افزودن عضو
    @FXML private TextField memberIdField;
    @FXML private TextField memberNameField;
    @FXML private Label memberMessageLabel;

    // جدول کتاب‌ها
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> borrowedColumn;

    // امانت دادن
    @FXML private ComboBox<Book> bookCombo;
    @FXML private ComboBox<Member> memberCombo;
    @FXML private Label borrowMessageLabel;

    // جستجو
    @FXML private TextField searchField;
    @FXML private TableView<Book> searchTable;
    @FXML private TableColumn<Book, String> searchIdColumn;
    @FXML private TableColumn<Book, String> searchTitleColumn;
    @FXML private TableColumn<Book, String> searchAuthorColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        library.loadBooksFromFile("books.txt");
        library.loadMembersFromFile("members.txt");

        // مقداردهی ستون‌های جدول کتاب
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        // اصلاح برای ستون borrowed
        borrowedColumn.setCellValueFactory(cellData -> {
            boolean isBorrowed = cellData.getValue().isBorrowed(); // فرض بر این است که متد isBorrowed() در کلاس Book وجود دارد
            return new SimpleStringProperty(isBorrowed ? "امانت داده شده" : "در دسترس");
        });

        // مقداردهی جدول جستجو
        searchIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        searchTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        searchAuthorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        // مقداردهی ComboBox‌ها
        bookCombo.setItems(FXCollections.observableArrayList(library.getBooks()));
        memberCombo.setItems(FXCollections.observableArrayList(library.getMembers()));

        refreshTables();
    }

    private void refreshTables() {
        booksTable.setItems(FXCollections.observableArrayList(library.getBooks()));
        bookCombo.setItems(FXCollections.observableArrayList(library.getBooks()));
        memberCombo.setItems(FXCollections.observableArrayList(library.getMembers()));
    }

    @FXML
    private void addBook(ActionEvent event) {
        String id = bookIdField.getText();
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();

        if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
            bookMessageLabel.setText("لطفاً تمام فیلدها را پر کنید.");
            return;
        }

        Book book = new Book(id, title, author);
        library.addBook(book);
        refreshTables();
        bookMessageLabel.setText("✅ کتاب اضافه شد.");

        library.saveBooksToFile("books.txt");

        bookIdField.clear();
        bookTitleField.clear();
        bookAuthorField.clear();
    }

    @FXML
    private void addMember(ActionEvent event) {
        String id = memberIdField.getText();
        String name = memberNameField.getText();

        if (id.isEmpty() || name.isEmpty()) {
            memberMessageLabel.setText("لطفاً تمام فیلدها را پر کنید.");
            return;
        }

        Member member = new Member(id, name);
        library.addMember(member);
        refreshTables();
        memberMessageLabel.setText("✅ عضو اضافه شد.");

        library.saveMembersToFile("members.txt");

        memberIdField.clear();
        memberNameField.clear();
    }

    @FXML
    private void borrowBook(ActionEvent event) {
        Book book = bookCombo.getValue();
        Member member = memberCombo.getValue();

        if (book == null || member == null) {
            borrowMessageLabel.setText("لطفاً کتاب و عضو را انتخاب کنید.");
            return;
        }

        if (library.borrowBook(book, member)) {
            borrowMessageLabel.setText("✅ کتاب امانت داده شد.");
            refreshTables();

            library.saveBooksToFile("books.txt");

        } else {
            borrowMessageLabel.setText("❌ کتاب قبلاً امانت داده شده.");
        }
    }

    @FXML
    private void searchBooks(ActionEvent event) {
        String keyword = searchField.getText();
        if (keyword.isEmpty()) return;
        searchTable.setItems(FXCollections.observableArrayList(library.searchBooks(keyword)));
    }
}


