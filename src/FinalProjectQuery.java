import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.String;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class FinalProjectQuery {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/ResultTracker";

    static final String USER = "root";
    static final String PASSWORD = "mypassword"; //REPLACE WITH YOUR PASSWORD



    public static void q1(Connection conn) throws SQLException {
        // create the query given a year and a month, provide the real name, tag, nationality and the number of
        // wins of players who were born in that month and year
        Scanner myObj = new Scanner(System.in);

        System.out.println("Creating q1 statement...");
        System.out.println("Enter a year: ");

        int year = myObj.nextInt();

        System.out.println("Enter a month (XX): ");

        int month = myObj.nextInt();

        Statement statement = conn.createStatement();

        // construct the sql statement in a string
        String createSql1 = "SELECT Player.real_name, Player.tag, Player.nationality, COUNT(*) " +
                "FROM Player, Earnings " + 
        		"WHERE Player.player_id = Earnings.player_id " +
                "AND Earnings.position = 1 " +
                "AND Player.birthday " +
                "BETWEEN '" + year + "-" + month + "-" + "01'" + " AND '" + year + "-" + month + "-" + "31' " +
                "GROUP BY Player.real_name, Player.tag, Player.nationality;";


        ResultSet rs = statement.executeQuery(createSql1);

        ResultSetMetaData rsmd = rs.getMetaData();


        System.out.println("");

        int numberOfColumns = rsmd.getColumnCount();

        for (int i = 1; i <= numberOfColumns; i++) {
            if (i > 1) System.out.print(",  ");
            String columnName = rsmd.getColumnName(i);
            System.out.print(columnName);
        }
        System.out.println("");

        while (rs.next()) {
            for (int i = 1; i <= numberOfColumns; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);
                System.out.print(columnValue);
            }
            System.out.println("");
        }

        statement.close();
        conn.close();

    }

    public static void q2(Connection conn) throws SQLException {
        // given a player id and a team id, add that player as a member of the specified team
        // with the START DATE set according to the current system time.

        // If the player is currently a member of another team, the database should be updated to reflect their departure
        // from the "old team, with the end date set

        // IF the player was already a current member of the given "new" team, no changes are necessary

        Scanner myObj = new Scanner(System.in);

        System.out.println("Creating q2 statement...");
        System.out.println("Enter player_id: ");

        int player_id = myObj.nextInt();

        System.out.println("Enter team_id for player to be added to: ");

        int team_id = myObj.nextInt();

        Statement statement = conn.createStatement();

        // construct the sql2 statement in a string
        String createSql2 = "";


        ResultSet rs = statement.executeQuery(createSql2);

        ResultSetMetaData rsmd = rs.getMetaData();


        System.out.println("");

        int numberOfColumns = rsmd.getColumnCount();

        for (int i = 1; i <= numberOfColumns; i++) {
            if (i > 1) System.out.print(",  ");
            String columnName = rsmd.getColumnName(i);
            System.out.print(columnName);
        }
        System.out.println("");

        while (rs.next()) {
            for (int i = 1; i <= numberOfColumns; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);
                System.out.print(columnValue);
            }
            System.out.println("");
        }

        statement.close();
        conn.close();

    }



    public static void main(String[] args) {
        Connection conn = null;

        // open a connection
        // execute a query -> constructed with String concatenation
        try {
            System.out.println("Connecting to database ...");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            q1(conn);

        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}


