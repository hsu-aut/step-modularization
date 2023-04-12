package step.utility;

import Jama.Matrix;
import step.face.AdvancedFace;
import step.point.CartesianPoint;

public class VectorMath {
  
  public static final double[] ORIGIN = {0.0, 0.0, 0.0};
  
  public static final double[] X = {1.0, 0.0, 0.0};
  public static final double[] Y = {0.0, 1.0, 0.0};
  public static final double[] Z = {0.0, 0.0, 1.0};
  
  public static double dotProd(double[] v1, double[] v2) {
    if (v1.length == 3 && v2.length == 3) {
      return v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2];
    }
    // error case
    return 0.0;
  }
  
  public static double[] crossProd(double[] v1, double[] v2) {
    if (v1.length == 3 && v2.length == 3) {
      return new double[] 
          { v1[1]*v2[2] - v1[2]*v2[1], 
            v1[2]*v2[0] - v1[0]*v2[2], 
            v1[0]*v2[1] - v1[1]*v2[0] };
    }
    // error case
    return new double[] {0.0, 0.0, 0.0};
  }
  
  public static double[] normal(double[] v1, double[] v2) {
    if (v1.length == 3 && v2.length == 3) {
      double[] n = crossProd(v1, v2);
      Matrix nVec = new Matrix(n, 3);
      return nVec.times(1.0 / nVec.norm2()).getColumnPackedCopy();
    }
    // error case
    return new double[] {0.0, 0.0, 0.0};
  }
  
  public static double[] normal(AdvancedFace face) {
    
    // get three reference point of the face to span the plan
    // as each face is at least a triangle, these three always exist
    CartesianPoint r1 = face.getVertices().get(0);
    CartesianPoint r2 = face.getVertices().get(1);
    CartesianPoint r3 = face.getVertices().get(2);
    
    // reference point s
    double[] s = new double[3]; 
    for(int i = 0; i < r1.getCoordinates().size(); i++) s[i] = r1.getCoordinates().get(i);

    // vectors u and v from r1 to r2 and r3
    double[] v = new double[3]; 
    for(int i = 0; i < r2.getCoordinates().size(); i++) v[i] = r2.getCoordinates().get(i) - s[i];
    
    double[] u = new double[3]; 
    for(int i = 0; i < r3.getCoordinates().size(); i++) u[i] = r3.getCoordinates().get(i) - s[i];
    
    return normal(v, u);
  }
  
  public static double vecLength(double[] v) {
    if (v.length == 3) {
      return Math.sqrt(Math.pow(v[0], 2) + Math.pow(v[1], 2) + Math.pow(v[2], 2));
    }
    // error case
    return -1.0;
  }
  
  public static double[] vecPlus(double[] v1, double[] v2) {
    if (v1.length == 3 && v2.length == 3) {
      return new double[] { v1[0] + v2[0], v1[1] + v2[1], v1[2] + v2[2] };
    }
    // error case
    return new double[] { 0.0, 0.0, 0.0 };
  }
  
  public static double[] vecMinus(double[] v1, double[] v2) {
    if (v1.length == 3 && v2.length == 3) {
      return new double[] { v1[0] - v2[0], v1[1] - v2[1], v1[2] - v2[2] };
    }
    // error case
    return new double[] { 0.0, 0.0, 0.0 };
  }
  
  public static double[] vecTimes(double[] v1, double[] v2) {
    if (v1.length == 3 && v2.length == 3) {
      return new double[] { v1[0] * v2[0], v1[1] * v2[1], v1[2] * v2[2] };
    }
    // error case
    return new double[] { 0.0, 0.0, 0.0 };
  }
  
  public static double[] vecDiv(double[] v1, double[] v2) {
    boolean nonZero = true;
    for(double val : v2) {
      nonZero &= val != 0;
    }
    
    if (nonZero && v1.length == 3 && v2.length == 3) {
      return new double[] { v1[0] / v2[0], v1[1] / v2[1], v1[2] / v2[2] };
    }
    // error case
    return new double[] { 0.0, 0.0, 0.0 };
  }
  
  public static double[] vecPlus(double[] v, double x) {
    double[] res = v.clone();
    for (int i = 0; i < res.length; i++) {
      res[i] += x;
    }
    return res;
  }
  
  public static double[] vecMinus(double[] v, double x) {
    double[] res = v.clone();
    for (int i = 0; i < res.length; i++) {
      res[i] -= x;
    }
    return res;
  }
  
  public static double[] vecTimes(double[] v, double x) {
    double[] res = v.clone();
    for (int i = 0; i < res.length; i++) {
      res[i] *= x;
    }
    return res;
  }
  
  public static double[] vecDiv(double[] v, double x) {
    if (x != 0.0) {
      double[] res = v.clone();
      for (int i = 0; i < res.length; i++) {
        res[i] /= x;
      }
      return res;
    }
    // error case
    return new double[] { 0.0, 0.0, 0.0 };
  }
  
  public static boolean vecEquals(double[] v1, double[] v2) {
    return vecEquals(v1, v2, 0);
  }
  
  public static boolean vecEquals(double[] v1, double[] v2, double delta) {
    if (v1.length == v2.length) {
      boolean res = true;
      for (int i = 0; i < v1.length; i++) {
        res &= (v1[i] + delta >= v2[i] && v1[i] - delta <= v2[i]);
      }
      return res;
    }
    return false;
  }
  
  public static boolean valEquals(double v1, double v2, double delta) {
	return (v1 + delta >= v2 && v1 - delta <= v2);
  }
  
  public static boolean isNullVec(double[] v) {
    return isNullVec(v, 0.0);
  }
  
  public static boolean isNullVec(double[] v, double delta) {
    return vecEquals(v, new double[] { 0.0, 0.0, 0.0 }, delta);
  }
  
  /**
   * Rotates the given point around the x-axis according to
   * the specified degrees. If the degree variable is positive,
   * it rotates counter-clockwise. If it is negative, it 
   * rotates clockwise.
   *  
   * @param v The point to be rotated
   * @param deg The degrees to rotate
   * @return The rotated point in 3d space
   */
  public static double[] rotX(double[] v, double deg) {
    double[] res = new double[3];
    
    double rad = Math.toRadians(deg);
    double sin = Math.sin(rad);
    double cos = Math.cos(rad);
        
    res[0] = v[0];
    res[1] = v[1]*cos - v[2]*sin;
    res[2] = v[1]*sin + v[2]*cos;
    
    return res;
  }
  
  /**
   * Rotates the given point around the y-axis according to
   * the specified degrees. If the degree variable is positive,
   * it rotates counter-clockwise. If it is negative, it 
   * rotates clockwise.
   *  
   * @param v The point to be rotated
   * @param deg The degrees to rotate
   * @return The rotated point in 3d space
   */
  public static double[] rotY(double[] v, double deg) {
    double[] res = new double[3];
    
    double rad = Math.toRadians(deg);
    double sin = Math.sin(rad);
    double cos = Math.cos(rad);
        
    res[0] = v[0]*cos + v[2]*sin;
    res[1] = v[1];
    res[2] = (-v[0]*sin) + v[2]*cos;
    
    return res;
  }
  
  /**
   * Rotates the given point around the z-axis according to
   * the specified degrees. If the degree variable is positive,
   * it rotates counter-clockwise. If it is negative, it 
   * rotates clockwise.
   *  
   * @param v The point to be rotated
   * @param deg The degrees to rotate
   * @return The rotated point in 3d space
   */
  public static double[] rotZ(double[] v, double deg) {
    double[] res = new double[3];
    
    double rad = Math.toRadians(deg);
    double sin = Math.sin(rad);
    double cos = Math.cos(rad);
        
    res[0] = v[0]*cos - v[1]*sin;
    res[1] = v[0]*sin + v[1]*cos;
    res[2] = v[2];
    
    return res;
  }
  
}
