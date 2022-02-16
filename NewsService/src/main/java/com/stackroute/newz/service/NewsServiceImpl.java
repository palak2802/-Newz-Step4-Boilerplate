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
	@Override
	public boolean addNews(News news){
		if(newsRepo.existsById(news.getNewsId().toString()) == false) {
			newsRepo.insert(news);
			return true;
		}
		return false;
	}

	/* This method should be used to delete an existing news. */
	
	public boolean deleteNews(String userId, Integer newsId) {
		if(newsRepo.existsById(userId) == true && newsRepo.existsById(newsId.toString()) == true) {
			newsRepo.deleteById(userId);
			return true;
		}
		return false;
	}

	/* This method should be used to delete all news for a  specific userId. */
	
	public boolean deleteAllNews(String userId) throws NewsNotFoundException {
		Optional<News> allNewsToDelete = newsRepo.findById(userId);
		if(allNewsToDelete.isPresent()) {
			newsRepo.delete(allNewsToDelete.get());
			return true;
		}
		throw new NewsNotFoundException("Can not Delete the News. The news with user ID: "+userId+ " does not exists in the database.");
	}

	/*
	 * This method should be used to update a existing news.
	 */

	public News updateNews(News news, int newsId, String userId) throws NewsNotFoundException {
		if(newsRepo.existsById(userId) == true) {
			Optional<News> newsToUpdate = newsRepo.findById(userId);
			newsToUpdate.get().setAuthor(news.getAuthor());
			newsToUpdate.get().setContent(news.getContent());
			newsToUpdate.get().setDescription(news.getDescription());
			newsToUpdate.get().setTitle(news.getTitle());
			newsToUpdate.get().setUrl(news.getUrl());
			newsToUpdate.get().setUrlToImage(news.getUrlToImage());
			newsToUpdate.get().setReminder(news.getReminder());
			return newsRepo.save(newsToUpdate.get());
		}
		throw new NewsNotFoundException("Can not Update the News. The news with user ID: "+userId+ " and news ID: "+newsId+" does not exists in the database.");
	}

	/*
	 * This method should be used to get a news by newsId created by specific user
	 */

	public News getNewsByNewsId(String userId, int newsId) throws NewsNotFoundException {
		if(newsRepo.existsById(userId) == true) {
			Optional<News> news = newsRepo.findById(userId);
			return news.get();
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
