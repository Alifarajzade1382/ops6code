package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class Library {

    private ObservableList<Book> books;
    private ObservableList<Member> members;

    // سازنده
    public Library() {
        this.books = FXCollections.observableArrayList();
        this.members = FXCollections.observableArrayList();
    }

    // متد اضافه کردن کتاب
    public void addBook(Book book) {
        books.add(book);
    }

    // متد اضافه کردن عضو
    public void addMember(Member member) {
        members.add(member);
    }

    // متد برای گرفتن لیست کتاب‌ها
    public ObservableList<Book> getBooks() {
        return books;
    }

    // متد برای گرفتن لیست اعضا
    public ObservableList<Member> getMembers() {
        return members;
    }

    // بارگذاری کتاب‌ها از فایل
    public void loadBooksFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String id = data[0];
                    String title = data[1];
                    String author = data[2];
                    Book book = new Book(id, title, author);
                    addBook(book);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading books from file: " + e.getMessage());
        }
    }

    // بارگذاری اعضا از فایل
    public void loadMembersFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    String id = data[0];
                    String name = data[1];
                    Member member = new Member(id, name);
                    addMember(member);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading members from file: " + e.getMessage());
        }
    }

    // ذخیره کتاب‌ها به فایل
    public void saveBooksToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Book book : books) {
                writer.write(book.getId() + "," + book.getTitle() + "," + book.getAuthor());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving books to file: " + e.getMessage());
        }
    }

    // ذخیره اعضا به فایل
    public void saveMembersToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Member member : members) {
                writer.write(member.getId() + "," + member.getName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving members to file: " + e.getMessage());
        }
    }

    // جستجو برای کتاب‌ها با کلمه کلیدی
    public List<Book> searchBooks(String keyword) {
        return books.stream()
                .filter(book -> book.getTitle().contains(keyword) || book.getAuthor().contains(keyword))
                .collect(Collectors.toList());
    }

    // قرض گرفتن کتاب به یک عضو
    public boolean borrowBook(Book book, Member member) {
        // فقط اگر کتاب در دسترس باشد (مثلاً borrowedText == false) قرض داده می‌شود
        if (book.getBorrowedText()) {  // اگر borrowedText بولی باشد، بررسی می‌شود که false است
            book.setBorrowedText(true); // قرض داده شد
            return true;
        }
        return false;  // اگر کتاب قرض گرفته شده باشد
    }
}

