package model;

public class Client {
    private int id;
    private String firstname;
    private String lastname;
    private String cin;
    private java.sql.Date birthdate;
    private String email;
    private String phoneNumber;
    private String password;

    public Client() {}

    public Client(int id, String firstname, String lastname, String cin, java.sql.Date birthdate, String email, String phoneNumber, String password) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.cin = cin;
        this.birthdate = birthdate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
    public java.sql.Date getBirthdate() { return birthdate; }
    public void setBirthdate(java.sql.Date birthdate) { this.birthdate = birthdate; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return firstname + " " + lastname + " (CIN: " + cin + ")";
    }
}
