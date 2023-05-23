import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import jade.gui.GuiEvent;
import java.util.Objects;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class BuyerGUI extends javax.swing.JFrame{
    Buyer myAgent;
    public BuyerGUI(Buyer a, List<Buyer.Data> offers, GuiEvent answers){
        myAgent = a;
        addLocationCombo();
        addRoomCountCombo();
        addDistCombo();
        addTypeCombo();
        rentstarttext.setText("Nuomos pradžia");
        rentstarttext.setForeground(Color.BLACK);
        rentendtext.setText("Nuomos pabaiga");
        rentendtext.setForeground(Color.BLACK);
        if (answers != null){
            typecombo.setSelectedItem(answers.getParameter(0));
            minprice.setText((String) answers.getParameter(1));
            maxprice.setText((String) answers.getParameter(2));
            locationcombo.setSelectedItem(answers.getParameter(3));
            locationexact.setSelected((Boolean) answers.getParameter(4));
            areatext.setText((String) answers.getParameter(5));
            agetext.setText((String) answers.getParameter(6));
            roomcountcombo.setSelectedItem(answers.getParameter(7));
            roomcountexact.setSelected((Boolean) answers.getParameter(8));
            garagecheck.setSelected((Boolean) answers.getParameter(9));
            garageexact.setSelected((Boolean) answers.getParameter(10));
            poolcheck.setSelected((Boolean) answers.getParameter(11));
            poolexact.setSelected((Boolean) answers.getParameter(12));
            basementcheck.setSelected((Boolean) answers.getParameter(13));
            basementexact.setSelected((Boolean) answers.getParameter(14));
            distcombo.setSelectedItem(answers.getParameter(15));
            distexact.setSelected((Boolean) answers.getParameter(16));
            rentstart.setText((String) answers.getParameter(17));
            rentend.setText((String) answers.getParameter(18));

            if (offers.size() == 1 && offers.get(0).getOnehot() == null){
                text1.setText("Property with LID " + offers.get(0).getListing().getLID() +
                        ((offers.get(0).getDistance() == 1)
                                ? " buying/renting succeeded"
                                : "buying/renting failed"));
            }else {

                boolean rentable = true;
                if (rentstart.getText().equals("-") || Integer.parseInt(rentstart.getText()) < 1 || Integer.parseInt(rentstart.getText()) > 52) {
                    rentstarttext.setText("Nuomos pradžia (Įveskite savaitės nr.)");
                    rentstarttext.setForeground(Color.RED);
                    rentable = false;
                }
                if (rentend.getText().equals("-") || Integer.parseInt(rentend.getText()) < 1 || Integer.parseInt(rentend.getText()) > 52) {
                    rentendtext.setText("Nuomos pabaiga (Įveskite savaitės nr.)");
                    rentendtext.setForeground(Color.RED);
                    rentable = false;
                }

                choicecombo.addItem("-");
                if (offers.size() > 0) {
                    text2.setText("Galimos savaitės nuomai:\n");
                    for (Buyer.Data offer : offers) {
                        if (offer.getListing().getPrice() < 0) continue;
                        text1.append(offer.getListing().getLID() + ") " + offer.ToString());
                        if (!offer.getListing().getRentable()) {
                            text2.append(offer.getListing().getLID() + ") Nuoma negalima.\n");
                            choicecombo.addItem(offer.getListing().getLID() + ": " + offer.getListing().getPrice() + "€");
                        } else if (rentable) {
                            {
                                int start = Integer.parseInt(rentstart.getText()) - 1;
                                int end = Integer.parseInt(rentend.getText()) - 1;
                                if (start <= end) {
                                    text2.append(offer.getListing().getLID() + ") " + offer.getAvailable(start, end) + "\n");
                                    if (offer.isRentable(start, end))
                                        choicecombo.addItem(offer.getListing().getLID() + ": " + (end - start + 1) * 7 * offer.getListing().getPrice() + "€");
                                }
                            }
                        }
                    }
                } else {
                    text2.setText("No available items. Modify the filter and try again.");
                }
            }
        }


        confirmbuybutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("confirming...");
                GuiEvent ge = new GuiEvent(this, Buyer.CONFIRM);
                ge.addParameter(Objects.requireNonNull(typecombo.getSelectedItem()).toString());//0

                ge.addParameter(minprice.getText().matches("\\d+") ? minprice.getText() : "-1");//1
                ge.addParameter(maxprice.getText().matches("\\d+") ? maxprice.getText() : "1000000000");//2

                ge.addParameter(Objects.requireNonNull(locationcombo.getSelectedItem()).toString());//3
                ge.addParameter(locationexact.isSelected());//4

                ge.addParameter(areatext.getText().matches("\\d+") ? areatext.getText() : "0");//5
                ge.addParameter(agetext.getText().matches("\\d+") ? agetext.getText() : "0");//6

                ge.addParameter(Objects.requireNonNull(roomcountcombo.getSelectedItem()).toString());//7
                ge.addParameter(roomcountexact.isSelected());//8

                ge.addParameter(garagecheck.isSelected());//9
                ge.addParameter(garageexact.isSelected());//10

                ge.addParameter(poolcheck.isSelected());//11
                ge.addParameter(poolexact.isSelected());//12

                ge.addParameter(basementcheck.isSelected());//13
                ge.addParameter(basementexact.isSelected());//14

                ge.addParameter(Objects.requireNonNull(distcombo.getSelectedItem()).toString());//15
                ge.addParameter(distexact.isSelected());//16

                ge.addParameter(rentstart.getText().matches("\\d+") ? rentstart.getText() : "1");//17
                ge.addParameter(rentend.getText().matches("\\d+") ? rentend.getText() : "52");//18

                ge.addParameter(Objects.requireNonNull(choicecombo.getSelectedItem()).toString());//19
                myAgent.onGuiEvent(ge);
            }
        });

        searchbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("searching...");
                GuiEvent ge = new GuiEvent(this, Buyer.SEARCH);

                ge.addParameter(Objects.requireNonNull(typecombo.getSelectedItem()).toString());//0

                ge.addParameter(minprice.getText().matches("\\d+") ? minprice.getText() : "-1");//1
                ge.addParameter(maxprice.getText().matches("\\d+") ? maxprice.getText() : "1000000000");//2

                ge.addParameter(Objects.requireNonNull(locationcombo.getSelectedItem()).toString());//3
                ge.addParameter(locationexact.isSelected());//4

                ge.addParameter(areatext.getText().matches("\\d+") ? areatext.getText() : "0");//5
                ge.addParameter(agetext.getText().matches("\\d+") ? agetext.getText() : "0");//6

                ge.addParameter(Objects.requireNonNull(roomcountcombo.getSelectedItem()).toString());//7
                ge.addParameter(roomcountexact.isSelected());//8

                ge.addParameter(garagecheck.isSelected());//9
                ge.addParameter(garageexact.isSelected());//10

                ge.addParameter(poolcheck.isSelected());//11
                ge.addParameter(poolexact.isSelected());//12

                ge.addParameter(basementcheck.isSelected());//13
                ge.addParameter(basementexact.isSelected());//14

                ge.addParameter(Objects.requireNonNull(distcombo.getSelectedItem()).toString());//15
                ge.addParameter(distexact.isSelected());//16

                ge.addParameter(rentstart.getText().matches("\\d+") ? rentstart.getText() : "1");//17
                ge.addParameter(rentend.getText().matches("\\d+") ? rentend.getText() : "52");//18

                ge.addParameter(null);//19

                myAgent.onGuiEvent(ge);
            }
        });
    }

    // Prices
    private JTextField minprice;
    private JTextField maxprice;

    // Location
    private JComboBox locationcombo;
    private JCheckBox locationexact;

    // Area
    private JTextField areatext;

    // Age
    private JTextField agetext;

    // Room count
    private JComboBox roomcountcombo;
    private JCheckBox roomcountexact;

    // Garage
    private JCheckBox garagecheck;
    private JCheckBox garageexact;

    // Pool
    private JCheckBox poolcheck;
    private JCheckBox poolexact;

    // Basement
    private JCheckBox basementcheck;
    private JCheckBox basementexact;

    // Distance to city
    private JComboBox distcombo;
    private JCheckBox distexact;

    // Type of housing
    private JComboBox typecombo;

    // Search button
    private JButton searchbutton;

    // Confirm button
    private JButton confirmbuybutton;

    //Choice combo box
    private JComboBox choicecombo;

    //Rent duration text fields
    private JTextField rentstart;
    private JTextField rentend;


    private JTextArea text1;
    private JTextArea text2;
    public JPanel Panel1;
    private JLabel rentstarttext;
    private JLabel rentendtext;

    public void addLocationCombo(){
        locationcombo.addItem("-");
        locationcombo.addItem("Vilnius");
        locationcombo.addItem("Kaunas");
        locationcombo.addItem("Klaipėda");
        locationcombo.addItem("Alytus");
        locationcombo.addItem("Panevėžys");
        locationcombo.addItem("Užmiestis");
    }

    public void addRoomCountCombo(){
        roomcountcombo.addItem("-");
        roomcountcombo.addItem("1");
        roomcountcombo.addItem("2");
        roomcountcombo.addItem("3");
        roomcountcombo.addItem("4");
        roomcountcombo.addItem("5+");
    }

    public void addDistCombo(){
        distcombo.addItem("-");
        distcombo.addItem("0-10");
        distcombo.addItem("11-100");
        distcombo.addItem("101+");
    }

    public void addTypeCombo(){
        typecombo.addItem("Both");
        typecombo.addItem("Rent");
        typecombo.addItem("Buy");
    }

}