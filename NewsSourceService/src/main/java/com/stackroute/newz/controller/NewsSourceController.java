package com.stackroute.newz.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.stackroute.newz.model.NewsSource;
import com.stackroute.newz.service.NewsSourceService;
import com.stackroute.newz.util.exception.NewsSourceNotFoundException;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
@RequestMapping("/api/v1/newssource")
public class NewsSourceController {

	/*
	 * Autowiring should be implemented for the NewsService. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword
	 */
	private NewsSourceService newsSourceService;
	@Autowired
	public NewsSourceController(NewsSourceService newsSourceService) {
		this.newsSourceService = newsSourceService;
	}
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	/*
	 * Define a handler method which will create a specific newssource by reading the
	 * Serialized object from request body and save the newssource details in the
	 * database.This handler method should return any one of the status messages
	 * basis on different situations: 
	 * 1. 201(CREATED) - If the newssource created successfully. 
	 * 2. 409(CONFLICT) - If the newssourceId conflicts with any existing user.
	 * 
	 * This handler method should map to the URL "/api/v1/newssource" using HTTP POST method
	 */
	@PostMapping
	public ResponseEntity<NewsSource> createNewsSource(@RequestBody NewsSource newsSource){
		Boolean isNewsSourceAdded = newsSourceService.addNewsSource(newsSource);
		if(isNewsSourceAdded == true) {
			logger.info("In controller - {}", "News Source created: " +newsSource);
			return new ResponseEntity<NewsSource>(newsSource, HttpStatus.CREATED);
		}
		logger.info("In controller - {}", "News ID "+ newsSource.getNewsSourceId() + " already exists.");
		return new ResponseEntity<NewsSource>(HttpStatus.CONFLICT);
	}


	/*
	 * Define a handler method which will delete a newssource from a database.
	 * This handler method should return any one of the status messages basis 
	 * on different situations: 
	 * 1. 200(OK) - If the newssource deleted successfully from database. 
	 * 2. 404(NOT FOUND) - If the newssource with specified newsId is not found.
	 *
	 * This handler method should map to the URL "/api/v1/newssource/{newssourceId}" 
	 * using HTTP Delete method where "userId" should be replaced by a valid userId 
	 * without {} and "newssourceId" should be replaced by a valid newsId 
	 * without {}.
	 * 
	 */
	@DeleteMapping("/{newssourceId}")
	public ResponseEntity<NewsSource> deleteNewsSource(@PathVariable("newssourceId") Integer newssourceId){
		boolean newsSourceDeleted = newsSourceService.deleteNewsSource(newssourceId);
		if(newsSourceDeleted == true) {
			logger.info("In controller - {}", "News Source deleted for Id - " +newssourceId);
			return new ResponseEntity<NewsSource>(HttpStatus.OK);
		}
		logger.info("In controller - {}", "News not found for news Id - " +newssourceId);
		return new ResponseEntity<NewsSource>(HttpStatus.NOT_FOUND);
	}

	/*
	 * Define a handler method which will update a specific newssource by reading the
	 * Serialized object from request body and save the updated newssource details in a
	 * database. This handler method should return any one of the status messages
	 * basis on different situations: 
	 * 1. 200(OK) - If the newssource updated successfully.
	 * 2. 404(NOT FOUND) - If the newssource with specified newssourceId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/newssource/{newssourceId}" using 
	 * HTTP PUT method where "newssourceId" should be replaced by a valid newssourceId
	 * without {}.
	 * 
	 */
	@PutMapping("/{newssourceId}")
	public ResponseEntity<NewsSource> updateNewsSource(@PathVariable("newssourceId") Integer newssourceId, @RequestBody NewsSource newsSource){
		NewsSource updateNewsSource;
		try {
			updateNewsSource = newsSourceService.updateNewsSource(newsSource, newssourceId);
			if(updateNewsSource != null) {
				logger.info("In controller - {}", "News updated for news Id - " +newssourceId + " is: " +updateNewsSource);
				return new ResponseEntity<NewsSource>(updateNewsSource, HttpStatus.OK);
			}
		} catch (NewsSourceNotFoundException e) {
			logger.info("In controller - {}", "News Source not found for Id - " +newssourceId);
			return new ResponseEntity<NewsSource>(HttpStatus.NOT_FOUND);
		}
		logger.info("In controller - {}", "News Source not found for Id - " +newssourceId);
		return new ResponseEntity<NewsSource>(HttpStatus.NOT_FOUND);
	}
	
	/*
	 * Define a handler method which will get us the specific newssource by a userId.
	 * This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the newssource found successfully. 
	 * 2. 404(NOT FOUND) - If the newssource with specified newsId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/newssource/{userId}/{newssourceId}" 
	 * using HTTP GET method where "userId" should be replaced by a valid userId 
	 * without {} and "newssourceId" should be replaced by a valid newsId without {}.
	 * 
	 */
	@GetMapping("/{userId}/{newssourceId}")
	public ResponseEntity<NewsSource> getNewsSourceById(@PathVariable("userId") String userId, @PathVariable("newssourceId") Integer newssourceId){
		NewsSource newsSourceById;
		try {
			newsSourceById = newsSourceService.getNewsSourceById(userId, newssourceId);
			if(newsSourceById != null) {
				logger.info("In controller - {}", "The news Source for Id - " +newssourceId+ " is: "+newsSourceById);
				return new ResponseEntity<NewsSource>(newsSourceById, HttpStatus.OK);
			}
		} catch (NewsSourceNotFoundException e) {
			logger.info("In controller - {}", "News Source ID "+newssourceId+ " not Found.");
			return new ResponseEntity<NewsSource>(HttpStatus.NOT_FOUND);
		}
		logger.info("In controller - {}", "News Source ID "+newssourceId+ " not Found.");
		return new ResponseEntity<NewsSource>(HttpStatus.NOT_FOUND);
	}
	
	/*
	 * Define a handler method which will show details of all newssource created by specific 
	 * user. This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the newssource found successfully. 
	 * 2. 404(NOT FOUND) - If the newssource with specified newsId is not found.
	 * This handler method should map to the URL "/api/v1/newssource/{userId}" using HTTP GET method
	 * where "userId" should be replaced by a valid userId without {}.
	 * 
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<List<NewsSource>> getAllNewsSource(@PathVariable("userId") String userId){
		List<NewsSource> allNewsSource = newsSourceService.getAllNewsSourceByUserId(userId);
		logger.info("In controller - {}", "List of all news Source By User Id: "+userId+ "is: "+allNewsSource);
		return new ResponseEntity<List<NewsSource>>(allNewsSource, HttpStatus.OK);
	}
}
