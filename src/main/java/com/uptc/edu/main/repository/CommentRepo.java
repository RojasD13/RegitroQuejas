package com.uptc.edu.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uptc.edu.main.model.Comment;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByComplaintId(Long complaintId);
    List<Comment> findByComplaintIdOrderByDateDesc(Long complaintId);
}