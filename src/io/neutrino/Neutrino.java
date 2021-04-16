package io.neutrino;

import io.neutrino.api.FileManager;
import io.neutrino.api.database.Database;
import io.neutrino.api.database.QueryBuilder;
import io.neutrino.listener.PlayerJoin;
import io.neutrino.model.NeutrinoProfile;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Neutrino extends JavaPlugin {

    private static Neutrino instance;
    private ConsoleCommandSender console;
    private Database database;
    private FileManager fileManager;
    private QueryBuilder queryBuilder;

    @Override
    public void onEnable() {
        setInstance(this);
        console = getServer().getConsoleSender();
        fileManager = new FileManager();
        registerListeners();
        database = new Database("jdbc:mysql://localhost:3306/", "neutrino", "root", "");
        fileManager.createDirectory("plugins/Neutrino/data");
        queryBuilder = new QueryBuilder();
        database.registerModel(new NeutrinoProfile());
        logSuccess("Core plugin enabled");
    }

    @Override
    public void onDisable() {
        setInstance(null);
        logSuccess("Core plugin disabled");
    }

    public static Neutrino getInstance() {
        return instance;
    }

    private static void setInstance(Neutrino neutrino) {
        instance = neutrino;
    }

    public Database getDatabase() {
        return database;
    }

    public QueryBuilder getQueryBuilder() { return queryBuilder; }

    public FileManager getFileManager() {
        return fileManager;
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoin(), this);
    }

    public void logSuccess(String message) {
        console.sendMessage(getPrefix() + "§a" + message);
    }

    public void logError(String message) {
        console.sendMessage(getPrefix() + "§c" + message);
    }

    public void log(String message) {
        console.sendMessage(getPrefix() + "§7" + message);
    }

    public void logWarning(String message) {
        console.sendMessage(getPrefix() + "§e" + message);
    }

    public String getPrefix() {
        return "§b§lNEUTRINO §r§7>> ";
    }
}
