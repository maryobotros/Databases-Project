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

public class MembersInsert {
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
	
	public static void insertMembers(Connection conn, String filename) {
//		List<List<String>> records = getRecordFromFile(filename);
//		sqlizeData(records);
		
		PreparedStatement statement = null;
		String insertSql = " INSERT IGNORE  into members values (?, ?, ?, ?)";
		
		// Reading the csv file 
		List<List<String>> membersRecords = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File("members.csv"));) {
			while (scanner.hasNextLine()) {
				membersRecords.add(getRecordFromLine(scanner.nextLine()));
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sqlizeData(membersRecords);
		
		try {
			statement = conn.prepareStatement(insertSql);
			for(int i = 0; i < membersRecords.size(); i++) {
                // setXXX() methods to set the values of these ?
				statement.setInt(1, Integer.valueOf(membersRecords.get(i).get(0)));
				statement.setInt(2, Integer.valueOf(membersRecords.get(i).get(1)));
				statement.setDate(3, Date.valueOf(membersRecords.get(i).get(2)));
				if(membersRecords.get(i).size() < 4) {
					statement.setNull(4,  Types.DATE);
				}
				else {
					statement.setDate(4,  Date.valueOf(membersRecords.get(i).get(3)));
				}
                
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
			
			insertMembers(conn, "members.csv");
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
