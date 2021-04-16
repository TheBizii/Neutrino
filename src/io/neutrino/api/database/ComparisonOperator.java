package io.neutrino.api.database;

public enum ComparisonOperator {

    EQUALS("="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUALS("<="),
    NOT_EQUAL("!="),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE");

    String str;

    ComparisonOperator(String str) {
        this.str = str;
    }

    public String toString() {
        return str;
    }
}
