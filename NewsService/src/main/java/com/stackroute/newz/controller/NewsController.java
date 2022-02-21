package com.stackroute.newz.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.newz.model.News;
import com.stackroute.newz.model.UserNews;
import com.stackroute.newz.service.NewsService;
import com.stackroute.newz.util.exception.NewsNotFoundException;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

	/*
	 * Autowiring should be implemented for the NewsService. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword
	 */
	@Autowired
	private NewsService newsService;
	
	@Autowired
	public NewsController(NewsService newsService) {
		this.newsService = newsService;
	}
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/*
	 * Define a handler method which will create a specific news by reading the
	 * Serialized object from request body and save the news details in the
	 * database.This handler method should return any one of the status messages
	 * basis on different situations: 
	 * 1. 201(CREATED) - If the news created successfully. 
	 * 2. 409(CONFLICT) - If the newsId conflicts with any existing user.
	 * 
	 * This handler method should map to the URL "/api/v1/news" using HTTP POST method
	 */
	@PostMapping
	public ResponseEntity<News> createNews(@RequestBody News news){
		Boolean isNewsExists = newsService.addNews(news);
		if(isNewsExists == true) {
			logger.info("In controller - {}", "News created: " +news);
			return new ResponseEntity<News>(news, HttpStatus.CREATED);
		}
		logger.info("In controller - {}", "News ID "+ news.getNewsId() + " already exists.");
		return new ResponseEntity<News>(HttpStatus.CONFLICT);
	}

	
	/*
	 * Define a handler method which will delete a news from a database.
	 * This handler method should return any one of the status messages basis 
	 * on different situations: 
	 * 1. 200(OK) - If the news deleted successfully from database. 
	 * 2. 404(NOT FOUND) - If the news with specified newsId is not found.
	 *
	 * This handler method should map to the URL "/api/v1/news/{userId}/{newsId}" 
	 * using HTTP Delete method where "userId" should be replaced by a valid userId 
	 * without {} and "newsId" should be replaced by a valid newsId 
	 * without {}.
	 * 
	 */
	@DeleteMapping("/{userId}/{newsId}")
	public ResponseEntity<News> deleteNewsById(@PathVariable("userId") String userId, @PathVariable("newsId") Integer newsId){
		Boolean isNewsDeleted = newsService.deleteNews(userId, newsId);
		if(isNewsDeleted == true) {
			logger.info("In controller - {}", "News deleted for user ID: "+userId+ " and news ID: " +newsId);
			return new ResponseEntity<News>(HttpStatus.OK);
		}
		logger.info("In controller - {}", "News not found for user ID: "+userId+ " and news ID: " +newsId);
		return new ResponseEntity<News>(HttpStatus.NOT_FOUND);
	}
	
	/*
	 * Define a handler method which will delete all the news of a specific user from 
	 * a database. This handler method should return any one of the status messages 
	 * basis on different situations: 
	 * 1. 200(OK) - If the newsId deleted successfully from database. 
	 * 2. 404(NOT FOUND) - If the note with specified newsId is not found.
	 *
	 * This handler method should map to the URL "/api/v1/news/{userId}" 
	 * using HTTP Delete method where "userId" should be replaced by a valid userId 
	 * without {} and "newsid" should be replaced by a valid newsId 
	 * without {}.
	 * 
	 */
	@DeleteMapping("/{userId}")
	public ResponseEntity<News> deleteNews(@PathVariable("userId") String userId){
		try {
			Boolean isNewsDeleted = newsService.deleteAllNews(userId);
			if(isNewsDeleted == true) {
				logger.info("In controller - {}", "All News deleted for User ID - " +userId);
				return new ResponseEntity<News>(HttpStatus.OK);
			}
		}
		catch (NewsNotFoundException e) {
			logger.info("In controller - {}", "News not found for User ID - " +userId);
			return new ResponseEntity<News>(HttpStatus.NOT_FOUND);
		}
		logger.info("In controller - {}", "News not found for User ID - " +userId);
		return new ResponseEntity<News>(HttpStatus.NOT_FOUND);
	}
	
	/*
	 * Define a handler method which will update a specific news by reading the
	 * Serialized object from request body and save the updated news details in a
	 * database. 
	 * This handler method should return any one of the status messages
	 * basis on different situations: 
	 * 1. 200(OK) - If the news updated successfully.
	 * 2. 404(NOT FOUND) - If the news with specified newsId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/news/{userId}/{newsId}" using 
	 * HTTP PUT method where "userId" should be replaced by a valid userId 
	 * without {} and "newsid" should be replaced by a valid newsId without {}.
	 * 
	 */
	@PutMapping("/{userId}/{newsId}")
	public ResponseEntity<News> updateNews(@PathVariable("userId") String userId, @PathVariable("newsId") Integer newsId, @RequestBody News news) 
		{
		try {
			News newsUpdated = newsService.updateNews(news, newsId, userId);
			if(newsUpdated != null) {
				logger.info("In controller - {}", "News updated for User ID: "+userId+ " and news ID: " +newsId + " is: " +news);
				return new ResponseEntity<News>(newsUpdated, HttpStatus.OK);
			}
		} catch (NewsNotFoundException e) {
			logger.info("In controller - {}", "News not found for User ID: "+userId+ " and news ID: " +newsId);
			return new ResponseEntity<News>(HttpStatus.NOT_FOUND);
		}
		logger.info("In controller - {}", "News not found for User ID: "+userId+ " and news ID: " +newsId);
		return new ResponseEntity<News>(HttpStatus.NOT_FOUND);
	}
	
	/*
	 * Define a handler method which will get us the specific news by a userId.
	 * This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the news found successfully. 
	 * 2. 404(NOT FOUND) - If the news with specified newsId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/news/{userId}/{newsId}" 
	 * using HTTP GET method where "userId" should be replaced by a valid userId 
	 * without {} and "newsid" should be replaced by a valid newsId without {}.
	 * 
	 */
	@GetMapping("/{userId}/{newsId}")
	public ResponseEntity<News> getNewsById(@PathVariable("userId") String userId, @PathVariable("newsId") Integer newsId){
		News newsById;
		try {
			newsById = newsService.getNewsByNewsId(userId, newsId);
			if(newsById != null) {
				logger.info("In controller - {}", "The news for User ID: "+userId+ " and news ID: " +newsId+ " is: "+newsById);
				return new ResponseEntity<News>(newsById, HttpStatus.OK);
			}
		} catch (NewsNotFoundException e) {
			logger.info("In controller - {}", "News ID "+newsId+ " not Found.");
			return new ResponseEntity<News>(HttpStatus.NOT_FOUND);
		}
		logger.info("In controller - {}", "News ID "+newsId+ " not Found.");
		return new ResponseEntity<News>(HttpStatus.NOT_FOUND);
	}

	/*
	 * Define a handler method which will show details of all news created by specific 
	 * user. This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the news found successfully. 
	 * 2. 404(NOT FOUND) - If the news with specified newsId is not found.
	 * This handler method should map to the URL "/api/v1/news/{userId}" using HTTP GET method
	 * where "userId" should be replaced by a valid userId without {}.
	 * 
	 */
	 
	@GetMapping("/{userId}")
	public ResponseEntity<List<News>> getAllNewsByUserId(@PathVariable("userId") String userId){
		List<News> allNews = newsService.getAllNewsByUserId(userId);
		if(allNews != null) {
			logger.info("In controller - {}", "List of all news: "+allNews);
			return new ResponseEntity<List<News>>(allNews, HttpStatus.OK);
		}
		else {
			logger.info("In controller - {}", "User ID "+userId+ " not Found.");
			return new ResponseEntity<List<News>>(HttpStatus.NOT_FOUND);
		}
	}

}
