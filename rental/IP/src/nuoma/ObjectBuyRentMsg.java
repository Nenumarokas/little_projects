package nuoma;


import nuoma.*;

/**
* Protege name: ObjectBuyRentMsg
* @author ontology bean generator
* @version 2023/05/23, 16:01:27
*/
public class ObjectBuyRentMsg implements ObjectBuyRentMsgIf {

  private static final long serialVersionUID = -5006673757578515637L;

  private String _internalInstanceName = null;

  public ObjectBuyRentMsg() {
    this._internalInstanceName = "";
  }

  public ObjectBuyRentMsg(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: RentEnd
   */
   private int rentEnd;
   public void setRentEnd(int value) { 
    this.rentEnd=value;
   }
   public int getRentEnd() {
     return this.rentEnd;
   }

   /**
   * Protege name: ObjectLID
   */
   private int objectLID;
   public void setObjectLID(int value) { 
    this.objectLID=value;
   }
   public int getObjectLID() {
     return this.objectLID;
   }

   /**
   * Protege name: RentStart
   */
   private int rentStart;
   public void setRentStart(int value) { 
    this.rentStart=value;
   }
   public int getRentStart() {
     return this.rentStart;
   }

}
