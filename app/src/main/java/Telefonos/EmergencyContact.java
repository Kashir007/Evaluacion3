package Telefonos;

public class EmergencyContact {
    private int id;
    private String name;
    private String phoneNumber;

    public EmergencyContact(int id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}

