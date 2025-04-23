package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {

    private final StringProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final BooleanProperty borrowedText; // تغییر به BooleanProperty

    // سازنده
    public Book(String id, String title, String author) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.borrowedText = new SimpleBooleanProperty(false); // به‌طور پیش‌فرض قرض گرفته نشده
    }

    // متدهای getter برای دسترسی به خصوصیات
    public String getId() {
        return id.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public boolean getBorrowedText() {
        return borrowedText.get();
    }

    // متدهای Property برای استفاده در TableView
    public StringProperty idProperty() {
        return id;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty authorProperty() {
        return author;
    }

    public BooleanProperty borrowedTextProperty() {
        return borrowedText;
    }

    // متدهای setter برای تنظیم خصوصیات
    public void setId(String id) {
        this.id.set(id);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setBorrowedText(boolean borrowedText) {
        this.borrowedText.set(borrowedText);
    }

    // متد اصلاح‌شده isBorrowed
    public boolean isBorrowed() {
        return borrowedText.get(); // وضعیت امانت گرفتن کتاب را برمی‌گرداند
    }
}

