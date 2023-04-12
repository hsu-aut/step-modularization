import de.monticore.prettyprint.IndentPrinter;
import step._ast.ASTANCHOR_NAME;
import step._ast.ASTConstantsSTEP;
import step._ast.ASTData;
import step._ast.ASTENUMERATION;
import step._ast.ASTHeader;
import step._ast.ASTId;
import step._ast.ASTList;
import step._ast.ASTSignature;
import step._ast.ASTStepExchangeFile;
import step._ast.ASTUNIVERSAL_RESOURCE_IDENTIFIER;
import step._visitor.STEPInheritanceVisitor;

public class STEP_PrettyPrinter extends IndentPrinter implements STEPInheritanceVisitor {
  /*
  • handle(ASTC node) defines the iteration algorithm of ASTC (default: depth
    first). By default handle calls visit, traverse (the children) and endVisit
    on node.
  • traverse(ASTC node) defines a climbdown strategy (i.e. order of children; no
    order is guaranteed by the default implementation).
  • visit(ASTC node) is called when entering node.
  • endVisit(ASTC node) is called when leaving node.
 */

  @Override
  public void visit(ASTStepExchangeFile node) {
    print("ISO-10303-21;");
  }

  @Override
  public void endVisit(ASTStepExchangeFile node) {
    print("END-ISO-10303-21;");
  }

  @Override
  public void visit(ASTId node){
    switch(node.getPre()) {
      case ASTConstantsSTEP.AT:
        print("@");
        break;
      case ASTConstantsSTEP.EXCLAMATIONMARK:
        print("!");
        break;
      case ASTConstantsSTEP.HASH:
        print("#");
        break;
    }
    if(node.isPresentDigits())
      print(node.getDigits());
    else
      print(node.getName());
  }

  @Override
  public void visit(ASTANCHOR_NAME node) {
    print("<");
  }

  @Override
  public void endVisit(ASTANCHOR_NAME node) {
    print(">");
  }

  @Override
  public void visit(ASTENUMERATION node) {
    print(".");
  }

  @Override
  public void endVisit(ASTENUMERATION node) {
    print(".");
  }

  // URI_FRAGMENT_IDENTIFIER = ("-"|Name)*;

  @Override
  public void visit(ASTUNIVERSAL_RESOURCE_IDENTIFIER node) {
    print("<");
  }

  @Override
  public void endVisit(ASTUNIVERSAL_RESOURCE_IDENTIFIER node) {
    print(">");
  }

  @Override
  public void visit(ASTHeader node){
    print("HEADER;");
  }

  @Override
  public void endVisit(ASTHeader node){
    print("ENDSEC;");
  }

  //Parameter

  @Override
  public void visit(ASTList node) {
    print("(");
  }

  @Override
  public void endVisit(ASTList node) {
    print(")");
  }

  @Override
  public void visit(ASTData node) {
    print("DATA;");
  }

  @Override
  public void endVisit(ASTData node) {
    print("ENDSEC;");
  }

  // Instance = Id "=" (NamedList|List) ";";

  // NamedList = name:Id List;

  @Override
  public void visit(ASTSignature node) {
    print("SIGNATURE");
  }

  @Override
  public void endVisit(ASTSignature node) {
    print("ENDSEC;");
  }

  // token OtherString = '\'' (SingleCharacter)* '\'' : {setText(getText().substring(1, getText().length() - 1));}; }

}
