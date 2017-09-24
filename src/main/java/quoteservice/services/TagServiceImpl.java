package quoteservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quoteservice.domain.Tag;
import quoteservice.repositories.TagRepository;

@Service
public class TagServiceImpl implements TagService{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private TagRepository tagRepository;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Iterable<Tag> listAllTags() {
        logger.debug("listAllTags called");
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagById(Integer id) {
        logger.debug("getTagById called");
        return tagRepository.findOne(id);
    }

    @Override
    public Tag saveTag(Tag tag) {
        logger.debug("saveTag called");
        try{
            tag.setName(tag.getName().toLowerCase());
            tag = tagRepository.save(tag);
        }catch (org.springframework.dao.DataIntegrityViolationException e){
            logger.info("Unable to save tag due to integrity constraint.");
            tag = null;
        }
        return tag;
    }

    @Override
    public void deleteTag(Integer id) {
        logger.debug("deleteTag called");
        tagRepository.delete(id);
    }

    @Override
    public Tag findTagByName(String name) {
        logger.debug("findTagByName called");
        return tagRepository.findByName(name.toLowerCase()).orElse(null);
    }


}
