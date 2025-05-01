package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemberFileManager {

    // متدی برای ذخیره لیستی از اعضا در فایل
    public static void saveMembersToFile(List<Member> members, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Member member : members) {
                writer.write(member.toFileString());  // ذخیره کردن به فرمت مناسب
                writer.newLine();  // خط جدید
            }
            System.out.println("Members saved successfully to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // متدی برای بارگذاری اعضا از فایل
    public static List<Member> loadMembersFromFile(String filename) {
        List<Member> members = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;  // Skip empty lines
                members.add(Member.fromFileString(line));  // استفاده از fromFileString برای تبدیل رشته به شیء
            }
            System.out.println("Members loaded successfully from " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return members;
    }
}

