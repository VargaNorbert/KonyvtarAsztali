package hu.petrik.konyvtarasztali;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Statisztika {
    private static List<Konyv> konyvek;

    public static void run() {
        try {
            readBooksFromDatabase();
            System.out.printf("500 oldalnál hosszabb könyvek száma: %d\n", countLongerThan500Pages());
            System.out.printf("%s 1950-nél régebbi könyv.", containsOlderThan1950()? "Van":"Nincs");
            printLongest();
            printAuthorWithMostBooks();
            String title = readTitleFromConsole();
            PrintAuthor(title);
        } catch (SQLException e) {
            System.out.println("Hiba történt az databázis kialakításakor:");
            System.out.println(e.getMessage());
        }
    }

    private static void PrintAuthor(String title) {
        Optional<Konyv> optionalKonyv= getBook(title);
        if (optionalKonyv.isPresent()){
            System.out.printf("A megadott könyv szerzője %s",optionalKonyv.get().getAuthor());
        }else{
            System.out.println("Nincs ilyen könyv");
        }
    }

    private static Optional<Konyv> getBook(String title) {
        return konyvek.stream().filter(konyv -> konyv.getTitle().equals(title)).findFirst();}

    private static String readTitleFromConsole() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Adjon meg egy könyv címet: ");
        return sc.nextLine();
    }

    private static void printAuthorWithMostBooks() {
        String authorWithMostBooks = getAuthorWithMostBooks();
        System.out.printf("A legtöbb könyvvel rendelkező szerző: %s\n", authorWithMostBooks);
    }

    private static String getAuthorWithMostBooks() {
        return konyvek.stream().collect(Collectors.groupingBy(Konyv::getAuthor, Collectors.counting())).entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue)).get().getKey();
    }

    private static void printLongest() {
        Konyv longestBook = getLongestBook();

        System.out.printf("A leghosszabb könyv: \n"+
                        "\tSzerző:%s\n"+
                        "\tCím:%s\n",
                longestBook.getAuthor(),
                longestBook.getTitle());
    }

    private static Konyv getLongestBook() {
        return konyvek.stream().max(Comparator.comparingInt(Konyv::getPage_count)).get();
    }

    private static boolean containsOlderThan1950() {
        return konyvek.stream().anyMatch(konyv -> konyv.getPublish_year()<1950);
    }

    private static long countLongerThan500Pages() {
        return konyvek.stream().filter(konyv-> konyv.getPage_count()>500).count();
    }

    private static void readBooksFromDatabase() throws SQLException {
        DBHelper db = new DBHelper();
        konyvek = db.readBooks();
    }



}
