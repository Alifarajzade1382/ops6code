package model;

import java.util.List;

public class ReportGenerator {
    private Library library;

    public ReportGenerator(Library library) {
        this.library = library;
    }

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

    // گزارش کامل (همه گزارش‌ها با هم)
    public String getFullReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== گزارش کتابخانه ===\n");
        report.append(getTotalBooks()).append("\n");
        report.append(getTotalMembers()).append("\n");
        report.append(getBorrowedBooks()).append("\n");
        report.append("====================");
        return report.toString();
    }
}