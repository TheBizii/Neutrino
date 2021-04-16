package io.neutrino.api.database;

import io.neutrino.api.database.query.CreateTableQuery;
import io.neutrino.api.database.query.InsertQuery;
import io.neutrino.api.database.query.SelectQuery;
import io.neutrino.api.database.query.UpdateQuery;

import javax.annotation.Nonnull;

public class QueryBuilder {

    public CreateTableQuery createTable(String name) {
        return new CreateTableQuery(name);
    }

    public InsertQuery insertInto(String tableName) {
        return new InsertQuery(tableName);
    }

    public UpdateQuery update(@Nonnull String tableName) {
        return new UpdateQuery(tableName);
    }

    public SelectQuery select(String... select) {
        return new SelectQuery(select);
    }
}
