package main.java.Base_Service;

import main.java.Base_Service.dao.UserDAO;
import main.java.Base_Service.dataSet.UsersDataSet;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
//сервис для работы с подключением к базам, выведении в консоль инфы о коннекте.
//имеет методы для добавления пользователя, получения, дропа таблицы.
public class DBService {
    private final Connection connection;

    public DBService() {
        this.connection = getMysqlConnection();
    }

    public static Connection getH2Connections() {
        try {
            String url = "jdbc:h2:./h2db";
            String name = "tully";
            String pass = "tully";

            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(url);
            ds.setUser(name);
            ds.setPassword(pass);

            Connection connection = null;

            connection = DriverManager.getConnection(url, name, pass);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;

    }

    public UsersDataSet getUser(long id) throws DBExeptions {
        try {
            return (new UserDAO(connection).get(id));
        } catch (SQLException e) {
            throw new DBExeptions(e);
        }
    }

    public long addUser(String name) throws DBExeptions {
        try {
            connection.setAutoCommit(false);
            UserDAO dao = new UserDAO(connection);
            dao.createTable();
            dao.insertUser(name);
            connection.commit();
            return dao.getUserId(name);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBExeptions(e);
        } finally {
            try {
                connection.setAutoCommit(true);

            } catch (SQLException ignore) {
            }
        }

    }
    public void cleanUp() throws DBExeptions {
        UserDAO dao = new UserDAO(connection);
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBExeptions(e);
        }
    }

    public void printConnectInfo() {
        try {
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").
                    append("localhost:").
                    append("3306/").
                    append("db_example?").
                    append("user=tully&").
                    append("password=tully");
            System.out.printf("URL: " + url + "\n");

            Connection connection = null;

            connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }


}
