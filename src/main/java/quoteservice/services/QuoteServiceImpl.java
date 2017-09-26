package quoteservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quoteservice.domain.Quote;
import quoteservice.domain.Tag;
import quoteservice.repositories.QuoteRepository;
import quoteservice.repositories.TagRepository;

@Service
public class QuoteServiceImpl implements QuoteService{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private QuoteRepository quoteRepository;
    private TagRepository tagRepository;

    @Autowired
    public void setQuoteRepository(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Iterable<Quote> listAllQuotes() {
        logger.debug("listAllQuotes called");
        return quoteRepository.findAll();
    }

    @Override
    public Quote getQuoteById(Integer id) {
        logger.debug("getQuoteById called");
        return quoteRepository.findOne(id);
    }

    @Override
    public Quote saveQuote(Quote quote) {
        logger.debug("saveQuote called");
        return quoteRepository.save(quote);
    }

    @Override
    public void deleteQuote(Integer id) {
        logger.debug("deleteQuote called");
        quoteRepository.delete(id);
    }

    @Override
    public Quote addTagToQuote(Integer quoteId, Integer tagId) {
        Tag tag = tagRepository.findOne(tagId);
        Quote quote = null;
        if(tag != null){
            quote = quoteRepository.findOne(quoteId);
            if(quote != null){
                quote.getTags().add(tag);
                quoteRepository.save(quote);
            }
        }
        return quote;
    }

    @Override
    public Quote removeTagFromQuote(Integer quoteId, Integer tagId) {
        Tag tag = tagRepository.findOne(tagId);
        Quote quote = null;
        if(tag != null){
            quote = quoteRepository.findOne(quoteId);
            if(quote != null){
                quote.getTags().remove(tag);
                quoteRepository.save(quote);
            }
        }
        return quote;
    }

    @Override
    public Quote getRandomQuote() {
        logger.debug("getRandomQuote invoked");
        return quoteRepository.getRandomQuote();
    }

    @Override
    public Iterable<Quote> findQuoteByAuthor(String author){
        logger.debug("findQuoteByAuthor invoked.");
        return quoteRepository.findByAuthorStartingWith(author).orElse(null);
    }
}
