package visitor;

import de.monticore.literals.mccommonliterals._ast.ASTSignedNatLiteral;
import step.*;
import step._ast.*;
import step._visitor.STEPVisitor;
import step.curve.*;
import step.curve.Vector;
import step.edge.*;
import step.face.*;
import step.point.CartesianPoint;
import step.point.Point;
import step.shell.ClosedShell;
import step.shell.OpenShell;
import step.shell.ShellBasedSurfaceModel;
import step.spline.*;
import step.vertex.Vertex;
import step.vertex.VertexPoint;

import java.util.*;

public class GeometryVisitor implements STEPVisitor {
    private Map<String,Entity> entities;
    private Map<String,List<String>> references;
    private Set<String> missing;

    public GeometryVisitor(){
        entities = new HashMap<>();
        references = new HashMap<>();
        missing = new HashSet<>();
    }

    @Override
    public void visit(ASTInstance node){
        String id = "";
        String label = "";
        if(node.isPresentNamedList()) {
            label = node.getNamedList().getName().getName();
        }
        if(node.getId().isPresentDigits()) {
            id = node.getId().getDigits();
        }
        List<String> refs = new ArrayList<String>();
        if(label.equals(ManifoldSolidBrep.LABEL)){
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            String outer = node.getNamedList().getList().getParameter(1).getId().getDigits();
            ManifoldSolidBrep manifoldSolidBrep = new ManifoldSolidBrep(name, null, id);
            entities.put(id, manifoldSolidBrep);
            refs.add(outer);
            references.put(id,refs);
        }else if(label.equals(ClosedShell.LABEL)){
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            ClosedShell closedShell = new ClosedShell(name, id);
            ASTList faces = node.getNamedList().getList().getParameter(1).getList();
            for (ASTParameter parameter: faces.getParameterList()) {
                String digits = parameter.getId().getDigits();
                refs.add(digits);
            }
            references.put(id,refs);
            entities.put(id,closedShell);
      }else if (label.equals(AdvancedFace.LABEL)){
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            boolean orientation = node.getNamedList().getList().getParameter(3).getENUMERATION().getName().equals("T");
            AdvancedFace advancedFace = new AdvancedFace(name, id, orientation);
            entities.put(id, advancedFace);
            for(ASTParameter parameter: node.getNamedList().getList().getParameter(1).getList().getParameterList()){
                String digits = parameter.getId().getDigits();
                refs.add(digits);
            }
            // directly set references to normal and surface inside the face
            advancedFace.setNormalRef(node.getNamedList().getList().getParameter(2).getId().getDigits());
//            advancedFace.setSurfaceRef(node.getNamedList().getList().getParameter(3).getId().getDigits());
            references.put(id,refs);
        }else if(label.equals(OrientedEdge.LABEL)){
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            boolean orientation = node.getNamedList().getList().getParameter(4).getENUMERATION().getName().equals("T");
            String edge = node.getNamedList().getList().getParameter(3).getId().getDigits();
            OrientedEdge orientedEdge = new OrientedEdge(name, id, orientation, null);
            refs.add(edge);
            entities.put(id,orientedEdge);
            references.put(id,refs);
        }else if(label.equals(EdgeLoop.LABEL)){
          String name = node.getNamedList().getList().getParameter(0).getOtherString();
          EdgeLoop edgeLoop = new EdgeLoop(name, id);
          node.getNamedList().getList().getParameter(1).getList().getParameterList().forEach(p -> refs.add(p.getId().getDigits()));
          entities.put(id,edgeLoop);
          references.put(id,refs);
        }else if(label.equals(EdgeCurve.LABEL)) {
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            boolean same_sense = node.getNamedList().getList().getParameter(4).getENUMERATION().getName().equals("T");
            EdgeCurve edgeCurve = new EdgeCurve(name, id, same_sense);
            for(ASTParameter parameter: node.getNamedList().getList().getParameterList().subList(1,4)){
                String digits = parameter.getId().getDigits();
                refs.add(digits);
            }
            // remove last entry as it contains the line connecting the vertices, 
            // which might introduce new points resulting in false positives
            refs.remove(refs.size() - 1);
            entities.put(id, edgeCurve);
            references.put(id, refs);
        } else if(label.equals(CartesianPoint.LABEL)) {
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            List<Double> coordinates = new ArrayList<>();
            CartesianPoint cartesianPoint = new CartesianPoint(name, id);
            for(ASTParameter parameter : node.getNamedList().getList().getParameterList().get(1).getList().getParameterList()) {
                String strValue = "";
                ASTSignedBasicDoubleLiteralWithExp cord = (ASTSignedBasicDoubleLiteralWithExp) parameter.getSignedNumericLiteral();
                if (cord.isNegative()) {
                  strValue += "-";
                }
                strValue += cord.getDecimalDoublePointLiteral();
                Double value = Double.valueOf(strValue);
                coordinates.add(value);
            }
            cartesianPoint.setCoordinates(coordinates);
            entities.put(id, cartesianPoint);
        }else if(label.equals(VertexPoint.LABEL)) {
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            VertexPoint vertexPoint = new VertexPoint(name, id, null);
            entities.put(id, vertexPoint);
            String pointId = node.getNamedList().getList().getParameter(1).getId().getDigits();
            refs.add(pointId);
            references.put(id, refs);
        }else if (label.equals(Line.LABEL)) {
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            Line line = new Line(name, id);
            refs.add(node.getNamedList().getList().getParameter(1).getId().getDigits());
            refs.add(node.getNamedList().getList().getParameter(2).getId().getDigits());
            references.put(id,refs);
            entities.put(id, line);
        }else if (label.equals(Vector.LABEL)) {
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            Vector vector = new Vector(name, id);
            String magnitudeStr = ((ASTSignedBasicDoubleLiteralWithExp)node.getNamedList().getList().getParameter(2).getSignedNumericLiteral()).getDecimalDoublePointLiteral();
            double magnitude = Double.parseDouble(magnitudeStr);
            vector.setMagnitude(magnitude);
            entities.put(id, vector);
        } else if (label.equals(Direction.LABEL)) {
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            Direction direction = new Direction(name, id);
            List<Double> directionRatios = new ArrayList<>();
            for(ASTParameter parameter: node.getNamedList().getList().getParameterList().get(1).getList().getParameterList()){
              String valueStr = "";
              ASTSignedBasicDoubleLiteralWithExp cord = (ASTSignedBasicDoubleLiteralWithExp)parameter.getSignedNumericLiteral();
              if (cord.isNegative()) {
                valueStr += "-";
              }
              valueStr += cord.getDecimalDoublePointLiteral();
              double value = Double.parseDouble(valueStr);
              directionRatios.add(value);
            }
            direction.setDirection_ratios(directionRatios);
            entities.put(id, direction);
        } else if (label.equals(BSplineSurfaceWithKnots.LABEL)) {
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            BSplineSurfaceWithKnots spline = new BSplineSurfaceWithKnots(name, id);
            String digits = ((ASTSignedNatLiteral) node.getNamedList().getList().getParameter(1).getSignedNumericLiteral()).getDigits();
            int degree = Integer.parseInt(digits);
            spline.setU_degree(degree);
            digits = ((ASTSignedNatLiteral) node.getNamedList().getList().getParameter(2).getSignedNumericLiteral()).getDigits();
            degree = Integer.parseInt(digits);
            spline.setV_degree(degree);
            List<List<Point>> control_points_list = new LinkedList<>();
            for(ASTParameter parameter: node.getNamedList().getList().getParameterList().get(3).getList().getParameterList()) {
                ArrayList<Point> list = new ArrayList<>();
                for (ASTParameter point : parameter.getList().getParameterList()) {
                    String ref = point.getId().getDigits();
                    refs.add(ref);
                    list.add(new Point("",ref));
                }
                control_points_list.add(list);
            }
            spline.setControl_points_list(control_points_list);
            String surfaceForm = node.getNamedList().getList().getParameter(4).getENUMERATION().getName();
            spline.setSurface_form(SurfaceForm.valueOf(surfaceForm));
            boolean uClosed = extractBoolean(node,5);
            boolean vClosed = extractBoolean(node,6);
            boolean selfIntersect = extractBoolean(node,7);

            List<Double> uknots = new ArrayList<>();
            List<Double> vknots = new ArrayList<>();
            List<Integer> umultiplicities = new ArrayList<>();
            List<Integer> vmultiplicities = new ArrayList<>();

            for(ASTParameter parameter: node.getNamedList().getList().getParameterList().get(8).getList().getParameterList()) {
                digits = ((ASTSignedNatLiteral) parameter.getSignedNumericLiteral()).getDigits();
                int value = Integer.parseInt(digits);
                umultiplicities.add(value);
            }
            for(ASTParameter parameter: node.getNamedList().getList().getParameterList().get(9).getList().getParameterList()) {
                digits = ((ASTSignedNatLiteral) parameter.getSignedNumericLiteral()).getDigits();
                int value = Integer.parseInt(digits);
                vmultiplicities.add(value);
            }
            for(ASTParameter parameter: node.getNamedList().getList().getParameterList().get(10).getList().getParameterList()) {
                digits = ((ASTSignedBasicDoubleLiteralWithExp)parameter.getSignedNumericLiteral()).getDecimalDoublePointLiteral();
                double value = Double.parseDouble(digits);
                uknots.add(value);
            }
            for(ASTParameter parameter: node.getNamedList().getList().getParameterList().get(11).getList().getParameterList()) {
                digits =  ((ASTSignedBasicDoubleLiteralWithExp)parameter.getSignedNumericLiteral()).getDecimalDoublePointLiteral();
                double value = Double.parseDouble(digits);
                vknots.add(value);
            }
            spline.setU_knots(uknots);
            spline.setV_knots(vknots);
            spline.setU_multiplicities(umultiplicities);
            spline.setV_multiplicities(vmultiplicities);

            String knotType = node.getNamedList().getList().getParameter(12).getENUMERATION().getName();
            spline.setKnot_spec(KnotType.valueOf(knotType));

            spline.setSelf_intersect(selfIntersect);
            spline.setU_closed(uClosed);
            spline.setV_closed(vClosed);
            entities.put(id, spline);
            references.put(id, refs);
        } else if(label.equals(ToroidalSurface.LABEL)){
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            String ref = node.getNamedList().getList().getParameter(1).getId().getDigits();
            refs.add(ref);
            ToroidalSurface toroidalSurface = new ToroidalSurface(name, id);
            toroidalSurface.setMajor_radius(extractDouble(node,2));
            toroidalSurface.setMinor_radius(extractDouble(node, 3));
            entities.put(id, toroidalSurface);
            references.put(id,refs);
        } else if(label.equals(SphericalSurface.LABEL)){
            String name = node.getNamedList().getList().getParameter(0).getOtherString();
            String ref = node.getNamedList().getList().getParameter(1).getId().getDigits();
            refs.add(ref);
            SphericalSurface sphericalSurface = new SphericalSurface(name, id);
            sphericalSurface.setRadius(extractDouble(node,2));
            entities.put(id, sphericalSurface);
            references.put(id,refs);
        } else if(label.equals(FaceOuterBound.LABEL)){
            String name = extractString(node, 0);
            String ref = extractIdAsString(node, 1);
            refs.add(ref);
            boolean orientation = extractBoolean(node, 2);
            FaceOuterBound faceOuterBound = new FaceOuterBound(name, id, orientation);
            entities.put(id, faceOuterBound);
            references.put(id,refs);
        }else if(label.equals(FaceBound.LABEL)){
            String name = extractString(node, 0);
            String ref = extractIdAsString(node, 1);
            refs.add(ref);
            boolean orientation = extractBoolean(node, 2);
            FaceBound faceBound = new FaceBound(name, id, orientation);
            entities.put(id, faceBound);
            references.put(id,refs);
        }else if(label.equals(BSplineCurveWithKnots.LABEL)){
            String name = extractString(node, 0);
            BSplineCurveWithKnots bSplineCurveWithKnots = new BSplineCurveWithKnots(name,id);
            bSplineCurveWithKnots.setDegree(extractInt(node, 1));
            refs.addAll(extractIdList(node,2));
            bSplineCurveWithKnots.setCurve_form(BSplineCurveForm.valueOf(extractEnumLiteral(node,3)));
            bSplineCurveWithKnots.setClosed_curve(extractBoolean(node, 4));
            bSplineCurveWithKnots.setSelf_intersect(extractBoolean(node, 5));
            bSplineCurveWithKnots.setKnot_multiplicities(extractIntList(node,6));
            bSplineCurveWithKnots.setKnots(extractDoubleList(node,7));
            bSplineCurveWithKnots.setKnot_spec(KnotType.valueOf(extractEnumLiteral(node,8)));
            entities.put(id, bSplineCurveWithKnots);
            references.put(id, refs);

        }else if(label.equals(Axis2Placement3D.LABEL)){
            String name = extractString(node, 0);
            refs.add(extractIdAsString(node,1));
            refs.add(extractIdAsString(node,2));
            refs.add(extractIdAsString(node,3));
            Axis2Placement3D axis2Placement3D = new Axis2Placement3D(name, id);
            entities.put(id,axis2Placement3D);
            references.put(id,refs);

        }else if(label.equals(TrimmedCurve.LABEL)){
            String name = extractString(node, 0);
            TrimmedCurve trimmedCurve = new TrimmedCurve(name, id);
            entities.put(id, trimmedCurve);
            trimmedCurve.setSense_agreement(extractBoolean(node, 4));
            Set<TrimmingSelect> trim_1 = new HashSet<>();
            Set<TrimmingSelect> trim_2 = new HashSet<>();
            TrimmingSelect t1 = new TrimmingSelect();
            TrimmingSelect t2 = new TrimmingSelect();

            String value1 = ((ASTSignedBasicDoubleLiteralWithExp) node.getNamedList().getList().getParameter(2).getList().getParameter(2).getList().getParameter(0).getSignedNumericLiteral()).getDecimalDoublePointLiteral();
            double v1 = Double.parseDouble(value1);
            ParameterValue parameterValue1 = new ParameterValue(v1);
            t1.setValue(parameterValue1);
            String id1 = node.getNamedList().getList().getParameter(2).getList().getParameter(0).getId().getDigits();

            String value2 = ((ASTSignedBasicDoubleLiteralWithExp) node.getNamedList().getList().getParameter(3).getList().getParameter(2).getList().getParameter(0).getSignedNumericLiteral()).getDecimalDoublePointLiteral();
            double v2 = Double.parseDouble(value2);
            ParameterValue parameterValue2 = new ParameterValue(v2);
            t2.setValue(parameterValue2);
            String id2 = node.getNamedList().getList().getParameter(3).getList().getParameter(0).getId().getDigits();

            t2.setCartesianPoint(new Point("",id2));
            trimmedCurve.setTrim_1(trim_1);
            trimmedCurve.setTrim_2(trim_2);
            trimmedCurve.setMaster_representation(TrimmingPreference.valueOf(extractEnumLiteral(node, 5)));

        }else if(label.equals(Circle.LABEL)){
            String name = extractString(node, 0);
            Circle circle = new Circle(name, id);
            circle.setRadius(extractDouble(node,2));
            refs.add(extractIdAsString(node, 1));
            entities.put(id, circle);
            references.put(id, refs);
        }else if(label.equals(Plane.LABEL)){
            String name = extractString(node, 0);
            Plane plane = new Plane(name, id);
            refs.add(extractIdAsString(node, 1));
            entities.put(id, plane);
            references.put(id, refs);
        }else if(label.equals(Polyline.LABEL)){
            String name = extractString(node, 0);
            Polyline polyline = new Polyline(name, id);
            refs.addAll(extractIdList(node,1));
            entities.put(id, polyline);
            references.put(id, refs);
        }else if(label.equals(OpenShell.LABEL)){
            String name = extractString(node, 0);
            OpenShell openShell = new OpenShell(id, name);
            refs.addAll(extractIdList(node,1));
            entities.put(id, openShell);
            references.put(id, refs);
        }else if(label.equals(ConicalSurface.LABEL)){
            String name = extractString(node, 0);
            ConicalSurface conicalSurface = new ConicalSurface(name, id);
            conicalSurface.setRadius(extractDouble(node, 2));
            conicalSurface.setSemi_angle(extractDouble(node, 3));
            refs.add(extractIdAsString(node,1));
            entities.put(id, conicalSurface);
            references.put(id, refs);
        }else if(label.equals(CylindricalSurface.LABEL)){
            String name = extractString(node, 0);
            CylindricalSurface cylindricalSurface = new CylindricalSurface(name, id);
            cylindricalSurface.setRadius(extractDouble(node,2));
            refs.add(extractIdAsString(node, 1));
            entities.put(id, cylindricalSurface);
            references.put(id, refs);
        }else if(label.equals(GeometricCurveSet.LABEL)){
            String name = extractString(node, 0);
            GeometricCurveSet geometricCurveSet = new GeometricCurveSet(name, id);
            refs.addAll(extractIdList(node, 1));
            entities.put(id, geometricCurveSet);
            references.put(id, refs);
        }else if(label.equals(ShellBasedSurfaceModel.LABEL)){
            String name = extractString(node, 0);
            ShellBasedSurfaceModel geometricCurveSet = new ShellBasedSurfaceModel(name, id);
            refs.addAll(extractIdList(node, 1));
            entities.put(id, geometricCurveSet);
            references.put(id, refs);
        }
        else if(label.equals(SurfaceCurve.LABEL)){
          String name = extractString(node, 0);
          SurfaceCurve surfaceCurve = new SurfaceCurve(name, id);
          refs.addAll(extractIdList(node, 2));
          entities.put(id, surfaceCurve);
          references.put(id, refs);
        }


        else {
                missing.add(label);
        }


    }

    private double extractDouble(ASTInstance node, int index) {
        String digits = ((ASTSignedBasicDoubleLiteralWithExp) node.getNamedList().getList().getParameter(index).getSignedNumericLiteral()).getDecimalDoublePointLiteral();
        return Double.parseDouble(digits);
    }

    private boolean extractBoolean(ASTInstance node, int index){
        return node.getNamedList().getList().getParameter(index).getENUMERATION().getName().equals("T");
    }

    private String extractString(ASTInstance node, int index){
        String otherString = node.getNamedList().getList().getParameter(index).getOtherString();
        return otherString;
    }

    private String extractIdAsString(ASTInstance node, int index){
        return node.getNamedList().getList().getParameter(index).getId().getDigits();
    }

    private int extractInt(ASTInstance node, int index){
        String digits = ((ASTSignedNatLiteral) node.getNamedList().getList().getParameter(index).getSignedNumericLiteral()).getDigits();
        return Integer.parseInt(digits);
    }

    private String extractEnumLiteral(ASTInstance node, int index){
        String enumLiteral = node.getNamedList().getList().getParameter(index).getENUMERATION().getName();
        return enumLiteral;
    }

    private List<String> extractIdList(ASTInstance node, int index){
        List<String> ids = new ArrayList<>();
        for(ASTParameter parameter: node.getNamedList().getList().getParameterList().get(index).getList().getParameterList()) {
            String id = parameter.getId().getDigits();
            ids.add(id);
        }
        return ids;
    }

    private List<Integer> extractIntList(ASTInstance node, int index){
        List<Integer> intList = new ArrayList<>();
        for(ASTParameter parameter: node.getNamedList().getList().getParameterList().get(index).getList().getParameterList()) {
            String digits = ((ASTSignedNatLiteral) parameter.getSignedNumericLiteral()).getDigits();
            int value = Integer.parseInt(digits);
            intList.add(value);
        }
        return intList;
    }

    private List<Double> extractDoubleList(ASTInstance node, int index){
        List<Double> doubleList = new ArrayList<>();
        for(ASTParameter parameter: node.getNamedList().getList().getParameterList().get(index).getList().getParameterList()) {
            String digits = ((ASTSignedBasicDoubleLiteralWithExp)parameter.getSignedNumericLiteral()).getDecimalDoublePointLiteral();
            double value = Double.parseDouble(digits);
            doubleList.add(value);
        }
        return doubleList;
    }

    public Map<String, Entity> getEntities() {
        return entities;
    }

    public Map<String, List<String>> getReferences() {
        return references;
    }

    public void setEntities(Map<String, Entity> entities) {
        this.entities = entities;
    }

    public void setReferences(Map<String, List<String>> references) {
        this.references= references;
    }

    public void resolveReferences(){
        for (String key: references.keySet()) {
            Entity entity = entities.get(key);
            if(entity instanceof ManifoldSolidBrep){
                for (String value : references.get(key)) {
                    ((ManifoldSolidBrep) entity).setOuter((ClosedShell) entities.get(value));
                }
            }else if (entity instanceof ClosedShell){
                for (String value : references.get(key)) {
                    ((ClosedShell) entity).getCfs_faces().add((AdvancedFace) entities.get(value));
                }
            }else if (entity instanceof OrientedEdge){
                for(String value: references.get(key)){
                    ((OrientedEdge) entity).setEdge_element((Edge) entities.get(value));
                }
            }else if (entity instanceof EdgeCurve) {
                List<String> ids = references.get(key);
                Vertex start = (Vertex) entities.get(ids.get(0));
                Vertex end = (Vertex) entities.get(ids.get(1));
                Curve curve = (Curve) entities.get(ids.get(2));
                ((EdgeCurve) entity).setEdge_start(start);
                ((EdgeCurve) entity).setEdge_end(end);
                ((EdgeCurve) entity).setEdge_geometry(curve);
            }else if (entity instanceof Line) {
                List<String> ids = references.get(key);
                Point pnt = (Point) entities.get(ids.get(0));
                Vector dir = (Vector) entities.get(ids.get(1));
                ((Line) entity).setPnt(pnt);
                ((Line) entity).setDir(dir);

            }else if (entity instanceof VertexPoint){
                String pointId = references.get(key).get(0);
                Point point = ((Point) entities.get(pointId));
                ((VertexPoint) entity).setVertex_geometry(point);

            }else if (entity instanceof BSplineSurfaceWithKnots){
                List<List<Point>> dummy = ((BSplineSurfaceWithKnots) entity).getControl_points_list();
                List<List<Point>> controlPoints = new LinkedList<>();
                for(List<Point> cp: dummy){
                    List<Point> knots = new LinkedList<>();
                    for(Point p: cp){
                        knots.add((Point) entities.get(p.getId()));
                    }
                    controlPoints.add(knots);
                }
            }
        }
    }

    public Set<String> getMissing(){
        return this.missing;
    }

    public Map<String, List<String>> findMultipleReferences(){
        Map<String, List<String>> referred = new HashMap<>();
        for(String key: references.keySet()){
            for(String value: references.get(key)){
                if(referred.containsKey(value)){
                    referred.get(value).add(key);
                }else {
                    List<String> refs = new ArrayList<>();
                    refs.add(key);
                    referred.put(value, refs);
                }
            }
        }
        Map<String, List<String>> referred2 = new HashMap<>();
        for(String key: referred.keySet()){
            List<String> values = referred.get(key);
            if (values.size() > 1){
                referred2.put(key, values);
            }
        }
        return referred2;
    }

    public Set<Set<String>> findComponents(){
        Set<String> visited = new HashSet<>();
        Set<Set<String>> components = new HashSet<>();
        for(String id: entities.keySet()){
            if(!visited.contains(id)){
                visited.add(id);
                Set<String> component = new HashSet<>();
                component.add(id);
                Set<String> c = utilM(component);
                component.addAll(c);
                visited.addAll(c);
                components.add(component);
            }
        }
        return components;
    }
    public Set<String> utilM(Set<String> component){
        Set<String> refs = new HashSet<>();
        if(!component.isEmpty()){
            for (String id : component) {
                if (references.containsKey(id)) {
                    refs.addAll(references.get(id));
                }
            }
            component.addAll(utilM(refs));
        }
        return component;
    }

}
