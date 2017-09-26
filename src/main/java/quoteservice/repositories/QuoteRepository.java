package quoteservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import quoteservice.domain.Quote;

import java.util.Optional;

@RepositoryRestResource
public interface QuoteRepository extends CrudRepository<Quote, Integer>, QuoteRepositoryCustom {

    Optional<Iterable<Quote>> findByAuthorStartingWith(String author);

}
