package io.github.hydos.gprotect.config;

import net.minecraft.util.math.BlockPos;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/*
    some example sql commands which i found
    CREATE TABLE cats
(
  id              INT unsigned NOT NULL AUTO_INCREMENT, # Unique ID for the record
  name            VARCHAR(150) NOT NULL,                # Name of the cat
  owner           VARCHAR(150) NOT NULL,                # Owner of the cat
  birth           DATE NOT NULL,                        # Birthday of the cat
  PRIMARY KEY     (id)                                  # Make the id the primary key
);

INSERT INTO cats ( name, owner, birth) VALUES
  ( 'Sandy', 'Lennon', '2015-01-03' ),
  ( 'Cookie', 'Casey', '2013-11-13' ),
  ( 'Charlie', 'River', '2016-05-21' );

SELECT * FROM cats;
SELECT name FROM cats WHERE owner = 'Casey';
DELETE FROM cats WHERE name='Cookie';

https://www.tutorialspoint.com/jdbc/jdbc-sample-code.htm
https://dev.mysql.com/doc/mysql-getting-started/en/
 */

public interface BlockChangeListener {

    void playerBlockChange(BlockHistory history);

    List<BlockHistory> getBlockHistory(BlockPos pos);

    class MySQLWriter implements BlockChangeListener {
        static final String DB_URL = "jdbc:mysql://localhost:3306";

        Connection conn = null;
        Statement stmt = null;

        //  Database credentials
        static final String USER = "root";
        static final String PASS = "password";

        public MySQLWriter() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Connecting to database...");
                Properties properties = new Properties();
                properties.setProperty("user", USER);
                properties.setProperty("password", PASS);
                properties.setProperty("serverTimezone", "Australia/Brisbane");
                conn = DriverManager.getConnection(DB_URL, properties);
                System.out.println("Connected! checking tables...");
                postConnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public ResultSet sendQuery(String statement) throws SQLException {
            stmt = conn.createStatement();
            String sql;
            sql = statement;
            return stmt.executeQuery(sql);
        }

        public void sendUpdate(String statement) throws SQLException {
            stmt = conn.createStatement();
            String sql;
            sql = statement;
            stmt.execute(sql);
        }

        public void closeConnection() {
            try {
                conn.close();
                stmt.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }

        //TODO: ChunkSerializer has code to serialise blocks and stuff

        public void postConnect() {
            try {
                sendQuery("USE gprotect");
                ResultSet blessedCurrentTable = sendQuery("SELECT id, oldblock, newblock, pos, time, player FROM playerModify");
            } catch (SQLException e) {
                System.out.println("Database tables dont exist! creating...");
                try {
                    sendUpdate("CREATE TABLE playerModify\n" +
                            "(\n" +
                            "id              INT unsigned NOT NULL AUTO_INCREMENT,\n" +
                            "oldblock            VARCHAR(150) NOT NULL,\n" +
                            "newblock            VARCHAR(150) NOT NULL,\n" +
                            "pos            LONG NOT NULL,\n" +
                            "time           DATE NOT NULL,\n" +
                            "player            VARCHAR(150) NOT NULL, #the players uuid\n" +
                            "type            VARCHAR(150) NOT NULL, #the type of action BREAK, PLACE, etc\n" +
                            "PRIMARY KEY     (id)\n" +
                            ");");
                } catch (SQLException sqlE) {
                    System.out.println("Tables could not be created! exiting...");
                    sqlE.printStackTrace();
                    closeConnection();
                    System.exit(-1);
                }
                postConnect();
            }
        }

        @Override
        public void playerBlockChange(BlockHistory history) {
            try {
                sendUpdate("INSERT INTO playerModify (id, oldblock, newblock, pos, time, player) VALUES" +
                        "(" + history.oldBlock + "," + history.newBlock + "," + history.position + "," + history.time + "," + history.playerUuid + ");");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        @Override
        public List<BlockHistory> getBlockHistory(BlockPos pos) {
            List<BlockHistory> history = new ArrayList<>();
            try {
                ResultSet result = sendQuery("SELECT id, oldblock, newblock, pos, time, player FROM playerModify");
                while (result.next()) {
                    int id = result.getInt("id");
                    String oldBlock = result.getString("oldblock");
                    String newBlock = result.getString("newblock");
                    long position = result.getLong("pos");
                    Date time = result.getTime("time");
                    String playerUuid = result.getString("player");
                    history.add(new BlockHistory(id, oldBlock, newBlock, position, time, playerUuid));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return history;
        }
    }
}