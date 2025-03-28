package com.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hr.entity.CreatePost;

@Repository
public interface CreatePostRepository extends JpaRepository<CreatePost, Integer>{

}
