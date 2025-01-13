package com.ll.simpleDb;

public class Sql {
    private StringBuilder sqlBuilder;

    public Sql() {
        this.sqlBuilder = new StringBuilder();
    }

    public Sql append(String sqlLine) {
        return this;
    }

    public Sql append(String sqlLine,Object... args ) {
        return this;
    }

    public long insert() {
        return 0;
    }
}
