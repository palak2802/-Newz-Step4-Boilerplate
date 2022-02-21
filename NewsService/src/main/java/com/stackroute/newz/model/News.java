package com.stackroute.newz.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Document
public class News {
	
	/*
	 * This class should have ten fields
	 * (newsId,title,author,description,publishedAt,content,url,urlToImage,Reminder,
	 * NewsSource). This class should also contain the getters and setters for the 
	 * fields along with the no-arg , parameterized	constructor and toString method.
	 * The value of publishedAt should not be accepted from the user but should be
	 * always initialized with the system date.
	 */
	@Id
	private Integer newsId;
	private String title;
	private String author;
	private String description;
	@JsonSerialize(using = ToStringSerializer.class)
	private LocalDateTime publishedAt;
	private String content;
	private String url;
	private String urlToImage;
	private Reminder reminder;
	private NewsSource newsSource;
	
	public News() {
		this.publishedAt = LocalDateTime.now();
	}
	
	public News(Integer newsId, String title, String author, String description, LocalDateTime publishedAt,
			String content, String url, String urlToImage, Reminder reminder, NewsSource newsSource) {
		super();
		this.newsId = newsId;
		this.title = title;
		this.author = author;
		this.description = description;
		this.content = content;
		this.url = url;
		this.urlToImage = urlToImage;
		this.reminder = reminder;
		this.newsSource = newsSource;
	}

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt() {
		this.publishedAt = LocalDateTime.now();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlToImage() {
		return urlToImage;
	}

	public void setUrlToImage(String urlToImage) {
		this.urlToImage = urlToImage;
	}

	public Reminder getReminder() {
		return reminder;
	}

	public void setReminder(Reminder reminder) {
		this.reminder = reminder;
	}

	public NewsSource getNewsSource() {
		return newsSource;
	}

	public void setNewssource(NewsSource newsSource) {
		this.newsSource = newsSource;
	}

	@Override
	public String toString() {
		return "News [newsId=" + newsId + ", title=" + title + ", author=" + author + ", description=" + description
				+ ", publishedAt=" + publishedAt + ", content=" + content + ", url=" + url + ", urlToImage="
				+ urlToImage + ", reminder=" + reminder + ", newsSource=" + newsSource + "]";
	}
	
}
