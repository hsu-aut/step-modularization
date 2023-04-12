package step;

import step.curve.Curve;
import step.curve.TrimmingPreference;
import step.curve.TrimmingSelect;

import java.util.HashSet;
import java.util.Set;

public class TrimmedCurve extends RepresentationItem{
    public static final String LABEL = "TRIMMED_CURVE";
    private Curve curve;
    private Set<TrimmingSelect> trim_1;
    private Set<TrimmingSelect> trim_2;
    private boolean sense_agreement;
    private TrimmingPreference master_representation;

    public TrimmedCurve(String name, String id) {
        super(name, id);
        trim_1 = new HashSet<>();
        trim_2 = new HashSet<>();
    }

    public Curve getCurve() {
        return curve;
    }

    public void setCurve(Curve curve) {
        this.curve = curve;
    }

    public Set<TrimmingSelect> getTrim_1() {
        return trim_1;
    }

    public void setTrim_1(Set<TrimmingSelect> trim_1) {
        this.trim_1 = trim_1;
    }

    public Set<TrimmingSelect> getTrim_2() {
        return trim_2;
    }

    public void setTrim_2(Set<TrimmingSelect> trim_2) {
        this.trim_2 = trim_2;
    }

    public boolean isSense_agreement() {
        return sense_agreement;
    }

    public void setSense_agreement(boolean sense_agreement) {
        this.sense_agreement = sense_agreement;
    }

    public TrimmingPreference getMaster_representation() {
        return master_representation;
    }

    public void setMaster_representation(TrimmingPreference master_representation) {
        this.master_representation = master_representation;
    }

}
