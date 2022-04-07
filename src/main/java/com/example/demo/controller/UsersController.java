package com.example.demo.controller;


import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private com.example.demo.service.usrService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/new")
    public String Backend()
    {
        return "Welcome to Tweetsy Backend Spring";
    }

    @PostMapping
    public User addAward(@RequestBody User user) {
        return userService.add(user);
    }

    @GetMapping
    public Map<String, Object> getUsers(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "20") Integer size) {
        return userService.findWithPaging(page, size);
    }

    @GetMapping("/auth/{id}")
    public String auth (@PathVariable String id) throws ServletException, IOException, TwitterException {
        return userService.auth(id);
    }



    @PutMapping("/auth/{id}/{tok}/{sec}/{pin}")
    public User Oauth (@PathVariable String id, @PathVariable String tok,@PathVariable String sec,@PathVariable String pin ) throws ServletException, IOException, TwitterException {
        return userService.Oauth(id, tok, sec, pin);
    }

    @PutMapping("/analysis/{id}/{dur}")
    public User analysis (@PathVariable String id, @PathVariable String dur) throws ServletException, IOException, TwitterException {
        return userService.analysis(id, dur);
    }

    @PostMapping("/tweet/{id}/{tweet}")
    public String post (@PathVariable String id, @PathVariable String tweet) throws ServletException, IOException, TwitterException {
        return userService.post(id,tweet);
    }

    @PostMapping("/reply/{id}/{tweetid}")
    public String reply (@PathVariable String id, @PathVariable String tweetid, @RequestBody String replyms) throws ServletException, IOException, TwitterException {
        return userService.reply(id, tweetid, replyms);
    }


    @PutMapping("/status/{id}/{st}")
    public User updatestatus(@PathVariable String id, @PathVariable String st) {
        return userService.stat(id,st);
    }



    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody User user) {
        return userService.update(id,user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable String id) {
        return userService.findById(id);
    }

    @PutMapping("/reset")
    public String get() {
        return userService.reset();
    }

    @GetMapping("/email/{em}")
    public List getbyemail(@PathVariable String em) {
        return (List) userService.findByEmail(em);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }

}
