package pl.krupski.blog.service;

import org.springframework.stereotype.Service;
import pl.krupski.blog.model.Post;
import pl.krupski.blog.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void createNewPost(String text) {
        Post post = new Post(text);
        postRepository.save(post);
    }

    public void deletePost(String id) {
        postRepository.deleteById(id);
    }

    public Post findPostById(String id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElse(null);
    }

    public void editPost(String id, String text) {
        Optional<Post> post = postRepository.findById(id);
        post.ifPresent(entity -> {
            entity.setText(text);
            postRepository.save(entity);
        });
    }
}
