package model;

import javafx.beans.property.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookRequest {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty requesterName;
    private final BooleanProperty approved;

    public BookRequest(int id, String title, String author, String requesterName, boolean approved) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.requesterName = new SimpleStringProperty(requesterName);
        this.approved = new SimpleBooleanProperty(approved);
    }

    // ----------- Getters -------------
    public int getId() {
        return id.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getRequesterName() {
        return requesterName.get();
    }

    public boolean isApproved() {
        return approved.get();
    }

    // ----------- Setters -------------
    public void setId(int id) {
        this.id.set(id);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setRequesterName(String requesterName) {
        this.requesterName.set(requesterName);
    }

    public void setApproved(boolean approved) {
        this.approved.set(approved);
    }

    // ----------- Property getters -------------
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty authorProperty() {
        return author;
    }

    public StringProperty requesterNameProperty() {
        return requesterName;
    }

    public BooleanProperty approvedProperty() {
        return approved;
    }

    // ----------- متد ذخیره داده‌ها در فایل -------------
    public static void saveRequestsToFile(List<BookRequest> requests, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (BookRequest request : requests) {
                writer.write(request.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // ----------- متد بارگذاری داده‌ها از فایل -------------
    public static List<BookRequest> loadRequestsFromFile(String filePath) {
        List<BookRequest> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;  // Skip empty lines
                requests.add(fromFileString(line));  // Parse the line into a BookRequest
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return requests;
    }

    // ----------- متد حذف درخواست از لیست -------------
    public static void removeRequestFromList(List<BookRequest> requests, int requestId) {
        requests.removeIf(request -> request.getId() == requestId);
    }

    // ----------- متد تبدیل داده‌ها به فرمت مناسب برای ذخیره در فایل -------------
    public String toFileString() {
        return getId() + "," + getTitle() + "," + getAuthor() + "," + getRequesterName() + "," + isApproved();
    }

    // ----------- متد تبدیل فرمت رشته‌ای به شیء BookRequest -------------
    public static BookRequest fromFileString(String fileString) {
        String[] data = fileString.split(",");
        if (data.length != 5) {
            throw new IllegalArgumentException("Invalid data format in file: " + fileString);
        }
        int id = Integer.parseInt(data[0]);
        String title = data[1];
        String author = data[2];
        String requesterName = data[3];
        boolean approved = Boolean.parseBoolean(data[4]);
        return new BookRequest(id, title, author, requesterName, approved);
    }
}

