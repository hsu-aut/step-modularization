package visitor;

import step.topology.Product;
import step._ast.*;
import step._visitor.STEPVisitor;

import java.util.LinkedList;
import java.util.List;

public class ProductVisitor implements STEPVisitor {
  private static final String PRODUCT = "PRODUCT_DEFINITION";
  private List<Product> products;

  public ProductVisitor() {
    this.products = new LinkedList<Product>();
  }

//  public void findToolingNames(ASTStepExchangeFile ast){
//    products = new LinkedList<ASTInstance>();
//    ast.accept(this);
//  }
  @Override
  public void visit(ASTInstance node){
    //System.out.println("visit called: " + node.getId().getDigits());
    if(!node.isPresentNamedList())
      return;
    ASTNamedList namedList = node.getNamedList();
    ASTId name = namedList.getName();
    if(name.getPre()!= ASTConstantsSTEP.HASH && name.getPre()!= ASTConstantsSTEP.AT) {
      if(!name.isPresentName())
        return;
      String listKeyword = name.getName();
      if (listKeyword.equals(PRODUCT)){
        Product product = new Product();
        product.setId(node.getId().getDigits());
        product.setName(namedList.getList().getParameter(1).getOtherString());
        product.setDescription(namedList.getList().getParameter(0).getOtherString());
        products.add(product);
     }
    }
  }



  public List<Product> getProducts(){
    return this.products;
  }
}
