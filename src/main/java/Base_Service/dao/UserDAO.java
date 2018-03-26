package main.java.Base_Service.dao;

import main.java.Base_Service.dataSet.UsersDataSet;
import main.java.Base_Service.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDAO {
    private Executor executor;

    public UserDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public UsersDataSet get(long id) throws SQLException {
        return executor.execQuery("select * from users where id=" + id, resultSet -> {
            resultSet.next();
            return new UsersDataSet(resultSet.getLong(1), resultSet.getString(2));
        });

    }

    public long getUserId(String name) throws SQLException {
        return executor.execQuery("select * from users where user_name='" + name + "'", resultSet -> {
            resultSet.next();
            return resultSet.getLong(1);
        });
    }

    public void insertUser(String name) throws SQLException {
        executor.execUpdate("insert into users (user_name) values ('" + name + "')");
    }

    public void createTable() throws SQLException {
        executor.execUpdate("create table if not exists users (id bigint auto_increment, " +
                "user_name varchar(256), primary key(id))");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("drop table users");
    }
}
