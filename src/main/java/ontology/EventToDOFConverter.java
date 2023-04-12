package ontology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Arrays;

import step.event.Event;
import step.event.RotationalUnit;
import step.event.TransaltionalRestriction;
import step.shell.ClosedShell;

import static step.utility.VectorMath.*;

import Jama.Matrix;

public class EventToDOFConverter {
    
    // constants
	public static final double[] null_vec = {0.0, 0.0, 0.0};
	public static final double[] x_dir = {1.0, 0.0, 0.0};
	public static final double[] y_dir = {0.0, 1.0, 0.0};
	public static final double[] z_dir = {0.0, 0.0, 1.0};
	public final static double delta = 0.00001;

	// lists
	private List<Component> componentList;
	private List<MechanicalInterface> mInterfaceList;
	private List<Event> eventList; 
	private List<ClosedShell> shellList;

	/**
	 * constructor. generates componentList and mInterfaceList from eventList.
	 * @param eventList
	 */
	public EventToDOFConverter(List<Event> eventList, List<ClosedShell> shellList) {
		this.eventList = eventList;
		this.shellList = shellList;
		this.events2MInterfaces();
	}

	public List<Component> getComponentList() {
		return this.componentList;
	}

	public List<MechanicalInterface> getMInterfaceList() {
		return this.mInterfaceList;
	}

	/**
	 * convert events from an event list to components and interfaces.
	 * @param eventList the input event list
	 * @param componentList	the target component list
	 * @param mInterfaceList the target interface list
	 */
	private void events2MInterfaces() {
		// inititalize lists
		this.componentList = new ArrayList<Component>();
		this.mInterfaceList = new ArrayList<MechanicalInterface>();
		// map list of events to their assembly tuple
		Map<Set<String>, List<Event>> eventMap = new HashMap<Set<String>, List<Event>>();
		for (Event event : this.eventList) {
			Set<String> key = new HashSet<String>();
			key.add(event.getAssemblies()[0].getId());
			key.add(event.getAssemblies()[1].getId());
			
			if (!eventMap.containsKey(key)) {
				eventMap.put(key, new ArrayList<Event>());
			} 
			eventMap.get(key).add(event);
		}
		
		// fill component list
		eventMap.keySet().stream()
				.flatMap(Collection::stream)
				.forEach(id -> componentList.add(new Component(id)));

		// add all components which are not part of an event to component list
		for (ClosedShell shell : shellList) {
			Component component = new Component(shell.getId());
			if (!(componentList.contains(component))) {
				componentList.add(component);
			}
		}
		
		for (Entry<Set<String>, List<Event>> entry : eventMap.entrySet()) {
			
			// we can assume that there are always exactly two elements
			String[] ids = entry.getKey().toArray(new String[0]);
			Component cmp1 = componentList.stream().filter(c -> c.getComponentName().equals(ids[0])).findAny().get();
			Component cmp2 = componentList.stream().filter(c -> c.getComponentName().equals(ids[1])).findAny().get();
			
			List<DOF> dofList = new ArrayList<DOF>();
			
			List<Event> localEventList = entry.getValue();
			List<TransaltionalRestriction> localTransRestr = localEventList.stream()
					.filter(e -> e instanceof TransaltionalRestriction)
					.map(e -> (TransaltionalRestriction) e)
					.collect(Collectors.toList());
			List<RotationalUnit> localRotUnits = localEventList.stream()
					.filter(e -> e instanceof RotationalUnit)
					.map(e -> (RotationalUnit) e)
					.collect(Collectors.toList());
						
			// derive translational & rotational DOFs
			dofList.addAll(getTranslationalDOFs(localTransRestr, localRotUnits));
			dofList.addAll(getRotationalDOFs(localRotUnits));
			
			// create Interface and add to list
			MechanicalInterface mInterface = new MechanicalInterface(cmp1, cmp2, dofList);
			mInterfaceList.add(mInterface);
		}
	}

    /**
     * combines multiple rotational units between two components to actual dofs. <p>
     * helper method - only used by {@link #events2MInterfaces}
     * @param rotUnits input rotaitional units
     * @return a list of dofs
     */
    private List<DOF> getRotationalDOFs(List<RotationalUnit> rotUnits) {
		List<DOF> dofList = new ArrayList<DOF>();
		
		// rotational DOF wrt. a functional unit can only exist if rot. events do
		if (!rotUnits.isEmpty()) {
			boolean sameLocAndDir = true;
			RotationalUnit firstEvent = rotUnits.get(0);
			for (RotationalUnit rotUnit : rotUnits) {
				sameLocAndDir &= vecEquals(firstEvent.getAxisOrigin(), rotUnit.getAxisOrigin(), delta);
				sameLocAndDir &= vecEquals(firstEvent.getAxisVec(), rotUnit.getAxisVec(), delta);
			}
			
			// iff all rotational events have the same origin and direction, create DOF
			if (sameLocAndDir) {
				dofList.add(new RotationalDOF(firstEvent.getAxisVec(), firstEvent.getAxisOrigin()));
			} 
		}
		return dofList;
	}

    /**
     * combines multiple translational restrictions and rotational units between two components to actual dofs. <p>
	 * helper method - only used by {@link #events2MInterfaces}
     * @param transRestr translational restrictions
     * @param rotUnits input rotaitional units
     * @return a list of dofs
     */
    private List<DOF> getTranslationalDOFs(List<TransaltionalRestriction> transRestr, List<RotationalUnit> rotUnits) {
		List<DOF> dofList = new ArrayList<DOF>();
		if (transRestr.isEmpty() && rotUnits.isEmpty()) {
			dofList.add(new TranslationalDOF(x_dir));
			dofList.add(new TranslationalDOF(y_dir));
			dofList.add(new TranslationalDOF(z_dir));
		} else if (transRestr.isEmpty()) {
			boolean sameLocAndDir = true;
			RotationalUnit firstEvent = rotUnits.get(0);
			for (RotationalUnit rotUnit : rotUnits) {
				sameLocAndDir &= vecEquals(firstEvent.getAxisOrigin(), rotUnit.getAxisOrigin(), delta);
				sameLocAndDir &= vecEquals(firstEvent.getAxisVec(), rotUnit.getAxisVec(), delta);
			}
			
			// iff all rotational events have the same origin and direction, create DOF
			if (sameLocAndDir) {
				dofList.add(new TranslationalDOF(firstEvent.getAxisVec()));
			} 
		} else {
			// blocking direction vectors are equal to the normal vectors of the 
			// plane that still allows translation
			List<double[]> normals = transRestr.stream().map(r -> r.getTranslationVec())
					.collect(Collectors.toList());
			
			// try to solve LES to identify intersection line if present
			// additionally add a value constraint to omit the null vector as solution
			normals.add(new double[] {1.0, 1.0, 1.0});
			Matrix lhs = new Matrix(normals.toArray(new double[0][0]));
			
			// create solution vector, which is all 0.0 with a concluding 1.0
			double[] lhsVec = new double[normals.size()];
			lhsVec[normals.size() - 1] = 1.0;
			Matrix rhs = new Matrix(lhsVec, normals.size());
			
			// solve LES 
			Optional<Matrix> res = Optional.empty();
			try {
				res = Optional.ofNullable(lhs.solve(rhs));
			} catch (RuntimeException e) {
				// if RT exception is thrown mean that matrix is singular, resulting in a plane
				
				// in this case, all normals are the same cn = common normal
				double[] cn = normals.get(0);
				
				if (rotUnits.isEmpty()) {
					// if no rotational events occur we compute two orthogonal vectors of the normal
					// vector, resulting in two DOFs spanning the translation plane
					double[] planeVec1 = cn[2] < cn[0] ? new double[]{cn[1], -cn[0], 0.0} : new double[]{0.0, -cn[2], cn[1]};
					
					// the second plane vector is orthogonal to both cn and planeVec1
					// thus we can calculate the normal of cn and planeVec1
					double[] planeVec2 = normal(cn, planeVec1);
					dofList.add(new TranslationalDOF(planeVec1));
					dofList.add(new TranslationalDOF(planeVec2));
				} else {
					// only if there is a common origin and direction in all rotational vectors,
					// there is a possibility for a DOF
					boolean sameLocAndDir = true;
					RotationalUnit firstEvent = rotUnits.get(0);
					for (RotationalUnit rotUnit : rotUnits) {
						sameLocAndDir &= vecEquals(firstEvent.getAxisOrigin(), rotUnit.getAxisOrigin(), delta);
						sameLocAndDir &= vecEquals(firstEvent.getAxisVec(), rotUnit.getAxisVec(), delta);
					}
					
					// if all rotational events have the same origin and direction, check if
					// direction lies in the plane, i.e., if the direction and cn are orthogonal
					if (sameLocAndDir) {
						double dpRes = dotProd(cn, firstEvent.getAxisVec());
						if (dpRes - delta <= 1.0 && dpRes + delta >= 1.0) {
							dofList.add(new TranslationalDOF(firstEvent.getAxisVec()));
						}
					} 
				}
				return dofList;
			}

			// if the sum of the resulting vector's elements is 1.0, we have a solution wrt.
			// translational restrictions
			double[] resVec = res.isPresent() ? res.get().getColumnPackedCopy() : null_vec;
			double resVecSum = Arrays.stream(resVec).sum();
			if (resVecSum - delta <= 1.0 && resVecSum + delta >= 1.0) {
				
				// finally check solution vector against viable direction vectors from rotational units
				// and check if all rotational units start at the same origin
				boolean sameLocAndDir = true;
				for (RotationalUnit rotUnit : rotUnits) {
					sameLocAndDir &= vecEquals(resVec, rotUnit.getAxisVec(), delta);
					sameLocAndDir &= vecEquals(rotUnits.get(0).getAxisOrigin(), rotUnit.getAxisOrigin(), delta);
				}
				
				if (sameLocAndDir) {
					dofList.add(new TranslationalDOF(resVec));
				}
				
			} else {
				// otherwise there is no translational DOF
				return new ArrayList<DOF>();
			}
		}

		return dofList;
	}

}
