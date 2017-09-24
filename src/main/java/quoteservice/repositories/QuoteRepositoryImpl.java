package quoteservice.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import quoteservice.domain.Quote;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class QuoteRepositoryImpl implements QuoteRepositoryCustom {

    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Quote getRandomQuote() {
        String query = "FROM Quote ORDER BY random()";
        Query q = entityManager.createQuery(query);
        q.setMaxResults(1);
        Quote result = null;
        if(!q.getResultList().isEmpty()){
            result = (Quote)q.getResultList().get(0);
        }
        return result;
    }
}
