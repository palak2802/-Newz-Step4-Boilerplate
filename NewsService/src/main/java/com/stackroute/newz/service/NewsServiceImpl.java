package com.stackroute.newz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.newz.model.News;
import com.stackroute.newz.model.UserNews;
import com.stackroute.newz.repository.NewsRepository;
import com.stackroute.newz.util.exception.NewsNotFoundException;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */

@Service
public class NewsServiceImpl implements NewsService {

	/*
	 * Autowiring should be implemented for the NewsDao and MongoOperation.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	
	private NewsRepository newsRepo;
	
	@Autowired
	public NewsServiceImpl(NewsRepository newsRepository) {
		this.newsRepo = newsRepository;
	}

	/*
	 * This method should be used to save a new news.
	 */
	
	@Override
	public boolean addNews(News news){
		Boolean isExistsUserNews = newsRepo.existsById(news.getAuthor());
		if(isExistsUserNews == false) {
			List<News> newsList = new ArrayList<News>();
			UserNews userNews = new UserNews();
				newsList.add(news);
				userNews.setUserId(news.getAuthor());
				userNews.setNewslist(newsList);
				UserNews userAdded = newsRepo.insert(userNews);
				if(userAdded != null) {
					return true;
				}
			}
		return false;
	}

	/* This method should be used to delete an existing news. */
	
	public boolean deleteNews(String userId, int newsId) {
		UserNews userNews = newsRepo.findById(userId).get();
		List<News> newsList = userNews.getNewslist();
		for(News newsWithId:newsList) {
			if(newsWithId.getNewsId().equals(newsId)) {
			newsRepo.deleteById(userNews.getUserId());
			return true;
			}
		}
		return false;
	}

	/* This method should be used to delete all news for a  specific userId. */
	
	public boolean deleteAllNews(String userId) throws NewsNotFoundException  {
		try {
		UserNews userNews = newsRepo.findById(userId).get();
		List<News> newsList = userNews.getNewslist();
		if(newsList != null) {
			newsRepo.delete(userNews);
			return true;
		}}
		catch(NoSuchElementException ex) {
			throw new NewsNotFoundException("Can not Delete the News. The news with user ID: "+userId+ " does not exists in the database.");
		}
		return false;
	}

	/*
	 * This method should be used to update a existing news.
	 */

	public News updateNews(News news, int newsId, String userId) throws NewsNotFoundException {
		try {
		UserNews userNews = newsRepo.findById(userId).get();
		List<News> newsList = userNews.getNewslist();
		for(News newsWithId:newsList) {
			if(newsWithId.getNewsId().equals(newsId)) {
				newsWithId.setAuthor(news.getAuthor());
				newsWithId.setContent(news.getContent());
				newsWithId.setDescription(news.getDescription());
				newsWithId.setTitle(news.getTitle());
				newsWithId.setUrl(news.getUrl());
				newsWithId.setUrlToImage(news.getUrlToImage());
				newsWithId.setReminder(news.getReminder());
				newsList.add(newsWithId);
				userNews.setNewslist(newsList);
				newsRepo.save(userNews);
				return newsWithId;
			}
		}}
		catch(NoSuchElementException ex) {
			throw new NewsNotFoundException("Can not Update the News. The news with user ID: "+userId+ " and news ID: "+newsId+" does not exists in the database.");
		}
		return null;
	}

	/*
	 * This method should be used to get a news by newsId created by specific user
	 */

	public News getNewsByNewsId(String userId, int newsId) throws NewsNotFoundException {
		try {
		UserNews userNews = newsRepo.findById(userId).get();
		List<News> newsList = userNews.getNewslist();
		for(News newsWithId:newsList) {
			if(newsWithId.getNewsId().equals(newsId)){
			return newsWithId;
			}}
		}catch(NoSuchElementException e) {
			throw new NewsNotFoundException("Can not Retrieve the News. The news with user ID: "+userId+ " and news  ID: "+newsId +" does not exists in the database.");
		}
		return null;
	}

	/*
	 * This method should be used to get all news for a specific userId.
	 */

	public List<News> getAllNewsByUserId(String userId) {
		return newsRepo.findById(userId).get().getNewslist();
	}

}
