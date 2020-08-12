package Models;

import java.util.ArrayList;
import java.util.List;

public class Agent {
    private int agent_id;
    private String firstName, lastName, username;
    private AgentAddress address;
    private String[] phoneNumbers;

    public Agent(int agent_id, String firstName, String lastName, String username, AgentAddress address, String[] phoneNumbers) {
        this.agent_id = agent_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.address = address;
        this.phoneNumbers = phoneNumbers;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public AgentAddress getAddress() {
        return address;
    }

    public void setAddress(AgentAddress address) {
        this.address = address;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public String[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}