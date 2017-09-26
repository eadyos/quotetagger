package quoteservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import quoteservice.domain.Tag;

import java.util.Optional;

@RepositoryRestResource
public interface TagRepository extends CrudRepository<Tag, Integer>{
    Optional<Iterable<Tag>> findByNameStartsWith(String name);
}
