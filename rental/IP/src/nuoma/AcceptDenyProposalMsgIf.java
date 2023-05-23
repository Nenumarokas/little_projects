package nuoma;



/**
* Protege name: AcceptDenyProposalMsg
* @author ontology bean generator
* @version 2023/05/23, 16:01:27
*/
public interface AcceptDenyProposalMsgIf extends jade.content.Predicate {

   /**
   * Protege name: Accepted
   */
   public void setAccepted(boolean value);
   public boolean getAccepted();

   /**
   * Protege name: ProposalLID
   */
   public void setProposalLID(int value);
   public int getProposalLID();

}
