package com.bunjlabs.medicineapp.db;

import java.util.ArrayList;
import java.util.List;
import org.sql2o.Connection;


public class Rule {
    private long id;
    private long deseaseId;
    
    public Rule(long desease_id) {
        this.deseaseId = desease_id;
    }
    
    public Rule() {
    }
    
    public long insert() {
        String query = "INSERT INTO rules VALUES (NULL, :deseaseId)";
        try (Connection con = Database.getInstance().getDB().open()) {
            return (int) con.createQuery(query).addParameter("deseaseId", deseaseId).executeUpdate().getKey();
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
                rh.desease = new Primitive("deseases").getById(el.deseaseId).getName();
                rh.recomendedMedicines = getRecomendedMedicines(el.id);
                rulesHuman.add(rh);
            });
        }
        return rulesHuman;
    }
    
    public List<String> getRecomendedMedicines(long ruleId) {
        String query = "SELECT medicine_id FROM rules WHERE ruleId=:ruleId";
        List<Long> ids;
        try (Connection con = Database.getInstance().getDB().open()) {
            ids = con.createQuery(query).addParameter("ruleId", ruleId).executeAndFetch(Long.class);
        }
        return new Primitive("medicines").getNamesByIds(ids);
    }
    
    public static class RuleHuman {
        public long id;
        public String desease;
        public List<String> recomendedMedicines;
        
        public void insertRule() {
            Rule r = new Rule();
            r.deseaseId = Primitive.insertOrGet("deseases", desease).getId();
            
            List<Long> medicines = new ArrayList<>();

            recomendedMedicines.forEach((el) -> {
                Primitive medicine = Primitive.insertOrGet("medicines", el);
                medicines.add(medicine.getId());
            });
            
            r.id = r.insert();

            medicines.forEach((bid) -> {
                Binding b = new Binding("plan_bindings", r.id, bid);
                b.insert();
            });
        }
    }
}
