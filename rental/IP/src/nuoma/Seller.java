package nuoma;


import nuoma.*;

/**
* Protege name: Seller
* @author ontology bean generator
* @version 2023/05/19, 12:44:29
*/
public class Seller implements SellerIf {

  private static final long serialVersionUID = -1093233199603083030L;

  private String _internalInstanceName = null;

  public Seller() {
    this._internalInstanceName = "";
  }

  public Seller(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: Phone
   */
   private String phone;
   public void setPhone(String value) { 
    this.phone=value;
   }
   public String getPhone() {
     return this.phone;
   }

   /**
   * Protege name: Name
   */
   private String name;
   public void setName(String value) { 
    this.name=value;
   }
   public String getName() {
     return this.name;
   }

   /**
   * Protege name: Surname
   */
   private String surname;
   public void setSurname(String value) { 
    this.surname=value;
   }
   public String getSurname() {
     return this.surname;
   }

   /**
   * Protege name: ID
   */
   private int iD;
   public void setID(int value) { 
    this.iD=value;
   }
   public int getID() {
     return this.iD;
   }

   /**
   * Protege name: Email
   */
   private String email;
   public void setEmail(String value) { 
    this.email=value;
   }
   public String getEmail() {
     return this.email;
   }

   /**
   * Protege name: Address
   */
   private String address;
   public void setAddress(String value) { 
    this.address=value;
   }
   public String getAddress() {
     return this.address;
   }

}
