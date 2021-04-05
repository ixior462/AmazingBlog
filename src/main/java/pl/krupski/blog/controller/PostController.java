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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/post", produces = "application/json;charset=utf-8")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/post")
    public void createNewPost(@RequestBody String text) {
        postService.createNewPost(text);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/post")
    public void updatePost(@RequestParam String id, @RequestBody String text) {
        postService.editPost(id, text);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/post")
    public void deletePost(@RequestBody String id) {
        postService.deletePost(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/findPost", produces = "application/json;charset=utf-8")
    public Post getPost(@RequestBody String id) {
        return postService.findPostById(id);
    }
}
