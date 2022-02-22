package com.stackroute.newz.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.newz.model.NewsSource;
import com.stackroute.newz.repository.NewsSourceRepository;
import com.stackroute.newz.util.exception.NewsSourceNotFoundException;

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
public class NewsSourceServiceImpl implements NewsSourceService {

	/*
	 * Autowiring should be implemented for the NewsDao and MongoOperation.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	private NewsSourceRepository newsSourceRepo;
	@Autowired
	public NewsSourceServiceImpl(NewsSourceRepository repo) {
		this.newsSourceRepo = repo;
	}
	
	/*
	 * This method should be used to save a newsSource.
	 */
	@Override
	public boolean addNewsSource(NewsSource newsSource) {
		NewsSource newsSourceAdded;
		if(newsSourceRepo.existsById(newsSource.getNewsSourceId()) == false) {
			newsSourceAdded = newsSourceRepo.insert(newsSource);
			if(newsSourceAdded != null) {
				return true;
			}
		}
		return false;
	}

	/* This method should be used to delete an existing newsSource. */

	@Override
	public boolean deleteNewsSource(int newsSourceId) {
		try {
		Optional<NewsSource> newsSource = newsSourceRepo.findById(newsSourceId);
		NewsSource newsSourceAdded = newsSourceRepo.save(newsSource.get());
		if(newsSourceAdded != null) {
			newsSourceRepo.deleteById(newsSourceId);
			return true;
			}
		}
		catch(Exception e) {
			return false;
		}
		return false;
	}

	/* This method should be used to update an existing newsSource. */
	
	@Override
	public NewsSource updateNewsSource(NewsSource newsSource, int newsSourceId) throws NewsSourceNotFoundException {
		NewsSource newsSourceToUpdate = newsSourceRepo.findById(newsSourceId).get();
		if(newsSourceToUpdate != null) {
			newsSourceToUpdate.setNewsSourceCreatedBy(newsSource.getNewsSourceCreatedBy());
			newsSourceToUpdate.setNewsSourceDesc(newsSource.getNewsSourceDesc());
			newsSourceToUpdate.setNewsSourceName(newsSource.getNewsSourceName());
			newsSourceRepo.save(newsSourceToUpdate);
			return newsSourceToUpdate;
		}
		throw new NewsSourceNotFoundException("Can not Update the News Source. The news source with ID: "+newsSourceId+" already exists in the database.");
	}

	/* This method should be used to get a specific newsSource for an user. */

	@Override
	public NewsSource getNewsSourceById(String userId, int newsSourceId) throws NewsSourceNotFoundException {
		try {
		List<NewsSource> newsSourceList = newsSourceRepo.findAllNewsSourceByNewsSourceCreatedBy(userId);
		if(!newsSourceList.isEmpty()) {
			for(NewsSource newsSource : newsSourceList) {
				if(newsSource.getNewsSourceId() == newsSourceId) {
					return newsSource;
				}
			}
		}}
		catch(NoSuchElementException e) {
			return null;
		}
		return null;
	}
	
	 /* This method should be used to get all newsSource for a specific userId.*/

	@Override
	public List<NewsSource> getAllNewsSourceByUserId(String createdBy) {
		return newsSourceRepo.findAllNewsSourceByNewsSourceCreatedBy(createdBy);
	}

}
