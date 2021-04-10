package io.neutrino.api.database.query;

import io.neutrino.Neutrino;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertQuery {

    private String tableName = "";
    private List<String> columns = new ArrayList<>();
    private List<Object> values = new ArrayList<>();

    public InsertQuery into(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public InsertQuery columns(String... columnNames) {
        for(String columnName : columnNames) {
            columns.add(fixAttributeName(columnName));
        }

        return this;
    }

    public InsertQuery values(Object... values) {
        if(values.length != columns.size() && columns.size() != 0) {
            int count = Integer.min(values.length, columns.size());
            for(int i = 0; i < count; i++) {
                this.values.add(values[i]);
            }
        } else {
            this.values.addAll(Arrays.asList(values));
        }

        return this;
    }

    public void execute() {
        StringBuilder sb = new StringBuilder("INSERT INTO " + tableName);
        if(columns.size() > 0) {
            sb.append(" (");
            for(String column : columns) {
                sb.append(column + ", ");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 2));
        }
        sb.append(" VALUES (");
        if(values.size() != 0) {
            for(int i = 0; i < values.size(); i++) {
                sb.append("?, ");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 2));
        }

        sb.append(")");

        try {
            PreparedStatement statement = Neutrino.getInstance().getDatabase().prepareStatement(sb.toString());
            for(int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            Neutrino.getInstance().getDatabase().execute(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String fixAttributeName(String attributeName) {
        return attributeName.replaceAll(" ", "_");
    }

}
