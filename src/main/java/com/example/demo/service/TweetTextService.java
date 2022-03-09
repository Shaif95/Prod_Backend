package com.example.demo.service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.demo.domain.TweetText;
import com.example.demo.domain.User;
import com.example.demo.enums.TweetStatus;
import com.example.demo.repository.TweetRepository;
import com.example.demo.repository.TweetTextRepository;
import com.example.demo.utils.CustomBeanUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import com.example.demo.domain.Award;
import com.example.demo.repository.AwardRepository;

@Service
public class TweetTextService {

    @Autowired
    private TweetTextRepository tweetTextRepository;

    @Autowired
    private com.example.demo.service.usrService userService;


    @SneakyThrows
    public String add(TweetText tweetText) {

        TweetStatus status = TweetStatus.PENDING;

        tweetText.setStatus(status);

        String all = tweetText.getDate().substring(0,6);

        String dt = all + tweetText.getDate().substring(8,10);
        String tm = tweetText.getTime();



        String date = dt.substring(6,8) + "-" + dt.substring(3,5) + "-"  + dt.substring(0,2) + "T" + tm ;

        tweetText.setDatetime(date);

        tweetTextRepository.save(tweetText);

        return new String("done");

    }

    public Map<String, Object> findEventsWithPaging(Integer page, Integer size) {
        Page<TweetText> tweetTexts = tweetTextRepository.findAll(PageRequest.of(page, size));

        Map<String, Object> TweetTextMap = new HashMap<>();
        TweetTextMap.put("TweetText", tweetTexts.getContent());
        TweetTextMap.put("pages", tweetTexts.getTotalPages());

        return TweetTextMap;
    }

    public TweetText update(String id, TweetText updatedTweetText) {
        TweetText tweetText = tweetTextRepository.findById(id).get();

        CustomBeanUtils.copyNonNullProperties(updatedTweetText, tweetText);

        return tweetTextRepository.save(tweetText);
    }

    public TweetText findById(String id) {
        return tweetTextRepository.findById(id).get();
    }

    public List<TweetText> findPending() {
        return  tweetTextRepository.findByStatus(TweetStatus.PENDING);
    }

    public void delete(String id) {
        tweetTextRepository.deleteById(id);
    }

    public List<TweetText> findByEmail(String id) {
        return  tweetTextRepository.findByUser(id);
    }

}
