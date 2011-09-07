package com.googlecode.tawus.addons.integration.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.googlecode.tawus.addons.User;
import com.googlecode.tawus.addons.annotations.InjectSelectSupport;

/**
 * A page to demostrate the use of @InjectSelectSupport annotation
 * 
 */
public class InjectSelectSupportDemo {

    @InjectSelectSupport(index = "id", label = "${name}", type = User.class)
    @Persist
    @Property
    private List<User> users;
    
    @SuppressWarnings("unused")
    @Property
    @Persist(PersistenceConstants.FLASH)
    private User user;

    void setupRender() {
        users = new ArrayList<User>();
        users.add(new User(1, "tawus", "kashmir"));
        users.add(new User(2, "haya", "delhi"));
    }
}
