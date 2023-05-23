import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.core.behaviours.CyclicBehaviour;
import jade.content.onto.OntologyException;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.content.lang.sl.SLCodec;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.Ontology;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.content.lang.Codec;
import jade.domain.DFService;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.core.Agent;
import java.util.List;
import jade.core.AID;
import java.util.*;
import nuoma.*;

public class Buyer extends GuiAgent {
    public static final int SEARCH = 1;
    public static final int CONFIRM = 2;
    private BuyerGUI GUI = null;
    public final Buyer this_one = this;
    public GuiEvent answers = null;
    private List<Listing> listings = new ArrayList<>();
    @Override
    public void setup(){
        System.out.println("hello, I am " + getLocalName());
        GUI = new BuyerGUI(this, null, null);
        GUI.setContentPane(GUI.Panel1);
        GUI.pack();
        GUI.setVisible(true);
        addBehaviour(new WaitForMessages());
    }

    @Override
    protected void onGuiEvent(GuiEvent ge) {
        String looking_for = (String) ge.getParameter(0);

        if (ge.getType() == Buyer.SEARCH || ge.getType() == Buyer.CONFIRM)
        {
            answers = ge;
            Map<String, List<Integer>> dict = new HashMap<>();

            int minprice = Integer.parseInt((String) ge.getParameter(1));
            dict.put("minprice", Collections.singletonList(minprice));

            int maxprice = Integer.parseInt((String) ge.getParameter(2));
            dict.put("maxprice", Collections.singletonList(maxprice));

            List<Integer> location = oneHotLocation((String) ge.getParameter(3));
            if ((boolean) ge.getParameter(4)) location.replaceAll(integer -> integer + 1000);
            dict.put("location", location);

            int area = Integer.parseInt((String) ge.getParameter(5));
            dict.put("area", Collections.singletonList(area));

            int age = Integer.parseInt((String) ge.getParameter(6));
            dict.put("age", Collections.singletonList(age));

            List<Integer> room = oneHotRoom((String) ge.getParameter(7));
            if ((boolean)ge.getParameter(8)) room.replaceAll(integer -> integer + 1000);
            dict.put("room", room);

            int garage = (boolean)ge.getParameter(9)? 1 : 0;
            dict.put("garage", Collections.singletonList((boolean) ge.getParameter(10) ? garage + 1000 : garage));

            int pool = (boolean)ge.getParameter(11)? 1 : 0;
            dict.put("pool", Collections.singletonList((boolean) ge.getParameter(12) ? pool + 1000 : pool));

            int basement = (boolean)ge.getParameter(13)? 1 : 0;
            dict.put("basement", Collections.singletonList((boolean) ge.getParameter(14) ? basement + 1000 : basement));

            List<Integer> dist = oneHotDist((String) ge.getParameter(15));
            if ((boolean) ge.getParameter(16)) dist.replaceAll(integer -> integer + 1000);
            dict.put("dist", dist);

            int start = Integer.parseInt((String) ge.getParameter(17));
            int end = Integer.parseInt((String) ge.getParameter(18));

            String listingToBuy = (String)ge.getParameter(19);

            if (ge.getType() == Buyer.SEARCH){
                List<AID> providers = looking_for.equals("Both")? getAllProviders() : getProviders(looking_for);
                listings.clear();
                for (AID ans : providers) {
                    System.out.println("A[" + getLocalName() + "] sending message \""+ looking_for +"\"");
                    Ontology onto = NuomaOntology.getInstance();
                    Codec codec = new SLCodec();

                    ACLMessage req = new ACLMessage(ACLMessage.REQUEST);

                    req.setLanguage(codec.getName());
                    req.setOntology(onto.getName());
                    ContentManager cm = getContentManager();
                    cm.registerLanguage(codec);
                    cm.registerOntology(onto);

                    AID a = new AID(ans.getName().split("@")[0], AID.ISLOCALNAME);
                    req.clearAllReceiver();
                    req.addReceiver(a);

                    ListingRequestMsg message = new ListingRequestMsg();
                    message.setLooking_for(looking_for);
                    try {
                        cm.fillContent(req, message);
                        req.addUserDefinedParameter("klase", "ListingRequest");
                        send(req);
                    } catch (Codec.CodecException | OntologyException e) {
                        throw new RuntimeException(e);
                    }

                }

                addBehaviour(new wakeup(this, 1000, dict));
            }else{
                if (listingToBuy.equals("-")){
                    GuiEvent newGe = new GuiEvent(this, Buyer.SEARCH);
                    for(int i = 0; i < 20; i++){
                        newGe.addParameter(ge.getParameter(i));
                    }
                    onGuiEvent(newGe);
                    return;
                }
                int LID = Integer.parseInt(listingToBuy.split(":")[0]);
                Listing selected = null;
                for(Listing listing: listings){
                    if (listing.getLID() == LID){
                        selected = listing;
                        break;
                    }
                }

                System.out.println("A[" + getLocalName() + "] buying object with LID " + LID);
                Ontology onto = NuomaOntology.getInstance();
                Codec codec = new SLCodec();

                ACLMessage req = new ACLMessage(ACLMessage.PROPOSE);
                req.setLanguage(codec.getName());
                req.setOntology(onto.getName());
                ContentManager cm = getContentManager();
                cm.registerLanguage(codec);
                cm.registerOntology(onto);

                assert selected != null;
                AID a = new AID(selected.getEstate().getComments().split("@")[0], AID.ISLOCALNAME);
                req.clearAllReceiver();
                req.addReceiver(a);

                ObjectBuyRentMsg message = new ObjectBuyRentMsg();
                message.setObjectLID(LID);
                message.setRentStart(start);
                message.setRentEnd(end);
                try {
                    cm.fillContent(req, message);
                    req.addUserDefinedParameter("klase", "ObjectBuyRent");
                    send(req);
                } catch (Codec.CodecException | OntologyException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class wakeup extends WakerBehaviour{
        Map<String, List<Integer>> data;
        public wakeup(Agent a, long timeout, Map<String, List<Integer>> data) {
            super(a, timeout);
            this.data = data;
        }

        @Override
        protected void onWake() {
            List<Listing> possible = new ArrayList<>();

            for (Listing listing: listings){
                if (listing.getPrice() < data.get("minprice").get(0)
                || listing.getPrice() > data.get("maxprice").get(0)
                || data.get("garage").get(0)-1000 == (listing.getEstate().getGarage()? 1 : 0)
                || data.get("pool").get(0)-1000 == (listing.getEstate().getPool()? 1 : 0)
                || data.get("basement").get(0)-1000 == (listing.getEstate().getBasement()? 1 : 0)
                || !(data.get("location").get(0) <= 999
                    || (listsEqual(data.get("location"), plusAThousand(oneHotLocation(listing.getEstate().getLocation())))
                    || listsEqual(data.get("location"), plusAThousand(oneHotLocation("")))))
                || !(data.get("room").get(0) <= 999
                    || listsEqual(data.get("room"), plusAThousand(oneHotRoom(String.valueOf(listing.getEstate().getRooms()))))
                    || listsEqual(data.get("room"), plusAThousand(oneHotRoom(""))))
                || !(data.get("dist").get(0) <= 999
                    || listsEqual(data.get("dist"), plusAThousand(oneHotDist(String.valueOf(listing.getEstate().getDist_to_city()))))
                    || listsEqual(data.get("dist"), plusAThousand(oneHotDist(""))))
                ) continue;
                possible.add(listing);
            }

            if (data.get("area").get(0) == 0 && listings.size() != 0){
                float average = 0;
                for(Listing listing: listings) average += listing.getEstate().getArea();
                data.put("area", Collections.singletonList((int) average / listings.size()));
            }
            if (data.get("age").get(0) == 0 && listings.size() != 0){
                float average = 0;
                for(Listing listing: listings) average += listing.getEstate().getAge();
                data.put("age", Collections.singletonList((int) average / listings.size()));
            }
            for(String key: new String[]{"location", "room", "dist"})
                if (data.get(key).get(0) > 999){
                    List<Integer> temp = data.get(key);
                    temp.replaceAll(number -> number-1000);
                    data.put(key, temp);
                }
            for(String key: new String[]{"garage", "pool", "basement"})
                if (data.get(key).get(0) > 999){
                    data.put(key, Collections.singletonList(data.get(key).get(0) - 1000));
                }

            List<Data> offers = getClosest(possible, currentToList(data), data.get("age").get(0), data.get("area").get(0));

            GUI.setVisible(false);
            GUI = new BuyerGUI(this_one, offers, answers);
            GUI.setContentPane(GUI.Panel1);
            GUI.pack();
            GUI.setVisible(true);
        }
    }

    private List<Data> getClosest(List<Listing> possible, List<Float> current, int maxAge, int maxArea){
        int age = 0, area = 0;
        for (Listing listing : possible) {
            int newAge = listing.getEstate().getAge();
            int newArea = listing.getEstate().getArea();
            if (newAge > age) age = newAge;
            if (newArea > area) area = newArea;
        }
        age = Math.max(maxAge, age);
        area = Math.max(maxArea, area);

        current.set(6, current.get(6)/area);
        current.set(7, current.get(7)/age);

        List<Data> data = new ArrayList<>();
        for(Listing listing: possible){
            List<Float> toList = listingToList(listing, age, area);
            data.add(new Data(vectorDiff(current, toList), listing, toList));
        }
        data.sort(Comparator.comparing(Data::getDistance));
        return data;
    }
    public class Data {
        private float distance;
        private Listing listing;
        private List<Float> onehot;
        public Data(float distance, Listing listing, List<Float> onehot) {
            this.distance = distance;
            this.listing = listing;
            this.onehot = onehot;
        }
        public float getDistance() {
            return distance;
        }
        public Listing getListing() {
            return listing;
        }
        public List<Float> getOnehot(){
            return onehot;
        }

        public String ToString() {
            String answer = listing.getPrice() + " ";
            if (listing.getRentable()) answer += "€/d. ";
                else answer += "€ ";
            answer += switch (listing.getEstate().getLocation()) {
                case "VLN" -> "Vilnius ";
                case "KNS" -> "Kaunas ";
                case "KLP" -> "Klaipėda ";
                case "ALT" -> "Alytus ";
                case "PNV" -> "Panevėžys ";
                case "other" -> "Užmiestis ";
                default -> "";
            };
            answer += listing.getEstate().getRooms() + "kamb. ";
            if (listing.getEstate().getGarage()) answer += "Garažas ";
            if (listing.getEstate().getPool()) answer += "Baseinas ";
            if (listing.getEstate().getBasement()) answer += "Rūsys ";
            if (listing.getEstate().getLocation().equals("other")) answer += "Atstumas iki miesto: " + switch (listing.getEstate().getDist_to_city()) {
                case 1 -> "0-10 km";
                case 2 -> "11-100 km";
                case 3 -> "101+ km";
                default -> "";
            };
            return answer + "\n";
        }
        public String getAvailable(int start, int end){
            StringBuilder answer = new StringBuilder();
            int counter = 0;
            for (jade.util.leap.Iterator it = listing.getAllAvailable(); it.hasNext(); ) {
                int j = (int) Long.parseLong(String.valueOf(it.next()));

                if (counter >= start && counter <= end){
                    if (j == 1) answer.append("●");
                    else answer.append("○");
                }else{
                    if (j == 1) answer.append("■");
                    else answer.append("□");
                }
                if ((counter+1)%5 == 0){
                    answer.append("|");
                }
                counter ++;
            }
            return answer.toString();
        }

        public boolean isRentable(int start, int end){
            boolean free = true;
            jade.util.leap.List available = listing.getAvailable();
            for(int i = 0; i < available.size(); i++){
                if (i >= start && i <= end && (long)available.get(i) == 1)
                    free = false;
            }
            return free;
        }

    }

    private List<Float> currentToList(Map<String, List<Integer>> data){
        List<Float> temp = new ArrayList<>();
        for(Integer a: data.get("location")) temp.add(Float.valueOf(a));
        List<Float> answer = new ArrayList<>(temp);

        answer.add(Float.valueOf(data.get("area").get(0)));

        answer.add(Float.valueOf(data.get("age").get(0)));

        temp.clear();
        for(Integer a: data.get("room")) temp.add(Float.valueOf(a));
        answer.addAll(temp);

        answer.add(Float.valueOf(data.get("garage").get(0)));

        answer.add(Float.valueOf(data.get("pool").get(0)));

        answer.add(Float.valueOf(data.get("basement").get(0)));

        temp.clear();
        for(Integer a: data.get("dist")) temp.add(Float.valueOf(a));
        answer.addAll(temp);

        return answer;
    }
    private List<Float> listingToList(Listing item, int age, int area){

        List<Integer> location = oneHotLocation(item.getEstate().getLocation());
        List<Float> loc = new ArrayList<>();
        for (Integer integer : location) loc.add(Float.valueOf(integer));
        List<Float> answer = new ArrayList<>(loc);

        answer.add((float) item.getEstate().getArea()/area);

        answer.add((float) item.getEstate().getAge()/age);

        List<Integer> rooms = oneHotRoom(String.valueOf(item.getEstate().getRooms()));
        loc.clear();
        for (Integer integer : rooms) loc.add(Float.valueOf(integer));
        answer.addAll(loc);

        answer.add((float) (item.getEstate().getGarage()? 1 : 0));

        answer.add((float) (item.getEstate().getPool()? 1 : 0));

        answer.add((float) (item.getEstate().getBasement()? 1 : 0));

        List<Integer> distance = oneHotDist(String.valueOf(item.getEstate().getDist_to_city()));
        loc.clear();
        for (Integer integer : distance) loc.add(Float.valueOf(integer));
        answer.addAll(loc);

        return answer;
    }

    private float vectorDiff(List<Float> first, List<Float> second){
        if (first.size() != second.size()){
            return -1;
        }
        float diffSum = 0;
        for(int i = 0; i < first.size(); i++){
            diffSum += Math.pow(first.get(i) - second.get(i), 2);
        }
        return (float) Math.sqrt(diffSum);
    }

    private List<Integer> plusAThousand(List<Integer> list){
        list.replaceAll(number -> number+1000);
        return list;
    }

    private boolean listsEqual(List<Integer> first, List<Integer> second){
        return Arrays.equals(new List[]{first}, new List[]{second});
    }

    public static List<Integer> convertStringToIntList(String string) {
        List<Integer> intList = new ArrayList<>();
        List<String> stringList = List.of(string.split(";"));
        for (String str : stringList) {
            intList.add(Integer.parseInt(str));
        }

        return intList;
    }

    private List<Integer> oneHotLocation(String item){
        return switch (item) {
            case "Vilnius", "VLN" -> convertStringToIntList("1;0;0;0;0;0");
            case "Kaunas", "KNS" -> convertStringToIntList("0;1;0;0;0;0");
            case "Klaipėda", "KLP" -> convertStringToIntList("0;0;1;0;0;0");
            case "Alytus", "ALT" -> convertStringToIntList("0;0;0;1;0;0");
            case "Panevėžys", "PNV" -> convertStringToIntList("0;0;0;0;1;0");
            case "Užmiestis", "other" -> convertStringToIntList("0;0;0;0;0;1");
            default -> convertStringToIntList("0;0;0;0;0;0");
        };
    }

    private List<Integer> oneHotRoom(String item){
        return switch (item) {
            case "1" -> convertStringToIntList("1;0;0;0;0");
            case "2" -> convertStringToIntList("0;1;0;0;0");
            case "3" -> convertStringToIntList("0;0;1;0;0");
            case "4" -> convertStringToIntList("0;0;0;1;0");
            case "5+", "5" -> convertStringToIntList("0;0;0;0;1");
            default -> convertStringToIntList("0;0;0;0;0");
        };
    }

    private List<Integer> oneHotDist(String item){
        return switch (item) {
            case "0-10", "1" -> convertStringToIntList("1;0;0");
            case "11-100", "2" -> convertStringToIntList("0;1;0");
            case "101+", "3" -> convertStringToIntList("0;0;1");
            default -> convertStringToIntList("0;0;0");
        };
    }

    private List<AID> getAllProviders(){
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        dfd.addServices(sd);
        DFAgentDescription[] result;
        try {
            result = DFService.search(this, dfd);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
        List<AID> providers = new ArrayList<>();
        for(DFAgentDescription provider: result){
            providers.add(provider.getName());
        }
        return providers;
    }

    private List<AID> getProviders(String type){
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType(type);
        dfd.addServices(sd);

        DFAgentDescription[] result;
        try {
            result = DFService.search(this, dfd);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
        List<AID> providers = new ArrayList<>();
        for(DFAgentDescription provider: result){
            providers.add(provider.getName());
        }

        sd.setType("Both");
        dfd.addServices(sd);
        try {
            result = DFService.search(this, dfd);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
        for(DFAgentDescription provider: result){
            providers.add(provider.getName());
        }
        return providers;
    }

    public void takeDown()
    {
        System.out.println("A["+getLocalName()+"] is being removed");
        if (GUI != null)
        {
            GUI.setVisible(false);
            GUI.dispose();
        }
    }

    private class WaitForMessages extends CyclicBehaviour {
        @Override
        public void action() {
            Ontology onto = NuomaOntology.getInstance();
            Codec codec = new SLCodec();
            ContentManager cm = getContentManager();
            cm.registerLanguage(codec);
            cm.registerOntology(onto);

            MessageTemplate proposal = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
            MessageTemplate accept = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            MessageTemplate reject = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
            ACLMessage proposals = myAgent.receive(proposal);
            ACLMessage response = myAgent.receive(MessageTemplate.or(accept, reject));

            if(proposals != null){
                AID sender = proposals.getSender();
                System.out.println("A[" + getLocalName() + "] proposal received from " + sender.getName() + ":");
                try {
                    ContentElement c = cm.extractContent(proposals);
                    if (c instanceof ListingSendMsg)
                    {
                        jade.util.leap.List sent_listings = ((ListingSendMsg) c).getListings();
                        for(int i = 0; i < sent_listings.size(); i++){
                            listings.add((Listing) sent_listings.get(i));
                        }
                    }
                } catch (Codec.CodecException | OntologyException e) {
                    throw new RuntimeException(e);
                }
            }else if(response != null){
                try {
                    ContentElement c = cm.extractContent(response);
                    if (c instanceof AcceptDenyProposalMsg)
                    {
                        boolean accepted = ((AcceptDenyProposalMsg) c).getAccepted();
                        System.out.println(accepted ? "Property purchase successful" : "Property purchase failed");

                        List<Data> offers = new ArrayList<>();
                        offers.add(new Data(accepted?1:0, getFromLID(((AcceptDenyProposalMsg) c).getProposalLID()), null));

                        GUI.setVisible(false);
                        GUI = new BuyerGUI(this_one, offers, answers);
                        GUI.setContentPane(GUI.Panel1);
                        GUI.pack();
                        GUI.setVisible(true);
                    }
                } catch (Codec.CodecException | OntologyException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public Listing getFromLID(int LID){
        for(Listing listing: listings){
            if (listing.getLID() == LID){
                return listing;
            }
        }
        return null;
    }
}
