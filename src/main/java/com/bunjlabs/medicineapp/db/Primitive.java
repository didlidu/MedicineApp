package com.bunjlabs.medicineapp.db;

import java.util.ArrayList;
import java.util.List;
import org.sql2o.Connection;

public class Primitive {

    private final String tableName;
    private long id;
    private String name;

    public Primitive(String tableName, String name) {
        this.tableName = tableName;
        this.name = name;
    }
    
    public Primitive(String tableName) {
        this.tableName = tableName;
    }

    public boolean insert() {
        String query = "INSERT INTO " + tableName + " VALUES (NULL, :name)";
        try (Connection con = Database.getInstance().getDB().open()) {
            return con.createQuery(query).addParameter("name", name).executeUpdate().getKey() != null;
        }
    }
    
    public List<Primitive> selectAll() {
        String query = "SELECT * FROM " + tableName + "";
        try (Connection con = Database.getInstance().getDB().open()) {
            return con.createQuery(query).executeAndFetch(Primitive.class);
        }
    }
    
    public Primitive getById(long id) {
        String query = "SELECT name FROM " + tableName + " WHERE id=:id";
        try (Connection con = Database.getInstance().getDB().open()) {
            this.name = con.createQuery(query).addParameter("id", id).executeAndFetchFirst(Primitive.class).name;
            this.id = id;
        }
        return this;
    }
    
    public List<String> getNamesByIds(List<Long> ids) {
        List<String> names = new ArrayList<>(ids.size());
        ids.forEach((el) -> {
            names.add(new Primitive(tableName).getById(el).name);
        });
        return names;
    }
    
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
