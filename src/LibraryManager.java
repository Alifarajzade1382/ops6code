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

        library.loadBooksFromFile("books.txt");
        library.loadMembersFromFile("members.txt");
        System.out.println("📁 فایل‌ها با موفقیت بارگذاری شدند.");

        while (true) {
            System.out.println("\n📚 سیستم مدیریت کتابخانه");
            System.out.println("1. اضافه کردن کتاب");
            System.out.println("2. اضافه کردن عضو");
            System.out.println("3. نمایش کتاب‌ها");
            System.out.println("4. نمایش اعضا");
            System.out.println("5. امانت دادن کتاب");
            System.out.println("6. جستجوی کتاب");
            System.out.println("0. خروج");

            System.out.print("انتخاب شما: ");
            int choice = Integer.parseInt(input.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("کد کتاب: ");
                    String id = input.nextLine();
                    System.out.print("عنوان کتاب: ");
                    String title = input.nextLine();
                    System.out.print("نویسنده: ");
                    String author = input.nextLine();
                    library.addBook(new Book(id, title, author));
                    System.out.println("✅ کتاب اضافه شد.");
                    break;
                case 2:
                    System.out.print("کد عضو: ");
                    String memberId = input.nextLine();
                    System.out.print("نام عضو: ");
                    String memberName = input.nextLine();
                    library.addMember(new Member(memberId, memberName));
                    System.out.println("✅ عضو اضافه شد.");
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
                    String bookId = input.nextLine();
                    System.out.print("کد عضو: ");
                    String borrowerId = input.nextLine();
                    Book book = library.getBooks().stream()
                            .filter(b -> b.getId().equals(bookId)).findFirst().orElse(null);
                    Member member = library.getMembers().stream()
                            .filter(m -> m.getId().equals(borrowerId)).findFirst().orElse(null);

                    if (book != null && member != null) {
                        if (library.borrowBook(book, member)) {
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
                    String keyword = input.nextLine();
                    List<Book> results = library.searchBooks(keyword);
                    System.out.println("🔍 نتایج جستجو:");
                    for (Book result : results) {
                        System.out.println(result);
                    }
                    break;
                case 0:
                    library.saveBooksToFile("books.txt");
                    library.saveMembersToFile("members.txt");
                    System.out.println("📁 اطلاعات ذخیره شد. خداحافظ!");
                    return;
                default:
                    System.out.println("❌ گزینه نامعتبر!");
            }
        }
    }
}
