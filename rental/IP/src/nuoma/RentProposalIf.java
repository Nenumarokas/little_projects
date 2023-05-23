package nuoma;


import jade.util.leap.*;

/**
* Protege name: RentProposal
* @author ontology bean generator
* @version 2023/05/19, 12:44:29
*/
public interface RentProposalIf extends jade.content.Concept {

   /**
   * Protege name: PropTimes
   */
   public void addPropTimes(int elem);
   public boolean removePropTimes(int elem);
   public void clearAllPropTimes();
   public Iterator getAllPropTimes();
   public List getPropTimes();
   public void setPropTimes(List l);

   /**
   * Protege name: RefID
   */
   public void setRefID(Housing value);
   public Housing getRefID();

}
