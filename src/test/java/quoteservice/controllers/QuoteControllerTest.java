package quoteservice.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import quoteservice.Application;
import quoteservice.domain.Quote;
import quoteservice.domain.Tag;
import quoteservice.repositories.QuoteRepository;
import quoteservice.repositories.TagRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class QuoteControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private TagRepository tagRepository;

    private Quote quote;
    private List<Tag> tags = new ArrayList<>();
    private Tag unassociatedTag;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }


    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.quoteRepository.deleteAll();
        this.tagRepository.deleteAll();

        this.quote = quoteRepository.save(new Quote("Test Quote", "Tester"));
        this.tags.add(tagRepository.save(new Tag("TestTag1", "TestDescription1")));
        this.tags.add(tagRepository.save(new Tag("TestTag2", "TestDescription2")));
        unassociatedTag = tagRepository.save(new Tag("TestTag3", "TestDescription3"));
        this.quote.getTags().add(tags.get(0));
        this.quote.getTags().add(tags.get(1));
        quoteRepository.save(quote);
    }

    @Test
    public void getQuote() throws Exception {
        mockMvc.perform(get("/quotes/"+ quote.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.quote.getId())))
                .andExpect(jsonPath("$.text", is(this.quote.getText())))
                .andExpect(jsonPath("$.author", is(this.quote.getAuthor())));
    }

    @Test
    public void listQuotes() throws Exception {
        mockMvc.perform(get("/quotes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(this.quote.getId())))
                .andExpect(jsonPath("$[0].text", is(this.quote.getText())))
                .andExpect(jsonPath("$[0].author", is(this.quote.getAuthor())));
    }

    @Test
    public void saveQuote() throws Exception {
        String quoteJson = json(new Quote("New Quote", "New Test Author"));
        this.mockMvc.perform(post("/quotes")
                .contentType(contentType)
                .content(quoteJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateQuote() throws Exception {
        quote.setText("Updated Text");
        quote.setAuthor(null);
        String quoteJson = json(quote);
        this.mockMvc.perform(put("/quotes/" + quote.getId())
                .contentType(contentType)
                .content(quoteJson))
                .andExpect(status().isOk());
        Quote updatedQuote = quoteRepository.findOne(quote.getId());
        assertEquals(quote.getId(), updatedQuote.getId());
        assertEquals(quote.getText(), updatedQuote.getText());
        assertEquals(quote.getAuthor(), updatedQuote.getAuthor());
    }

    @Test
    public void patchQuote() throws Exception {
        quote.setText("Updated Text");
        String originalAuthor = quote.getAuthor();
        quote.setAuthor(null);
        String quoteJson = json(quote);
        this.mockMvc.perform(patch("/quotes/" + quote.getId())
                .contentType(contentType)
                .content(quoteJson))
                .andExpect(status().isOk());
        Quote updatedQuote = quoteRepository.findOne(quote.getId());
        assertEquals(quote.getId(), updatedQuote.getId());
        assertEquals(quote.getText(), updatedQuote.getText());
        assertEquals(originalAuthor, updatedQuote.getAuthor());
    }

    @Test
    public void deleteQuote() throws Exception {
        this.mockMvc.perform(delete("/quotes/" + quote.getId()))
                .andExpect(status().isOk());
        assertFalse(quoteRepository.exists(quote.getId()));
    }

    @Test
    public void getTags() throws Exception {
        this.mockMvc.perform(get("/quotes/" + quote.getId() + "/tags"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void addTagToQuote() throws Exception {
        this.mockMvc.perform(put("/quotes/" + quote.getId() + "/tags/" + unassociatedTag.getId()))
                .andExpect(status().isOk());
        Quote q = quoteRepository.findOne(quote.getId());
        assertTrue(q.getTags().size() == 3);
    }

    @Test
    public void removeTagFromQuote() throws Exception {
        this.mockMvc.perform(delete("/quotes/" + quote.getId() + "/tags/" + tags.get(0).getId()))
                .andExpect(status().isOk());
        Quote q = quoteRepository.findOne(quote.getId());
        assertTrue(q.getTags().size() == 1);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }



}
