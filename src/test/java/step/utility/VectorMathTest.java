package step.utility;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static step.utility.VectorMath.*;

import org.junit.Assert;
import org.junit.Test;

public class VectorMathTest {
  
  @Test
  public void testDotProd() {
    double[] a = {1, 2, 3};
    double[] b = {-7, 8, 9};
    
    double res = dotProd(a,b);
    Assert.assertEquals(36.0, res, 0);
  }
  
  @Test
  public void testCrossProd() {
    double[] a = {1, 2, 3};
    double[] b = {-7, 8, 9};
    
    double[] res = crossProd(a,b);
    double[] exRes = {-6, -30, 22};
    Assert.assertArrayEquals(exRes, res, 0);
  }
  
  @Test
  public void testVecLength() {
    double[] a = {1, 2, 3};

    double res = vecLength(a);
    Assert.assertEquals(3.74165, res, 0.0001);
  }
  
  @Test
  public void testVecPlus() {
    double[] v1 = new double[] { 1.0, 2.0, 3.0 };
    double[] v2 = new double[] { 4.0, 5.0, 6.0 };
    
    double[] exRes = new double[] { 5.0, 7.0, 9.0 };
    double[] res = vecPlus(v1, v2);
    
    Assert.assertArrayEquals(exRes, res, 0);
  }
  
  @Test
  public void testVecMinus() {
    double[] v1 = new double[] { 1.0, 2.0, 3.0 };
    double[] v2 = new double[] { 4.0, 5.0, 6.0 };
    
    double[] exRes = new double[] { -3.0, -3.0, -3.0 };
    double[] res = vecMinus(v1, v2);
    
    Assert.assertArrayEquals(exRes, res, 0);
  }
  
  @Test
  public void testVecTimes() {
    double[] v1 = new double[] { 1.0, 2.0, 3.0 };
    double[] v2 = new double[] { 4.0, 5.0, 6.0 };
    
    double[] exRes = new double[] { 4.0, 10.0, 18.0 };
    double[] res = vecTimes(v1, v2);
    
    Assert.assertArrayEquals(exRes, res, 0);
  }
  
  @Test
  public void testVecDiv() {
    double[] v1 = new double[] { 5.0, 2.0, 3.0 };
    double[] v2 = new double[] { 2.0, 2.0, 6.0 };
    
    double[] exRes = new double[] { 2.5, 1.0, 0.5 };
    double[] res = vecDiv(v1, v2);
    
    Assert.assertArrayEquals(exRes, res, 0);
  }
  
  @Test
  public void testVecDivError() {
    double[] v1 = new double[] { 5.0, 2.0, 3.0 };
    double[] v2 = new double[] { 2.0, 0.0, 6.0 };
    
    double[] exRes = new double[] { 0.0, 0.0, 0.0 };
    double[] res = vecDiv(v1, v2);
    
    Assert.assertArrayEquals(exRes, res, 0);
  }
  
  @Test
  public void testVecPlusValue() {
    double[] v = new double[] { 10.0, 20.0, 30.0 };
    double x = 5.0;
    
    double[] exRes = new double[] { 15.0, 25.0, 35.0 };
    double[] res = vecPlus(v, x);
    
    Assert.assertArrayEquals(exRes, res, 0);
  }
  
  @Test
  public void testVecMinusValue() {
    double[] v = new double[] { 10.0, 20.0, 30.0 };
    double x = 5.0;
    
    double[] exRes = new double[] { 5.0, 15.0, 25.0 };
    double[] res = vecMinus(v, x);
    
    Assert.assertArrayEquals(exRes, res, 0);
  }
  
  @Test
  public void testVecTimesValue() {
    double[] v = new double[] { 10.0, 20.0, 30.0 };
    double x = 5.0;
    
    double[] exRes = new double[] { 50.0, 100.0, 150.0 };
    double[] res = vecTimes(v, x);
    
    Assert.assertArrayEquals(exRes, res, 0);
  }
  
  @Test
  public void testVecDivValue() {
    double[] v = new double[] { 10.0, 20.0, 30.0 };
    double x = 5.0;
    
    double[] exRes = new double[] { 2.0, 4.0, 6.0 };
    double[] res = vecDiv(v, x);
    
    Assert.assertArrayEquals(exRes, res, 0);
  }
  
  @Test
  public void testVecDivValueError() {
    double[] v = new double[] { 10.0, 20.0, 30.0 };
    double x = 0.0;
    
    double[] exRes = new double[] { 0.0, 0.0, 0.0 };
    double[] res = vecDiv(v, x);
    
    Assert.assertArrayEquals(exRes, res, 0);
  }
  
  @Test
  public void testVecEquals() {
    double[] p1 = new double[] { 2.0, 2.5, 2.0 };
    double[] p2 = new double[] { 2.0, 2.5, 2.0 };
    double[] p3 = new double[] { 20.0, 2.5, 2.0 };
    double[] p4 = new double[] { 2.0, 2.5 };
    
    assertTrue(vecEquals(p1, p2));
    assertFalse(vecEquals(p1, p3));
    assertFalse(vecEquals(p1, p4));
  }
  
  @Test
  public void testRotations() {
    
    double[] p = {0.0, 10.0, 0.0};
    double deg = 26.43;
    
    // counter-clockwise rotation
    double[] rX = VectorMath.rotX(p, deg);
    double[] rY = VectorMath.rotY(p, deg);
    double[] rZ = VectorMath.rotZ(p, deg);
    
    double[] resX = {0.0, 8.9547, 4.4510};
    double[] resY = {0.0, 10.0, 0.0};
    double[] resZ = {-4.4510, 8.9547, 0.0};
    
    assertArrayEquals(resX, rX, 0.001);
    assertArrayEquals(resY, rY, 0.001);
    assertArrayEquals(resZ, rZ, 0.001);
    
    // clockwise rotation
    rX = VectorMath.rotX(p, -deg);
    rY = VectorMath.rotY(p, -deg);
    rZ = VectorMath.rotZ(p, -deg);
    
    double[] resXc = {0.0, 8.9547, -4.4510};
    double[] resYc = {0.0, 10.0, 0.0};
    double[] resZc = {4.4510, 8.9547, 0.0};
    
    assertArrayEquals(resXc, rX, 0.001);
    assertArrayEquals(resYc, rY, 0.001);
    assertArrayEquals(resZc, rZ, 0.001);
  }
  
}
