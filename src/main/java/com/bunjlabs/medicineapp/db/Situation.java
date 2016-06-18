package com.bunjlabs.medicineapp.db;

import java.util.ArrayList;
import java.util.List;
import org.sql2o.Connection;

public class Situation {

    private long id;
    private String name;
    private long deseaseId;
    private String fio;
    private long age;
    private long growth;
    private long weight;

    public Situation(String name, long deseaseId, String fio, long age, long growth, long weight) {
        this.name = name;
        this.deseaseId = deseaseId;
        this.fio = fio;
        this.age = age;
        this.growth = growth;
        this.weight = weight;
    }
    
    public Situation() {
    }

    public boolean insert() {
        String query = "INSERT INTO situations VALUES (NULL, :name, :deseaseId, :fio, :age, :growth, :weight)";
        try (Connection con = Database.getInstance().getDB().open()) {
            return con.createQuery(query)
                    .addParameter("name", name)
                    .addParameter("deseaseId", deseaseId)
                    .addParameter("fio", fio)
                    .addParameter("age", age)
                    .addParameter("growth", growth)
                    .addParameter("weight", weight)
                    .executeUpdate().getKey() != null;
        }
    }
    
    public List<SituationHuman> selectAll() {
        String query = "SELECT * FROM situations";
        List<Situation> situations;
        List<SituationHuman> situationsHuman;
        try (Connection con = Database.getInstance().getDB().open()) {
            situations = con.createQuery(query).executeAndFetch(Situation.class);
            situationsHuman = new ArrayList<>(situations.size());
            situations.forEach((el) -> {
                SituationHuman sh = new SituationHuman();
                sh.id = el.id;
                sh.name = el.name;
                sh.desease = new Primitive("deseases").getById(el.deseaseId).getName();
                sh.fio = el.fio;
                sh.age = el.age;
                sh.growth = el.growth;
                sh.weight = el.weight;
                sh.plan = new Primitive("medicines").getNamesByIds(new Binding("plan_bindings").selectAll(el.id));
                sh.factors = new Primitive("factors").getNamesByIds(new Binding("factor_bindings").selectAll(el.id));
                sh.coDeseases = new Primitive("deseases").getNamesByIds(new Binding("codesease_bindings").selectAll(el.id));
                sh.specials = new Primitive("specials").getNamesByIds(new Binding("special_bindings").selectAll(el.id));
                situationsHuman.add(sh);
            });
        }
        return situationsHuman;
    }

    public class SituationHuman {
        public long id;
        public String name;
        public String desease;
        public String fio;
        public long age;
        public long growth;
        public long weight;
        public List<String> plan;
        public List<String> factors;
        public List<String> coDeseases;
        public List<String> specials;
    }

}
