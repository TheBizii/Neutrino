package io.neutrino.model;

import io.neutrino.Neutrino;
import io.neutrino.api.database.DatabaseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class NeutrinoProfile extends DatabaseModel {

    private UUID uuid;

    public NeutrinoProfile() {
        uuid = null;
        created = LocalDateTime.now();
        updated = created;
        active = true;
    }

    public NeutrinoProfile(UUID uuid) {
        this.uuid = uuid;
        created = LocalDateTime.now();
        updated = created;
        active = true;
        load();
    }

    public UUID getUUID() {
        return uuid;
    }

    private void setUUID(String uuid) {
        this.uuid = UUID.fromString(uuid);
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
    public void save() {
        if (exists()) {
            update();
        } else {
            insert();
        }
    }

    @Override
    protected void insert() {
        Neutrino.getInstance().getQueryBuilder()
                .insert()
                .columns("uuid", "created", "active")
                .values(uuid, created, active)
                .execute();
    }

    @Override
    protected void update() {
        updated = LocalDateTime.now();
        /*String qry = "UPDATE neutrino_profile " +
                "SET " +
                "updated = '" + updated + "', " +
                "active = " + active + " " +
                "WHERE " +
                "uuid = '" + uuid + "'";
        Neutrino.getInstance().getDatabase().execute(qry);*/
    }

    @Override
    public boolean exists() {
        if (getID() == 0) {
            String qry = "SELECT COUNT(*) FROM neutrino_profile WHERE uuid = '" + uuid + "'";
            ResultSet rs = Neutrino.getInstance().getDatabase().executeAndReturn(qry);
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

    @Override
    public void load() {
        String qry = "SELECT id, created, updated, active FROM neutrino_profile WHERE uuid = '" + uuid + "'";
        ResultSet rs = Neutrino.getInstance().getDatabase().executeAndReturn(qry);
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
