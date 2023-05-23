package nuoma;


import jade.util.leap.*;
import nuoma.*;

/**
* Protege name: RentProposal
* @author ontology bean generator
* @version 2023/05/19, 12:44:29
*/
public class RentProposal implements RentProposalIf {

  private static final long serialVersionUID = -1093233199603083030L;

  private String _internalInstanceName = null;

  public RentProposal() {
    this._internalInstanceName = "";
  }

  public RentProposal(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: PropTimes
   */
   private List propTimes = new ArrayList();
   public void addPropTimes(int elem) { 
     propTimes.add(elem);
   }
   public boolean removePropTimes(int elem) {
     propTimes.remove(elem);
     return true;
   }
   public void clearAllPropTimes() {
     propTimes.clear();
   }
   public Iterator getAllPropTimes() {return propTimes.iterator(); }
   public List getPropTimes() {return propTimes; }
   public void setPropTimes(List l) {propTimes = l; }

   /**
   * Protege name: RefID
   */
   private Housing refID;
   public void setRefID(Housing value) { 
    this.refID=value;
   }
   public Housing getRefID() {
     return this.refID;
   }

}
