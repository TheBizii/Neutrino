package io.neutrino.api.database.query;

import io.neutrino.Neutrino;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateTableQuery {

    private String tableName;
    private boolean ifNotExists = false;
    private List<String> attributes = new ArrayList<>();
    // If there are multiple columns marked as primary key, a composite key is created.
    private List<String> primaryKeys = new ArrayList<>();

    public CreateTableQuery(String tableName) {
        this.tableName = tableName;
    }

    public CreateTableQuery ifNotExists() {
        ifNotExists = true;
        return this;
    }

    public CreateTableQuery withAttribute(String name, String type) {
        name = fixAttributeName(name);
        return withAttribute(name, type, true);
    }

    public CreateTableQuery withAttribute(String name, String type, boolean allowNull) {
        name = fixAttributeName(name);
        attributes.add(name + " " + type.toUpperCase() + (allowNull ? "" : " NOT NULL"));
        return this;
    }

    public CreateTableQuery withAttribute(String name, String type, Object defaultValue) {
        name = fixAttributeName(name);
        return withAttribute(name, type, true, defaultValue);
    }

    public CreateTableQuery withAttribute(String name, String type, boolean allowNull, Object defaultValue) {
        name = fixAttributeName(name);
        attributes.add(name + " " + type.toUpperCase() + (allowNull ? "" : " NOT NULL") + " DEFAULT " + defaultValue);
        return this;
    }

    public CreateTableQuery withPrimaryKey(String attributeName) {
        attributeName = fixAttributeName(attributeName);
        primaryKeys.add(attributeName);
        return this;
    }

    public CreateTableQuery withCompositePrimaryKey(String... attributeNames) {
        for(String attributeName : attributeNames) {
            withPrimaryKey(attributeName);
        }

        return this;
    }

    public void execute() {
        boolean ignorePrimaryKeys = false;
        if(primaryKeys.size() == 1) {
            String attributeName = fixAttributeName(primaryKeys.get(0));
            for(int i = 0; i < attributes.size(); i++) {
                if(attributes.get(i).startsWith(attributeName)) {
                    attributes.set(i, attributes.get(i) + " PRIMARY KEY AUTO_INCREMENT");
                    ignorePrimaryKeys = true;
                }
            }
        }

        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        if(ifNotExists) {
            sb.append("IF NOT EXISTS ");
        }
        sb.append(tableName).append(" (");
        for(String attribute : attributes) {
            sb.append(attribute).append(", ");
        }

        if(!ignorePrimaryKeys) {
            sb.append("PRIMARY KEY (");
            for(String primaryKey : primaryKeys) {
                sb.append(primaryKey).append(", ");
            }
        }

        sb = new StringBuilder(sb.substring(0, sb.length() - 2));
        sb.append(")");

        if(!ignorePrimaryKeys) {
            sb.append(")");
        }

        try {
            PreparedStatement ps = Neutrino.getInstance().getDatabase().prepareStatement(sb.toString());
            Neutrino.getInstance().getDatabase().execute(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String fixAttributeName(String attributeName) {
        return attributeName.replaceAll(" ", "_");
    }

}
