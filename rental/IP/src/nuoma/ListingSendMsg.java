package nuoma;


import jade.util.leap.*;
import nuoma.*;

/**
* Protege name: ListingSendMsg
* @author ontology bean generator
* @version 2023/05/23, 15:32:51
*/
public class ListingSendMsg implements ListingSendMsgIf {

  private static final long serialVersionUID = 1949104663289299611L;

  private String _internalInstanceName = null;

  public ListingSendMsg() {
    this._internalInstanceName = "";
  }

  public ListingSendMsg(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: Listings
   */
   private List listings = new ArrayList();
   public void addListings(Listing elem) { 
     listings.add(elem);
   }
   public boolean removeListings(Listing elem) {
     boolean result = listings.remove(elem);
     return result;
   }
   public void clearAllListings() {
     listings.clear();
   }
   public Iterator getAllListings() {return listings.iterator(); }
   public List getListings() {return listings; }
   public void setListings(List l) {listings = l; }

}
