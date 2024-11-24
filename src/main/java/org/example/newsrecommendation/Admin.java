package org.example.newsrecommendation;

public class Admin extends Common{
    private String admin_ID;

    public Admin(String name,String email,int age,String gender,String password,String admin_ID){
        super(name, email, age, gender, password);
        this.admin_ID= admin_ID;
    }

    public String getAdmin_ID() {
        return admin_ID;
    }

    public void setAdmin_ID(String admin_ID) {
        this.admin_ID = admin_ID;
    }
}
