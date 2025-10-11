package com.uptc.edu.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uptc.edu.main.dto.CommentDTO;
import com.uptc.edu.main.model.Comment;
import com.uptc.edu.main.model.Complaint;
import com.uptc.edu.main.repository.CommentRepo;
import com.uptc.edu.main.repository.ComplaintRepo;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepository;    
    @Autowired
    private ComplaintRepo complaintRepository;
    @Transactional

    public Comment addCommentToComplaint(Long complaintId, String content) {
        Complaint complaint = complaintRepository.findById(complaintId).orElseThrow(() -> new RuntimeException("Queja no encontrada"));
        Comment comment = new Comment(content, complaint);
        complaint.addComment(comment);
        return commentRepository.save(comment);
    }
    public List<Comment> getCommentsByComplaintId(Long complaintId) {
        return commentRepository.findByComplaintIdOrderByDateDesc(complaintId);
    }    
    public List<CommentDTO> getCommentResponsesByComplaintId(Long complaintId) {
        List<Comment> comments = getCommentsByComplaintId(complaintId);
        return comments.stream()
                .map(comment -> {
                    CommentDTO response = new CommentDTO();
                    response.setId(comment.getId());
                    response.setText(comment.getContent());
                    response.setTimestamp(comment.getDate());
                    return response;
                })
                .toList();
    }
    @Transactional
    public Comment updateComment(Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        
        comment.updateContent(newContent);
        return commentRepository.save(comment);
    }
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));        
        commentRepository.delete(comment);
    }
}