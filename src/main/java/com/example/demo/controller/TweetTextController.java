package com.example.demo.controller;

import com.example.demo.domain.Award;
import com.example.demo.domain.TweetText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "https://tweetsy-frontend.herokuapp.com/")
@RequestMapping("/TweetText")
public class TweetTextController {

    @Autowired
    private com.example.demo.service.TweetTextService tweetTextService;

    @PostMapping
    public String addAward(@RequestBody TweetText tweetText) {

        return    tweetTextService.add(tweetText);
    }

    @GetMapping
    public Map<String, Object> getTweets(@RequestParam(defaultValue = "0") Integer page,
                                         @RequestParam(defaultValue = "30") Integer size) {

        return tweetTextService.findEventsWithPaging(page, size);
    }

    @PutMapping("/{id}")
    public TweetText updateAward(@PathVariable String id, @RequestBody TweetText tweetText) {
        return tweetTextService.update(id, tweetText);
    }

    @GetMapping("/pending")
    public List<TweetText> getPending() {
        return tweetTextService.findPending();
    }

    @GetMapping("/{id}")
    public TweetText getTweet(@PathVariable String id) {
        return tweetTextService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTweets(@PathVariable String id) {
        tweetTextService.delete(id);
    }

    @GetMapping("/email/{em}")
    public List getbyemail(@PathVariable String em) {
        return (List) tweetTextService.findByEmail(em);
    }

}
