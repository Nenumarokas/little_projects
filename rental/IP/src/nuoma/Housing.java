package nuoma;


import nuoma.*;

/**
* Protege name: Housing
* @author ontology bean generator
* @version 2023/05/19, 12:44:29
*/
public class Housing implements HousingIf {

  private static final long serialVersionUID = -1093233199603083030L;

  private String _internalInstanceName = null;

  public Housing() {
    this._internalInstanceName = "";
  }

  public Housing(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: Pool
   */
   private boolean pool;
   public void setPool(boolean value) { 
    this.pool=value;
   }
   public boolean getPool() {
     return this.pool;
   }

   /**
   * Protege name: Basement
   */
   private boolean basement;
   public void setBasement(boolean value) { 
    this.basement=value;
   }
   public boolean getBasement() {
     return this.basement;
   }

   /**
   * Protege name: Dist_to_city
   */
   private int dist_to_city;
   public void setDist_to_city(int value) { 
    this.dist_to_city=value;
   }
   public int getDist_to_city() {
     return this.dist_to_city;
   }

   /**
   * Protege name: Location
   */
   private String location;
   public void setLocation(String value) { 
    this.location=value;
   }
   public String getLocation() {
     return this.location;
   }

   /**
   * Protege name: Rooms
   */
   private int rooms;
   public void setRooms(int value) { 
    this.rooms=value;
   }
   public int getRooms() {
     return this.rooms;
   }

   /**
   * Protege name: Garage
   */
   private boolean garage;
   public void setGarage(boolean value) { 
    this.garage=value;
   }
   public boolean getGarage() {
     return this.garage;
   }

   /**
   * Protege name: Area
   */
   private int area;
   public void setArea(int value) { 
    this.area=value;
   }
   public int getArea() {
     return this.area;
   }

   /**
   * Protege name: Age
   */
   private int age;
   public void setAge(int value) { 
    this.age=value;
   }
   public int getAge() {
     return this.age;
   }

   /**
   * Protege name: HID
   */
   private int hiD;
   public void setHID(int value) { 
    this.hiD=value;
   }
   public int getHID() {
     return this.hiD;
   }

   /**
   * Protege name: Comments
   */
   private String comments;
   public void setComments(String value) { 
    this.comments=value;
   }
   public String getComments() {
     return this.comments;
   }

}
