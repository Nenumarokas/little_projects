// file: NuomaOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package nuoma;

import jade.content.onto.*;
import jade.content.schema.*;

/** file: NuomaOntology.java
 * @author ontology bean generator
 * @version 2023/05/23, 16:01:27
 */
public class NuomaOntology extends jade.content.onto.Ontology  {

  private static final long serialVersionUID = -5006673757578515637L;

  //NAME
  public static final String ONTOLOGY_NAME = "nuoma";
  // The singleton instance of this ontology
  private static Ontology theInstance = new NuomaOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String ACCEPTDENYPROPOSALMSG_PROPOSALLID="ProposalLID";
    public static final String ACCEPTDENYPROPOSALMSG_ACCEPTED="Accepted";
    public static final String ACCEPTDENYPROPOSALMSG="AcceptDenyProposalMsg";
    public static final String OBJECTBUYRENTMSG_RENTSTART="RentStart";
    public static final String OBJECTBUYRENTMSG_OBJECTLID="ObjectLID";
    public static final String OBJECTBUYRENTMSG_RENTEND="RentEnd";
    public static final String OBJECTBUYRENTMSG="ObjectBuyRentMsg";
    public static final String LISTINGSENDMSG_LISTINGS="Listings";
    public static final String LISTINGSENDMSG="ListingSendMsg";
    public static final String LISTINGREQUESTMSG_LOOKING_FOR="looking_for";
    public static final String LISTINGREQUESTMSG="ListingRequestMsg";
    public static final String LISTING_PRICE="Price";
    public static final String LISTING_ESTATE="Estate";
    public static final String LISTING_AGENT="Agent";
    public static final String LISTING_RENTABLE="Rentable";
    public static final String LISTING_AVAILABLE="Available";
    public static final String LISTING_LID="LID";
    public static final String LISTING="Listing";
    public static final String SELLER_ADDRESS="Address";
    public static final String SELLER_EMAIL="Email";
    public static final String SELLER_ID="ID";
    public static final String SELLER_SURNAME="Surname";
    public static final String SELLER_NAME="Name";
    public static final String SELLER_PHONE="Phone";
    public static final String SELLER="Seller";
    public static final String RENTPROPOSAL_REFID="RefID";
    public static final String RENTPROPOSAL_PROPTIMES="PropTimes";
    public static final String RENTPROPOSAL="RentProposal";
    public static final String HOUSING_COMMENTS="Comments";
    public static final String HOUSING_HID="HID";
    public static final String HOUSING_AGE="Age";
    public static final String HOUSING_AREA="Area";
    public static final String HOUSING_GARAGE="Garage";
    public static final String HOUSING_ROOMS="Rooms";
    public static final String HOUSING_LOCATION="Location";
    public static final String HOUSING_DIST_TO_CITY="Dist_to_city";
    public static final String HOUSING_BASEMENT="Basement";
    public static final String HOUSING_POOL="Pool";
    public static final String HOUSING="Housing";

  /**
   * Constructor
  */
  private NuomaOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema housingSchema = new ConceptSchema(HOUSING);
    add(housingSchema, nuoma.Housing.class);
    ConceptSchema rentProposalSchema = new ConceptSchema(RENTPROPOSAL);
    add(rentProposalSchema, nuoma.RentProposal.class);
    ConceptSchema sellerSchema = new ConceptSchema(SELLER);
    add(sellerSchema, nuoma.Seller.class);
    ConceptSchema listingSchema = new ConceptSchema(LISTING);
    add(listingSchema, nuoma.Listing.class);

    // adding AgentAction(s)

    // adding AID(s)

    // adding Predicate(s)
    PredicateSchema listingRequestMsgSchema = new PredicateSchema(LISTINGREQUESTMSG);
    add(listingRequestMsgSchema, nuoma.ListingRequestMsg.class);
    PredicateSchema listingSendMsgSchema = new PredicateSchema(LISTINGSENDMSG);
    add(listingSendMsgSchema, nuoma.ListingSendMsg.class);
    PredicateSchema objectBuyRentMsgSchema = new PredicateSchema(OBJECTBUYRENTMSG);
    add(objectBuyRentMsgSchema, nuoma.ObjectBuyRentMsg.class);
    PredicateSchema acceptDenyProposalMsgSchema = new PredicateSchema(ACCEPTDENYPROPOSALMSG);
    add(acceptDenyProposalMsgSchema, nuoma.AcceptDenyProposalMsg.class);


    // adding fields
    housingSchema.add(HOUSING_POOL, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.MANDATORY);
    housingSchema.add(HOUSING_BASEMENT, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.MANDATORY);
    housingSchema.add(HOUSING_DIST_TO_CITY, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    housingSchema.add(HOUSING_LOCATION, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    housingSchema.add(HOUSING_ROOMS, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    housingSchema.add(HOUSING_GARAGE, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.MANDATORY);
    housingSchema.add(HOUSING_AREA, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    housingSchema.add(HOUSING_AGE, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    housingSchema.add(HOUSING_HID, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    housingSchema.add(HOUSING_COMMENTS, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    rentProposalSchema.add(RENTPROPOSAL_PROPTIMES, (TermSchema)getSchema(BasicOntology.INTEGER), 1, ObjectSchema.UNLIMITED);
    rentProposalSchema.add(RENTPROPOSAL_REFID, housingSchema, ObjectSchema.MANDATORY);
    sellerSchema.add(SELLER_PHONE, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    sellerSchema.add(SELLER_NAME, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    sellerSchema.add(SELLER_SURNAME, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    sellerSchema.add(SELLER_ID, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    sellerSchema.add(SELLER_EMAIL, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    sellerSchema.add(SELLER_ADDRESS, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    listingSchema.add(LISTING_LID, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    listingSchema.add(LISTING_AVAILABLE, (TermSchema)getSchema(BasicOntology.INTEGER), 1, ObjectSchema.UNLIMITED);
    listingSchema.add(LISTING_RENTABLE, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.MANDATORY);
    listingSchema.add(LISTING_AGENT, sellerSchema, ObjectSchema.MANDATORY);
    listingSchema.add(LISTING_ESTATE, housingSchema, ObjectSchema.MANDATORY);
    listingSchema.add(LISTING_PRICE, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    listingRequestMsgSchema.add(LISTINGREQUESTMSG_LOOKING_FOR, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    listingSendMsgSchema.add(LISTINGSENDMSG_LISTINGS, listingSchema, 1, ObjectSchema.UNLIMITED);
    objectBuyRentMsgSchema.add(OBJECTBUYRENTMSG_RENTEND, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    objectBuyRentMsgSchema.add(OBJECTBUYRENTMSG_OBJECTLID, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    objectBuyRentMsgSchema.add(OBJECTBUYRENTMSG_RENTSTART, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    acceptDenyProposalMsgSchema.add(ACCEPTDENYPROPOSALMSG_ACCEPTED, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.MANDATORY);
    acceptDenyProposalMsgSchema.add(ACCEPTDENYPROPOSALMSG_PROPOSALLID, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);

    // adding name mappings

    // adding inheritance

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
}
