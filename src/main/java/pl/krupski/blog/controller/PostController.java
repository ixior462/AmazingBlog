package pl.krupski.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.krupski.blog.model.Post;
import pl.krupski.blog.service.PostService;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/post", produces = "application/json;charset=utf-8")
    public List<Post> getAllUsers() {
        return postService.getAllPosts();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/post")
    public void createNewPost(@RequestBody String text) {
        postService.createNewPost(text);
    }
}
