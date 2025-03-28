package com.hr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hr.entity.CreatePost;
import com.hr.repository.CreatePostRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CreatePostServiceImpl implements CreatePostService{
	
	@Autowired
	CreatePostRepository cpRepo;

	@Override
	public CreatePost save(CreatePost createPost) {
		CreatePost post= cpRepo.save(createPost);
		return post;
	}

	@Override
	public List<CreatePost> findAll(Sort by) {
		List<CreatePost> posts = cpRepo.findAll(Sort.by(Sort.Order.desc("postDate")));
		return posts;
	}

}
