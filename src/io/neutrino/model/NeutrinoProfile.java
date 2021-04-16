package io.neutrino.model;

import io.neutrino.Neutrino;
import io.neutrino.api.database.ComparisonOperator;
import io.neutrino.api.database.DatabaseModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class NeutrinoProfile extends DatabaseModel {

    private String uuid;

    public NeutrinoProfile() {
        uuid = null;
        created = LocalDateTime.now();
        updated = created;
        active = true;
    }

    public NeutrinoProfile(UUID uuid) {
        this.uuid = uuid.toString();
        created = LocalDateTime.now();
        updated = created;
        active = true;
        loadBy("uuid", this.uuid);
    }

    public String getUUID() {
        return uuid;
    }

    private void setUUID(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public void createTable() {
        Neutrino.getInstance().getQueryBuilder()
                .createTable(getTableName())
                .ifNotExists()
                .withAttribute("id", "INT", false)
                .withPrimaryKey("id")
                .withAttribute("uuid", "VARCHAR(40)", false)
                .withAttribute("created", "DATETIME", false)
                .withAttribute("updated", "DATETIME")
                .withAttribute("active", "BOOLEAN", 1)
                .execute();
    }

    @Override
    protected void insert() {
        Neutrino.getInstance().getQueryBuilder()
                .insertInto(getTableName())
                .columns("uuid", "created", "updated", "active")
                .values(uuid, created, updated, active)
                .execute();
    }

    @Override
    protected void update() {
        updated = LocalDateTime.now();
        Neutrino.getInstance().getQueryBuilder()
                .update(getTableName())
                .set("updated", updated)
                .set("active", active)
                .where("id", ComparisonOperator.EQUALS, getID())
                .execute();
    }

    @Override
    public boolean exists() {
        ResultSet rs = Neutrino.getInstance().getQueryBuilder()
                .select("COUNT(*)")
                .from(getTableName())
                .where("uuid", ComparisonOperator.EQUALS, uuid)
                .execute();
        if (getID() == 0) {
            try {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public void loadBy(String key, Object value) {
        ResultSet rs = Neutrino.getInstance().getQueryBuilder()
                .select("id", "created", "updated", "active")
                .from(getTableName())
                .where(key, ComparisonOperator.EQUALS, value)
                .execute();
        try {
            if (rs.next()) {
                setID(rs.getInt(1));
                setCreated(rs.getTimestamp(2));
                Timestamp updatedTimestamp = rs.getTimestamp(3);
                if (updatedTimestamp != null) {
                    setUpdated(updatedTimestamp);
                }
                setActive(rs.getBoolean(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
