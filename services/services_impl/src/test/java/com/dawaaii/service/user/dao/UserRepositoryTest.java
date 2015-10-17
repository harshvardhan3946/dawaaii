package com.dawaaii.service.user.dao;

import com.dawaaii.service.dao.jpa.UserRepository;
import com.dawaaii.service.user.model.User;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Ignore
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAll() {
        List<User> users = userRepository.findAll();
        Assert.assertNotNull(users);
        Assert.assertEquals(1, users.size());
    }
}
