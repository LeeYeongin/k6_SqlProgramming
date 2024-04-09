package world_practice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Practice04 {
	public static void main(String[] args) throws ClassNotFoundException {

		Scanner sc = new Scanner(System.in);
		System.out.print("Code: ");
		String code = sc.next();

		Statement st = null;
		ResultSet rs = null;

		// 데이터베이스 연결 객체 가져오기
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world", "scott", "tiger")) {

			// 질의 객체 생성
			st = con.createStatement();
			rs = st.executeQuery(
					"SELECT Name, Population FROM city WHERE CountryCode = '" + code + "' ORDER BY Population DESC");

			// 결과 출력
			while (rs.next()) {
				System.out.println(rs.getString("Name") + "," + rs.getInt("Population"));
			}

		} catch (Exception e) {
			try {
				if (st != null)
					st.close();
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
