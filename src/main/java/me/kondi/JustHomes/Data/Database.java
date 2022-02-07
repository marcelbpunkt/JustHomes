package me.kondi.JustHomes.Data;

import me.kondi.JustHomes.Home.Home;
import me.kondi.JustHomes.JustHomes;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Database {


    //Cache
    private HashMap<String, Set<String>> CachedListOfHomes = new HashMap<>();
    private BukkitScheduler scheduler;

    //Database objects and connection data
    private Connection con;
    private Statement st;
    private String host;
    private String database;
    private String username;
    private String password;
    private String databaseType;

    //Config and plugin objects
    private FileConfiguration config;
    private JustHomes plugin;
    private String prefix;
    private ConsoleCommandSender console;


    //Create database
    public Database(JustHomes plugin) {
        this.plugin = plugin;
        this.prefix = plugin.prefix;
        this.config = plugin.config;
        this.databaseType = config.getString("DatabaseType");
        this.console = plugin.getServer().getConsoleSender();

        this.scheduler = plugin.getServer().getScheduler();
        try {
            if (databaseType.equalsIgnoreCase("MYSQL")) {
                this.host = config.getString("Host");
                this.database = config.getString("DatabaseName");
                this.username = config.getString("Username");
                this.password = config.getString("Password");
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "", username, password);
            } else if (databaseType.equalsIgnoreCase("SQLITE")) {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/playerdata/homeData.db");

            }


            st = con.createStatement();
            createTable();
            getCachedHomesList();
        } catch (Exception ex) {

            console.sendMessage(prefix + "ERROR: " + ChatColor.RED + ex);

        }

    }

    //Save home
    public void setHome(Home home) throws SQLException {
        try {
            String exist = "SELECT * FROM HOMES WHERE UUID = ? AND HomeName = ?";
            PreparedStatement preparedStmt = con.prepareStatement(exist);
            preparedStmt.setString(1, home.getOwner());
            preparedStmt.setString(2, home.getHomeName());

            ResultSet results = preparedStmt.executeQuery();
            if (!results.next()) {
                String query = "INSERT INTO HOMES (UUID, WorldName, HomeName, X, Y, Z, Pitch, Yaw)" + "VALUES(?, ?, ?,?, ?, ?, ?, ?)";
                PreparedStatement preparedStmtInsert = con.prepareStatement(query);
                preparedStmtInsert.setString(1, home.getOwner());
                preparedStmtInsert.setString(2, home.getWorldName());
                preparedStmtInsert.setString(3, home.getHomeName());
                preparedStmtInsert.setDouble(4, home.getX());
                preparedStmtInsert.setDouble(5, home.getY());
                preparedStmtInsert.setDouble(6, home.getZ());
                preparedStmtInsert.setFloat(7, home.getPitch());
                preparedStmtInsert.setFloat(8, home.getYaw());

                preparedStmtInsert.execute();
            } else {
                String query = "UPDATE HOMES SET WorldName=?, X=?, Y=?, Z=?, Pitch=?, Yaw=? WHERE UUID = ? AND HomeName=?";
                PreparedStatement preparedStmtUpdate = con.prepareStatement(query);
                preparedStmtUpdate.setString(1, home.getWorldName());
                preparedStmtUpdate.setDouble(2, home.getX());
                preparedStmtUpdate.setDouble(3, home.getY());
                preparedStmtUpdate.setDouble(4, home.getZ());
                preparedStmtUpdate.setFloat(5, home.getPitch());
                preparedStmtUpdate.setFloat(6, home.getYaw());
                preparedStmtUpdate.setString(7, home.getOwner());
                preparedStmtUpdate.setString(8, home.getHomeName());
                preparedStmtUpdate.execute();
            }


        } catch (Exception ex) {

            console.sendMessage(prefix + "ERROR:" + ChatColor.RED + ex);

        }

    }

    //Get homes amount
    public int getHomesAmount(String uuid) throws SQLException {
        try {


            String query = "SELECT COUNT(UUID) AS 'rowcount' FROM HOMES WHERE UUID=?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, uuid);
            ResultSet results = preparedStmt.executeQuery();
            while (results.next())
                return results.getInt("rowcount");


        } catch (Exception ex) {

            console.sendMessage("[JHomes] ERROR:" + ChatColor.RED + ex);

        }
        return 0;
    }

    //Get cached home list
    public Set<String> getCachedListOfHomes(String uuid) {
        return CachedListOfHomes.get(uuid);
    }

    //Get homes list to cache - tabCompleter
    public void getCachedHomesList() {
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                try {


                    String query = "SELECT HomeName, UUID FROM HOMES";
                    PreparedStatement preparedStmt = con.prepareStatement(query);
                    ResultSet results = preparedStmt.executeQuery();
                    Set<String> listOfHomes = new HashSet<>();
                    while (results.next()) {
                        String uuid = results.getString("UUID");
                        if (!CachedListOfHomes.containsKey(uuid)) {
                            listOfHomes.add(results.getString("HomeName"));
                            CachedListOfHomes.put(uuid, listOfHomes);

                        } else {
                            if (!CachedListOfHomes.get(uuid).contains(results.getString("HomeName")))
                                CachedListOfHomes.get(uuid).add(results.getString("HomeName"));

                        }
                    }


                } catch (Exception ex) {

                    console.sendMessage("[JHomes] ERROR:" + ChatColor.RED + ex);

                }
            }
        }, 0, 20);


    }


    //Get homes to list
    public Set<String> getHomesList(String uuid) throws SQLException {
        try {


            String query = "SELECT HomeName FROM HOMES WHERE UUID=?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, uuid);
            ResultSet results = preparedStmt.executeQuery();
            Set<String> listOfHomes = new HashSet<>();
            while (results.next()) {
                listOfHomes.add(results.getString("HomeName"));
            }


            return listOfHomes;


        } catch (Exception ex) {

            console.sendMessage("[JHomes] ERROR:" + ChatColor.RED + ex);

        }
        return null;
    }


    //Get Home
    public Home getHome(String uuid, String homeName) throws SQLException {
        try {


            String query = "SELECT WorldName, X, Y, Z, Pitch, Yaw FROM HOMES WHERE UUID=? AND HomeName = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, uuid);
            preparedStmt.setString(2, homeName);
            ResultSet results = preparedStmt.executeQuery();
            while (results.next()) {
                Home home = new Home(uuid, homeName, results.getString("WorldName"),
                        results.getDouble("X"),
                        results.getDouble("Y"),
                        results.getDouble("z"),
                        results.getFloat("Pitch"),
                        results.getFloat("Yaw"));

                return home;
            }


        } catch (Exception ex) {

            console.sendMessage("[JHomes] ERROR:" + ChatColor.RED + ex);

        }
        return null;

    }


    //Delete Home
    public void deleteHome(String uuid, String homeName) throws SQLException {
        try {

            String query = "DELETE FROM HOMES WHERE UUID=? AND HomeName = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, uuid);
            preparedStmt.setString(2, homeName);
            preparedStmt.execute();


        } catch (Exception ex) {
            console.sendMessage("[JHomes] ERROR:" + ChatColor.RED + ex);

        }
    }


    //Create table
    public void createTable() {
        try {
            DatabaseMetaData meta = con.getMetaData();
            ResultSet tables = meta.getTables(null, null, "HOMES", null);

            while (!tables.next()) {
                String playerDataTable = "CREATE TABLE HOMES" +
                        "(UUID VARCHAR(255) NOT NULL, " +
                        "HomeName VARCHAR(255) NOT NULL, " +
                        "WorldName VARCHAR(255), " +
                        "X DOUBLE, " +
                        "Y DOUBLE, " +
                        "Z DOUBLE, " +
                        "Pitch FLOAT, " +
                        "Yaw FLOAT," +
                        "PRIMARY KEY (UUID, HomeName))";

                st.executeUpdate(playerDataTable);
                console.sendMessage(prefix + ChatColor.GREEN + "Table [HOMES] Created!");
                break;
            }


        } catch (Exception ex) {

            console.sendMessage(prefix + "ERROR: " + ChatColor.RED + ex);
        }
    }

    //Stops database connection and schedulers

    public void stopDatabaseConnection() {
        scheduler.cancelTasks(plugin);
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
