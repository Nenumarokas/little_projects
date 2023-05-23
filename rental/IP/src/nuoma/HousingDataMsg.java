package nuoma;


import nuoma.*;

/**
* Protege name: HousingDataMsg
* @author ontology bean generator
* @version 2023/05/19, 12:44:29
*/
public class HousingDataMsg implements HousingDataMsgIf {

  private static final long serialVersionUID = -1093233199603083030L;

  private String _internalInstanceName = null;

  public HousingDataMsg() {
    this._internalInstanceName = "";
  }

  public HousingDataMsg(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: HousingLine
   */
   private Housing housingLine;
   public void setHousingLine(Housing value) { 
    this.housingLine=value;
   }
   public Housing getHousingLine() {
     return this.housingLine;
   }

}
