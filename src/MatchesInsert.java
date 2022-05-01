import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.String;

public class MatchesInsert {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/ResultTracker";
	
	static final String USER = "root";
	static final String PASSWORD = "mypassword";
	
	private static List<String> getRecordFromLine(String line) {
		
		List<String> values = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
		}
		
		// This loop removes the quotation marks
//		for(int i = 0; i < values.size(); i ++) {
//			values.set(i, values.get(i).replaceAll("\"", ""));
//		}
		
		return values;
	}

	
	public static void sqlizeData(List<List<String>> data) {
		for(int i = 0; i < data.size(); i ++) {
			for(int j = 0; j < data.get(i).size();  j++){
				if(data.get(i).get(j).length() == 0) {
					data.get(i).set(j,  null);
				}
				else {
					data.get(i).set(j, data.get(i).get(j).replaceAll("^\"|\"$", ""));
				}
			}
		}
	}
	
	public static void insertMatches(Connection conn, String filename) {
//		List<List<String>> records = getRecordFromFile(filename);
//		sqlizeData(records);
		
		PreparedStatement statement = null;
		String insertSql = " INSERT IGNORE  into matches values (?, ?, ?, ?, ?, ?, ?, ?)";
		
		// Reading the csv file 
		List<List<String>> matchesRecords = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File("matches.csv"));) {
			while (scanner.hasNextLine()) {
				matchesRecords.add(getRecordFromLine(scanner.nextLine()));
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sqlizeData(matchesRecords);
		
		try {
			statement = conn.prepareStatement(insertSql);
			for(int i = 0; i < matchesRecords.size(); i++) {
                // setXXX() methods to set the values of these ?
				statement.setInt(1, Integer.valueOf(matchesRecords.get(i).get(0)));
				statement.setDate(2, Date.valueOf(matchesRecords.get(i).get(1)));
				statement.setInt(3, Integer.valueOf(matchesRecords.get(i).get(2)));
				statement.setInt(4, Integer.valueOf(matchesRecords.get(i).get(3)));
				statement.setInt(5, Integer.valueOf(matchesRecords.get(i).get(4)));
				statement.setInt(6, Integer.valueOf(matchesRecords.get(i).get(5)));
				statement.setInt(7, Integer.valueOf(matchesRecords.get(i).get(6)));
				statement.setBoolean(8, Boolean.valueOf(matchesRecords.get(i).get(7)));
                
                System.out.println(statement); 
                statement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		
		// open connection
		// execute a query -> constructed with String concatenation
		try {
			System.out.println("Connecting to database ....");
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			
			insertMatches(conn, "matches.csv");
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
