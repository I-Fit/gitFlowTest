package kr.co.ifit.api.service;

import kr.co.ifit.api.request.LikeDtoReq;
import kr.co.ifit.api.request.PostDtoReq;
import kr.co.ifit.api.response.PostDtoRes;
import kr.co.ifit.common.util.UserContextUtil;
import kr.co.ifit.db.entity.Like;
import kr.co.ifit.db.entity.Post;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.CommentRepository;
import kr.co.ifit.db.repository.LikeRepository;
import kr.co.ifit.db.repository.PostRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserContextUtil userContextUtil;

    // 게시글 생성
    public PostDtoRes createPost(PostDtoReq postReq, String imageStr) {
        // 사용자 검증
        User user = userRepository.findById(postReq.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // post 엔티티 생성
        Post post = new Post(
                postReq.getTitle(),
                postReq.getContent(),
                imageStr,
                postReq.getExercise(),
                postReq.getLocation(),
                user
        );
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        return new PostDtoRes(savedPost);
    }

    // 게시글 상세
    // 정상 코드
//    public PostDtoRes getPost(Long postId, Long userId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("Post not found"));
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//
//        boolean isHeartFilled = false;
//        if (user != null) {
//            System.out.println("유저아이디: " + userId);
//            isHeartFilled = likeRepository.existsByUserAndPost(user, post);
//        }
//
//        return new PostDtoRes(post, isHeartFilled);
//    }

    public PostDtoRes getPost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));

        boolean isHeartFilled = false;

        if (user != null) {
//            profileUrl = user.getProfileUrl();
            // 사용자가 존재하는지 확인하고, 그에 따라 isHeartFilled 설정
            isHeartFilled = likeRepository.existsByUserAndPost(user, post);
        }
        return new PostDtoRes(post, isHeartFilled);
    }

    // 게시글 수정
    public boolean updatePost(Long id, PostDtoReq postReq, String base64Image) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getUserId().equals(postReq.getUserId())) {
            throw new RuntimeException("You do not have permission to update this post");
        }

        post.setTitle(postReq.getTitle());
        post.setContent(postReq.getContent());
        post.setExercise(postReq.getExercise());
        post.setLocation(postReq.getLocation());
        post.setUpdatedAt(LocalDateTime.now());
        post.setImageStr(base64Image);

        postRepository.save(post);
        return true;
    }

    // 게시글 삭제
//    @Transactional
    public boolean deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        likeRepository.deleteByPost(post);
        commentRepository.deleteByPost(post);

        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("You do not have permission to delete this post");
        }

        postRepository.delete(post);
        return true;
    }

    // 게시글 목록
    // 정상코드
//    public List<PostDtoRes> getAllPosts() {
//        List<Post> posts = postRepository.findAll();
//        return posts.stream().map(PostDtoRes::new).toList();
//    }

    public List<PostDtoRes> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostDtoRes::new).toList();
//        return posts.stream().map(post -> {
//            // PostDtoRes 생성 시 username 설정
//            User user = post.getUser(); // 게시글 작성자 정보 가져오기
//            PostDtoRes dto = new PostDtoRes(post);
//            dto.setUsername(user.getUsername()); // username 설정
//            return dto;
//        }).toList();
    }

    // 게시글 좋아요
    public void likePost(LikeDtoReq likeReq) {
        System.out.println("좋아요 생성: " + likeReq);
        User user = userRepository.findById(likeReq.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(likeReq.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (likeRepository.existsByUserAndPost(user, post)) {
            unlikePost(likeReq);
            return;
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);

        likeRepository.save(like);

        post.setHeartFilled(true);
        post.setLikesCnt(post.getLikesCnt() + 1);
        postRepository.save(post);
    }

    public void unlikePost(LikeDtoReq likeReq) {
        System.out.println("좋아요 취소: " + likeReq);
        User user = userRepository.findById(likeReq.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(likeReq.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Like like = likeRepository.findByUserAndPost(user, post);
        if (like != null) {
            likeRepository.delete(like);

            post.setHeartFilled(false);
            post.setLikesCnt(post.getLikesCnt() - 1);
            postRepository.save(post);
        } else {
            throw new RuntimeException("좋아요한 게시글이 없습니다");
        }
    }

//    public PostDtoRes likePost(Long id, UserPrincipal currentUser) {
//        User user = (User)currentUser;
//        Post post = postRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Post not found"));
//
//        boolean alreadyLiked = checkIfUserLikedPost(user, post);
//
//        int likesCnt = post.getLikesCnt();
//        boolean isHeartFilled;
//
//        if (alreadyLiked) {
//            post.setLikesCnt(likesCnt - 1);
//            isHeartFilled = false;
//            removeLikeForUser(user, post);
//        } else {
//            post.setLikesCnt(likesCnt + 1);
//            isHeartFilled = true;
//            addLikeForUser(user, post);
//        }
//
//        postRepository.save(post);
//
//        return new PostDtoRes(post.getLikesCnt(), isHeartFilled);
//    }

    private boolean checkIfUserLikedPost(User user, Post post) {
        return likeRepository.existsByUserAndPost(user, post);
    }

    private void addLikeForUser(User user, Post post) {
        System.out.println("유저 객체 : " + user);
//        System.out.println("유저아이디 : " + user.getUserId());
//        System.out.println("게시글 아이디: " + post.getPostId());
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);
    }

    private void removeLikeForUser(User user, Post post) {
//        Like like = likeRepository.findByUserAndPost(user, post);
//        if (like != null) {
//            likeRepository.delete(like);
//        }
        likeRepository.deleteByUserAndPost(user, post);
    }

    // 키워드로 게시글 검색
    public List<PostDtoRes> searchByKeyword(String keyword) {
        List<Post> posts = postRepository.searchByKeyword(keyword);
        return posts.stream().map(PostDtoRes::new).toList();
//        return posts.stream().map(PostDtoRes::convertToDto).collect(Collectors.toList());
    }

    // 운동명으로 게시글 검색
    public List<Post> searchByExercise(String exercise) {
        List<Post> posts = postRepository.findAllByExercise(exercise);

        return posts;
    }

    // 게시글 정렬
    public List<PostDtoRes> getSortedPosts(String sort, String direction) {
        System.out.println("received sort: " + sort + " direction: " + direction);

        Sort.Direction sortDirection = (direction != null &&
                direction.equalsIgnoreCase("DESC")) ? Sort.Direction.DESC : Sort.Direction.ASC;

        if (sort == null || sort.isEmpty()) {
            sort = "createdAt";
        }

        List<Post> posts;
        switch (sort) {
            case "popularity":
                posts = postRepository.findAll(Sort.by(sortDirection, "likesCnt"));
                System.out.println("sorting by likesCnt with direction : " + sortDirection);
                break;
            case "oldest":
                posts = postRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));
                System.out.println();
                break;
//            case "latest":
//                posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
//                break;
            default:
                posts = postRepository.findAll(Sort.by(sortDirection, "createdAt"));
                System.out.println();
                break;

        }
        System.out.println("sorting by: " + sort + "direction: " + direction);
//        return posts.stream().map(PostDtoRes::convertToDto).collect(Collectors.toList());
        return posts.stream().map(PostDtoRes::new).toList();
    }

    // 내가 쓴 게시글
    public List<Post> getPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("UserId: " + userId);
        System.out.println("Found User: " + user);
        return postRepository.findAllByUser(user);
    }

    // 내가 쓴 게시글 검색
    public List<PostDtoRes> searchMyPosts(Long userId, String keyword) {
        // 사용자가 작성한 게시글 가져오기
        List<Post> myPosts = postRepository.findByUser_UserId(userId); // 사용자 ID로 게시글을 가져오는 메서드

        // 제목이나 내용으로 필터링
        List<Post> filteredPosts = myPosts.stream()
                .filter(post -> post.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        post.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        return filteredPosts.stream().map(PostDtoRes::new).collect(Collectors.toList());
    }

    // 내가 좋아요한 게시글
    public List<PostDtoRes> getLikedPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Like> likes = likeRepository.findByUser_UserId(userId);

        return likes.stream().map(like -> new PostDtoRes(like.getPost()))
                .collect(Collectors.toList());
    }

    // 내가 좋아요한 게시글 정렬
    public List<PostDtoRes> getSortedLikedPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Like> likes = likeRepository.findByUser_UserId(userId);

        // 게시글 정렬 (예: 게시글 생성 날짜 기준)
        List<PostDtoRes> likedPosts = likes.stream()
                .map(like -> new PostDtoRes(like.getPost()))
                .sorted(Comparator.comparing(post -> post.getCreatedAt())) // 생성 날짜로 정렬
                .collect(Collectors.toList());

        return likedPosts;
    }

    // 내가 좋아요한 게시글 검색
    public List<PostDtoRes> searchLikedPostsByKeyword(User user, String keyword) {
        List<Like> likes = likeRepository.findByUser_UserId(user.getUserId());

        List<Long> postIds = likes.stream()
                                .map(like -> like.getPost().getPostId())
                                .collect(Collectors.toList());

        System.out.println("좋아요한 게시글 ID 수: " + postIds.size());
        List<Post> likedPosts = postRepository.findByPostIdIn(postIds);

        List<Post> filteredPosts = likedPosts.stream()
                .filter(post -> post.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        post.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        return filteredPosts.stream().map(PostDtoRes::new).collect(Collectors.toList());
    }

    public PostDtoRes convertToDto(Post post, User user) {
        PostDtoRes dto = new PostDtoRes();
        dto.setPostId(post.getPostId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setImageStr(post.getImageStr());
        dto.setExercise(post.getExercise());
        dto.setLocation(post.getLocation());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setCreatedAt(LocalDateTime.now());
        dto.setLikesCnt(post.getLikesCnt());
        dto.setCommentsCnt(post.getCommentsCnt());
        dto.setIsHeartFilled(checkIfUserLikedPost(user, post));

        return dto;
//        return PostDtoRes.builder()
//                .userId(post.getUser().getUserId())
//                .postId(post.getPostId())
//                .title(post.getTitle())
//                .content(post.getContent())
//                .imageStr(post.getImageStr())
//                .exercise(post.getExercise())
//                .location(post.getLocation())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .likesCnt(post.getLikesCnt())
//                .commentsCnt(post.getCommentsCnt())
//                .isHeartFilled(checkIfUserLikedPost(post.getUser(), post));
//                .build();
    }


}
