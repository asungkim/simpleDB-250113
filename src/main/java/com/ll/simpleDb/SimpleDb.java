package com.ll.simpleDb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleDb {
    private final String host; // 데이터베이스 서버 주소 (예: localhost)
    private final String user; // 데이터베이스 사용자명 (예: root)
    private final String password; // 데이터베이스 비밀번호
    private final String databaseName; // 사용할 데이터베이스 이름
    private boolean devMode = false; // 개발 모드 여부 (로그 출력용)

    public SimpleDb(String host, String user, String password, String databaseName) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.databaseName = databaseName;
    }

    /**
     * 개발 모드 설정 (true일 경우 실행 쿼리를 출력)
     */
    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    /**
     * 데이터베이스 연결을 생성합니다.
     * @return Connection 객체
     */
    private Connection getConnection() throws SQLException {
        // JDBC URL에 Docker에서 매핑된 포트를 반영
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", host, 3307, databaseName);
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    /**
     * 데이터베이스에 쿼리를 실행합니다. (결과를 반환하지 않는 쿼리용)
     * @param sql 실행할 SQL 쿼리
     */
    public void run(String sql) {
        // try-with-resources를 사용해 자원을 자동으로 해제
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (devMode) {
                System.out.println("Executing SQL: " + sql);
            }

            statement.executeUpdate(); // INSERT, UPDATE, DELETE 등에 사용
        } catch (SQLException e) {
            // 예외 처리 및 에러 메시지 출력
            System.err.println("Error executing SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 데이터베이스에서 쿼리를 실행하고 결과를 반환합니다.
     * @param sql 실행할 SQL 쿼리
     * @return ResultSet 객체 (결과를 사용해야 함)
     */
    public ResultSet executeQuery(String sql) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            if (devMode) {
                System.out.println("Executing SQL: " + sql);
            }

            return statement.executeQuery(); // SELECT 쿼리에 사용
        } catch (SQLException e) {
            // 예외 처리 및 에러 메시지 출력
            System.err.println("Error executing SQL: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}