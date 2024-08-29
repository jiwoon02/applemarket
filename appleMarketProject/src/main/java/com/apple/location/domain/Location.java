package com.apple.location.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "location")
public class Location {
	
	@Id
	@Column(name="location_id")
	private long locationID;
	
	@Column(name="location_name", nullable = false)
	private String locationName;
	
	
}
