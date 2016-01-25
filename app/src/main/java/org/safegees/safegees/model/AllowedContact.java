package org.safegees.safegees.model;

/**
 * Created by victor on 2/1/16.
 */
public class AllowedContact {

    private String email;
    private int positionConfidenciallity;

    public AllowedContact(String email, int positionConfidenciallity) {
        this.email = email;
        this.positionConfidenciallity = positionConfidenciallity;
    }

    public String getEmail() {
        return email;
    }

    public int getPositionConfidenciallity() {
        return positionConfidenciallity;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPositionConfidenciallity(int positionConfidenciallity) {
        this.positionConfidenciallity = positionConfidenciallity;
    }
}
