package quoteservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import quoteservice.domain.Quote;

@RepositoryRestResource
public interface QuoteRepository extends CrudRepository<Quote, Integer> {
}
