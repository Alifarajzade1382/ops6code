package model;

import javafx.beans.property.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Member {

    private IntegerProperty id;
    private StringProperty name;
    private StringProperty borrowedDate; // اضافه کردن ویژگی تاریخ امانت

    // سازنده با آرگومان‌ها
    public Member(int id, String name, String borrowedDate) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.borrowedDate = new SimpleStringProperty(borrowedDate); // مقداردهی تاریخ امانت
    }

    // سازنده بدون تاریخ امانت (اختیاری)
    public Member(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.borrowedDate = new SimpleStringProperty(""); // مقدار پیش‌فرض برای تاریخ امانت
    }

    // Getter و Setter برای id
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Getter و Setter برای name
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // Getter و Setter برای تاریخ امانت
    public String getBorrowedDate() {
        return borrowedDate.get();
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate.set(borrowedDate);
    }

    public StringProperty borrowedDateProperty() {
        return borrowedDate;
    }

    // متدی برای نمایش عضو به صورت متنی
    @Override
    public String toString() {
        return "Member [id=" + getId() + ", name=" + getName() + ", borrowedDate=" + getBorrowedDate() + "]";
    }

    // متدی برای تبدیل داده‌ها به فرمت مناسب برای ذخیره در فایل
    public String toFileString() {
        return getId() + "," + getName() + "," + getBorrowedDate();
    }

    // متد ذخیره داده‌ها در فایل
    public static void saveMembersToFile(List<Member> members, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Member member : members) {
                writer.write(member.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // متد بارگذاری داده‌ها از فایل
    public static List<Member> loadMembersFromFile(String filePath) {
        List<Member> members = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;  // Skip empty lines
                members.add(fromFileString(line));
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return members;
    }

    // متد حذف عضو از لیست
    public static void removeMemberFromList(List<Member> members, int memberId) {
        members.removeIf(member -> member.getId() == memberId);
    }

    // متد تبدیل فرمت رشته‌ای به شیء Member
    public static Member fromFileString(String fileString) {
        String[] data = fileString.split(",");
        int id = Integer.parseInt(data[0]);
        String name = data[1];
        String borrowedDate = data[2];  // بارگذاری تاریخ امانت
        return new Member(id, name, borrowedDate);
    }
}
