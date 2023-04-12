package step.face;

import java.util.List;
import java.util.Optional;

import step.CylindricalSurface;
import step.Surface;
import step.point.CartesianPoint;

public class AdvancedFace extends Face {
    public static final String LABEL = "ADVANCED_FACE";
    private boolean same_sense;
    private Surface face_geometry;
    private List<CartesianPoint> vertices;
    private double[] normal = {0.0, 0.0, 0.0};
    private String normalRef;
    private boolean isCylindrical = false;
    private boolean isPlane = false;
    private String surfaceRef;
    private Optional<CylindricalSurface> cylSurface = Optional.empty();
    
    public AdvancedFace(String name, String id, boolean same_sense) {
        super(name, id);
        this.same_sense = same_sense;
    }

    public boolean isSame_sense() {
        return same_sense;
    }

    public void setSame_sense(boolean same_sense) {
        this.same_sense = same_sense;
    }

    public Surface getFace_geometry() {
        return face_geometry;
    }

    public void setFace_geometry(Surface face_geometry) {
        this.face_geometry = face_geometry;
    }
    
    public List<CartesianPoint> getVertices() {
      return vertices;
    }
    
    public void setVertices(List<CartesianPoint> vertices) {
      this.vertices = vertices;
    }
    
    public double[] getNormal() {
      return normal;
    }
    
    public void setNormal(double[] normal) {
      this.normal = normal;
    }
    
    public String printNormal() {
    	String res = "";
    	for (double c : getNormal()) {
    		res += c + ", ";
    	}
    	return res;
    }
    
    public String getNormalRef() {
      return normalRef;
    }

    public void setNormalRef(String normalRef) {
      this.normalRef = normalRef;
    }
    
    public boolean isCylindrical() {
      return isCylindrical;
    }
    
    public void setCylindrical(boolean isCylindrical) {
      this.isCylindrical = isCylindrical;
    }
    
    public String getSurfaceRef() {
      return surfaceRef;
    }

    public void setSurfaceRef(String surfaceRef) {
      this.surfaceRef = surfaceRef;
    }
    
    public Optional<CylindricalSurface> getCylSurface() {
      return cylSurface;
    }
    
    public void setCylSurface(Optional<CylindricalSurface> cylSurface) {
      this.cylSurface = cylSurface;
    }
    
    public void setCylSurface(CylindricalSurface cylSurface) {
      this.cylSurface = Optional.ofNullable(cylSurface);
    }
    
    @Override
    public String getEntityLabel() {
      return LABEL;
    }

	public boolean isPlane() {
	  return isPlane;
	}

	public void setPlane(boolean isPlane) {
	  this.isPlane = isPlane;
	}

}
