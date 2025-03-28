package com.hr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import com.hr.entity.CreatePost;
import com.hr.repository.CreatePostRepository;

public class CreatePostServiceTest {

	@Mock
	CreatePostRepository cpRepo;
	
	@InjectMocks
	CreatePostServiceImpl createService;
	
	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testSave()
	{
		CreatePost crPost = new CreatePost();
		crPost.setPostId(1);
		crPost.setPostTitle("General Info about USA");
		crPost.setPostDiscription("Known for its vast landscapes, the U.S. is home to the world's largest economy");
		
		// Use LocalDateTime to set the postDate
        LocalDateTime postDate = LocalDateTime.now();
        crPost.setPostDate(postDate);
		
		when(cpRepo.save(crPost)).thenReturn(crPost);
		
		CreatePost savePost = createService.save(crPost);
		
		verify(cpRepo).save(crPost);
		
		assertEquals(crPost, savePost);
	}
	
	@Test
	void testFindAll() {
	    // Arrange: Create some posts with LocalDateTime values
	    List<CreatePost> listOfPost = new ArrayList<>();

	    CreatePost post1 = new CreatePost(1, "Post 1", "Description 1", LocalDateTime.parse("2025-02-10T00:00:00"));
	    CreatePost post2 = new CreatePost(2, "Post 2", "Description 2", LocalDateTime.parse("2024-03-15T00:00:00"));
	    CreatePost post3 = new CreatePost(3, "Post 3", "Description 3", LocalDateTime.parse("2025-01-20T00:00:00"));

	    listOfPost.add(post1);
	    listOfPost.add(post2);
	    listOfPost.add(post3);

	    // Manually sort the list by postDate in descending order
	    listOfPost.sort((p1, p2) -> p2.getPostDate().compareTo(p1.getPostDate()));

	    // Mock the repository's findAll method with sorting
	    when(cpRepo.findAll(Sort.by(Sort.Order.desc("postDate")))).thenReturn(listOfPost);

	    // Act: Call the service method to find all posts
	    List<CreatePost> sortedPostsList = createService.findAll(Sort.by(Sort.Order.desc("postDate")));

	    // Verify that the repository's findAll method was called with sorting
	    verify(cpRepo).findAll(Sort.by(Sort.Order.desc("postDate")));

	    // Assert: Verify that the posts are sorted by postDate in descending order
	    assertEquals(listOfPost, sortedPostsList);  // The lists should now match
	}

}
