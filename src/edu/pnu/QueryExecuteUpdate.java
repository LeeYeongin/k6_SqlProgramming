package edu.pnu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

// Insert
public class QueryExecuteUpdate {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int num, year, select = 0;
		String s1, s2;

		// No suitable driver found for jdbc:h2 오류
		// package Build path -> library -> class path -> program(x86) -> H2 -> bin ->
		// jar파일
		try (Connection con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/.h2/sqlprg", "sa", "abcd")) {
			
			while(true) {
				System.out.print("(0)종료 (1) 테이블 생성 (2) 데이터 입력 (3) Board 테이블 데이터 삭제\n"
						+ "(4) Member 테이블 데이터 삭제 (5) Board 테이블 데이터 수정 (6) Member 테이블 데이터 수정\n"
						+ "(7) Member 테이블 조회 (8) Board 테이블 조회: ");
				select = sc.nextInt();
				switch (select) {
					case 0:
						System.out.println("Done");
						return;
					case 1:
						if (!createTable(con))
							return;
						break;
					case 2:
						if (!InsertData(con))
							return;
						break;
					case 3:
						System.out.print("Board에서 삭제할 num을 입력하세요: ");
						num = sc.nextInt();
						if (!DeleteBoardData(con, num))
							return;
						break;
					case 4:
						System.out.print("Member에서 삭제할 id를 입력하세요: ");
						num = sc.nextInt();
						if (!DeleteMemData(con, num))
							return;
						break;
					case 5:
						System.out.print("Board에서 수정할 num을 입력하세요: ");
						num = sc.nextInt();
						sc.nextLine();
						System.out.print("새로운 title: ");
						s1 = sc.nextLine();
						System.out.print("새로운 content: ");
						s2 = sc.nextLine();
						System.out.println(s2);
						if (!UpdateBoardData(con, num, s1, s2))
							return;
						break;
					case 6:
						String username = null, password = null;
						
						System.out.print("Member에서 수정할 id를 입력하세요: ");
						num = sc.nextInt();
						sc.nextLine();
						
						System.out.print("새로운 username를 입력하세요(수정할 값이 아니라면 enter key를 누르세요): ");
						username = sc.nextLine();
						if(username.isEmpty())
							username = null;

						System.out.print("새로운 password를 입력하세요(수정할 값이 아니라면 enter key를 누르세요): ");
						password = sc.nextLine();
						if(password.isEmpty())
							password = null;
						
						System.out.print("새로운 birthyear를 입력하세요(수정할 값이 아니라면 0을 입력하세요): ");
						year = sc.nextInt();
						
						if(!UpdateMemData(con, num, username, password, year))
							return;
						break;
					case 7:
						if(!showUserList(con))
							return;
						break;
					case 8:
						if(!showBoardList(con))
							return;
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		System.out.println("테이블이 생성되었습니다.");
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

	public static boolean UpdateMemData(Connection con, int id, String user, String pwd, int year) {
		try {
			// 코드 1
			String str = "";
			if(user != null) {
				str += "username='" + user + "' ";
			}
			if(pwd != null) {
				if(!str.isEmpty()) str += ",";
				str += "password='" + pwd + "' ";
			}
			if(year != 0) {
				if(!str.isEmpty()) str += ",";
				str += "birthyear='" + year + "' ";
			}
			
			String sql = "Update member set " + str + "where id=" + id;
			Statement st = con.createStatement();
			st.executeUpdate(sql);
			
			
			// 코드 2?
//			PreparedStatement psmt = con.prepareStatement(
//							"UPDATE Member " 
//							+ "SET username = CASE WHEN ? IS NOT NULL THEN ? ELSE username END, "
//							+ "password = CASE WHEN ? IS NOT NULL THEN ? ELSE password END, " 
//							+ "birthyear = CASE WHEN ? != 0 THEN ? ELSE birthyear END "
//							+ "WHERE id = ?");
//			psmt.setString(1, user);
//			psmt.setString(2, user);
//			psmt.setString(3, pwd);
//			psmt.setString(4, pwd);
//			psmt.setInt(5, year);
//			psmt.setInt(6, year);
//			psmt.setInt(7, id);
//
//			int result = psmt.executeUpdate();
//			if (result == 0)
//				System.out.println("해당 데이터가 없습니다.");
//			else
//				System.out.println("Member 데이블에서 " + id + "데이터 " + result + "개가 수정되었습니다.");
			
			System.out.println("Member 테이블이 수정되었습니다.");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean showUserList(Connection con) {
		try {
			String sql = "Select * from Member";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				System.out.print(rs.getString("username")+","); 
				System.out.print(rs.getString("password")+",");
				System.out.print(rs.getInt("birthyear")+",");
				System.out.print(rs.getString("regidate")+"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static boolean showBoardList(Connection con) {
		try {
			PreparedStatement psmt = con.prepareStatement("Select * from Board limit ?,5");

			for(int i=0; i<=9; i++) {
				psmt.setInt(1, i*5);				
				ResultSet rs = psmt.executeQuery();
				
				while(rs.next()) {
					System.out.print(rs.getString("num")+","); 
					System.out.print(rs.getString("title")+",");
					System.out.print(rs.getString("content")+",");
					System.out.print(rs.getInt("id")+",");
					System.out.print(rs.getString("postdate")+",");
					System.out.print(rs.getInt("visitcount")+"\n");
				}
				System.out.println("------------------------------------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
