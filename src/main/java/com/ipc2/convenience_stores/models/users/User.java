package com.ipc2.convenience_stores.models.users;

import com.ipc2.convenience_stores.models.stores.Store;
import lombok.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@ToString
@AllArgsConstructor

public class User {

    @Getter private int id = 0;
    @Getter @Setter private UserType type;
    @Getter @Setter private String name;
    @Getter @Setter private String username;
    @Getter @Setter private String email;
    @Getter private String password;
    @Getter @Setter private Store store;


    public User(UserType type, String name, String username, String email, String password, Store store) {
        this.type = type;
        this.name = name;
        this.username = username;
        this.email = email;
        this.store = store;
        this.setPassword(password);
    }

    public void setPassword(String password) {
        this.password = encryptPassword(password);
    }

    public boolean validatePassword(String password) {
        return this.password.equals(encryptPassword(password));
    }

    private String encryptPassword(String password){
        String seasonedPassword = this.getId() + password + this.getType().getName();
        MessageDigest md = null;
	    try {
	    	md = MessageDigest.getInstance("SHA-256");
	    }
	    catch (NoSuchAlgorithmException e) {
	    	e.printStackTrace();
	    	return null;
	    }
	    byte[] hash = md.digest(seasonedPassword.getBytes());
	    StringBuffer sb = new StringBuffer();
	    for(byte b : hash) {
	    	sb.append(String.format("%02x", b));
	    }
	    return sb.toString();
    }

}
