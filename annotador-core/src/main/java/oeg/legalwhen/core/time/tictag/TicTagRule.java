package oeg.annotador.core.time.tictag;

/**
 *
 *
 * @author mnavas
 */
public class TicTagRule {

    public String rule;
    public String type;
    public String id;
    public String norm;
    public String desc;

    public TicTagRule(String id, String rule, String type, String norm, String desc) {
        this.id = id;
        this.rule = rule;
        this.type = type;
        this.norm = norm;
        this.desc = desc;
    }

}
