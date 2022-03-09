package com.example.demo.controller;

import com.example.demo.domain.Award;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "https://tweetsy-frontend.herokuapp.com/")
@RequestMapping("/awards")
public class AwardController {

	@Autowired
	private com.example.demo.service.AwardService awardService;

	@PostMapping
	public Award addAward(@RequestBody Award award) {
		return awardService.add(award);
	}

	@GetMapping
	public Map<String, Object> getAwards(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "20") Integer size) {
		return awardService.findEventsWithPaging(page, size);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public Award updateAward(@PathVariable String id, @RequestBody Award award) {
		return awardService.update(id, award);
	}

	@GetMapping("/{id}")
	public Award getEvent(@PathVariable String id) {
		return awardService.findById(id);
	}

	@DeleteMapping("/{id}")
	public void deleteEvent(@PathVariable String id) {
		awardService.delete(id);
	}

}
