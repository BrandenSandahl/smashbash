package com.teamSmash;

import org.junit.Test;
import spark.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by branden on 3/3/16 at 18:18.
 */
public class MainTest {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./test");
        Main.createTables(conn);
        return conn;
    }

    //kill the tables so we have fresh data for new tests
    public void endConnection(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE account");
        stmt.execute("DROP TABLE event");
        stmt.execute("DROP TABLE account_event_map");
        conn.close();
    }

    @Test
    public void testCreateAccount() throws SQLException {
        Connection conn = startConnection();
        int affected = Main.createAccount(conn, "bob", "bobby");
        Main.createAccount(conn, "Sal", "Amander");
        endConnection(conn);
        assertTrue(affected == 1);
    }

    @Test
    public void testCreateEvent() throws SQLException {
        Connection conn = startConnection();
        int affected = Main.createEvent(conn, "event", "place", LocalTime.now(), LocalDate.now(), "image", "descrip", 1);
        endConnection(conn);
        assertTrue(affected == 1);
    }

    @Test
    public void testTableMapping() throws SQLException {
        Connection connection = startConnection();
        Main.createAccount(connection, "bob", "bob");
        Main.createEvent(connection, "event", "place", LocalTime.now(), LocalDate.now(), "image", "descrip", 1);


       // int affected = Main.mapUserToEvent(connection, 1, 1);
        ArrayList<AccountEvents> accountEventList = Main.getAccountEvents(connection, 1);

        endConnection(connection);
        assertTrue(accountEventList.get(0).getAccount().getName().equals("bob"));
    }

    @Test
    public void testSelectAccounts() throws SQLException {
        Connection conn = startConnection();

        Main.createAccount(conn, "bob", "bobby");
        Main.createAccount(conn, "Sal", "Amander");

        ArrayList<Account> accountList = Main.selectAccounts(conn);
        endConnection(conn);

        assertTrue(!accountList.isEmpty());
    }

    @Test
    public void testSelectAccount() throws SQLException {
        Connection conn = startConnection();
        Main.createAccount(conn, "bob", "bobby");
        Main.createAccount(conn, "Sal", "Amander");
        Account a = Main.selectAccount(conn, 1);
        endConnection(conn);

        assertTrue(a.getName().equals("bob"));
    }

    @Test
    public void testSelectEvents() throws SQLException {
        Connection conn = startConnection();
        Main.createEvent(conn, "event", "place", LocalTime.now(), LocalDate.now(), "image", "descrip", 1);
        Main.createEvent(conn, "event2", "place2", LocalTime.now(), LocalDate.now(), "image2", "descrip2", 1);
        ArrayList<Event> eventList = Main.selectEvents(conn);

        assertTrue(eventList.size() == 2);
    }

    @Test
    public void testSelectEvent() throws SQLException {
        Connection conn = startConnection();
        Main.createEvent(conn, "event", "place", LocalTime.now(), LocalDate.now(), "image", "descrip", 1);
        Event event = Main.selectEvent(conn, 1);

        assertTrue(event != null);
    }

    @Test
    public void testLogOut() throws SQLException {

    }

    @Test
    public void testEditEvent() throws SQLException {
        Connection conn = startConnection();
        Main.createEvent(conn, "event", "place", LocalTime.now(), LocalDate.now(), "image", "descrip", 1);
        Main.editEvent(conn, 1, "eventEdit", "place", LocalTime.now(), LocalDate.now(), "image", "descrip", 1);
        Event event = Main.selectEvent(conn, 1);

        assertTrue(event.getName().equals("eventEdit"));
    }


    @Test
    public void testGetAccountById() throws SQLException {
        Connection conn = startConnection();

        Main.createAccount(conn, "bob", "bobby");

        int id = Main.getAccountId(conn, "bob");

        assertTrue(id == 1);
    }

    @Test
    public void testSelectAccountByName() throws SQLException {
        Connection conn = startConnection();

        Main.createAccount(conn, "bob", "bobby");

        Account account = Main.selectAccount(conn, "bob");

        assertTrue(account.getName().equals("bob"));
    }

    @Test
    public void testGetEventsCreatedByAccount() throws SQLException {
        Connection conn = startConnection();

        Main.createAccount(conn, "bob", "bobby");
        Main.createEvent(conn, "event", "place", LocalTime.now(), LocalDate.now(), "image", "descrip", 1);

        ArrayList<Event> eventsByAccountList = Main.getEventsCreatedByAccount(conn, 1);

        assertTrue(eventsByAccountList.get(0).getName().equals("event"));

    }

}