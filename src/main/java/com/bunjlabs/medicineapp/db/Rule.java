package com.bunjlabs.medicineapp.db;

import java.util.ArrayList;
import java.util.List;
import org.sql2o.Connection;


public class Rule {
    private long id;
    private long desease_id;
    
    public Rule(long desease_id) {
        this.desease_id = desease_id;
    }
    
    public Rule() {
    }
    
    public boolean insert() {
        String query = "INSERT INTO rules VALUES (NULL, :desease_id)";
        try (Connection con = Database.getInstance().getDB().open()) {
            return con.createQuery(query).addParameter("desease_id", desease_id).executeUpdate().getKey() != null;
        }
    }
    
    public List<RuleHuman> selectAll() {
        String query = "SELECT * FROM rules";
        List<Rule> rules;
        List<RuleHuman> rulesHuman;
        try (Connection con = Database.getInstance().getDB().open()) {
            rules = con.createQuery(query).executeAndFetch(Rule.class);
            rulesHuman = new ArrayList<>(rules.size());
            rules.forEach((el) -> {
                RuleHuman rh = new RuleHuman();
                rh.id = el.id;
                rh.desease = new Primitive("deseases").getById(el.desease_id).getName();
                rh.recomendedMedicines = getRecomendedMedicines(el.id);
                rulesHuman.add(rh);
            });
        }
        return rulesHuman;
    }
    
    public List<String> getRecomendedMedicines(long ruleId) {
        String query = "SELECT medicine_id FROM rules WHERE rule_id=:rule_id";
        List<Long> ids;
        try (Connection con = Database.getInstance().getDB().open()) {
            ids = con.createQuery(query).addParameter("rule_id", ruleId).executeAndFetch(Long.class);
        }
        return new Primitive("medicines").getNamesByIds(ids);
    }
    
    public class RuleHuman {
        public long id;
        public String desease;
        public List<String> recomendedMedicines;
    }
}
