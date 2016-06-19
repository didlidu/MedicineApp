package com.bunjlabs.medicineapp.db;

import java.util.List;
import org.sql2o.Connection;


public class Binding {
    
    private final String tableName;
    private long id1, id2;
    
    public Binding(String tableName, long id1, long id2) {
        this.id1 = id1;
        this.id2 = id2;
        this.tableName = tableName;
    }
    
    public Binding(String tableName) {
        this.tableName = tableName;
    }
    
    public boolean insert() {
        String query = "INSERT INTO " + tableName + " VALUES (:id1, :id2)";
        try (Connection con = Database.getInstance().getDB().open()) {
            return con.createQuery(query).addParameter("id1", id1)
                    .addParameter("id2", id2).executeUpdate().getKey() != null;
        }
    }
    
    public List<Long> selectAll(long situationId) {
        String query = "SELECT id FROM " + tableName + " WHERE situationId=:situationId";
        try (Connection con = Database.getInstance().getDB().open()) {
            return con.createQuery(query).addParameter("situationId", situationId).executeAndFetch(Long.class);
        }
    }

    public long getId1() {
        return id1;
    }

    public long getId2() {
        return id2;
    }
}
