package io.neutrino.api.database.query;

import io.neutrino.Neutrino;
import io.neutrino.api.database.ComparisonOperator;
import io.neutrino.api.database.DatabaseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectQuery {

    private String tableName;
    private List<String> select = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private List<String> whereFields = new ArrayList<>();
    private List<Object> whereValues = new ArrayList<>();

    public SelectQuery(String... select) {
        if(select.length == 0) {
            this.select.add("*");
        } else {
            this.select.addAll(Arrays.asList(select));
        }
    }

    public SelectQuery from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SelectQuery join(JoinType joinType, Class<? extends DatabaseModel> targetModel, String targetColumn, Class<? extends DatabaseModel> model, String modelColumn) {
        joins.add(joinType.toString().toUpperCase() +
                " JOIN " + targetModel.getSimpleName().toLowerCase() +
                " ON " + model.getSimpleName().toLowerCase() + "." + modelColumn +
                " = " + targetModel.getSimpleName().toLowerCase() + "." + targetColumn);
        return this;
    }

    public SelectQuery where(String column, ComparisonOperator operator, Object value) {
        whereFields.add(column + " " + operator.toString());
        whereValues.add(value);
        return this;
    }

    //TODO: Order by, limit

    public ResultSet execute() {
        StringBuilder sb = new StringBuilder("SELECT ");
        if(select.size() == 0) {
            sb.append("* ");
        } else {
            for(String s : select) {
                sb.append(s).append(", ");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 2));
            sb.append(" ");
        }
        sb.append("FROM ").append(tableName).append(" ");
        for(String join : joins) {
            sb.append(join).append(" ");
        }
        if(whereFields.size() != 0) {
            sb.append("WHERE ");
            for (int i = 0; i < whereFields.size(); i++) {
                sb.append(whereFields.get(i)).append(" ?, ");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 2));
        }

        try {
            PreparedStatement ps = Neutrino.getInstance().getDatabase().prepareStatement(sb.toString());
            for(int i = 0; i < whereValues.size(); i++) {
                ps.setObject(i + 1, whereValues.get(i));
            }
            return Neutrino.getInstance().getDatabase().executeAndReturn(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
