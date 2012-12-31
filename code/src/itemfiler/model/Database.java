package itemfiler.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.h2.jdbc.JdbcSQLException;

public class Database {

	private static final boolean DEBUG = true;
	private static Connection conn;

	public static void init(String path) {
		try {
			Class.forName("org.h2.Driver");
			if (DEBUG)
				System.out.println("connecting to jdbc:h2:"
						+ path.replace(".h2.db", ""));
			conn = DriverManager.getConnection(
					"jdbc:h2:" + path.replace(".h2.db", ""), "", "");

			// check structure
			// - go for the objects table
			Statement stmt = conn.createStatement();
			stmt.executeQuery("SELECT * FROM objects");
			stmt.close();

		} catch (JdbcSQLException e) {
			/*
			 * no pictures table there. thus we go and remove all tables to
			 * avoid collisions and recreate them from scratch
			 */
			Statement stmt;
			try {
				stmt = conn.createStatement();
				try {
					stmt.execute("DROP TABLE objects, tags");
				} catch (Exception e1) {
					// we go here when a drop table is unsuccessful (not
					// existing e.g.)
				}
				stmt.execute("CREATE TABLE objects ("
						+ "oid int NOT NULL AUTO_INCREMENT PRIMARY KEY,"
						+ "name varchar(25)," + "trash boolean DEFAULT false, "
						+ "creationDate varchar(20),"
						+ "path varchar(350) NOT NULL UNIQUE)");
				stmt.execute("CREATE TABLE objects_tags ("
						+ "oid int NOT NULL, tid int NOT NULL)");
				stmt.execute("CREATE TABLE tags ("
						+ "tid int NOT NULL AUTO_INCREMENT PRIMARY KEY,"
						+ "name varchar(200) NOT NULL UNIQUE)");
				stmt.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection() {
		try {
			if (DEBUG)
				System.out.println("connection closed");
			if (null != conn)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void execute(String sql) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			if (DEBUG)
				System.out.println(sql);
			stmt.execute(sql);
			// conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			if (null != stmt)
				stmt.close();
		}
	}

	public static List<String> getStringList(String sql) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			if (DEBUG)
				System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			List<String> result = new ArrayList<String>();
			while (rs.next()) {
				result.add(rs.getString(1));
			}
			return result;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (null != stmt)
				stmt.close();
		}
	}

	public static List<Integer> getIntegerList(String sql) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			if (DEBUG)
				System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			List<Integer> result = new ArrayList<Integer>();
			while (rs.next()) {
				result.add(rs.getInt(1));
			}
			return result;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (null != stmt)
				stmt.close();
		}
	}

	public static int getInteger(String sql) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			if (DEBUG)
				System.out.println(sql + " LIMIT 1");
			ResultSet rs = stmt.executeQuery(sql + " LIMIT 1");
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} finally {
			if (null != stmt)
				stmt.close();
		}
	}

	public static String getString(String sql) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			if (DEBUG)
				System.out.println(sql + " LIMIT 1");
			ResultSet rs = stmt.executeQuery(sql + " LIMIT 1");
			rs.next();
			return rs.getString(1);
		} finally {
			if (null != stmt)
				stmt.close();
		}
	}
}
