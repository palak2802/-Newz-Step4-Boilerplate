package com.stackroute.newz.service;

import java.util.List;
import java.util.Optional;

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
	
	@Autowired
	private NewsRepository newsRepo;
	@Autowired
	public NewsServiceImpl(NewsRepository newsRepository) {
		this.newsRepo = newsRepository;
	}

	/*
	 * This method should be used to save a new news.
	 */
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean addNews(News news){
		UserNews userNews = new UserNews();
		List<News> newsList = userNews.getNewslist();
		News newsWithId = newsList.stream().filter(id -> news.getNewsId().equals(id)).findAny().orElse(null);
		if(newsWithId == null) {
			newsList.add(news);
			userNews.setNewslist(newsList);
			newsRepo.save(userNews);
			return true;
		}
		return false;
	}

	/* This method should be used to delete an existing news. */
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean deleteNews(String userId, int newsId) {
		UserNews userNews = newsRepo.findById(userId).get();
		List<News> newsList = userNews.getNewslist();
		News newsWithId = newsList.stream().filter(id -> Integer.valueOf(newsId).equals(id)).findAny().orElse(null);
		if(newsWithId != null) {
			newsRepo.deleteById(userNews.getUserId());
			return true;
		}
		return false;
	}

	/* This method should be used to delete all news for a  specific userId. */
	
	public boolean deleteAllNews(String userId) throws NewsNotFoundException {
		UserNews userNews = newsRepo.findById(userId).get();
		List<News> newsList = userNews.getNewslist();
		if(newsList != null) {
			newsRepo.delete(userNews);
			return true;
		}
		throw new NewsNotFoundException("Can not Delete the News. The news with user ID: "+userId+ " does not exists in the database.");
	}

	/*
	 * This method should be used to update a existing news.
	 */

	@SuppressWarnings("unlikely-arg-type")
	public News updateNews(News news, int newsId, String userId) throws NewsNotFoundException {
		UserNews userNews = newsRepo.findById(userId).get();
		List<News> newsList = userNews.getNewslist();
		News newsWithId = newsList.stream().filter(id -> Integer.valueOf(newsId).equals(id)).findAny().orElse(null);
		if(newsWithId != null) {
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
		throw new NewsNotFoundException("Can not Update the News. The news with user ID: "+userId+ " and news ID: "+newsId+" does not exists in the database.");
	}

	/*
	 * This method should be used to get a news by newsId created by specific user
	 */

	public News getNewsByNewsId(String userId, int newsId) throws NewsNotFoundException {
		UserNews userNews = newsRepo.findById(userId).get();
		List<News> newsList = userNews.getNewslist();
		News newsWithId = newsList.stream().filter(id -> Integer.valueOf(newsId).equals(id)).findAny().orElse(null);
		if(newsWithId != null) {
			return newsWithId;
		}
		throw new NewsNotFoundException("Can not Retrieve the News. The news with user ID: "+userId+ " and news  ID: "+newsId +" does not exists in the database.");
	}

	/*
	 * This method should be used to get all news for a specific userId.
	 */

	public List<News> getAllNewsByUserId(String userId) {
		return newsRepo.findById(userId).get().getNewslist();
	}

}
