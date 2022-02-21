package com.stackroute.newz.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Document
public class Reminder {

	/*
	 * This class should have two fields(reminderId,schedule).
	 * This class should also contain the getters and setters for the 
	 * fields along with the parameterized	constructor and toString method.
	 * The value of newssourceCreationDate should not be accepted from the user but should be
	 * always initialized with the system date.
	 */
	@Id
	private String reminderId;
	@JsonSerialize(using = ToStringSerializer.class)
	private LocalDateTime schedule;
	
	public Reminder() {
		super();
		this.schedule = LocalDateTime.now();
	}
	
	public Reminder(String reminderId, LocalDateTime schedule) {
		super();
		this.reminderId = reminderId;
	}

	public String getReminderId() {
		return reminderId;
	}

	public void setReminderId(String reminderId) {
		this.reminderId = reminderId;
	}

	public LocalDateTime getSchedule() {
		return schedule;
	}

	public void setSchedule() {
		this.schedule = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "Reminder [reminderId=" + reminderId + ", schedule=" + schedule + "]";
	}
	
}
