package io.neutrino.api.database;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class DatabaseModel {

    private int id = 0;
    protected LocalDateTime created;
    protected LocalDateTime updated;
    protected boolean active = true;
    private final String tableName = getClass().getSimpleName().toLowerCase();

    public int getID() {
        return id;
    }

    protected void setID(int id) {
        this.id = id;
    }

    protected void setCreated(Timestamp timestamp) {
        this.created = timestamp.toLocalDateTime();
    }

    protected void setUpdated(Timestamp timestamp) {
        this.updated = timestamp.toLocalDateTime();
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    protected String getTableName() {
        return tableName;
    }

    public abstract void createTable();
    public abstract boolean exists();
    protected abstract void insert();
    protected abstract void update();

    public void save() {
        if(exists()) {
            update();
        } else {
            insert();
        }
    }

    public void delete() {
        active = false;
        update();
    }

    public void loadBy(String key, Object value) {}
}
