package edu.gatech.cs6310.dao;

import java.util.List;

public interface DatabaseClient {

    void save(Object object);

    List selectWhere(String table, String property, String value);

    List selectWhere(String table, String property, String value, String property2, String value2);

    List selectAll(String table);

    List selectLike(String table, String property, String value, String property2, String value2, String property3, String value3);

    Boolean deleteWhere(String table, String property, String value);

    Boolean deleteWhere(String table, String property, String value, String property2, String value2);

    Boolean updateWhere(String table, String setProperty, String setValue, String whereProperty, String whereValue);

}
