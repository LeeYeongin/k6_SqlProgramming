package edu.pnu;

public class DriverLoading {

	public static void main(String[] args) {
		// db 사용을 위한 api 호출을 위한 드라이버 로딩
		
		
		try {
			// 드라이버 로딩 (Driver.class의 인스턴스 생성 요청)
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			System.out.println("로딩 성공");
		} catch (Exception e) {
			System.out.println("로딩 실패");
			System.out.println(e.getMessage());
		} 

	}

}
