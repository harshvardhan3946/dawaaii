package com.dawaaii.user;

import com.dawaaii.service.user.UserService;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserCreationSource;
import com.dawaaii.test.AbstractDawaaiiServiceBaseIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
* Created by Rohit on 27/10/15.
*/
public class UserServiceTest extends AbstractDawaaiiServiceBaseIntegrationTest {
    @Autowired
    private UserService userService;

    private static final String userName = "rohit test";
    private static final String firstName = "rohit";
    private static final String lastName = "mishra";
    private static final String email = "rohit.test@gmail.com";
    private static final char gender = 'M';

    @Test
    public void saveUserTestCreate() {

        User user = new User();
        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword("abc@123");
        user.setGender(gender);
        user.setActive(true);
        user.setCreationSource(UserCreationSource.MOBILE);

        user = userService.createUser(user);

        flush();
        clear();

        Assert.assertNotNull(user);
        Assert.assertEquals(userName, user.getUserName());
        Assert.assertEquals(firstName, user.getFirstName());
        Assert.assertEquals(lastName, user.getLastName());
        Assert.assertEquals(email, user.getEmail());
        Assert.assertTrue(user.getGender()=='M');

        User userById = userService.findUserById(user.getId()).get();
        Assert.assertNotNull(userById);
        Assert.assertEquals(userName, userById.getUserName());
        Assert.assertEquals(firstName, userById.getFirstName());
        Assert.assertEquals(lastName, userById.getLastName());
        Assert.assertEquals(email, userById.getEmail());
        Assert.assertTrue(userById.getGender()=='M');

        User userByIdGet = userService.getUserById(user.getId());

        Assert.assertNotNull(userByIdGet);
        Assert.assertEquals(userName, userByIdGet.getUserName());
        Assert.assertEquals(firstName, userByIdGet.getFirstName());
        Assert.assertEquals(lastName, userByIdGet.getLastName());
        Assert.assertEquals(email, userByIdGet.getEmail());
        Assert.assertTrue(userByIdGet.getGender()=='M');

        long userCount = userService.getUsersCount();
        Assert.assertEquals(userCount,2);

        User userByEmail = userService.getUserByEmail(email);

        Assert.assertNotNull(userByEmail);
        Assert.assertEquals(userName, userByEmail.getUserName());
        Assert.assertEquals(firstName, userByEmail.getFirstName());
        Assert.assertEquals(lastName, userByEmail.getLastName());
        Assert.assertEquals(email, userByEmail.getEmail());
        Assert.assertTrue(userByEmail.getGender()=='M');
    }
}