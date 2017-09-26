package quoteservice.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import quoteservice.domain.Quote;
import quoteservice.domain.Tag;
import quoteservice.services.QuoteService;

import java.net.URI;

@RestController
@RequestMapping(value = "/quotes")
@CrossOrigin
@Api(value="quote service", description = "Operations for storing and updating quotations.")
public class QuoteController {

    private QuoteService quoteService;
    //private TagService tagService;

    @Autowired
    public void setQuoteService(QuoteService quoteService){
        this.quoteService = quoteService;
    }

    @ApiOperation(value = "Get list of quotations", response = Iterable.class)
    @RequestMapping(method = RequestMethod.GET)
    Iterable<Quote> listQuotes(Model model) {
        return quoteService.listAllQuotes();
    }

    @ApiOperation(value = "Save a new quotation", response = ResponseEntity.class)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveQuote(@RequestBody Quote input){
        Quote result = quoteService.saveQuote(input);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{quoteId}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).body(result);
    }

    @ApiOperation(value = "Update an existing quotation", response = ResponseEntity.class)
    @RequestMapping(value = "/{quoteId}", method = RequestMethod.PUT)
    public ResponseEntity updateQuote(@PathVariable Integer quoteId, @RequestBody Quote input){
        Quote existingQuote = quoteService.getQuoteById(quoteId);
        if(existingQuote == null){
            throw new ResourceNotFoundException();
        }
        existingQuote.setText(input.getText());
        existingQuote.setAuthor(input.getAuthor());
        Quote updatedQuote = quoteService.saveQuote(existingQuote);
        return ResponseEntity.ok(updatedQuote);
    }

    @ApiOperation(value = "Partially update an existing quotation", response = ResponseEntity.class)
    @RequestMapping(value = "/{quoteId}", method = RequestMethod.PATCH)
    public ResponseEntity patchQuote(@PathVariable Integer quoteId, @RequestBody Quote input){
        Quote existingQuote = quoteService.getQuoteById(quoteId);
        if(existingQuote == null){
            throw new ResourceNotFoundException();
        }
        if(input.getText() != null){
            existingQuote.setText(input.getText());
        }
        if(input.getAuthor() != null){
            existingQuote.setAuthor(input.getAuthor());
        }
        Quote updatedQuote = quoteService.saveQuote(existingQuote);
        return ResponseEntity.ok(updatedQuote);
    }

    @ApiOperation(value = "Delete a quotation", response = ResponseEntity.class)
    @RequestMapping(value="/{quoteId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteQuote(@PathVariable Integer quoteId){
        if(quoteService.getQuoteById(quoteId) == null){
            throw new ResourceNotFoundException();
        }
        quoteService.deleteQuote(quoteId);
        return ResponseEntity.ok("Quote deleted");
    }

    @ApiOperation(value = "Get list of tags for this quotation", response = Iterable.class)
    @RequestMapping(method = RequestMethod.GET, value = "/{quoteId}/tags")
    Iterable<Tag> getTags(@PathVariable Integer quoteId) {
        Quote quote = quoteService.getQuoteById(quoteId);
        if(quote == null){
            throw new ResourceNotFoundException();
        }
        return quote.getTags();
    }

    @ApiOperation(value = "Get specific quotation", response = Quote.class)
    @RequestMapping(method = RequestMethod.GET, value = "/{quoteId}")
    public Quote getQuote(@PathVariable Integer quoteId, Model model){
        Quote quote = quoteService.getQuoteById(quoteId);
        if(quote == null){
            throw new ResourceNotFoundException();
        }
        return quote;
    }

    @ApiOperation(value = "Add tag to a quote", response = ResponseEntity.class)
    @RequestMapping(value = "/{quoteId}/tags/{tagId}", method = RequestMethod.PUT)
    public ResponseEntity addTagToQuote(@PathVariable Integer quoteId, @PathVariable Integer tagId){
        Quote quote = quoteService.addTagToQuote(quoteId, tagId);
        if(quote == null){
            throw new ResourceNotFoundException();
        }
        return ResponseEntity.ok("Tag added");
    }

    @ApiOperation(value = "Remove tag from a quote", response = ResponseEntity.class)
    @RequestMapping(value="/{quoteId}/tags/{tagId}", method = RequestMethod.DELETE)
    public ResponseEntity removeTagFromQuote(@PathVariable Integer quoteId, @PathVariable Integer tagId){
        Quote quote = quoteService.removeTagFromQuote(quoteId, tagId);
        if(quote == null){
            throw new ResourceNotFoundException();
        }
        return ResponseEntity.ok("Tag removed");
    }

    @ApiOperation(value = "Get a random quotation", response = Quote.class)
    @RequestMapping(method = RequestMethod.GET, value = "/random")
    public Quote getRandomQuote(Model model){
        Quote quote = quoteService.getRandomQuote();
        if(quote == null){
            throw new ResourceNotFoundException();
        }
        return quote;
    }

    ///Temp
    @ApiOperation(value = "Search for a quote (by author)", response = Tag.class)
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public Iterable<Quote> searchForQuote(@RequestParam(name = "author", required = true) String author){
        Iterable<Quote> quotes = quoteService.findQuoteByAuthor(author);
        return quotes;
    }



}
