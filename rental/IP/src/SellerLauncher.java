import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import static java.lang.Thread.sleep;
import jade.wrapper.AgentController;
import jade.wrapper.AgentContainer;
import jade.domain.FIPAException;
import jade.domain.DFService;
import jade.core.Agent;
import java.util.*;
import java.io.*;
import nuoma.*;
public class SellerLauncher extends Agent {

    @Override
    public void setup(){

        Object[] args = getArguments();
        if (args == null || args.length == 0) {
            System.out.println("No arguments provided.");
        }else {

            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());

            if (args.length == 3) {
                ServiceDescription sd;
                for (int i = 0; i < 3; i++) {
                    sd = new ServiceDescription();
                    String s = String.valueOf(args[i]);
                    if (s.equals("1")) {
                        sd.setName("Service" + (i + 1));
                        sd.setType("Service" + (i + 1));
                        dfd.addServices(sd);
                    }
                }
                try {
                    DFService.register(this, dfd);
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }
            }

            List<Listing> listings = new ArrayList<>();
            List<Seller> sellers = new ArrayList<>();
            List<Housing> housings = new ArrayList<>();
            List<String[]> additional = new ArrayList<>();

            try {
                File myObj = new File("src/data.csv");
                Scanner myReader = new Scanner(myObj);

                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] split = data.split(",");
                    sellers.add(make_seller(
                            sellers.size(), //ID
                            split[1], //name
                            split[2], //surname
                            split[3], //phone
                            split[4], //email
                            split[6])); //address
                    housings.add(make_housing(
                            housings.size(), //HID
                            split[7], //location
                            Integer.parseInt(split[8]), //area
                            Integer.parseInt(split[9]), //age
                            Integer.parseInt(split[10]), //rooms
                            Boolean.parseBoolean(split[11]), //garage
                            Boolean.parseBoolean(split[12]), //pool
                            Boolean.parseBoolean(split[13]), //basement
                            Integer.parseInt(split[14]))); //dist_to_city
                    additional.add(new String[]{
                            split[15], // rentable
                            split[16], // price
                            split[17], // available
                            split[5]}); //random number
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("Could not read the file.");
                e.printStackTrace();
            }
            Random random = new Random(6);
            for (int i = 0; i < housings.size(); i++) {
                String[] addition = additional.get(i);
                listings.add(make_listing(
                        i,
                        housings.get(i),
                        sellers.get(Integer.parseInt(addition[3]) % (sellers.size() / 2)),
                        Float.parseFloat(addition[1]),
                        Boolean.parseBoolean(addition[0]),
                        make_reservations(random.nextInt())));
            }

            try {
                AgentContainer mc = this.getContainerController();
                AgentController actor;
                actor = mc.createNewAgent("Buyer", "Buyer", null);
                actor.start();
                for (int i = 0; i < sellers.size() / 2; i++) {
                    Object[] arguments = new Object[]{sellers.get(i), null};
                    List<Listing> listings1 = new ArrayList<>();
                    for (Listing current : listings) {
                        Seller agent = current.getAgent();
                        if (sellers.get(i).getID() == agent.getID()) {
                            listings1.add(current);
                        }
                    }
                    arguments[1] = listings1;
                    actor = mc.createNewAgent("Seller" + i, "Realtor", arguments);//new String[]{encodeObject(sellers.get(i))});
                    actor.start();
                    sleep(100);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            this.doDelete();
        }
    }

    private int[] make_reservations(int seed){
        int[] answer = new int[52];
        Random random = new Random(seed);
        for (int i = 0; i < answer.length; i++) {
            answer[i] = (random.nextDouble() < 0.3) ? 1 : 0;
        }
        return answer;
    }

    private Housing make_housing(int HID, String location, int area, int age, int rooms, boolean garage, boolean pool, boolean basement, int dist_to_city){
        Housing answer = new Housing();
        answer.setHID(HID);
        answer.setLocation(location);
        answer.setArea(area);
        answer.setAge(age);
        answer.setRooms(rooms);
        answer.setGarage(garage);
        answer.setPool(pool);
        answer.setBasement(basement);
        answer.setDist_to_city(dist_to_city);
        answer.setComments("");
        return answer;
    }
    private Listing make_listing(int LID, Housing estate, Seller agent, float price, boolean rentable, int[] available){
        Listing answer = new Listing();
        answer.setLID(LID);
        answer.setEstate(estate);
        answer.setAgent(agent);
        answer.setPrice(price);
        answer.setRentable(rentable);
        jade.util.leap.List a = new jade.util.leap.ArrayList();
        for (int j : available) {
            a.add(j);
        }
        answer.setAvailable(a);
        return answer;
    }

    private Seller make_seller(int ID, String name, String surname, String phone, String email, String address){
        Seller answer = new Seller();
        answer.setID(ID);
        answer.setName(name);
        answer.setSurname(surname);
        answer.setPhone(phone);
        answer.setEmail(email);
        answer.setAddress(address);
        return answer;
    }


}
