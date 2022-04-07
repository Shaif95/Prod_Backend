package com.example.demo.controller;


import com.example.demo.domain.loginuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login_users")
public class Login_Controller {

    @Autowired
    private com.example.demo.service.LoginService loginService;

    @PostMapping
    public loginuser addAward(@RequestBody loginuser user) {
        return loginService.add(user);
    }

    @GetMapping
    public Map<String, Object> getUsers(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "20") Integer size) {
        return loginService.findWithPaging(page, size);
    }


    @GetMapping("/email/{em}")
    public List getbyemail(@PathVariable String em) {
        return (List) loginService.findByEmail(em);
    }

    @PutMapping("/email/{em}")
    public loginuser updatebyemail(@PathVariable String em, @RequestBody loginuser user) {
        return  loginService.updateByEmail(em, user);
    }

    @PutMapping("/{id}")
    public loginuser update(@PathVariable String id, @RequestBody loginuser user) {
        return loginService.update(id,user);
    }

    @PutMapping("/{id}/follow/{acs}")
    public loginuser updateFollowSet(@PathVariable String id, @PathVariable String acs) throws TwitterException {
        return loginService.updateFollowSet(id,acs);
    }

    @PutMapping("/reset")
    public String get() {
        return loginService.reset();
    }


    @GetMapping("/{id}")
    public loginuser get(@PathVariable String id) {
        return loginService.findById(id);
    }


    @GetMapping("/{id}/followset")
    public List<String> getFollowSet(@PathVariable String id) {
        return (loginService.findById(id).getFollowSet());
    }



    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        loginService.delete(id);
    }

}
