import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.Member;
import model.Library;

public class FXMLDocumentController implements Initializable {

    private Library library = new Library();

    // بخش افزودن کتاب
    @FXML private TextField bookTitleField;
    @FXML private TextField bookAuthorField;
    @FXML private Label bookMessageLabel;

    // بخش افزودن عضو
    @FXML private TextField memberIdField;
    @FXML private TextField memberNameField;
    @FXML private Label memberMessageLabel;

    // جدول کتاب‌ها
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, Boolean> borrowedColumn;

    // امانت دادن کتاب
    @FXML private TableView<Book> borrowBooksTable;
    @FXML private TableColumn<Book, Integer> borrowBookIdColumn;
    @FXML private TableColumn<Book, String> borrowBookTitleColumn;
    @FXML private TableColumn<Book, String> borrowBookAuthorColumn;

    @FXML private TableView<Member> borrowMembersTable;
    @FXML private TableColumn<Member, Integer> borrowMemberIdColumn;
    @FXML private TableColumn<Member, String> borrowMemberNameColumn;

    @FXML private DatePicker borrowDatePicker;
    @FXML private DatePicker returnDatePicker;
    @FXML private Label borrowMessageLabel;

    // جستجو
    @FXML private TextField searchField;
    @FXML private TableView<Book> searchTable;
    @FXML private TableColumn<Book, Integer> searchIdColumn;
    @FXML private TableColumn<Book, String> searchTitleColumn;
    @FXML private TableColumn<Book, String> searchAuthorColumn;

    // فیلدهای جستجو در بخش امانت دادن کتاب
    @FXML private TextField searchBorrowBookField;
    @FXML private TextField searchBorrowMemberField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            library.loadBooksFromFile("books.txt");
            library.loadMembersFromFile("members.txt");
            library.loadReturnDatesFromFile("dates.txt");
        } catch (Exception e) {
            bookMessageLabel.setText("خطا در بارگذاری داده‌ها: " + e.getMessage());
            memberMessageLabel.setText("خطا در بارگذاری داده‌ها: " + e.getMessage());
            System.err.println("خطا در initialize: " + e.getMessage());
        }

        // مقداردهی ستون‌های جدول کتاب‌ها
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        borrowedColumn.setCellValueFactory(new PropertyValueFactory<>("borrowed"));
        borrowedColumn.setCellFactory(column -> new TableCell<Book, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "امانت داده شده" : "در دسترس");
                }
            }
        });

        // مقداردهی جدول جستجو
        searchIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        searchTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        searchAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        // مقداردهی جدول امانت کتاب‌ها
        borrowBookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        borrowBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowBookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        // مقداردهی جدول امانت اعضا
        borrowMemberIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        borrowMemberNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        refreshTables();

        // افزودن listener برای جستجوی real-time
        searchField.textProperty().addListener((obs, oldVal, newVal) -> searchBooks(null));
        searchBorrowBookField.textProperty().addListener((obs, oldVal, newVal) -> searchBorrowBooks(null));
        searchBorrowMemberField.textProperty().addListener((obs, oldVal, newVal) -> searchBorrowMembers(null));
    }

    private void refreshTables() {
        ObservableList<Book> books = FXCollections.observableArrayList(library.getBooks());
        ObservableList<Member> members = FXCollections.observableArrayList(library.getMembers());

        booksTable.setItems(books);
        borrowBooksTable.setItems(books);
        borrowMembersTable.setItems(members);
        searchTable.setItems(books);
    }

    @FXML
    private void addBook(ActionEvent event) {
        String title = bookTitleField.getText().trim();
        String author = bookAuthorField.getText().trim();

        if (title.isEmpty() || author.isEmpty()) {
            bookMessageLabel.setText("لطفاً تمام فیلدها را پر کنید.");
            return;
        }

        // تولید ID با بررسی حداکثر ID موجود
        int newId = library.getBooks().stream()
                .mapToInt(Book::getId)
                .max()
                .orElse(0) + 1;

        Book book = new Book(newId, title, author, false);
        library.addBook(book);
        library.saveBooksToFile("books.txt");
        refreshTables();

        bookMessageLabel.setText("✅ کتاب اضافه شد.");
        bookTitleField.clear();
        bookAuthorField.clear();
    }

    @FXML
    private void addMember(ActionEvent event) {
        String idText = memberIdField.getText().trim();
        String name = memberNameField.getText().trim();

        if (idText.isEmpty() || name.isEmpty()) {
            memberMessageLabel.setText("لطفاً تمام فیلدها را پر کنید.");
            return;
        }

        // اعتبارسنجی ID
        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            memberMessageLabel.setText("شناسه باید عدد باشد.");
            return;
        }

        // بررسی وجود ID تکراری
        if (library.getMembers().stream().anyMatch(member -> member.getId() == id)) {
            memberMessageLabel.setText("شناسه قبلاً وجود دارد.");
            return;
        }

        Member member = new Member(id, name);
        library.addMember(member);
        library.saveMembersToFile("members.txt");
        refreshTables();

        memberMessageLabel.setText("✅ عضو اضافه شد.");
        memberIdField.clear();
        memberNameField.clear();
    }

    @FXML
    private void deleteSelectedBook(ActionEvent event) {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            bookMessageLabel.setText("لطفاً یک کتاب انتخاب کنید.");
            return;
        }

        library.removeBook(selectedBook);
        library.saveBooksToFile("books.txt");
        refreshTables();
        bookMessageLabel.setText("✅ کتاب حذف شد.");
    }

    @FXML
    private void deleteSelectedMember(ActionEvent event) {
        Member selectedMember = borrowMembersTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            memberMessageLabel.setText("لطفاً یک عضو انتخاب کنید.");
            return;
        }

        library.getMembers().remove(selectedMember);
        library.saveMembersToFile("members.txt");
        refreshTables();
        memberMessageLabel.setText("✅ عضو حذف شد.");
    }

    @FXML
    private void borrowBook(ActionEvent event) {
        Book selectedBook = borrowBooksTable.getSelectionModel().getSelectedItem();
        Member selectedMember = borrowMembersTable.getSelectionModel().getSelectedItem();
        LocalDate borrowDate = borrowDatePicker.getValue();
        LocalDate returnDate = returnDatePicker.getValue();

        if (selectedBook == null || selectedMember == null || borrowDate == null || returnDate == null) {
            borrowMessageLabel.setText("لطفاً کتاب، عضو، تاریخ امانت و تاریخ بازگشت را انتخاب کنید.");
            return;
        }

        if (returnDate.isBefore(borrowDate)) {
            borrowMessageLabel.setText("تاریخ بازگشت نمی‌تواند قبل از تاریخ امانت باشد.");
            return;
        }

        if (library.borrowBook(selectedBook, selectedMember, borrowDate.toString(), returnDate.toString())) {
            library.saveBooksToFile("books.txt");
            library.saveMembersToFile("members.txt");
            library.saveReturnDatesToFile("dates.txt");
            refreshTables();
            borrowMessageLabel.setText("✅ کتاب امانت داده شد.");
            clearBorrowFields();
        } else {
            borrowMessageLabel.setText("❌ کتاب قبلاً امانت داده شده.");
        }
    }

    @FXML
    private void returnBook(ActionEvent event) {
        Book selectedBook = borrowBooksTable.getSelectionModel().getSelectedItem();
        Member selectedMember = borrowMembersTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null || selectedMember == null) {
            borrowMessageLabel.setText("لطفاً کتاب و عضو را انتخاب کنید.");
            return;
        }

        if (library.returnBook(selectedBook, selectedMember)) {
            library.saveBooksToFile("books.txt");
            library.saveMembersToFile("members.txt");
            library.saveReturnDatesToFile("dates.txt");
            refreshTables();
            borrowMessageLabel.setText("✅ کتاب بازگردانده شد.");
            clearBorrowFields();
        } else {
            borrowMessageLabel.setText("❌ کتاب امانت داده نشده است.");
        }
    }

    @FXML
    private void searchBooks(ActionEvent event) {
        String keyword = searchField.getText().trim();
        ObservableList<Book> filteredBooks;
        if (keyword.isEmpty()) {
            filteredBooks = FXCollections.observableArrayList(library.getBooks());
        } else {
            filteredBooks = FXCollections.observableArrayList(library.searchBooks(keyword));
        }
        searchTable.setItems(filteredBooks);
    }

    @FXML
    private void searchBorrowBooks(ActionEvent event) {
        String keyword = searchBorrowBookField.getText().trim();
        ObservableList<Book> filteredBooks;
        if (keyword.isEmpty()) {
            filteredBooks = FXCollections.observableArrayList(library.getBooks());
        } else {
            filteredBooks = FXCollections.observableArrayList(library.searchBooks(keyword));
        }
        borrowBooksTable.setItems(filteredBooks);
    }

    @FXML
    private void searchBorrowMembers(ActionEvent event) {
        String keyword = searchBorrowMemberField.getText().trim();
        ObservableList<Member> filteredMembers;
        if (keyword.isEmpty()) {
            filteredMembers = FXCollections.observableArrayList(library.getMembers());
        } else {
            filteredMembers = FXCollections.observableArrayList(library.searchMembers(keyword));
        }
        borrowMembersTable.setItems(filteredMembers);
    }

    @FXML
    private void resetSearchFields(ActionEvent event) {
        searchField.clear();
        searchBorrowBookField.clear();
        searchBorrowMemberField.clear();
        searchTable.setItems(FXCollections.observableArrayList(library.getBooks()));
        borrowBooksTable.setItems(FXCollections.observableArrayList(library.getBooks()));
        borrowMembersTable.setItems(FXCollections.observableArrayList(library.getMembers()));
    }

    @FXML
    private void resetAllFields(ActionEvent event) {
        bookTitleField.clear();
        bookAuthorField.clear();
        memberIdField.clear();
        memberNameField.clear();
        searchField.clear();
        searchBorrowBookField.clear();
        searchBorrowMemberField.clear();
        borrowDatePicker.setValue(null);
        returnDatePicker.setValue(null);

        bookMessageLabel.setText("");
        memberMessageLabel.setText("");
        borrowMessageLabel.setText("");

        booksTable.getSelectionModel().clearSelection();
        borrowBooksTable.getSelectionModel().clearSelection();
        borrowMembersTable.getSelectionModel().clearSelection();
        searchTable.getSelectionModel().clearSelection();

        refreshTables();
    }

    private void clearBorrowFields() {
        searchBorrowBookField.clear();
        searchBorrowMemberField.clear();
        borrowDatePicker.setValue(null);
        returnDatePicker.setValue(null);
        borrowBooksTable.getSelectionModel().clearSelection();
        borrowMembersTable.getSelectionModel().clearSelection();
    }
}