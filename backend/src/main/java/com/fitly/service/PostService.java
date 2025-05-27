package com.fitly.service;

import com.fitly.dto.PostDTO;
import com.fitly.model.Post;
import com.fitly.model.User;
import com.fitly.repository.PostRepository;
import com.fitly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PostDTO createPost(PostDTO postDTO, Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Uživatel nenalezen."));
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUser(user);
        Post savedPost = postRepository.save(post);
        return convertToDTO(savedPost);
    }

    public PostDTO updatePost(Long postId, PostDTO postDTO, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Příspěvek nenalezen."));
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("Nemáte oprávnění upravit tento příspěvek.");
        }
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        Post updatedPost = postRepository.save(post);
        return convertToDTO(updatedPost);
    }

    public void deletePost(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Příspěvek nenalezen."));
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("Nemáte oprávnění smazat tento příspěvek.");
        }
        postRepository.delete(post);
    }

    private PostDTO convertToDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setUserId(post.getUser().getId());
        dto.setAuthor(post.getUser().getUsername());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }
}
