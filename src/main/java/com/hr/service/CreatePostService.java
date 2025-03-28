package com.hr.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.hr.entity.CreatePost;

public interface CreatePostService {
	public CreatePost save(CreatePost createPost);
	public List<CreatePost> findAll(Sort by);
}
