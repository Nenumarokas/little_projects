package nuoma;


import nuoma.*;

/**
* Protege name: RentProposalMsg
* @author ontology bean generator
* @version 2023/05/19, 12:44:29
*/
public class RentProposalMsg implements RentProposalMsgIf {

  private static final long serialVersionUID = -1093233199603083030L;

  private String _internalInstanceName = null;

  public RentProposalMsg() {
    this._internalInstanceName = "";
  }

  public RentProposalMsg(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: RentLine
   */
   private RentProposal rentLine;
   public void setRentLine(RentProposal value) { 
    this.rentLine=value;
   }
   public RentProposal getRentLine() {
     return this.rentLine;
   }

}
