package ontology;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import step.event.Event;
import step.event.RotationalUnit;
import step.shell.ClosedShell;

public class GraphAnalyzerTest {

	@Test
	public void test() throws FileNotFoundException {

		// create graph from step file
		// VDI2206Graph graph = new VDI2206Graph(pathName + fileName);
		VDI2206GraphTest graph = new VDI2206GraphTest();

		// queries
		// path name
		String queryPathName = "src/test/resources/ontology/";
		// list of sparql queries to be executed (in the right order!)
		String[] queryFileNames = {
			"u_add_static_dummy",
			"s_comps",
			"s_temp_object_prop",
			"u_temp_object_prop",
			"s_add_modules",
			"u_add_modules",
			// "s_modules_and_comps",
			"s_static_list",
			"u_static_list",
			"s_static_list2",
			"s_static_components",
			"u_static_components",
			"s_modules_and_comps",
			"s_static_list2",
			"s_clean_static_list",
			"u_clean_static_list",
			"s_static_list2",
			"u_undefined_list",
			"s_undefined_list",
			// "u_test_add_dummy",
			"s_modules_and_comps"
		};
		// execute all queries
		for (String queryFileName: queryFileNames) {
			System.out.println(queryFileName.substring(0, 1));
			Boolean isUpdate = queryFileName.substring(0, 1).equals("u");
			// get query string from query file
			String queryPathFileName = queryPathName + queryFileName + ".sparql";
			// execute query
			graph.executeQuery(queryPathFileName, isUpdate);
		}

	}


  @Test
  public void testUnrelatedComps() throws FileNotFoundException {
    // we assume three closed shells #1, #2, #3
    // #2 and #3 have a rotational event (around y)
    // #1 is not involved in any event

    ClosedShell cs1 = new ClosedShell("1", "1");
    ClosedShell cs2 = new ClosedShell("2", "2");
    ClosedShell cs3 = new ClosedShell("3", "3");

    List<ClosedShell> shellList = new ArrayList<>();
    shellList.add(cs1);
    shellList.add(cs2);
    shellList.add(cs3);

    List<Event> eventList = new ArrayList<>();
    double[] axisVec = {0.0, 1.0, 0.0};
    double[] axisOrigin = {0.0, 0.0, 0.0};
    eventList.add(new RotationalUnit(cs2, cs3, axisVec, axisOrigin));

    VDI2206Graph graph = new VDI2206Graph(eventList, shellList);
    applyQuery(graph);

    // expected:
    //    #2 and #3 in one module (fulfilled)
    //    #1 as undefined component or as its own module (not fulfilled)
  }

  @Test
  public void testUnrelatedCompGroups() throws FileNotFoundException {
    // we assume four closed shells #1, #2, #3, #4
    // #1 and #2 as well as #3 and #4 have a rotational event

    ClosedShell cs1 = new ClosedShell("1", "1");
    ClosedShell cs2 = new ClosedShell("2", "2");
    ClosedShell cs3 = new ClosedShell("3", "3");
    ClosedShell cs4 = new ClosedShell("4", "4");

    List<Event> eventList = new ArrayList<>();
    double[] axisVec1 = {0.0, 1.0, 0.0};
    double[] axisOrigin1 = {0.0, 0.0, 0.0};
    eventList.add(new RotationalUnit(cs1, cs2, axisVec1, axisOrigin1));
    double[] axisVec2 = {1.0, 0.0, 0.0};
    double[] axisOrigin2 = {10.0, 10.0, 10.0};
    eventList.add(new RotationalUnit(cs3, cs4, axisVec2, axisOrigin2));

    VDI2206Graph graph = new VDI2206Graph(eventList, new ArrayList<ClosedShell>());
    applyQuery(graph);

    // expected:
    //    #1 and #2 in one module (fulfilled)
    //    #3 and #4 in one module (fulfilled)
  }

  public void applyQuery(VDI2206Graph graph) throws FileNotFoundException {
    // queries
    // path name
    String queryPathName = "src/main/resources/ontology/";
    // list of sparql queries to be executed (in the right order!)
    String[] queryFileNames = {
      "u_temp_object_prop",
      "u_add_modules",
      "u_static_list",
      "u_static_components",
      "u_clean_static_list",
      "u_undefined_list",
      "s_modules_and_comps",
      "s_undefined_list"
    };
    // execute all queries
    for (String queryFileName : queryFileNames) {
      Boolean isUpdate = queryFileName.substring(0, 1).equals("u");
      // get query string from query file
      String queryPathFileName = queryPathName + queryFileName + ".sparql";
      // execute query
      graph.executeQuery(queryPathFileName, isUpdate);
    }

  }

  /* ##### evaluation tests ##### */
  @Test
  public void testEval_bgr1() throws FileNotFoundException {
    String[] args = {"src/test/resources/evaluation_step/", "bgr1.stp"};
    GraphAnalyzer.main(args);
  }
  
  @Test
  public void testEval_bgr2() throws FileNotFoundException {
    String[] args = {"src/test/resources/evaluation_step/", "bgr2.stp"};
    GraphAnalyzer.main(args);
  }
  
  @Test
  public void testEval_bgr3() throws FileNotFoundException {
    String[] args = {"src/test/resources/evaluation_step/", "bgr3.stp"};
    GraphAnalyzer.main(args);
  }
  
  @Test
  public void testEval_bgr4() throws FileNotFoundException {
    String[] args = {"src/test/resources/evaluation_step/", "bgr4.stp"};
    GraphAnalyzer.main(args);
  }
  
//  @Test  // infinite loop
  public void testEval_CNC_AP214() throws FileNotFoundException {
    String[] args = {"src/test/resources/evaluation_step/", "CNC_AP214.stp"};
    GraphAnalyzer.main(args);
  }
  
  @Test
  public void testEval_drehtisch_mw_verschoben_xyz_ap214() throws FileNotFoundException {
    String[] args = {"src/test/resources/evaluation_step/", "drehtisch-mw-verschoben-xyz-ap214.stp"};
    GraphAnalyzer.main(args);
  }
  
//  @Test // infinite loop
  public void testEval_FORKLIFT_AP214() throws FileNotFoundException {
    String[] args = {"src/test/resources/evaluation_step/", "FORKLIFT_AP214.stp"};
    GraphAnalyzer.main(args);
  }
  
//    @Test // infinite loop
  public void testEval_Kuka_AP214() throws FileNotFoundException {
    String[] args = {"src/test/resources/evaluation_step/", "Kuka_AP214.stp"};
    GraphAnalyzer.main(args);
  }
  
  @Test
  public void testEval_LinearRail_AP214() throws FileNotFoundException {
    String[] args = {"src/test/resources/evaluation_step/", "LinearRail_AP214.stp"};
    GraphAnalyzer.main(args);
  }
  
  @Test
  public void testEval_RobotArm_AP214() throws FileNotFoundException {
    String[] args = {"src/test/resources/evaluation_step/", "RobotArm_AP214.stp"};
    GraphAnalyzer.main(args);
  }
  
  //  @Test // infinite loop
  public void testEval_Wheel_loader_AP214() throws FileNotFoundException {
    String[] args = {"src/test/resources/evaluation_step/", "Wheel_loader_AP214.stp"};
    GraphAnalyzer.main(args);
  }

}