package io.neutrino.api.database.query;

import io.neutrino.Neutrino;
import io.neutrino.api.database.ComparisonOperator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdateQuery {

    private String tableName;
    private List<String> setFields = new ArrayList<>();
    private List<Object> setValues = new ArrayList<>();
    private List<String> whereFields = new ArrayList<>();
    private List<Object> whereValues = new ArrayList<>();

    public UpdateQuery(String tableName) {
        this.tableName = tableName;
    }

    public UpdateQuery set(String field, Object value) {
        setFields.add(field);
        setValues.add(value);
        return this;
    }

    public UpdateQuery where(String column, ComparisonOperator operator, Object value) {
        whereFields.add(column + " " + operator.toString());
        whereValues.add(value);
        return this;
    }

    public void execute() {
        StringBuilder sb = new StringBuilder("UPDATE " + tableName + " SET ");
        if(setFields.size() > 0) {
            for (String setField : setFields) {
                sb.append(setField).append(" = ?, ");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 2));
        }
        sb.append(" WHERE ");
        if(whereFields.size() > 0) {
            for(String whereField : whereFields) {
                sb.append(whereField).append(" ?, ");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 2));
        }

        try {
            PreparedStatement ps = Neutrino.getInstance().getDatabase().prepareStatement(sb.toString());
            for(int i = 1; i <= setValues.size(); i++) {
                ps.setObject(i, setValues.get(i - 1));
            }
            for(int i = 1; i <= whereValues.size(); i++) {
                ps.setObject(setValues.size() + i, whereValues.get(i - 1));
            }
            Neutrino.getInstance().getDatabase().execute(ps);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
