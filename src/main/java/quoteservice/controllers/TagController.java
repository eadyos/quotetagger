package quoteservice.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import quoteservice.domain.Quote;
import quoteservice.domain.Tag;
import quoteservice.services.TagService;

import java.net.URI;

@RestController
@RequestMapping(value = "/tags")
@Api(value="quote service", description = "Operations for storing and updating tags.")
public class TagController {

    private TagService tagService;

    @Autowired
    public void setTagService(TagService tagService){
        this.tagService = tagService;
    }


    @ApiOperation(value = "Get list of tags", response = Iterable.class)
    @RequestMapping(method = RequestMethod.GET)
    Iterable<Tag> listTags(Model model) {
        return tagService.listAllTags();
    }

    @ApiOperation(value = "Get specific tag", response = Tag.class)
    @RequestMapping(method = RequestMethod.GET, value = "/{tagId}")
    public Tag getTag(@PathVariable Integer tagId, Model model){
        Tag tag = tagService.getTagById(tagId);
        if(tag == null){
            throw new ResourceNotFoundException();
        }
        return tag;
    }

    @ApiOperation(value = "Search for a tag (by name)", response = Tag.class)
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public Tag searchForTag(@RequestParam(name = "name", required = true) String name){
        Tag tag = tagService.findTagByName(name);
        return tag;
    }

    @ApiOperation(value = "Save a new tag", response = ResponseEntity.class)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveTag(@RequestBody Tag input){
        Tag result = tagService.saveTag(input);
        if(result != null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{quoteId}")
                    .buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(location).body("Tag saved.");
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There was a conflict saving " +
                    "this tag because the name already exists.");
        }
    }

    @ApiOperation(value = "Update an existing tag", response = ResponseEntity.class)
    @RequestMapping(value = "/{tagId}", method = RequestMethod.PUT)
    public ResponseEntity updateTag(@PathVariable Integer tagId, @RequestBody Tag input){
        Tag existingTag = tagService.getTagById(tagId);
        if(existingTag == null){
            throw new ResourceNotFoundException();
        }
        existingTag.setName(input.getName());
        existingTag.setDescription(input.getDescription());
        Tag result = tagService.saveTag(existingTag);
        return ResponseEntity.ok("Quote updated");
    }

    @ApiOperation(value = "Partially update an existing tag", response = ResponseEntity.class)
    @RequestMapping(value = "/{tagId}", method = RequestMethod.PATCH)
    public ResponseEntity patchTag(@PathVariable Integer tagId, @RequestBody Tag input){
        Tag existingTag = tagService.getTagById(tagId);
        if(existingTag == null){
            throw new ResourceNotFoundException();
        }
        if(input.getName() != null){
            existingTag.setName(input.getName());
        }
        if(input.getName() != null){
            existingTag.setName(input.getName());
        }
        Tag result = tagService.saveTag(existingTag);
        return ResponseEntity.ok("Tag updated");
    }

    @ApiOperation(value = "Delete a tag", response = ResponseEntity.class)
    @RequestMapping(value="/{tagId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTag(@PathVariable Integer tagId){
        if(tagService.getTagById(tagId) == null){
            throw new ResourceNotFoundException();
        }
        tagService.deleteTag(tagId);
        return ResponseEntity.ok("Tag deleted");
    }

    @ApiOperation(value = "Get quotes for specific tag", response = Iterable.class)
    @RequestMapping(method = RequestMethod.GET, value = "/{tagId}/quotes")
    public Iterable<Quote> getQuotesByTag(@PathVariable Integer tagId){
        Tag tag = tagService.getTagById(tagId);
        if(tag == null){
            throw new ResourceNotFoundException();
        }
        return tag.getQuotes();
    }



}
