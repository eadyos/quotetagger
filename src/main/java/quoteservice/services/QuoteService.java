package quoteservice.services;

import quoteservice.domain.Quote;

public interface QuoteService {

    Iterable<Quote> listAllQuotes();

    Quote getQuoteById(Integer id);

    Quote saveQuote(Quote quote);

    void deleteQuote(Integer id);

    Quote addTagToQuote(Integer quoteId, Integer tagId);

    Quote removeTagFromQuote(Integer quoteId, Integer tagId);

}
