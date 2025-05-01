package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Library {

    private ObservableList<Book> books;
    private ObservableList<Member> members;
    private Map<Integer, String> returnDates;

    public Library() {
        this.books = FXCollections.observableArrayList();
        this.members = FXCollections.observableArrayList();
        this.returnDates = new HashMap<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public ObservableList<Book> getBooks() {
        return books;
    }

    public ObservableList<Member> getMembers() {
        return members;
    }

    public Map<Integer, String> getReturnDates() {
        return returnDates;
    }

    public void loadBooksFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    int id = Integer.parseInt(data[0]);
                    String title = data[1];
                    String author = data[2];
                    boolean borrowed = Boolean.parseBoolean(data[3]);
                    String borrowDate = data.length > 4 ? data[4] : "";
                    Book book = new Book(id, title, author, borrowed, borrowDate);
                    addBook(book);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading books from file: " + e.getMessage());
        }
    }

    public void loadMembersFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("File not found: " + filename);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] data = line.split(",");
                if (data.length >= 2) {
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        String name = data[1].trim();
                        String borrowedDate = data.length > 2 ? data[2].trim() : "";
                        Member member = new Member(id, name, borrowedDate);
                        addMember(member);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed line (invalid ID format): " + line);
                    }
                } else {
                    System.err.println("Skipping malformed line (incorrect number of fields): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    public void saveBooksToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Book book : books) {
                writer.write(book.getId() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.isBorrowed() + "," + book.getBorrowedDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving books to file: " + e.getMessage());
        }
    }

    public void saveMembersToFile(String filename) {
        File file = new File(filename);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
                for (Member member : members) {
                    writer.write(member.getId() + "," + member.getName() + "," + member.getBorrowedDate());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error writing to the file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error creating the file: " + e.getMessage());
        }
    }

    public List<Book> searchBooks(String keyword) {
        return books.stream()
                .filter(book -> book.getTitle().contains(keyword) || book.getAuthor().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<Member> searchMembers(String keyword) {
        return members.stream()
                .filter(member -> member.getName().contains(keyword) || String.valueOf(member.getId()).contains(keyword))
                .collect(Collectors.toList());
    }

    public boolean borrowBook(Book book, Member member, String borrowDate, String returnDate) {
        if (!book.isBorrowed()) {
            book.setBorrowed(true);
            book.setBorrowedDate(borrowDate);
            member.setBorrowedDate(borrowDate);
            returnDates.put(book.getId(), returnDate);
            saveReturnDatesToFile("dates.txt");
            saveBooksToFile("books.txt");
            saveMembersToFile("members.txt");
            return true;
        }
        return false;
    }

    public boolean returnBook(Book book, Member member) {
        if (book.isBorrowed()) {
            book.setBorrowed(false);
            book.setBorrowedDate("");
            member.setBorrowedDate("");
            returnDates.remove(book.getId());
            saveReturnDatesToFile("dates.txt");
            saveBooksToFile("books.txt");
            saveMembersToFile("members.txt");
            return true;
        }
        return false;
    }

    public void loadReturnDatesFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    int bookId = Integer.parseInt(data[0].trim());
                    String returnDate = data[1].trim();
                    returnDates.put(bookId, returnDate);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading return dates from file: " + e.getMessage());
        }
    }

    public void saveReturnDatesToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<Integer, String> entry : returnDates.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving return dates to file: " + e.getMessage());
        }
    }
}