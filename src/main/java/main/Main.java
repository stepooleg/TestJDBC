package main.java.main;

import main.java.Base_Service.DBExeptions;
import main.java.Base_Service.DBService;
import main.java.Base_Service.dataSet.UsersDataSet;

//Тест JDBC. Создаем таблицу в базе, заводим пользователя, получаем его по id и дропаем таблицу.
//Работает с mysql и h2db.
public class Main {
    public static void main(String[] args) {
        try {
            DBService dbService = new DBService();
            dbService.printConnectInfo();
            long userId = dbService.addUser("tully");
            System.out.printf("Adden user id: " + userId);

            UsersDataSet dataSet = dbService.getUser(userId);
            System.out.printf("User data set: " + dataSet);


            dbService.cleanUp();
        } catch (DBExeptions dbExeptions) {
            dbExeptions.printStackTrace();
        }

    }
}
