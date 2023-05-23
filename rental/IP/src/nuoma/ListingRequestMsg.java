package nuoma;


import nuoma.*;

/**
* Protege name: ListingRequestMsg
* @author ontology bean generator
* @version 2023/05/23, 15:32:51
*/
public class ListingRequestMsg implements ListingRequestMsgIf {

  private static final long serialVersionUID = 1949104663289299611L;

  private String _internalInstanceName = null;

  public ListingRequestMsg() {
    this._internalInstanceName = "";
  }

  public ListingRequestMsg(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: looking_for
   */
   private String looking_for;
   public void setLooking_for(String value) { 
    this.looking_for=value;
   }
   public String getLooking_for() {
     return this.looking_for;
   }

}
