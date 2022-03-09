package com.example.demo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "bugs")
public class Award {

	@Id
	private String id;
	private String title;            //username
	private String description;      //description
	private String date;

}
