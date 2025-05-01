import model.Book;
import model.Member;
import model.Library;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class LibraryManager {

    public static void main(String[] args) {
        Library library = new Library();
        Scanner input = new Scanner(System.in);

        // بارگذاری فایل‌ها با مدیریت خطا
        try {
            library.loadBooksFromFile("books.txt");
            library.loadMembersFromFile("members.txt");
            library.loadReturnDatesFromFile("dates.txt");
            System.out.println("📁 فایل‌ها با موفقیت بارگذاری شدند.");
        } catch (Exception e) {
            System.err.println("خطا در بارگذاری فایل‌ها: " + e.getMessage());
        }

        while (true) {
            System.out.println("\n📚 سیستم مدیریت کتابخانه");
            System.out.println("1. اضافه کردن کتاب");
            System.out.println("2. اضافه کردن عضو");
            System.out.println("3. نمایش کتاب‌ها");
            System.out.println("4. نمایش اعضا");
            System.out.println("5. امانت دادن کتاب");
            System.out.println("6. جستجوی کتاب");
            System.out.println("7. حذف کتاب"); // گزینه جدید
            System.out.println("8. حذف عضو");  // گزینه جدید
            System.out.println("0. خروج");

            System.out.print("انتخاب شما: ");
            int choice;
            try {
                choice = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ لطفاً یک عدد معتبر وارد کنید!");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("کد کتاب (عدد صحیح): ");
                    String bookIdStr = input.nextLine().trim();
                    int bookId;
                    try {
                        bookId = Integer.parseInt(bookIdStr);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ کد کتاب باید عدد باشد!");
                        break;
                    }

                    // بررسی ID تکراری
                    if (library.getBooks().stream().anyMatch(b -> b.getId() == bookId)) {
                        System.out.println("❌ کد کتاب قبلاً وجود دارد!");
                        break;
                    }

                    System.out.print("عنوان کتاب: ");
                    String title = input.nextLine().trim();
                    System.out.print("نویسنده: ");
                    String author = input.nextLine().trim();

                    if (title.isEmpty() || author.isEmpty()) {
                        System.out.println("❌ عنوان و نویسنده نمی‌توانند خالی باشند!");
                        break;
                    }

                    library.addBook(new Book(bookId, title, author, false));
                    System.out.println("✅ کتاب اضافه شد.");
                    library.saveBooksToFile("books.txt");
                    break;

                case 2:
                    System.out.print("کد عضو (عدد صحیح): ");
                    String memberIdStr = input.nextLine().trim();
                    int memberId;
                    try {
                        memberId = Integer.parseInt(memberIdStr);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ کد عضو باید عدد باشد!");
                        break;
                    }

                    // بررسی ID تکراری
                    if (library.getMembers().stream().anyMatch(m -> m.getId() == memberId)) {
                        System.out.println("❌ کد عضو قبلاً وجود دارد!");
                        break;
                    }

                    System.out.print("نام عضو: ");
                    String memberName = input.nextLine().trim();

                    if (memberName.isEmpty()) {
                        System.out.println("❌ نام عضو نمی‌تواند خالی باشد!");
                        break;
                    }

                    library.addMember(new Member(memberId, memberName));
                    System.out.println("✅ عضو اضافه شد.");
                    library.saveMembersToFile("members.txt");
                    break;

                case 3:
                    if (library.getBooks().isEmpty()) {
                        System.out.println("❌ هیچ کتابی در کتابخانه موجود نیست.");
                    } else {
                        System.out.println("📚 لیست کتاب‌ها:");
                        for (Book book : library.getBooks()) {
                            System.out.println(book);
                        }
                    }
                    break;

                case 4:
                    if (library.getMembers().isEmpty()) {
                        System.out.println("❌ هیچ عضوی در کتابخانه ثبت نشده است.");
                    } else {
                        System.out.println("👥 لیست اعضا:");
                        for (Member member : library.getMembers()) {
                            System.out.println(member);
                        }
                    }
                    break;

                case 5:
                    System.out.print("کد کتاب برای امانت: ");
                    String bookIdToBorrowStr = input.nextLine().trim();
                    int bookIdToBorrow;
                    try {
                        bookIdToBorrow = Integer.parseInt(bookIdToBorrowStr);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ کد کتاب باید عدد باشد!");
                        break;
                    }

                    System.out.print("کد عضو: ");
                    String borrowerIdStr = input.nextLine().trim();
                    int borrowerId;
                    try {
                        borrowerId = Integer.parseInt(borrowerIdStr);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ کد عضو باید عدد باشد!");
                        break;
                    }

                    System.out.print("تاریخ امانت (مثال: 2025-04-30): ");
                    String borrowDate = input.nextLine().trim();
                    System.out.print("تاریخ بازگشت (مثال: 2025-05-07): ");
                    String returnDate = input.nextLine().trim();

                    if (borrowDate.isEmpty() || returnDate.isEmpty()) {
                        System.out.println("❌ تاریخ امانت و بازگشت نمی‌توانند خالی باشند!");
                        break;
                    }

                    Book bookToBorrow = library.getBooks().stream()
                            .filter(b -> b.getId() == bookIdToBorrow)
                            .findFirst()
                            .orElse(null);
                    Member memberToBorrow = library.getMembers().stream()
                            .filter(m -> m.getId() == borrowerId)
                            .findFirst()
                            .orElse(null);

                    if (bookToBorrow != null && memberToBorrow != null) {
                        if (library.borrowBook(bookToBorrow, memberToBorrow, borrowDate, returnDate)) {
                            System.out.println("✅ کتاب با موفقیت امانت داده شد.");
                        } else {
                            System.out.println("❌ این کتاب قبلاً امانت داده شده.");
                        }
                    } else {
                        System.out.println("❌ کتاب یا عضو یافت نشد.");
                    }
                    break;

                case 6:
                    System.out.print("عبارت جستجو: ");
                    String keyword = input.nextLine().trim();
                    List<Book> results = library.searchBooks(keyword);
                    if (results.isEmpty()) {
                        System.out.println("❌ هیچ کتابی یافت نشد.");
                    } else {
                        System.out.println("🔍 نتایج جستجو:");
                        for (Book result : results) {
                            System.out.println(result);
                        }
                    }
                    break;

                case 7: // حذف کتاب
                    System.out.print("کد کتاب برای حذف: ");
                    String bookIdToDeleteStr = input.nextLine().trim();
                    int bookIdToDelete;
                    try {
                        bookIdToDelete = Integer.parseInt(bookIdToDeleteStr);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ کد کتاب باید عدد باشد!");
                        break;
                    }

                    Book bookToDelete = library.getBooks().stream()
                            .filter(b -> b.getId() == bookIdToDelete)
                            .findFirst()
                            .orElse(null);

                    if (bookToDelete != null) {
                        library.removeBook(bookToDelete);
                        System.out.println("✅ کتاب با موفقیت حذف شد.");
                        library.saveBooksToFile("books.txt");
                    } else {
                        System.out.println("❌ کتاب یافت نشد.");
                    }
                    break;

                case 8: // حذف عضو
                    System.out.print("کد عضو برای حذف: ");
                    String memberIdToDeleteStr = input.nextLine().trim();
                    int memberIdToDelete;
                    try {
                        memberIdToDelete = Integer.parseInt(memberIdToDeleteStr);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ کد عضو باید عدد باشد!");
                        break;
                    }

                    Member memberToDelete = library.getMembers().stream()
                            .filter(m -> m.getId() == memberIdToDelete)
                            .findFirst()
                            .orElse(null);

                    if (memberToDelete != null) {
                        library.getMembers().remove(memberToDelete);
                        System.out.println("✅ عضو با موفقیت حذف شد.");
                        library.saveMembersToFile("members.txt");
                    } else {
                        System.out.println("❌ عضو یافت نشد.");
                    }
                    break;

                case 0:
                    try {
                        library.saveBooksToFile("books.txt");
                        library.saveMembersToFile("members.txt");
                        library.saveReturnDatesToFile("dates.txt");
                        System.out.println("📁 اطلاعات ذخیره شد. خداحافظ!");
                    } catch (Exception e) {
                        System.err.println("خطا در ذخیره فایل‌ها: " + e.getMessage());
                    }
                    input.close();
                    return;

                default:
                    System.out.println("❌ گزینه نامعتبر!");
            }
        }
    }
}
