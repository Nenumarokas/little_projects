package nuoma;


import jade.util.leap.*;

/**
* Protege name: ListingSendMsg
* @author ontology bean generator
* @version 2023/05/23, 15:32:51
*/
public interface ListingSendMsgIf extends jade.content.Predicate {

   /**
   * Protege name: Listings
   */
   public void addListings(Listing elem);
   public boolean removeListings(Listing elem);
   public void clearAllListings();
   public Iterator getAllListings();
   public List getListings();
   public void setListings(List l);

}
