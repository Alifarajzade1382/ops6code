package model;

import javafx.beans.property.*;

public class Book {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty author = new SimpleStringProperty();
    private final BooleanProperty borrowed = new SimpleBooleanProperty();
    private final StringProperty borrowDate = new SimpleStringProperty();

    // سازنده اصلی با 5 پارامتر
    public Book(int id, String title, String author, boolean borrowed, String borrowDate) {
        this.id.set(id);
        this.title.set(title);
        this.author.set(author);
        this.borrowed.set(borrowed);
        this.borrowDate.set(borrowDate);
    }

    // سازنده جدید با 4 پارامتر (بدون borrowDate)
    public Book(int id, String title, String author, boolean borrowed) {
        this(id, title, author, borrowed, "");
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public boolean isBorrowed() {
        return borrowed.get();
    }

    public BooleanProperty borrowedProperty() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed.set(borrowed);
    }

    public String getBorrowedDate() {
        return borrowDate.get();
    }

    public StringProperty borrowDateProperty() {
        return borrowDate;
    }

    public void setBorrowedDate(String borrowDate) {
        this.borrowDate.set(borrowDate);
    }
}
