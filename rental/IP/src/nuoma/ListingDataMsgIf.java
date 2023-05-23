package nuoma;


import jade.util.leap.*;

/**
* Protege name: ListingDataMsg
* @author ontology bean generator
* @version 2023/05/19, 12:44:29
*/
public interface ListingDataMsgIf extends jade.content.Predicate {

   /**
   * Protege name: Listingline
   */
   public void addListingline(Listing elem);
   public boolean removeListingline(Listing elem);
   public void clearAllListingline();
   public Iterator getAllListingline();
   public List getListingline();
   public void setListingline(List l);

}
