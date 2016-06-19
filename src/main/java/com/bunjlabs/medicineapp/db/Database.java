package com.bunjlabs.medicineapp.db;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sqlite.SQLiteDataSource;

public class Database {

    private final Sql2o sql2o;

    private final String deseasesTable
            = "CREATE TABLE IF NOT EXISTS deseases ("
            + "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
            + "    name TEXT NOT NULL"
            + ");";
    private final String medicinesTable
            = "CREATE TABLE IF NOT EXISTS medicines ("
            + "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "    name TEXT NOT NULL"
            + ");";
    private final String factorsTable
            = "CREATE TABLE IF NOT EXISTS factors ("
            + "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "    name TEXT NOT NULL"
            + ");";
    private final String specialsTable
            = "CREATE TABLE IF NOT EXISTS specials ("
            + "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "    name TEXT NOT NULL"
            + ");";

    private final String situationsTable
            = "CREATE TABLE IF NOT EXISTS situations ("
            + "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "    name TEXT NOT NULL,"
            + "    desease_id INTEGER NOT NULL,"
            + "    fio VARCHAR(255) NOT NULL,"
            + "    age INTEGER  NOT NULL,"
            + "    growth INTEGER  NOT NULL,"
            + "    weight INTEGER  NOT NULL"
            + ");";
    private final String rulesTable
            = "CREATE TABLE IF NOT EXISTS rules ("
            + "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "    desease_id INTEGER NOT NULL"
            + ");";

    private final String planBindingsTable
            = "CREATE TABLE IF NOT EXISTS plan_bindings ("
            + "    id INTEGER NOT NULL,"
            + "    situation_id INTEGER NOT NULL"
            + ");";
    private final String factorBindingsTable
            = "CREATE TABLE IF NOT EXISTS factor_bindings ("
            + "    id INTEGER NOT NULL,"
            + "    situation_id INTEGER NOT NULL"
            + ");";
    private final String coDeseaseBindingsTable
            = "CREATE TABLE IF NOT EXISTS codesease_bindings ("
            + "    id INTEGER NOT NULL,"
            + "    situation_id INTEGER NOT NULL"
            + ");";
    private final String specialBindingsTable
            = "CREATE TABLE IF NOT EXISTS special_bindings ("
            + "    id INTEGER NOT NULL,"
            + "    situation_id INTEGER NOT NULL"
            + ");";

    private final String recomendedMedicineBindingsTable
            = "CREATE TABLE IF NOT EXISTS recomended_medicine_bindings ("
            + "    rule_id INTEGER NOT NULL,"
            + "    medicine_id INTEGER NOT NULL"
            + ");";

    private final String usersTable
            = "CREATE TABLE IF NOT EXISTS users ("
            + "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "    role TEXT NOT NULL,"
            + "    login TEXT NOT NULL,"
            + "    password TEXT NOT NULL"
            + ");";

    private Database() {
        SQLiteDataSource datasource = new SQLiteDataSource();
        datasource.setEncoding("UTF-8");
        datasource.setUrl("jdbc:sqlite:main.db");

        this.sql2o = new Sql2o(datasource);
    }

    public void init() {
        try (Connection con = sql2o.open()) {
            con.createQuery(deseasesTable).executeUpdate();
            con.createQuery(medicinesTable).executeUpdate();
            con.createQuery(factorsTable).executeUpdate();
            con.createQuery(specialsTable).executeUpdate();

            con.createQuery(situationsTable).executeUpdate();
            con.createQuery(rulesTable).executeUpdate();

            con.createQuery(planBindingsTable).executeUpdate();
            con.createQuery(factorBindingsTable).executeUpdate();
            con.createQuery(coDeseaseBindingsTable).executeUpdate();
            con.createQuery(specialBindingsTable).executeUpdate();

            con.createQuery(recomendedMedicineBindingsTable).executeUpdate();

            con.createQuery(usersTable).executeUpdate();

            new User(1, "medic", "medic", "medic").insertIgnore();
            new User(2, "researcher", "researcher", "researcher").insertIgnore();
            new User(3, "admin", "admin", "admin").insertIgnore();
        }
    }

    public Sql2o getDB() {
        return sql2o;
    }

    private static Database instance;

    public static Database getInstance() {
        return (instance == null) ? instance = new Database() : instance;
    }
}
