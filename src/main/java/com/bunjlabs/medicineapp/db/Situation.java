package com.bunjlabs.medicineapp.db;

import java.util.ArrayList;
import java.util.List;
import org.sql2o.Connection;

public class Situation {

    private long id;
    private long deseaseId;
    private String fio;
    private long age;
    private long sex;
    private long growth;
    private long weight;

    public Situation(long deseaseId, String fio, long age, long growth, long weight) {
        this.deseaseId = deseaseId;
        this.fio = fio;
        this.age = age;
        this.growth = growth;
        this.weight = weight;
    }

    public Situation() {
    }

    public long insert() {
        String query = "INSERT INTO situations VALUES (NULL, :deseaseId, :fio, :age, :sex, :growth, :weight)";
        try (Connection con = Database.getInstance().getDB().open()) {
            return (int) con.createQuery(query)
                    .addParameter("deseaseId", deseaseId)
                    .addParameter("fio", fio)
                    .addParameter("age", age)
                    .addParameter("sex", sex)
                    .addParameter("growth", growth)
                    .addParameter("weight", weight)
                    .executeUpdate().getKey();
        }
    }

    public static void delete(long id) {
        String query = "DELETE FROM situations WHERE id = :id";
        
        try (Connection con = Database.getInstance().getDB().open()) {
            con.createQuery(query).addParameter("id", id).executeUpdate();
        }
    }

    public static List<SituationHuman> selectAll() {
        String query = "SELECT * FROM situations";
        List<Situation> situations;
        List<SituationHuman> situationsHuman;
        try (Connection con = Database.getInstance().getDB().open()) {
            situations = con.createQuery(query).executeAndFetch(Situation.class);
            situationsHuman = new ArrayList<>(situations.size());
            situations.forEach((el) -> {
                SituationHuman sh = new SituationHuman();
                sh.id = el.id;
                sh.desease = new Primitive("deseases").getById(el.deseaseId).getName();
                sh.fio = el.fio;
                sh.age = el.age;
                sh.sex = el.sex == 0 ? "Муж." :  "Жен.";
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

    public static class SituationHuman {

        public long id;
        public String desease;
        public String fio;
        public long age;
        public long growth;
        public long weight;
        public String sex;
        public List<String> plan;
        public List<String> factors;
        public List<String> coDeseases;
        public List<String> specials;

        public Situation insertSituation() {
            Situation s = new Situation();
            s.id = id;
            s.deseaseId = Primitive.insertOrGet("deseases", desease).getId();
            s.fio = fio;
            s.age = age;
            s.sex = sex.equals("Муж.") ? 0 : 1;
            s.growth = growth;
            s.weight = weight;

            List<Long> plans = new ArrayList<>();

            plan.forEach((el) -> {
                Primitive medicine = Primitive.insertOrGet("medicines", el);
                plans.add(medicine.getId());
            });

            List<Long> factorss = new ArrayList<>();

            factors.forEach((el) -> {
                Primitive factor = Primitive.insertOrGet("factors", el);
                factorss.add(factor.getId());
            });
            List<Long> coDeseasess = new ArrayList<>();

            coDeseases.forEach((el) -> {
                Primitive desease2 = Primitive.insertOrGet("deseases", el);
                coDeseasess.add(desease2.getId());
            });

            List<Long> specialss = new ArrayList<>();

            specials.forEach((el) -> {
                Primitive special = Primitive.insertOrGet("specials", el);
                specialss.add(special.getId());
            });

            s.id = s.insert();

            plans.forEach((bid) -> {
                Binding b = new Binding("plan_bindings", bid, s.id);
                b.insert();
            });

            factorss.forEach((bid) -> {
                Binding b = new Binding("factor_bindings", bid, s.id);
                b.insert();
            });

            coDeseasess.forEach((bid) -> {
                Binding b = new Binding("codesease_bindings", bid, s.id);
                b.insert();
            });

            specialss.forEach((bid) -> {
                Binding b = new Binding("special_bindings", bid, s.id);
                b.insert();
            });

            return s;
        }
    }

}
