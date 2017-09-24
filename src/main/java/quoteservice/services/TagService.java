package quoteservice.services;

import quoteservice.domain.Tag;

public interface TagService {

    Iterable<Tag> listAllTags();

    Tag getTagById(Integer id);

    Tag saveTag(Tag quote);

    void deleteTag(Integer id);

    Tag findTagByName(String name);

}
