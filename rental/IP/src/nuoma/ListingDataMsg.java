package nuoma;


import jade.util.leap.*;
import nuoma.*;

/**
* Protege name: ListingDataMsg
* @author ontology bean generator
* @version 2023/05/19, 12:44:29
*/
public class ListingDataMsg implements ListingDataMsgIf {

  private static final long serialVersionUID = -1093233199603083030L;
  private int counter = 0;

  private String _internalInstanceName = null;

  public ListingDataMsg() {
    this._internalInstanceName = "";
  }

  public ListingDataMsg(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: Listingline
   */
   private List listingline = new ArrayList();
   public void addListingline(Listing elem) { 
     listingline.add(elem);
     counter++;
   }
   public boolean removeListingline(Listing elem) {
     boolean result = listingline.remove(elem);
     counter--;
     return result;
   }
   public void clearAllListingline() {
     listingline.clear();
     counter = 0;
   }
   public Iterator getAllListingline() {return listingline.iterator(); }
   public List getListingline() {return listingline; }
    public int size(){
       return counter;
    }
   public void setListingline(List l) {listingline = l; }

}
