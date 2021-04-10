package io.neutrino.api.database;

import io.neutrino.api.database.query.CreateTableQuery;
import io.neutrino.api.database.query.InsertQuery;

public class QueryBuilder {

    public CreateTableQuery createTable(String name) {
        return new CreateTableQuery(name);
    }

    public InsertQuery insert() {
        return new InsertQuery();
    }
}
