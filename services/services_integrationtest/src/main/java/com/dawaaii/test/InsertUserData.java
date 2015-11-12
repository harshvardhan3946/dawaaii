package com.dawaaii.test;

import com.dawaaii.service.security.DawaaiiRoleService;
import com.dawaaii.service.security.model.Permission;
import com.dawaaii.service.security.model.RoleCode;
import com.dawaaii.service.user.UserService;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserCreationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by rohit on 29/10/15.
 */
@Component
public class InsertUserData {

    @Autowired
    private UserService userService;

    @Autowired
    private DawaaiiRoleService dawaaiiRoleService;

    public void insertData() {
        createRoleAndPermissions();
        User user = userService.createUser(createUserObj("Rohit","Mishra","rohit.mishra0411@gmail.com","abc",'M',true));
        associateRolesToUser(user);
    }

    private void associateRolesToUser(User user) {
        user.addRole(dawaaiiRoleService.getRoleByCode(RoleCode.SYSTEM_ADMINISTRATOR));
        userService.saveUser(user);
    }

    private void createRoleAndPermissions() {
        dawaaiiRoleService.createRole("System Administrator", RoleCode.SYSTEM_ADMINISTRATOR.name(), Arrays.asList(Permission.values()));
        dawaaiiRoleService.createRole("General User", RoleCode.WEBSITE_APP_GENERAL_USER.name(),  null);

    }

    private User createUserObj(String userName, String lastName, String email, String password, Character gender, boolean active) {
        User user = new User();
        user.setUserName(userName);
        user.setFirstName(userName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEmailConfirmed(true);
        user.setGender(gender);
        user.setActive(active);
        user.setPassword(password);
        user.setPhoneNumber("9999175430");
        user.setCreationSource(UserCreationSource.WEBSITE);
        return user;
    }

}
