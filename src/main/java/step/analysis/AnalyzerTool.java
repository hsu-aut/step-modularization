package step.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import step._ast.ASTStepExchangeFile;
import step._parser.STEPParser;
import step.event.Event;
import step.face.AdvancedFace;
import step.point.CartesianPoint;
import step.shell.ClosedShell;

public class AnalyzerTool {
  
  protected List<ClosedShell> closedShells = new ArrayList<>();
  
  public List<Event> analyze(String fileName) {
    
    List<Event> res = new ArrayList<Event>();
    
    try {
      STEPParser stepParser = new STEPParser();
      Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
      
      if (ast.isPresent()) {
        Preprocessor prep = new Preprocessor(ast.get());
        prep.preprocess();
        closedShells = prep.getClosedShells();
        
        TranslationAnalyzer transAn = new TranslationAnalyzer();
        RotationAnalyzer rotAn = new RotationAnalyzer();
        
        res.addAll(transAn.analyze(prep.getClosedShells()));
        res.addAll(rotAn.analyze(prep.getClosedShells()));
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return res;
  }
  
  public List<ClosedShell> getClosedShells() {
    return closedShells;
  }
  
}
