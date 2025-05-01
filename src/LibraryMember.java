package controller;

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
import model.BookRequest;

import java.io.*;
import java.net.URL;

public class LibraryMember {

    // --- بخش امانت دادن ---
    @FXML private TextField searchBorrowBookField;
    @FXML private TableView<Book> borrowBooksTable;
    @FXML private TableColumn<Book, Integer> borrowBookIdColumn;
    @FXML private TableColumn<Book, String> borrowBookTitleColumn;
    @FXML private TableColumn<Book, String> borrowBookAuthorColumn;
    @FXML private TableColumn<Book, Boolean> borrowBookStatusColumn;
    @FXML private Label borrowMessageLabel;
    private ObservableList<Book> allBooks = FXCollections.observableArrayList();

    // --- بخش درخواست کتاب ---
    @FXML private TextField requestBookTitleField, requestBookAuthorField, requesterNameField;
    @FXML private Label requestMessageLabel;
    @FXML private TableView<BookRequest> requestsTable;
    @FXML private TableColumn<BookRequest, Integer> requestIdColumn;
    @FXML private TableColumn<BookRequest, String> requestTitleColumn, requestAuthorColumn, requesterNameColumn;
    @FXML private TableColumn<BookRequest, Boolean> approvedColumn;

    private ObservableList<BookRequest> bookRequests = FXCollections.observableArrayList();
    private String currentMemberName = "عضو تستی"; // فرض کنید این نام از سیستم لاگین به‌دست آمده است.
    private boolean isAdmin = false; // نقش کاربر: اگر true باشد، کاربر کتابدار است.

    @FXML
    public void initialize() {
        setupBookTable();
        loadBooksFromFile("books.txt");
        setupSearchListeners();
        loadRequestsFromFile("book_requests.txt");
        setupRequestTable();
    }

    private void setupBookTable() {
        borrowBookIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        borrowBookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        borrowBookAuthorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        borrowBookStatusColumn.setCellValueFactory(cellData -> cellData.getValue().borrowedProperty());
        borrowBooksTable.setItems(allBooks);
    }

    private void setupSearchListeners() {
        if (searchBorrowBookField != null) {
            searchBorrowBookField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchBorrowBooks();
            });
        }
    }

    @FXML
    private void searchBorrowBooks() {
        String keyword = searchBorrowBookField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            borrowBooksTable.setItems(allBooks);
            return;
        }
        ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
        for (Book book : allBooks) {
            if (book.getTitle().toLowerCase().contains(keyword)) {
                filteredBooks.add(book);
            }
        }
        borrowBooksTable.setItems(filteredBooks);
    }

    @FXML
    private void resetAllFields() {
        searchBorrowBookField.clear();
        borrowBooksTable.setItems(allBooks);
        borrowMessageLabel.setText("");
    }

    private void setupRequestTable() {
        if (requestIdColumn != null && requestTitleColumn != null && requestAuthorColumn != null && requesterNameColumn != null && approvedColumn != null) {
            requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            requestTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            requestAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
            requesterNameColumn.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
            approvedColumn.setCellValueFactory(new PropertyValueFactory<>("approved"));

            // نمایش درخواست‌ها فقط برای کتابدار یا درخواست‌های عضو خاص
            if (isAdmin) {
                requestsTable.setItems(bookRequests);
            } else {
                requestsTable.setItems(getRequestsForCurrentMember());
            }
        }
    }

    private ObservableList<BookRequest> getRequestsForCurrentMember() {
        ObservableList<BookRequest> filteredRequests = FXCollections.observableArrayList();
        for (BookRequest request : bookRequests) {
            if (request.getRequesterName().equals(currentMemberName)) {
                filteredRequests.add(request);
            }
        }
        return filteredRequests;
    }

    @FXML
    private void BookRequest() {
        // دریافت عنوان کتاب
        String title = requestBookTitleField.getText().trim();
        
        // دریافت نویسنده کتاب
        String author = requestBookAuthorField.getText().trim();
        
        // دریافت نام درخواست‌دهنده
        String requester = requesterNameField.getText().trim();

        // بررسی اینکه تمام فیلدها پر شده باشند
        if (title.isEmpty() || author.isEmpty() || requester.isEmpty()) {
            requestMessageLabel.setText("لطفاً تمام اطلاعات را وارد کنید.");
            return;
        }

        // اطمینان از مقداردهی صحیح bookRequests
        if (bookRequests == null) {
            bookRequests = FXCollections.observableArrayList(); // مقداردهی در صورت عدم مقداردهی قبلی
        }

        // محاسبه شناسه جدید برای درخواست کتاب
        int nextId = bookRequests.size() + 1;
        
        // ساخت شیء جدید برای درخواست کتاب
        BookRequest newRequest = new BookRequest(nextId, title, author, requester, false);
        
        // افزودن درخواست کتاب به لیست
        bookRequests.add(newRequest);

        // نمایش پیام موفقیت
        requestMessageLabel.setText("درخواست کتاب با موفقیت ثبت شد!");

        // بروزرسانی جدول درخواست‌ها
        updateRequestTable();
        
        // ذخیره درخواست‌ها در فایل
        saveRequestsToFile("book_requests.txt");

        // پاک کردن فیلدهای ورودی
        clearRequestFields();
    }

    private void updateRequestTable() {
        if (requestsTable != null && bookRequests != null) {
            if (isAdmin) {
                requestsTable.setItems(bookRequests);
            } else {
                requestsTable.setItems(getRequestsForCurrentMember());
            }
        }
    }

    private void clearRequestFields() {
        requestBookTitleField.clear();
        requestBookAuthorField.clear();
        requesterNameField.clear();
    }

    private void saveRequestsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (BookRequest request : bookRequests) {
                writer.write(request.getId() + "," + request.getTitle() + "," + request.getAuthor() + "," +
                        request.getRequesterName() + "," + request.isApproved());
                writer.newLine();
            }
        } catch (IOException e) {
            requestMessageLabel.setText("خطا در ذخیره‌سازی درخواست‌ها: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadRequestsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String author = parts[2];
                    String requester = parts[3];
                    boolean approved = Boolean.parseBoolean(parts[4]);

                    if (bookRequests == null) {
                        bookRequests = FXCollections.observableArrayList(); // مقداردهی در صورت عدم مقداردهی قبلی
                    }

                    bookRequests.add(new BookRequest(id, title, author, requester, approved));
                }
            }
        } catch (IOException e) {
            requestMessageLabel.setText("خطا در بارگذاری درخواست‌ها: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void borrowBook() {
        Book selectedBook = borrowBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            borrowMessageLabel.setText("لطفاً یک کتاب انتخاب کنید.");
            return;
        }
        if (selectedBook.isBorrowed()) {
            borrowMessageLabel.setText("کتاب انتخابی قبلاً امانت داده شده است.");
        } else {
            selectedBook.setBorrowed(true);
            borrowMessageLabel.setText("کتاب با موفقیت امانت گرفته شد.");
            ObservableList<Book> currentItems = borrowBooksTable.getItems();
            borrowBooksTable.setItems(null);
            borrowBooksTable.setItems(currentItems);
            saveBooksToFile("books.txt");
        }
    }

    private void loadBooksFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    int id = Integer.parseInt(data[0]);
                    String title = data[1];
                    String author = data[2];
                    boolean borrowed = Boolean.parseBoolean(data[3]);
                    allBooks.add(new Book(id, title, author, borrowed));
                }
            }
        } catch (IOException e) {
            borrowMessageLabel.setText("خطا در بارگذاری کتاب‌ها: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveBooksToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Book book : allBooks) {
                writer.write(book.getId() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.isBorrowed());
                writer.newLine();
            }
        } catch (IOException e) {
            borrowMessageLabel.setText("خطا در ذخیره‌سازی کتاب‌ها: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
