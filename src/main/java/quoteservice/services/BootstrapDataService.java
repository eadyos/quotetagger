package quoteservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import quoteservice.domain.Quote;
import quoteservice.domain.Tag;
import quoteservice.repositories.QuoteRepository;
import quoteservice.repositories.TagRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class BootstrapDataService implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    QuoteRepository quoteRepository;
    @Autowired
    TagRepository tagRepository;

    Tag testTag;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Bootstrapping data...");
        createTags();
        createQuotes();
        logger.info("...Bootstrapping data completed");
    }

    private void createQuotes() throws IOException {

        List<Quote> quotes = new ArrayList();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("quotes.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine()) != null){
            String[] parts = line.split(";");
            String text = parts[0];
            String author;
            if(parts.length == 3){
                text += parts[1];
                author = parts[2];
            }else if(parts.length > 1){
                author = parts[1];
            }else{
                author = "unknown";
            }
            Quote q = new Quote(text, author);
            q.getTags().add(testTag);
            quotes.add(q);
        }
        quoteRepository.save(quotes);
    }

    private void createTags(){
        testTag = tagRepository.save(new Tag("test tag", "Tag for testing"));
        tagRepository.save(new Tag("funny", "Funny, humorous, or witty.  Unlike me."));
        tagRepository.save(new Tag("inspirational", "Daily doses of inspiration"));
    }
}
