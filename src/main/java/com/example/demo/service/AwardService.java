package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.utils.CustomBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Award;
import com.example.demo.repository.AwardRepository;

@Service
public class AwardService {

	@Autowired
	private AwardRepository awardRepository;

	public Award add(Award award) {
		return awardRepository.save(award);
	}

	public Map<String, Object> findEventsWithPaging(Integer page, Integer size) {
		Page<Award> awards = awardRepository.findAll(PageRequest.of(page, size));
		
		Map<String, Object> awardsMap = new HashMap<>();
		awardsMap.put("awards", awards.getContent());
		awardsMap.put("pages", awards.getTotalPages());

		return awardsMap;
	}

	public Award update(String id, Award updatedAward) {
		Award award = awardRepository.findById(id).get();

		CustomBeanUtils.copyNonNullProperties(updatedAward, award);

		return awardRepository.save(award);
	}

	public Award findById(String id) {
		return awardRepository.findById(id).get();
	}

	public void delete(String id) {
		awardRepository.deleteById(id);
	}

}
