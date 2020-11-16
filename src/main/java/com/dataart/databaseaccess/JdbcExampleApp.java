package com.dataart.databaseaccess;

import com.dataart.databaseaccess.model.Person;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import static com.dataart.databaseaccess.utils.Utils.getConnection;

public class JdbcExampleApp {

    public static void main(String[] args) {
        String createTable =
                "CREATE TABLE \"Persons\"(" +
                    "\"id\" int," +
                    "\"name\" varchar," +
                    "\"surname\" varchar," +
                    "CONSTRAINT \"PK_100\" PRIMARY KEY(\"id\"));";
        boolean created = create(createTable);
        System.out.println(" Created successfully?: " + created);

        String insertNewPerson = "INSERT INTO \"People\"" +
                "VALUES (11,'Peter','Wilson');";
        int row = update(insertNewPerson);
        System.out.println("Person added to row " + row);

        String read = "SELECT * FROM \"People\"";
        List<Person> people = read(read);
        System.out.println(people.size() + " people were returned");
        for (Person person : people) {
            System.out.println(person.toString());
        }
    }

    public static List<Person> read(String query) {
        List<Person> people = new LinkedList<>();
        Connection conn = getConnection("postgres");
        if (conn == null) {
            System.out.println("Connection was not established successfully");
            return people;
        }
        Statement stmt = null;

        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            if (result.wasNull()) {
                System.out.println("Query did not find matching data.");
                conn.rollback();
                return people;
            }
            conn.commit();
            while (result.next()) {
                Person person = new Person(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("surname")
                );
                people.add(person);
            }
        } catch (SQLException e) {
            handleException(conn, e);
        } finally {
            closeResources(stmt, conn);
        }
        return people;
    }

    public static int update(String query) {
        Connection conn = getConnection("postgres");
        int result = 0;
        if (conn == null) {
            System.out.println("Connection was not established successfully");
            return result;
        }
        Statement stmt = null;

        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            result = stmt.executeUpdate(query);
            if (result == 0) {
                throw new SQLException("Update unsuccessful.");
            }
            conn.commit();
        } catch (SQLException se) {
            handleException(conn, se);
        } finally {
            closeResources(stmt, conn);
        }
        return result;
    }

    public static boolean create(String query) {
        boolean result = false;
        Connection conn = getConnection("postgres");
        if (conn == null) {
            System.out.println("Connection was not established successfully");
            return result;
        }
        Statement stmt = null;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            result = stmt.execute(query);
            if (!result) {
                throw new SQLException("Query execution was not successful.");
            }
            conn.commit();
        } catch (SQLException se) {
            handleException(conn, se);
        } finally {
            closeResources(stmt, conn);
        }
        return result;
    }

    private static void handleException(Connection conn, SQLException se) {
        System.out.println("Statement was not executed successfully.\n" + se.getMessage());
        try {
            conn.rollback();
        } catch (SQLException e) {
            System.out.println("Connection could not rollback the changes:\n%s." + e.getMessage());
        }
    }

    private static void closeResources(Statement stmt, Connection conn) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("Could not close statement.\n" + e.getMessage());
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Could not close connection.\n" + e.getMessage());
        }
    }
}
