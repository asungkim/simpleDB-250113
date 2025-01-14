package com.ll.simpleDb;

import java.time.LocalDateTime;
import java.util.*;

public class Sql {
    private SimpleDb simpleDb;
    private final List<Object> params;
    private StringBuilder sqlBuilder;

    public Sql(SimpleDb simpleDb) {
        this.sqlBuilder = new StringBuilder();
        this.simpleDb = simpleDb;
        this.params = new ArrayList<>();
    }

    public Sql append(String sqlLine) {
        sqlBuilder.append(sqlLine);
        sqlBuilder.append(" ");
        return this;
    }

    public Sql append(String sqlLine, Object... args) {
        this.params.addAll(Arrays.stream(args).toList());
        sqlBuilder.append(sqlLine);
        sqlBuilder.append(" ");
        return this;
    }

    public int update() {
        return simpleDb.update(sqlBuilder.toString(),params);
    }

    public int delete() {
        return simpleDb.delete(sqlBuilder.toString(), params);
    }

    public long insert() {
        return 1;
    }

    public List<Map<String, Object>> selectRows() {
        return simpleDb.selectRows(sqlBuilder.toString(),params);
    }

    public Map<String, Object> selectRow() {

        return simpleDb.selectRow(sqlBuilder.toString(),params);
    }

    public Long selectLong() {
        return simpleDb.selectLong(sqlBuilder.toString(),params);
    }

    public String selectString() {
        return simpleDb.selectString(sqlBuilder.toString(),params);
    }

    public Boolean selectBoolean() {
        return simpleDb.selectBoolean(sqlBuilder.toString(),params);
    }

    public LocalDateTime selectDatetime() {
        return simpleDb.selectDateTime(sqlBuilder.toString(),params);
    }


}
