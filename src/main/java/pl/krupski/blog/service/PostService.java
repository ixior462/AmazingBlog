package pl.krupski.blog.service;

import org.springframework.stereotype.Service;
import pl.krupski.blog.model.Post;
import pl.krupski.blog.repository.PostRepository;

import java.util.List;

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
}
