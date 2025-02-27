package com.ll.simpleDb;

import lombok.Setter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class SimpleDb {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private Connection connection;
    @Setter
    private boolean devMode = false;

    // 생성자: 데이터베이스 연결 정보 초기화
    public SimpleDb(String host, String user, String password, String dbName) {
        this.dbUrl = "jdbc:mysql://" + host + ":3307/" + dbName; // JDBC URL
        this.dbUser = user;                                    // 사용자 이름
        this.dbPassword = password;                            // 비밀번호

        // 연결 초기화
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            if (devMode) {
                System.out.println("데이터베이스에 성공적으로 연결되었습니다.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 연결 실패: " + e.getMessage());
        }
    }

    public boolean selectBoolean(String sql, List<Object> params) {
        return _run(sql, Boolean.class, params);
    }

    public String selectString(String sql, List<Object> params) {
        return _run(sql, String.class, params);
    }

    public Long selectLong(String sql, List<Object> params) {
        return _run(sql, Long.class, params);
    }

    public List<Long> selectLongs(String sql, List<Object> params) {
        List<Map<String, Object>> maps = selectRows(sql, params);
        return maps.stream()
                .map(map -> (Long) map.values().iterator().next()).toList();
    }

    public LocalDateTime selectDateTime(String sql, List<Object> params) {
        return _run(sql, LocalDateTime.class, params);
    }

    public Map<String, Object> selectRow(String sql, List<Object> params) {
        return _run(sql, Map.class, params);
    }

    public List<Map<String, Object>> selectRows(String sql, List<Object> params) {
        return _run(sql, List.class, params);
    }

    public <T> List<T>selectRows(String sql, List<Object> params,Class<T> cls) {
        return selectRows(sql,params).stream()
                .map(map->{
                    return Util.mapToObj(map, cls);
                }).toList();
    }

    public int delete(String sql, List<Object> params) {
        return _run(sql, Integer.class, params);
    }

    public int update(String sql, List<Object> params) {
        return _run(sql, Integer.class, params);
    }

    public long insert(String sql, List<Object> params) {
        return _run(sql, Long.class, params);
    }


    public int run(String sql, Object... params) {
        return _run(sql, Integer.class, Arrays.stream(params).toList());
    }

    // SQL 실행 (PreparedStatement와 파라미터)
    private <T> T _run(String sql, Class<T> cls, List<Object> params) {
        System.out.println("sql: " + sql);
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(stmt, params);

            if (sql.startsWith("SELECT")) {
                ResultSet rs = stmt.executeQuery();// 결과가 있는 것
                return parseSelectQuery(rs, cls);
            }

            if (sql.startsWith("INSERT")) {
                if (cls.equals(Long.class)) {
                    stmt.executeUpdate();
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        return cls.cast(rs.getLong(1));
                    }
                }
            }


            return cls.cast(stmt.executeUpdate());
        } catch (SQLException e) {
            throw new RuntimeException("SQL 실행 실패: " + e.getMessage());
        }
    }

    private <T> T parseSelectQuery(ResultSet rs, Class<T> cls) throws SQLException {

        if (cls.equals(Boolean.class)) {
            rs.next();
            return cls.cast(rs.getBoolean(1));
        } else if (cls.equals(String.class)) {
            rs.next();
            return cls.cast(rs.getString(1));
        } else if (cls.equals(Long.class)) {
            rs.next();
            return cls.cast(rs.getLong(1));
        } else if (cls.equals(LocalDateTime.class)) {
            rs.next();
            return cls.cast(rs.getTimestamp(1).toLocalDateTime());
        } else if (cls.equals(Map.class)) {
            rs.next();
            Map<String, Object> row = rsRowToMap(rs);

            return cls.cast(row);
        } else if (cls.equals(List.class)) {
            List<Map<String, Object>> rows = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> row = rsRowToMap(rs);
                rows.add(row);
            }

            return cls.cast(rows);
        }

        throw new RuntimeException();
    }

    private Map<String, Object> rsRowToMap(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Map<String, Object> row = new HashMap<>();

        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String cname = metaData.getColumnName(i);
            row.put(cname, rs.getObject(i));
        }
        return row;
    }

    // PreparedStatement에 파라미터 바인딩
    private void setParams(PreparedStatement stmt, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i)); // '?' 위치에 값 설정
        }
    }

    public Sql genSql() {
        return new Sql(this);
    }


}