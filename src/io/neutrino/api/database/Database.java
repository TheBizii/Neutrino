package io.neutrino.api.database;

import io.neutrino.Neutrino;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String url;
    private String databaseName;
    private String user;
    private String password;
    private Connection con;
    private List<DatabaseModel> registeredModels = new ArrayList<>();

    public Database(String url, String databaseName, String user, String password) {
        this.url = url;
        if(!databaseName.trim().equals("")) {
            this.databaseName = databaseName;
        } else {
            throw new IllegalArgumentException("Database name can not be null.");
        }
        this.user = user;
        this.password = password;
    }

    private void connect() {
        try {
            if(!isConnected()) {
                con = DriverManager.getConnection(url + databaseName, user, password);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean isConnected() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        connect();
        return con.prepareStatement(sql);
    }

    private void disconnect() {
        if(isConnected()) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerModel(DatabaseModel model) {
        registeredModels.add(model);
        model.createTable();
    }

    public void execute(PreparedStatement preparedStatement) {
        Neutrino neutrino = Neutrino.getInstance();
        connect();
        neutrino.log(preparedStatement.toString());
        if(isConnected()) {
            try {
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            neutrino.logError("Something went wrong while establishing a connection with database. Fix the error and retry.");
        }

        disconnect();
    }

    public ResultSet executeAndReturn(PreparedStatement preparedStatement) {
        Neutrino neutrino = Neutrino.getInstance();
        connect();
        neutrino.log(preparedStatement.toString());
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return null;
    }
}
