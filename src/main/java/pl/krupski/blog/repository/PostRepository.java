package pl.krupski.blog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.krupski.blog.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
}
