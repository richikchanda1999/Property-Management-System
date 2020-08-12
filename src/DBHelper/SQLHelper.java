package DBHelper;

import Models.Agent;
import Models.AgentAddress;
import Models.Property;
import Models.PropertyAddress;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SQLHelper {
    private static SQLHelper helper = new SQLHelper();
    private Connection connection = null;
    private PreparedStatement propertyInsert, agentInsert, propertyAddressInsert, agentAddressInsert,
            phoneNumberInsert, credentialsInsert, addedByInsert, soldByInsert, rentedByInsert,
            agentSelect, agentAddressSelect, agentPhoneNumberSelect, propertiesSelect, isPropertySold, isPropertyRented,
            allAgentSelect, allSoldByAgentSelect, allRentedByAgentSelect, avgSellingPrice, avgRentAmount, selectProperty;

    private SQLHelper() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/REAL_ESTATE?allowPublicKeyRetrieval=true&useSSL=false", "richik", "password");
            propertyInsert = connection.prepareStatement("INSERT INTO Property values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            propertyAddressInsert = connection.prepareStatement("INSERT INTO Property_Address values (?, ?, ?, ?, ?, ?, ?)");
            agentInsert = connection.prepareStatement("INSERT INTO Agent values (?, ?, ?, ?)");
            agentAddressInsert = connection.prepareStatement("INSERT INTO Agent_Address values (?, ?, ?, ?, ?, ?, ?)");
            phoneNumberInsert = connection.prepareStatement("INSERT INTO Phone_Number values (?, ?)");
            credentialsInsert = connection.prepareStatement("INSERT INTO Credentials values (?, ?, ?)");
            addedByInsert = connection.prepareStatement("INSERT INTO Added_By values (?, ?, ?)");
            soldByInsert = connection.prepareStatement("INSERT INTO Sold_By values (?, ?, ?, ?)");
            rentedByInsert = connection.prepareStatement("INSERT INTO Rented_By values (?, ?, ?, ?, ?)");

            agentSelect = connection.prepareStatement("SELECT * FROM Agent where username = ?");
            agentAddressSelect = connection.prepareStatement("SELECT * from Agent_Address where agent_id = ?");
            agentPhoneNumberSelect = connection.prepareStatement("SELECT phone_number FROM Phone_Number where agent_id = ?");

            propertiesSelect = connection.prepareStatement("SELECT * FROM Property P left join Property_Address PA on P.property_id = PA.property_id left join Added_By A on P.property_id = A.property_id where A.agent_id = ?");
            isPropertySold = connection.prepareStatement("SELECT count(*) FROM Sold_By where property_id = ? and agent_id = ?");
            isPropertyRented = connection.prepareStatement("SELECT count(*) FROM Rented_By where property_id = ? and agent_id = ?");

            allAgentSelect = connection.prepareStatement("SELECT agent_id, first_name, last_name FROM Agent");
            allSoldByAgentSelect = connection.prepareStatement("SELECT property_id, selling_price, selling_date FROM Sold_By where agent_id = ?");
            allRentedByAgentSelect = connection.prepareStatement("SELECT property_id, agreement_start_date, agreement_end_date, rent_amount FROM Rented_By where agent_id = ?");

            avgSellingPrice = connection.prepareStatement("SELECT avg(selling_price) from Sold_By where agent_id = ?");
            avgRentAmount = connection.prepareStatement("SELECT avg(rent_amount) from Rented_By where agent_id = ?");

            selectProperty = connection.prepareStatement("SELECT * FROM Property natural join Property_Address natural join Added_By where agent_id = ? and property_id = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SQLHelper getInstance() {
        return helper;
    }

    public Connection getConnection() {
        return connection;
    }

    public String toQuery(HashMap<String, String> map) {
        if (map.keySet().size() == 0) return "";
        String ret = "where ";
        for (Map.Entry<String, String> e : map.entrySet()) {
            ret += e.getKey() + "=" + e.getValue() + ", ";
        }
        return ret.substring(0, ret.length() - 1);
    }

    public int getPropertyID() {
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Property");
            rs.next();
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getAgentID() {
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Agent");
            rs.next();
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean addProperty(Property property, Agent agent) {
        try {
            connection.setAutoCommit(false);
            propertyInsert.setInt(1, property.getProperty_id());
            propertyInsert.setInt(2, property.getNo_of_bathrooms());
            propertyInsert.setInt(3, property.getNo_of_bedrooms());
            propertyInsert.setInt(4, property.getNo_of_balconies());
            propertyInsert.setDouble(5, property.getCarpet_area());
            propertyInsert.setInt(6, property.isElevator() ? 1 : 0);
            propertyInsert.setInt(7, property.getStorey());
            propertyInsert.setString(8, property.getType_of_property());
            propertyInsert.setDouble(9, property.getSize_of_kitchen());
            propertyInsert.setInt(10, property.isTerrace_access() ? 1 : 0);
            propertyInsert.execute();

            PropertyAddress address = property.getAddress();
            propertyAddressInsert.setInt(1, property.getProperty_id());
            propertyAddressInsert.setString(2, address.getCountry());
            propertyAddressInsert.setString(3, address.getState());
            propertyAddressInsert.setString(4, address.getCity());
            propertyAddressInsert.setInt(5, address.getPincode());
            propertyAddressInsert.setString(6, address.getLocality());
            propertyAddressInsert.setString(7, address.getLandmark());
            propertyAddressInsert.execute();

            addedByInsert.setInt(1, property.getProperty_id());
            addedByInsert.setInt(2, agent.getAgent_id());
            addedByInsert.setDate(3, new Date(new java.util.Date().getTime()));
            addedByInsert.execute();

            connection.commit();
            return true;
        } catch (SQLException e) {
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        return false;
    }

    public boolean addAgent(Agent agent) {
        try {
            agentInsert.setInt(1, agent.getAgent_id());
            agentInsert.setString(2, agent.getFirstName());
            agentInsert.setString(3, agent.getLastName());
            agentInsert.setString(4, agent.getUsername());
            agentInsert.execute();

            AgentAddress address = agent.getAddress();
            agentAddressInsert.setInt(1, agent.getAgent_id());
            agentAddressInsert.setString(2, address.getCountry());
            agentAddressInsert.setString(3, address.getState());
            agentAddressInsert.setString(4, address.getCity());
            agentAddressInsert.setInt(5, address.getPincode());
            agentAddressInsert.setString(6, address.getLocality());
            agentAddressInsert.setString(7, address.getLandmark());
            agentAddressInsert.execute();

            String[] phoneNumbers = agent.getPhoneNumbers();
            for (String number : phoneNumbers) {
                phoneNumberInsert.setInt(1, agent.getAgent_id());
                phoneNumberInsert.setString(2, number);
                phoneNumberInsert.execute();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String encrypt(String password) {
        return password;
    }

    public boolean addUser(String username, String password) {
        try {
            credentialsInsert.setString(1, username);
            credentialsInsert.setString(2, encrypt(password));
            credentialsInsert.setInt(3, 0);
            credentialsInsert.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sellProperty(int property_id, int agent_id, double sellingPrice, java.util.Date date) {
        try {
            soldByInsert.setInt(1, property_id);
            soldByInsert.setInt(2, agent_id);
            soldByInsert.setDouble(3, sellingPrice);
            soldByInsert.setDate(4, new Date(date.getTime()));
            soldByInsert.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean rentProperty(int property_id, int agent_id, double rent_amount, java.util.Date start, java.util.Date end) {
        try {
            rentedByInsert.setInt(1, property_id);
            rentedByInsert.setInt(2, agent_id);
            rentedByInsert.setDate(3, new Date(start.getTime()));
            rentedByInsert.setDate(4, new Date(end.getTime()));
            rentedByInsert.setDouble(5, rent_amount);
            rentedByInsert.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Agent agentFromUsername(String username) {
        try {
            agentSelect.setString(1, username);
            ResultSet rs = agentSelect.executeQuery();
            if (rs == null) return null;
            rs.next();

            int agent_id = rs.getInt(1);
            String firstName = rs.getString(2);
            String lastName = rs.getString(3);

            agentAddressSelect.setInt(1, agent_id);
            rs = agentAddressSelect.executeQuery();
            rs.next();
            String country = rs.getString(2);
            String state = rs.getString(3);
            String city = rs.getString(4);
            int pincode = rs.getInt(5);
            String locality = rs.getString(6);
            String landmark = rs.getString(7);

            agentPhoneNumberSelect.setInt(1, agent_id);
            rs = agentPhoneNumberSelect.executeQuery();
            rs.last();
            String[] phoneNumbers = new String[rs.getRow()];
            rs.first();
            int i = 0;
            while (rs.next()) phoneNumbers[i++] = rs.getString(1);

            Agent agent = new Agent(agent_id, firstName, lastName, username, new AgentAddress(agent_id, country, state, city, pincode, locality, landmark), phoneNumbers);
            return agent;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //-1 if invalid
    //0 if real estate office
    // > 0 if an agent
    public int login(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Credentials where username = ? and password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs == null) return -1;
            rs.last();
            if (rs.getRow() == 0) return -1;

            if (username.equals("admin")) return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public ArrayList<Property> getProperties(int agent_id) {
        try {
            propertiesSelect.setInt(1, agent_id);
            ResultSet rs = propertiesSelect.executeQuery();

            ArrayList<Property> properties = new ArrayList<>();
            while (rs.next()) {
                int property_id = rs.getInt(1);
                int no_of_bathrooms = rs.getInt(2);
                int no_bedrooms = rs.getInt(3);
                int no_of_balconies = rs.getInt(4);
                double carpet_area = rs.getDouble(5);
                boolean elevator = rs.getInt(6) == 1;
                int storey = rs.getInt(7);
                String type_of_property = rs.getString(8);
                double size_of_kitchen = rs.getDouble(9);
                boolean terrace_access = rs.getInt(10) == 1;

                String country = rs.getString(12);
                String state = rs.getString(13);
                String city = rs.getString(14);
                int pincode = rs.getInt(15);
                String locality = rs.getString(16);
                String landmark = rs.getString(17);
                PropertyAddress address = new PropertyAddress(property_id, country, state, city, pincode, locality, landmark);

                Date date = rs.getDate(20);
                java.util.Date dateAdded = new java.util.Date(date.getTime());
                properties.add(new Property(property_id, no_of_bathrooms, no_bedrooms, no_of_balconies, carpet_area, elevator, storey, type_of_property, size_of_kitchen, terrace_access, address, dateAdded));
            }

            return properties;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void populateModelForAgent(int agent_id, DefaultTableModel model) {
        model.setColumnCount(0);
        while(model.getRowCount() != 0) model.removeRow(0);

        String[] labelHeads = {"Property ID", "No. of bathrooms", "No. of bedrooms", "No. of balconies", "Carpet Area",
                "Elevator", "Storey", "Type of Property", "Size of kitchen", "Terrace Access", "Country", "State",
                "City", "Pincode", "Locality", "Landmark", "Date Added"};
        for (String label : labelHeads) model.addColumn(label);

        ArrayList<Property> properties = getProperties(agent_id);
        for (Property property : properties) {
            model.addRow(property.toArray());
        }
    }

    public int getPropertyStatus(int property_id, int agent_id) {
        try {
            isPropertySold.setInt(1, property_id);
            isPropertySold.setInt(2, agent_id);
            ResultSet rs = isPropertySold.executeQuery();

            if(rs != null) {
                rs.next();
                int count = rs.getInt(1);
                if(count > 0) return Property.SOLD;
            }

            isPropertyRented.setInt(1, property_id);
            isPropertyRented.setInt(2, agent_id);
            rs = isPropertyRented.executeQuery();

            if(rs != null) {
                rs.next();
                int count = rs.getInt(1);
                if(count > 0) return Property.RENTED;
            }

            return Property.IN_MARKET;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Property.ERROR;
    }

    public boolean populateAllAgents(DefaultTableModel model) {
        try {
            ResultSet rs = allAgentSelect.executeQuery();
            if (rs == null) return false;
            model.setColumnCount(0);
            while(model.getRowCount() != 0) model.removeRow(0);

            model.addColumn("Agent ID");
            model.addColumn("Name");

            while(rs.next()) {
                int agent_id = rs.getInt(1);
                String name = rs.getString(2) + " " + rs.getString(3);

                Object[] arr = {agent_id, name};
                model.addRow(arr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void populateAllSoldPropertiesWithID(int id, DefaultTableModel model) {
        try {
            allSoldByAgentSelect.setInt(1, id);
            ResultSet rs = allSoldByAgentSelect.executeQuery();
            if (rs == null) return;
            model.setColumnCount(0);
            while(model.getRowCount() != 0) model.removeRow(0);

            model.addColumn("Property ID");
            model.addColumn("Selling Price");
            model.addColumn("Selling Date");

            while(rs.next()) {
                int property_id = rs.getInt(1);
                double selling_price = rs.getDouble(2);
                Date selling_date = rs.getDate(3);

                Object[] arr = {property_id, selling_price, selling_date};
                model.addRow(arr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateAllRentedPropertiesWithID(int id, DefaultTableModel model) {
        try {
            allRentedByAgentSelect.setInt(1, id);
            ResultSet rs = allRentedByAgentSelect.executeQuery();
            if (rs == null) return;

            model.setColumnCount(0);
            while(model.getRowCount() != 0) model.removeRow(0);

            model.addColumn("Property ID");
            model.addColumn("Rent Amount");
            model.addColumn("Agreement Start Date");
            model.addColumn("Agreement End Date");

            while(rs.next()) {
                int property_id = rs.getInt(1);
                double rent_amount = rs.getDouble(4);
                Date start = rs.getDate(2);
                Date end = rs.getDate(3);

                Object[] arr = {property_id, rent_amount, start, end};
                model.addRow(arr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double getSoldPropertyAvgSellingPrice(int agent_id) {
        double value = 0.0;
        try {
            avgSellingPrice.setInt(1, agent_id);
            ResultSet rs = avgSellingPrice.executeQuery();

            rs.next();
            value = rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
            value = -1;
        }
        return value;
    }

    public double getRentedPropertyAvgRentAmount(int agent_id) {
        double value = 0.0;
        try {
            avgRentAmount.setInt(1, agent_id);
            ResultSet rs = avgRentAmount.executeQuery();

            rs.next();
            value = rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
            value = -1;
        }
        return value;
    }

    public Property getProperty(int agent_id, int property_id) {
        Property property = null;
        try {
            selectProperty.setInt(1, agent_id);
            selectProperty.setInt(2, property_id);

            ResultSet rs = selectProperty.executeQuery();
            if(rs != null) {
                rs.next();

                property_id = rs.getInt(1);
                int no_of_bathrooms = rs.getInt(2);
                int no_bedrooms = rs.getInt(3);
                int no_of_balconies = rs.getInt(4);
                double carpet_area = rs.getDouble(5);
                boolean elevator = rs.getInt(6) == 1;
                int storey = rs.getInt(7);
                String type_of_property = rs.getString(8);
                double size_of_kitchen = rs.getDouble(9);
                boolean terrace_access = rs.getInt(10) == 1;

                String country = rs.getString(11);
                String state = rs.getString(12);
                String city = rs.getString(13);
                int pincode = rs.getInt(14);
                String locality = rs.getString(15);
                String landmark = rs.getString(16);
                PropertyAddress address = new PropertyAddress(property_id, country, state, city, pincode, locality, landmark);

                Date date = rs.getDate(18);
                java.util.Date dateAdded = new java.util.Date(date.getTime());
                property = new Property(property_id, no_of_bathrooms, no_bedrooms, no_of_balconies, carpet_area, elevator, storey, type_of_property, size_of_kitchen, terrace_access, address, dateAdded);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return property;
    }

    public Agent agentFromID(int agent_id) {
        PreparedStatement agentSelect = null;
        try {
            agentSelect = connection.prepareStatement("SELECT username from Agent where agent_id = ?");
            agentSelect.setInt(1, agent_id);
            ResultSet rs = agentSelect.executeQuery();

            rs.next();
            String username = rs.getString(1);
            return agentFromUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPropertiesAddedCount(int agent_id) {
        PreparedStatement propertiesAddedCount = null;
        try {
            propertiesAddedCount = connection.prepareStatement("SELECT count(*) FROM Added_By where agent_id = ?");
            propertiesAddedCount.setInt(1, agent_id);

            ResultSet rs = propertiesAddedCount.executeQuery();
            rs.next();

            int count = rs.getInt(1);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
