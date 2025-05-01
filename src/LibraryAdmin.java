package controller;

import java.net.URL;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Book;
import model.Member;
import model.BookRequest;
import model.Library;

import java.io.*;

public class LibraryAdmin {

    private Library library = new Library();

    // ---------------------- کتاب‌ها ----------------------
    @FXML private TextField bookTitleField, bookAuthorField, searchField, searchBookField;
    @FXML private Label bookMessageLabel;
    @FXML private TableView<Book> booksTable, searchTable;
    @FXML private TableColumn<Book, Integer> idColumn, searchIdColumn;
    @FXML private TableColumn<Book, String> titleColumn, authorColumn, searchTitleColumn, searchAuthorColumn;
    @FXML private TableColumn<Book, Boolean> borrowedColumn, searchStatusColumn;
    @FXML private TableColumn<Book, String> borrowDateColumn, returnDateColumn, searchBorrowDateColumn, searchReturnDateColumn;
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ObservableList<Book> searchResults = FXCollections.observableArrayList();

    // ---------------------- اعضا ----------------------
    @FXML private TextField memberNameField, memberIdField, searchMemberField;
    @FXML private Label memberMessageLabel;
    @FXML private TableView<Member> membersTable;
    @FXML private TableColumn<Member, Integer> memberIdColumn;
    @FXML private TableColumn<Member, String> memberNameColumn;
    @FXML private TableColumn<Member, String> memberBorrowDateColumn;
    private ObservableList<Member> members = FXCollections.observableArrayList();

    // ---------------------- درخواست کتاب ----------------------
    @FXML private TextField requestBookTitleField, requestBookAuthorField, requesterNameField;
    @FXML private Label requestMessageLabel;
    @FXML private TableView<BookRequest> requestsTable;
    @FXML private TableColumn<BookRequest, Integer> requestIdColumn;
    @FXML private TableColumn<BookRequest, String> requestTitleColumn, requestAuthorColumn, requesterNameColumn;
    @FXML private TableColumn<BookRequest, Boolean> approvedColumn;
    private ObservableList<BookRequest> bookRequests = FXCollections.observableArrayList();

    // ---------------------- مدیریت امانت ----------------------
    @FXML private TextField borrowSearchField, memberSearchField;
    @FXML private TableView<Book> borrowTable;
    @FXML private TableColumn<Book, Integer> borrowIdColumn;
    @FXML private TableColumn<Book, String> borrowTitleColumn, borrowAuthorColumn;
    @FXML private TableColumn<Book, Boolean> borrowStatusColumn;
    @FXML private TableColumn<Book, String> borrowDateColumnBorrow, returnDateColumnBorrow;
    @FXML private TableView<Member> memberBorrowTable;
    @FXML private TableColumn<Member, Integer> memberBorrowIdColumn;
    @FXML private TableColumn<Member, String> memberBorrowNameColumn, memberBorrowBorrowDateColumn;
    @FXML private DatePicker borrowDatePicker, returnDatePicker;
    @FXML private Button confirmBorrowButton, cancelBorrowButton, returnBookButton;
    @FXML private Label borrowMessageLabel;
    private ObservableList<Book> borrowSearchResults = FXCollections.observableArrayList();
    private ObservableList<Member> memberBorrowSearchResults = FXCollections.observableArrayList();

    // ---------------------- گزارش‌ها ----------------------
    @FXML private TextArea reportTextArea;
    @FXML private Label reportMessageLabel;

    @FXML private TabPane tabPane;

    @FXML
    public void initialize() {
        initializeTableColumns();
        loadData();
        setupSearchListeners();
        setupBorrowComponents();
    }

    private void initializeTableColumns() {
        // ستون‌های کتاب
        if (idColumn != null) idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (titleColumn != null) titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        if (authorColumn != null) authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        if (borrowedColumn != null) borrowedColumn.setCellValueFactory(new PropertyValueFactory<>("borrowed"));
        if (borrowDateColumn != null) borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        if (returnDateColumn != null) {
            returnDateColumn.setCellValueFactory(cellData -> {
                String returnDateStr = library.getReturnDates().getOrDefault(cellData.getValue().getId(), "");
                return new SimpleStringProperty(returnDateStr);
            });
        }

        if (searchIdColumn != null) searchIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (searchTitleColumn != null) searchTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        if (searchAuthorColumn != null) searchAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        if (searchStatusColumn != null) searchStatusColumn.setCellValueFactory(new PropertyValueFactory<>("borrowed"));
        if (searchBorrowDateColumn != null) searchBorrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        if (searchReturnDateColumn != null) {
            searchReturnDateColumn.setCellValueFactory(cellData -> {
                String returnDateStr = library.getReturnDates().getOrDefault(cellData.getValue().getId(), "");
                return new SimpleStringProperty(returnDateStr);
            });
        }

        if (booksTable != null) booksTable.setItems(books);
        if (searchTable != null) searchTable.setItems(searchResults);

        // ستون‌های عضو
        if (memberIdColumn != null) memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (memberNameColumn != null) memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (memberBorrowDateColumn != null) memberBorrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));

        if (membersTable != null) membersTable.setItems(members);

        // ستون‌های درخواست
        if (requestIdColumn != null) requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (requestTitleColumn != null) requestTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        if (requestAuthorColumn != null) requestAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        if (requesterNameColumn != null) requesterNameColumn.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
        if (approvedColumn != null) approvedColumn.setCellValueFactory(new PropertyValueFactory<>("approved"));

        if (requestsTable != null) requestsTable.setItems(bookRequests);

        // ستون‌های امانت - کتاب
        if (borrowIdColumn != null) borrowIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (borrowTitleColumn != null) borrowTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        if (borrowAuthorColumn != null) borrowAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        if (borrowStatusColumn != null) borrowStatusColumn.setCellValueFactory(new PropertyValueFactory<>("borrowed"));
        if (borrowDateColumnBorrow != null) borrowDateColumnBorrow.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        if (returnDateColumnBorrow != null) {
            returnDateColumnBorrow.setCellValueFactory(cellData -> {
                String returnDateStr = library.getReturnDates().getOrDefault(cellData.getValue().getId(), "");
                return new SimpleStringProperty(returnDateStr);
            });
        }

        if (borrowTable != null) borrowTable.setItems(borrowSearchResults);

        // ستون‌های امانت - عضو
        if (memberBorrowIdColumn != null) memberBorrowIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (memberBorrowNameColumn != null) memberBorrowNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (memberBorrowBorrowDateColumn != null) memberBorrowBorrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));

        if (memberBorrowTable != null) memberBorrowTable.setItems(memberBorrowSearchResults);
    }

    private void setupSearchListeners() {
        if (searchField != null) searchField.textProperty().addListener((obs, oldVal, newVal) -> filterBooks());
        if (searchBookField != null) searchBookField.textProperty().addListener((obs, oldVal, newVal) -> filterBooks());
        if (borrowSearchField != null) borrowSearchField.textProperty().addListener((obs, oldVal, newVal) -> filterBorrowBooks());
        if (memberSearchField != null) memberSearchField.textProperty().addListener((obs, oldVal, newVal) -> filterBorrowMembers());
    }

    private void setupBorrowComponents() {
        memberBorrowSearchResults.setAll(members);
    }

    private void loadData() {
        library.loadBooksFromFile("books.txt");
        library.loadMembersFromFile("members.txt");
        library.loadReturnDatesFromFile("dates.txt");
        books.setAll(library.getBooks());
        members.setAll(library.getMembers());
        borrowSearchResults.setAll(books);
        memberBorrowSearchResults.setAll(members);
        loadRequestsFromFile("book_requests.txt");
    }

    // ---------------------- عملیات کتاب ----------------------
    @FXML
    private void addBook() {
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();

        if (title.isEmpty() || author.isEmpty()) {
            bookMessageLabel.setText("لطفاً نام کتاب و نویسنده را وارد کنید.");
            return;
        }

        Book newBook = new Book(books.size() + 1, title, author, false);
        library.addBook(newBook);
        books.setAll(library.getBooks());
        borrowSearchResults.setAll(books);
        clearBookFields();
        library.saveBooksToFile("books.txt");
    }

    private void clearBookFields() {
        bookTitleField.clear();
        bookAuthorField.clear();
        bookMessageLabel.setText("کتاب با موفقیت اضافه شد!");
    }

    @FXML
    private void filterBooks() {
        String searchText = searchField.getText();
        searchResults.clear();
        if (searchText == null || searchText.isEmpty()) {
            searchResults.addAll(books);
        } else {
            for (Book book : books) {
                boolean match = book.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                                book.getAuthor().toLowerCase().contains(searchText.toLowerCase());
                try {
                    int searchId = Integer.parseInt(searchText);
                    match = match || book.getId() == searchId;
                } catch (NumberFormatException ignored) {
                }
                if (match) {
                    searchResults.add(book);
                }
            }
        }
    }

    @FXML
    public void resetBookSearch() {
        searchField.clear();
        filterBooks();
    }

    @FXML
    public void deleteSelectedBook(ActionEvent event) {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            bookMessageLabel.setText("لطفاً یک کتاب انتخاب کنید.");
            return;
        }

        // حذف کتاب از کتابخانه
        library.removeBook(selectedBook);
        books.setAll(library.getBooks());
        borrowSearchResults.setAll(books);
        searchResults.clear();
        filterBooks(); // به‌روزرسانی جدول جستجو
        library.saveBooksToFile("books.txt");
        bookMessageLabel.setText("کتاب با موفقیت حذف شد!");
    }

    // ---------------------- عملیات عضو ----------------------
    @FXML
    private void addMember() {
        String name = memberNameField.getText();
        String idText = memberIdField.getText();

        if (name.isEmpty() || idText.isEmpty()) {
            memberMessageLabel.setText("لطفاً نام عضو و کد عضو را وارد کنید.");
            return;
        }

        try {
            Member newMember = new Member(Integer.parseInt(idText), name);
            library.addMember(newMember);
            members.setAll(library.getMembers());
            memberBorrowSearchResults.setAll(members);
            clearMemberFields();
            library.saveMembersToFile("members.txt");
        } catch (NumberFormatException e) {
            memberMessageLabel.setText("کد عضو باید عدد باشد.");
        }
    }

    private void clearMemberFields() {
        memberNameField.clear();
        memberIdField.clear();
        memberMessageLabel.setText("عضو با موفقیت اضافه شد!");
    }

    @FXML
    public void resetMemberSearch() {
        searchMemberField.clear();
    }

    @FXML
    public void deleteSelectedMember(ActionEvent event) {
        Member selectedMember = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            memberMessageLabel.setText("لطفاً یک عضو انتخاب کنید.");
            return;
        }

        // حذف عضو از کتابخانه
        library.getMembers().remove(selectedMember);
        members.setAll(library.getMembers());
        memberBorrowSearchResults.setAll(library.getMembers());
        library.saveMembersToFile("members.txt");
        memberMessageLabel.setText("عضو با موفقیت حذف شد!");
    }

    // ---------------------- عملیات درخواست ----------------------
    @FXML
    private void addBookRequest() {
        String title = requestBookTitleField.getText();
        String author = requestBookAuthorField.getText();
        String requester = requesterNameField.getText();

        if (title.isEmpty() || author.isEmpty() || requester.isEmpty()) {
            requestMessageLabel.setText("لطفاً اطلاعات کامل را وارد کنید.");
            return;
        }

        bookRequests.add(new BookRequest(bookRequests.size() + 1, title, author, requester, false));
        clearRequestFields();
        saveRequestsToFile("book_requests.txt");
    }

    private void clearRequestFields() {
        requestBookTitleField.clear();
        requestBookAuthorField.clear();
        requesterNameField.clear();
        requestMessageLabel.setText("درخواست کتاب با موفقیت ثبت شد!");
    }

    @FXML
    public void approveSelectedRequest(ActionEvent event) {
        BookRequest selected = requestsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setApproved(true);
            Book newBook = new Book(books.size() + 1, selected.getTitle(), selected.getAuthor(), false);
            library.addBook(newBook);
            books.setAll(library.getBooks());
            borrowSearchResults.setAll(books);
            saveBooksToFile("books.txt");
            saveRequestsToFile("book_requests.txt");
        }
    }

    @FXML
    public void rejectSelectedRequest(ActionEvent event) {
        BookRequest selected = requestsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            bookRequests.remove(selected);
            saveRequestsToFile("book_requests.txt");
            requestsTable.setItems(bookRequests);
        }
    }

    // ---------------------- مدیریت امانت ----------------------
    @FXML
    private void filterBorrowBooks() {
        String searchText = borrowSearchField.getText();
        borrowSearchResults.clear();
        if (searchText == null || searchText.isEmpty()) {
            borrowSearchResults.addAll(books);
        } else {
            for (Book book : books) {
                boolean match = book.getTitle().toLowerCase().contains(searchText.toLowerCase());
                try {
                    int searchId = Integer.parseInt(searchText);
                    match = match || book.getId() == searchId;
                } catch (NumberFormatException ignored) {
                }
                if (match) {
                    borrowSearchResults.add(book);
                }
            }
        }
    }

    @FXML
    private void filterBorrowMembers() {
        String searchText = memberSearchField.getText();
        memberBorrowSearchResults.clear();
        if (searchText == null || searchText.isEmpty()) {
            memberBorrowSearchResults.addAll(members);
        } else {
            for (Member member : members) {
                boolean match = member.getName().toLowerCase().contains(searchText.toLowerCase());
                try {
                    int searchId = Integer.parseInt(searchText);
                    match = match || member.getId() == searchId;
                } catch (NumberFormatException ignored) {
                }
                if (match) {
                    memberBorrowSearchResults.add(member);
                }
            }
        }
    }

    @FXML
    private void resetBorrowSearch() {
        borrowSearchField.clear();
        filterBorrowBooks();
    }

    @FXML
    private void resetMemberBorrowSearch() {
        memberSearchField.clear();
        filterBorrowMembers();
    }

    @FXML
    private void confirmBorrow(ActionEvent event) {
        Book selectedBook = borrowTable.getSelectionModel().getSelectedItem();
        Member selectedMember = memberBorrowTable.getSelectionModel().getSelectedItem();
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

        boolean success = library.borrowBook(selectedBook, selectedMember, borrowDate.toString(), returnDate.toString());
        if (success) {
            borrowMessageLabel.setText("کتاب با موفقیت امانت داده شد!");
            refreshTables();
            clearBorrowFields();
        } else {
            borrowMessageLabel.setText("امانت دادن کتاب ممکن نیست (کتاب قبلاً امانت داده شده).");
        }
    }

    @FXML
    private void cancelBorrow(ActionEvent event) {
        clearBorrowFields();
        borrowMessageLabel.setText("عملیات امانت لغو شد.");
    }

    @FXML
    private void returnBook(ActionEvent event) {
        Book selectedBook = borrowTable.getSelectionModel().getSelectedItem();
        Member selectedMember = memberBorrowTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null || selectedMember == null) {
            borrowMessageLabel.setText("لطفاً یک کتاب و یک عضو انتخاب کنید.");
            return;
        }

        boolean success = library.returnBook(selectedBook, selectedMember);
        if (success) {
            borrowMessageLabel.setText("کتاب با موفقیت بازگردانده شد!");
            refreshTables();
            clearBorrowFields();
        } else {
            borrowMessageLabel.setText("بازگرداندن کتاب ممکن نیست (کتاب امانت داده نشده).");
        }
    }

    private void refreshTables() {
        if (booksTable != null) {
            booksTable.setItems(null);
            booksTable.setItems(books);
        }
        if (searchTable != null) {
            searchTable.setItems(null);
            searchTable.setItems(searchResults);
        }
        if (borrowTable != null) {
            borrowTable.setItems(null);
            borrowTable.setItems(borrowSearchResults);
        }
        if (membersTable != null) {
            membersTable.setItems(null);
            membersTable.setItems(members);
        }
        if (memberBorrowTable != null) {
            memberBorrowTable.setItems(null);
            memberBorrowTable.setItems(memberBorrowSearchResults);
        }
    }

    private void clearBorrowFields() {
        if (borrowSearchField != null) borrowSearchField.clear();
        if (memberSearchField != null) memberSearchField.clear();
        if (borrowDatePicker != null) borrowDatePicker.setValue(null);
        if (returnDatePicker != null) returnDatePicker.setValue(null);
        filterBorrowBooks();
        filterBorrowMembers();
    }

    // ---------------------- گزارش‌دهی ----------------------
    // گزارش تعداد کل کتاب‌ها
    public String getTotalBooks() {
        return "تعداد کل کتاب‌ها: " + library.getBooks().size();
    }

    // گزارش تعداد اعضای ثبت‌شده
    public String getTotalMembers() {
        return "تعداد کل اعضا: " + library.getMembers().size();
    }

    // گزارش تعداد کتاب‌های امانت‌داده‌شده
    public String getBorrowedBooks() {
        int borrowedCount = 0;
        for (Book book : library.getBooks()) {
            if (book.isBorrowed()) {
                borrowedCount++;
            }
        }
        return "تعداد کتاب‌های امانت‌داده‌شده: " + borrowedCount;
    }

    // گزارش جدید: لیست کتاب‌های امانت‌داده‌شده
    public String getBorrowedBooksDetails() {
        StringBuilder details = new StringBuilder("لیست کتاب‌های امانت‌داده‌شده:\n");
        boolean hasBorrowedBooks = false;
        for (Book book : library.getBooks()) {
            if (book.isBorrowed()) {
                hasBorrowedBooks = true;
                details.append(String.format("- کتاب: %s، نویسنده: %s، تاریخ امانت: %s، تاریخ بازگشت: %s\n",
                        book.getTitle(), book.getAuthor(), book.getBorrowedDate(),
                        library.getReturnDates().getOrDefault(book.getId(), "نامشخص")));
            }
        }
        if (!hasBorrowedBooks) {
            details.append("هیچ کتابی امانت داده نشده است.\n");
        }
        return details.toString();
    }

    // گزارش کامل (همه گزارش‌ها با هم)
    public String getFullReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== گزارش کتابخانه ===\n");
        report.append("تاریخ گزارش: ").append(LocalDate.now()).append("\n\n");
        report.append(getTotalBooks()).append("\n");
        report.append(getTotalMembers()).append("\n");
        report.append(getBorrowedBooks()).append("\n\n");
        report.append(getBorrowedBooksDetails()).append("\n");
        report.append("====================");
        return report.toString();
    }

    // ذخیره گزارش کامل در فایل متنی
    public void saveReportToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            writer.write(getFullReport());
            reportMessageLabel.setText("گزارش با موفقیت در فایل " + filePath + " ذخیره شد.");
        } catch (IOException e) {
            reportMessageLabel.setText("خطا در ذخیره گزارش: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // نمایش گزارش در TextArea
    @FXML
    public void showReport() {
        reportTextArea.setText(getFullReport());
        reportMessageLabel.setText("گزارش با موفقیت نمایش داده شد.");
    }

    // ذخیره گزارش با دکمه
    @FXML
    public void saveReport() {
        saveReportToFile("library_report.txt");
    }

    // ---------------------- برگشت به صفحه ورود ----------------------
    @FXML
    private void goBackToLogin(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/login.fxml");
            if (fxmlLocation == null) {
                System.out.println("login.fxml not found!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("ورود - سیستم کتابخانه");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------- ذخیره و بارگذاری داده‌ها ----------------------
    private <T> void saveToFile(String filename, ObservableList<T> items, DataParser<T> parser) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (T item : items) {
                writer.write(parser.parse(item));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBooksToFile(String filename) {
        saveToFile(filename, books, new BookParser());
    }

    private void saveMembersToFile(String filename) {
        saveToFile(filename, members, new MemberParser());
    }

    private void saveRequestsToFile(String filename) {
        saveToFile(filename, bookRequests, new BookRequestParser());
    }

    private <T> void loadFromFile(String filename, ObservableList<T> items, DataParser<T> parser) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                items.add(parser.parse(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBooksFromFile(String filename) {
        loadFromFile(filename, books, new BookParser());
    }

    private void loadMembersFromFile(String filename) {
        loadFromFile(filename, members, new MemberParser());
    }

    private void loadRequestsFromFile(String filename) {
        loadFromFile(filename, bookRequests, new BookRequestParser());
    }

    // ---------------------- پارسرها ----------------------
    private interface DataParser<T> {
        String parse(T item);
        T parse(String line);
    }

    private static class BookParser implements DataParser<Book> {
        @Override
        public String parse(Book book) {
            return book.getId() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.isBorrowed() + "," + book.getBorrowedDate();
        }

        @Override
        public Book parse(String line) {
            String[] parts = line.split(",");
            return new Book(Integer.parseInt(parts[0]), parts[1], parts[2], Boolean.parseBoolean(parts[3]), parts.length > 4 ? parts[4] : "");
        }
    }

    private static class MemberParser implements DataParser<Member> {
        @Override
        public String parse(Member member) {
            return member.getId() + "," + member.getName() + "," + member.getBorrowedDate();
        }

        @Override
        public Member parse(String line) {
            String[] parts = line.split(",");
            return new Member(Integer.parseInt(parts[0]), parts[1], parts.length > 2 ? parts[2] : "");
        }
    }

    private static class BookRequestParser implements DataParser<BookRequest> {
        @Override
        public String parse(BookRequest request) {
            return request.getId() + "," + request.getTitle() + "," + request.getAuthor() + "," + request.getRequesterName() + "," + request.isApproved();
        }

        @Override
        public BookRequest parse(String line) {
            String[] parts = line.split(",");
            return new BookRequest(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], Boolean.parseBoolean(parts[4]));
        }
    }
}