import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.core.behaviours.CyclicBehaviour;
import jade.content.onto.OntologyException;
import jade.lang.acl.MessageTemplate;
import jade.content.lang.sl.SLCodec;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.Ontology;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.content.lang.Codec;
import jade.domain.DFService;
import jade.core.Agent;
import jade.core.AID;
import java.util.*;
import nuoma.*;

public class Realtor extends Agent{
    List<Listing> listing_list = new ArrayList<>();
    @Override
    public void setup() {
        Object[] args = getArguments();
        if (args == null) {
            System.out.println("No arguments provided.");
            return;
        }

        addBehaviour(new ReceiverBehaviour());
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        if (args.length == 2)  {
            List<Listing> listings = (List<Listing>) args[1];
            if (listings.size() != 0) {
                int rentable = 0;
                int buyable = 0;
                for (Listing listing : listings) {
                    if (listing.getRentable()) rentable++;
                    else buyable++;
                    listing.getEstate().setComments(getLocalName());
                    listing_list.add(listing);
                }
                ServiceDescription sd;
                sd = new ServiceDescription();

                if (rentable == 0) sd.setType("Buy");
                else if(buyable == 0) sd.setType("Rent");
                else sd.setType("Both");
                sd.setName(sd.getType());

                System.out.println("I am " + getLocalName() + " and I have " + sd.getType());
                dfd.addServices(sd);

                try {
                    DFService.register(this, dfd);
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }
            }
        }
    }

    @Override
    public void takeDown()
    {
        System.out.println("A["+getLocalName()+"] is being removed");
        System.out.println("A["+getLocalName()+"] de-registering all services");
        try { DFService.deregister(this); }
        catch (Exception e) {e.printStackTrace();}
    }

    private class ReceiverBehaviour extends CyclicBehaviour {
        @Override
        public void action() {

            Ontology onto = NuomaOntology.getInstance();
            Codec codec = new SLCodec();
            ContentManager cm = getContentManager();
            cm.registerLanguage(codec);
            cm.registerOntology(onto);

            MessageTemplate request = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            MessageTemplate propose = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
            ACLMessage requests = myAgent.receive(request);
            ACLMessage proposals = myAgent.receive(propose);

            if (requests != null) {
                AID sender = requests.getSender();
                System.out.println("A["+getLocalName()+"] message received.");
                try {
                    ContentElement c = cm.extractContent(requests);
                    if (c instanceof ListingRequestMsg)
                    {
                        String looking_for = ((ListingRequestMsg) c).getLooking_for();
                        send_listings(sender, looking_for);
                    }
                } catch (Codec.CodecException | OntologyException e) {
                    throw new RuntimeException(e);
                }
            }else if (proposals != null){
                AID sender = proposals.getSender();
                System.out.println("A["+getLocalName()+"] message received.");
                try {
                    ContentElement c = cm.extractContent(proposals);
                    if (c instanceof ObjectBuyRentMsg)
                    {
                        int LID = ((ObjectBuyRentMsg) c).getObjectLID();
                        int rentStart = ((ObjectBuyRentMsg) c).getRentStart();
                        int rentEnd = ((ObjectBuyRentMsg) c).getRentEnd();

                        boolean can_sell = canSell(LID, rentStart, rentEnd);
                        AcceptDenyProposalMsg message = new AcceptDenyProposalMsg();
                        message.setAccepted(can_sell);
                        message.setProposalLID(LID);
                        ACLMessage req = new ACLMessage(can_sell ? ACLMessage.ACCEPT_PROPOSAL : ACLMessage.REJECT_PROPOSAL);
                        req.setLanguage(codec.getName());
                        req.setOntology(onto.getName());
                        req.clearAllReceiver();
                        req.addReceiver(sender);
                        cm.fillContent(req, message);
                        req.addUserDefinedParameter("klase", "AcceptDenyProposal");
                        send(req);
                    }
                } catch (Codec.CodecException | OntologyException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private boolean canSell(int LID, int start, int end){
        for(Listing listing: listing_list){
            if (listing.getLID() == LID && listing.getAgent() != null){//listing.getPrice() > 0){
                if (!listing.getRentable()){
                    List<Listing> new_listing_list = new ArrayList<>();
                    for (Listing new_listing: listing_list){
                        if (new_listing != listing ){
                            new_listing_list.add(new_listing);
                        }else{
                            Seller sold = new Seller();
                            sold.setName("Sold");
                            new_listing.setAgent(sold);
                            new_listing_list.add(new_listing);
                        }
                    }
                    listing_list = new_listing_list;
                    return true;
                }
                List<Listing> new_listing_list = new ArrayList<>();
                for (Listing new_listing: listing_list){
                    if (new_listing != listing ){
                        new_listing_list.add(new_listing);
                    }else{
                        jade.util.leap.List available = listing.getAvailable();
                        jade.util.leap.List a = new jade.util.leap.ArrayList();
                        for(int i = 0; i < listing.getAvailable().size(); i++){
                            if (i >= start-1 && i <= end+1)
                                a.add(1);
                            else
                                a.add(available.get(i));
                        }
                        new_listing.setAvailable(a);
                        new_listing_list.add(new_listing);
                    }
                }
                listing_list = new_listing_list;
                return true;
            }
        }
        return false;
    }

    private void send_listings(AID receiver, String content){
        List<String> stringlines = new ArrayList<>();
        if (content.equals("Both")){
            for(Listing element1: listing_list){
                if (!Objects.equals(element1.getAgent().getName(), "Sold")){
                    stringlines.add("works");}}}
        else{
            for (Listing element: listing_list){
                if ((element.getRentable() == content.equals("Rent")) && !Objects.equals(element.getAgent().getName(), "Sold")){
                    stringlines.add("works");}}
        }
        if (stringlines.size() > 0){
            try {
                Ontology onto = NuomaOntology.getInstance();
                Codec codec = new SLCodec();

                ACLMessage req = new ACLMessage(ACLMessage.AGREE);

                req.setLanguage(codec.getName());
                req.setOntology(onto.getName());
                ContentManager cm = getContentManager();
                cm.registerLanguage(codec);
                cm.registerOntology(onto);

                AID a = new AID(receiver.getName().split("@")[0], AID.ISLOCALNAME);
                req.clearAllReceiver();
                req.addReceiver(a);

                ListingSendMsg message = new ListingSendMsg();
                if (content.equals("Both")){
                    for(Listing element1: listing_list){
                        if (!Objects.equals(element1.getAgent().getName(), "Sold")){
                            message.addListings(element1);}}}
                else{
                    for (Listing element: listing_list){
                        if ((element.getRentable() == content.equals("Rent")) && !Objects.equals(element.getAgent().getName(), "Sold")){
                            message.addListings(element);}}
                }

                cm.fillContent(req, message);
                req.addUserDefinedParameter("klase", "ListingSend");
                send(req);
            }
            catch (Exception ex) {
                System.out.println("A[" + getLocalName() + "] Error while building message: " + ex.getMessage());
            }
        }
    }
}
