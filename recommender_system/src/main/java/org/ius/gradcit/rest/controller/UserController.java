package org.ius.gradcit.rest.controller;

import org.ius.gradcit.logic.user.UserService;
import org.ius.gradcit.rest.entity.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("incUserInterest")
    public void incUserInterest(@RequestBody UserAction userAction) {
        userService.incInterest(userAction);
    }

}
