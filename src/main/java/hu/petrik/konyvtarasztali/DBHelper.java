package hu.petrik.konyvtarasztali;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private Connection conn;
    public static String DB_DRIVER="mysql";
    public static String DB_HOST="localhost";
    public static String DB_PORT="3306";
    public static String DB_DBNAME="vizsga2023";
    public static String DB_USER="root";
    public static String DB_PASS="";

    public DBHelper() throws SQLException {
        String url = String.format("jdbc:%s://%s:%s/%s",DB_DRIVER,DB_HOST,DB_PORT,DB_DBNAME);
        conn = DriverManager.getConnection(url,DB_USER,DB_PASS);
    }

    public List<Konyv> readBooks() throws SQLException {
        List<Konyv> books = new ArrayList<>();

        String sql = "SELECT * FROM books";
        Statement stmt = conn.createStatement();
        ResultSet result= stmt.executeQuery(sql);
        while(result.next()){
            int id= result.getInt("id");
            String title= result.getString("title");
            String author= result.getString("author");
            int publish_year= result.getInt("publish_year");
            int page_count= result.getInt("page_count");

            Konyv book = new Konyv(id,title,author,publish_year,page_count);

            books.add(book);
        }

        return books;
    }

    public boolean deleteBook(Konyv selected) throws SQLException {
        String sql= "DELETE from books WHERE id=?";
        PreparedStatement stmt= conn.prepareStatement(sql);
        stmt.setInt(1,selected.getId());
        return stmt.executeUpdate() == 1;
    }
}
