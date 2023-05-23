package nuoma;


import jade.util.leap.*;
import nuoma.*;

/**
* Protege name: Listing
* @author ontology bean generator
* @version 2023/05/19, 12:44:29
*/
public class Listing implements ListingIf {

  private static final long serialVersionUID = -1093233199603083030L;

  private String _internalInstanceName = null;

  public Listing() {
    this._internalInstanceName = "";
  }

  public Listing(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: LID
   */
   private int liD;
   public void setLID(int value) { 
    this.liD=value;
   }
   public int getLID() {
     return this.liD;
   }

   /**
   * Protege name: Available
   */
   private List available = new ArrayList();
   public void addAvailable(int elem) { 
     available.add(elem);
   }
   public boolean removeAvailable(int elem) {
     available.remove(elem);
     return true;
   }
   public void clearAllAvailable() {
     available = new ArrayList();
   }
   public Iterator getAllAvailable() {return available.iterator(); }
   public List getAvailable() {return available; }

   public void setAvailable(List l) {available = l; }

   /**
   * Protege name: Rentable
   */
   private boolean rentable;
   public void setRentable(boolean value) { 
    this.rentable=value;
   }
   public boolean getRentable() {
     return this.rentable;
   }

   /**
   * Protege name: Agent
   */
   private Seller agent;
   public void setAgent(Seller value) { 
    this.agent=value;
   }
   public Seller getAgent() {
     return this.agent;
   }

   /**
   * Protege name: Estate
   */
   private Housing estate;
   public void setEstate(Housing value) { 
    this.estate=value;
   }
   public Housing getEstate() {
     return this.estate;
   }

   /**
   * Protege name: Price
   */
   private float price;
   public void setPrice(float value) { 
    this.price=value;
   }
   public float getPrice() {
     return this.price;
   }

}
