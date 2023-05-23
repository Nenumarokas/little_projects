package nuoma;



/**
* Protege name: Housing
* @author ontology bean generator
* @version 2023/05/19, 12:44:29
*/
public interface HousingIf extends jade.content.Concept {

   /**
   * Protege name: Pool
   */
   public void setPool(boolean value);
   public boolean getPool();

   /**
   * Protege name: Basement
   */
   public void setBasement(boolean value);
   public boolean getBasement();

   /**
   * Protege name: Dist_to_city
   */
   public void setDist_to_city(int value);
   public int getDist_to_city();

   /**
   * Protege name: Location
   */
   public void setLocation(String value);
   public String getLocation();

   /**
   * Protege name: Rooms
   */
   public void setRooms(int value);
   public int getRooms();

   /**
   * Protege name: Garage
   */
   public void setGarage(boolean value);
   public boolean getGarage();

   /**
   * Protege name: Area
   */
   public void setArea(int value);
   public int getArea();

   /**
   * Protege name: Age
   */
   public void setAge(int value);
   public int getAge();

   /**
   * Protege name: HID
   */
   public void setHID(int value);
   public int getHID();

   /**
   * Protege name: Comments
   */
   public void setComments(String value);
   public String getComments();

}
