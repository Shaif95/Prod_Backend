package com.example.demo.service;

import com.example.demo.domain.Award;
import com.example.demo.domain.Niche;
import com.example.demo.repository.AwardRepository;
import com.example.demo.repository.NicheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NicheService {

    @Autowired
    private NicheRepository nicheRepository;

    public Niche add(Niche niche) {
        return nicheRepository.save(niche);
    }

    public void delete(String id) {
        nicheRepository.deleteById(id);
    }

    public Map<String, Object> findWithPaging(Integer page, Integer size) {
        Page<Niche> niches = nicheRepository.findAll(PageRequest.of(page, size));

        Map<String, Object> nichesMap = new HashMap<>();
        nichesMap.put("awards", niches.getContent());
        nichesMap.put("pages", niches.getTotalPages());

        return nichesMap;
    }

    public Niche findById(String id) {
        return nicheRepository.findById(id).get();
    }

}
