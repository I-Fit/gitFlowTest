package kr.co.ifit.api.service;

import kr.co.ifit.api.request.CommentReq;
import kr.co.ifit.api.response.CommentRes;
import kr.co.ifit.db.entity.Comment;
import kr.co.ifit.db.repository.PostRepository;
import kr.co.ifit.db.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 생성
    public CommentRes createComment(CommentReq commentReq) {
        Comment comment = new Comment(
                commentReq.getContent(),
                commentReq.getAuthor(),
                commentReq.getCreatedAt(),
                commentReq.getPostId()
        );

        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return new CommentRes(savedComment);
    }

    public void updateComment() {

    }

    public void deleteComment() {

    }
}
