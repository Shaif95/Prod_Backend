package com.example.demo.controller;

import com.example.demo.domain.Award;
import com.example.demo.domain.Niche;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/niche")
public class NicheController {

    @Autowired
    private com.example.demo.service.NicheService nicheService;

    @PostMapping
    public Niche add(@RequestBody Niche niche) {
        return nicheService.add(niche);
    }

    @GetMapping
    public Map<String, Object> getNiches(@RequestParam(defaultValue = "0") Integer page,
                                         @RequestParam(defaultValue = "20") Integer size) {
        return nicheService.findWithPaging(page, size);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        nicheService.delete(id);
    }


}
