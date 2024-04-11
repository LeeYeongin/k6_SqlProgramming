package edu.pnu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

// Insert
public class QueryExecuteUpdate {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int num, year;
		String s1, s2;

		// No suitable driver found for jdbc:h2 오류
		// package Build path -> library -> class path -> program(x86) -> H2 -> bin ->
		// jar파일
		try (Connection con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/.h2/sqlprg", "sa", "abcd")) {
			if (!createTable(con))
				return;
			if (!InsertData(con))
				return;

			System.out.print("Board에서 삭제할 num을 입력하세요: ");
			num = sc.nextInt();
			if (!DeleteBoardData(con, num))
				return;

			System.out.print("Member에서 삭제할 id를 입력하세요: ");
			num = sc.nextInt();
			if (!DeleteMemData(con, num))
				return;

			System.out.print("Board에서 수정할 num을 입력하세요: ");
			num = sc.nextInt();
			System.out.print("새로운 title: ");
			s1 = sc.next();
			System.out.print("새로운 content: ");
			s2 = sc.next();
			System.out.println(s2);
			if (!UpdateBoardData(con, num, s1, s2))
				return;

//			String username = null, password = null;
//			System.out.print("Member에서 수정할 id를 입력하세요: ");
//			num = sc.nextInt();
//			System.out.print("새로운 username, password, birthyear를 입력하세요: ");

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}

	public static boolean createTable(Connection con) {
		Statement st = null;
		try {
			st = con.createStatement();
			st.execute("DROP TABLE Member IF EXISTS");
			st.execute("CREATE TABLE Member (" + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," + "username varchar(10),"
					+ "password varchar(10)," + "birthyear int not null,"
					+ "regidate date NOT NULL default(curdate()))");

			st.execute("DROP TABLE Board IF EXISTS");
			st.execute("CREATE TABLE Board (" + "num INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
					+ "title varchar(200) NOT NULL," + "content varchar(2000) NOT NULL," + "id int not null,"
					+ "postdate date NOT NULL default(curdate())," + "visitcount int DEFAULT 0)");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static boolean InsertData(Connection con) {
		Random random = new Random();

		try {
			PreparedStatement psmt = con
					.prepareStatement("insert into Member(username, password, birthyear) values(?,?,?)");
			PreparedStatement psmt2 = con
					.prepareStatement("insert into Board(title, content, id, visitcount) values(?,?,?,?)");

			for (int i = 1; i <= 5; i++) {
				int rndyear = random.nextInt(11) + 2000;

				psmt.setString(1, "user" + i);
				psmt.setString(2, "pass" + i);
				psmt.setInt(3, rndyear);

				int result = psmt.executeUpdate();
				System.out.println("Member 테이블에 " + result + "개가 입력되었습니다.");

				for (int j = 1; j <= 10; j++) {
					int rndvisit = random.nextInt(101);

					psmt2.setString(1, "user" + i + ": title" + j);
					psmt2.setString(2, "user" + i + ": content" + j);
					psmt2.setInt(3, i);
					psmt2.setInt(4, rndvisit);

					result = psmt2.executeUpdate();
					System.out.println("Board 테이블에 " + result + "개가 입력되었습니다.");
				}
			}

			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean DeleteBoardData(Connection con, int num) {
		try {
			PreparedStatement psmt = con.prepareStatement("Delete from Board where num=?");
			psmt.setInt(1, num);

			int result = psmt.executeUpdate();
			if (result == 0)
				System.out.println("해당 데이터가 없습니다.");
			else
				System.out.println("Board 데이블에서 " + num + "데이터 " + result + "개가 삭제되었습니다.");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean DeleteMemData(Connection con, int id) {
		try {
			PreparedStatement psmt = con.prepareStatement("Delete from Member where id=?");
			psmt.setInt(1, id);

			int result = psmt.executeUpdate();
			if (result == 0)
				System.out.println("해당 데이터가 없습니다.");
			else
				System.out.println("Member 데이블에서 " + id + "데이터 " + result + "개가 삭제되었습니다.");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean UpdateBoardData(Connection con, int num, String title, String content) {
		try {
			PreparedStatement psmt = con.prepareStatement("Update Board set title=?,content=? where num=?");
			psmt.setString(1, title);
			psmt.setString(2, content);
			psmt.setInt(3, num);

			int result = psmt.executeUpdate();
			if (result == 0)
				System.out.println("해당 데이터가 없습니다.");
			else
				System.out.println("Board 데이블에서 " + num + "데이터 " + result + "개가 수정되었습니다.");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean UpdateMemData(Connection con, int num, String user, String pwd, int year) {
		try {
			PreparedStatement psmt = con.prepareStatement(
							"UPDATE Member " 
							+ "SET username = CASE WHEN ? IS NOT NULL THEN ? ELSE username END, "
							+ "password = CASE WHEN ? IS NOT NULL THEN ? ELSE password END, " 
							+ "birthyear = CASE WHEN ? != 0 THEN ? ELSE password END "
							+ "WHERE id = ?");
			psmt.setString(1, user);
			psmt.setString(2, user);
			psmt.setString(3, pwd);
			psmt.setString(4, pwd);
			psmt.setInt(5, year);
			psmt.setInt(6, year);
			psmt.setInt(7, num);

			int result = psmt.executeUpdate();
			if (result == 0)
				System.out.println("해당 데이터가 없습니다.");
			else
				System.out.println("Board 데이블에서 " + num + "데이터 " + result + "개가 수정되었습니다.");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
