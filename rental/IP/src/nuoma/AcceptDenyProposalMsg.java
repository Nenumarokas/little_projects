package nuoma;


import nuoma.*;

/**
* Protege name: AcceptDenyProposalMsg
* @author ontology bean generator
* @version 2023/05/23, 16:01:27
*/
public class AcceptDenyProposalMsg implements AcceptDenyProposalMsgIf {

  private static final long serialVersionUID = -5006673757578515637L;

  private String _internalInstanceName = null;

  public AcceptDenyProposalMsg() {
    this._internalInstanceName = "";
  }

  public AcceptDenyProposalMsg(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: Accepted
   */
   private boolean accepted;
   public void setAccepted(boolean value) { 
    this.accepted=value;
   }
   public boolean getAccepted() {
     return this.accepted;
   }

   /**
   * Protege name: ProposalLID
   */
   private int proposalLID;
   public void setProposalLID(int value) { 
    this.proposalLID=value;
   }
   public int getProposalLID() {
     return this.proposalLID;
   }

}
