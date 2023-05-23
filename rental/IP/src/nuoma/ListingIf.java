package nuoma;


import jade.util.leap.*;

/**
* Protege name: Listing
* @author ontology bean generator
* @version 2023/05/19, 12:44:29
*/
public interface ListingIf extends jade.content.Concept {

   /**
   * Protege name: LID
   */
   public void setLID(int value);
   public int getLID();

   /**
   * Protege name: Available
   */
   public void addAvailable(int elem);
   public boolean removeAvailable(int elem);
   public void clearAllAvailable();
   public Iterator getAllAvailable();
   public List getAvailable();
   public void setAvailable(List l);

   /**
   * Protege name: Rentable
   */
   public void setRentable(boolean value);
   public boolean getRentable();

   /**
   * Protege name: Agent
   */
   public void setAgent(Seller value);
   public Seller getAgent();

   /**
   * Protege name: Estate
   */
   public void setEstate(Housing value);
   public Housing getEstate();

   /**
   * Protege name: Price
   */
   public void setPrice(float value);
   public float getPrice();

}
