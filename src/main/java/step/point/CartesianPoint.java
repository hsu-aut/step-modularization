package step.point;

import java.util.ArrayList;
import java.util.List;

/**
 * #3797=CARTESIAN_POINT('',(0.,0.,0.));
 */
public class CartesianPoint extends Point{
    public static final String LABEL = "CARTESIAN_POINT";
    private List<Double> coordinates;

    public CartesianPoint(String name, String id) {
        super(name, id);
        this.coordinates = new ArrayList<>();
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String getEntityLabel() {
        return LABEL;
    }
    
    public String printCoordinates() {
    	String res = "";
    	for (double c : coordinates) {
    		res += c + " | ";
    	}
    	return res;
    }
}
