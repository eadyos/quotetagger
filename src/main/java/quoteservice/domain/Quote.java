package quoteservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Quote {

    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "The database generated ID")
    private Integer id;
    @ApiModelProperty(notes = "The quotation text", example = "To be, or not to be, that is the question.")
    private String text;
    @ApiModelProperty(notes = "The author of the quotation", example = "Hamlet")
    private String author;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Tag> tags = new HashSet<>();


    Quote(){} //jpa only

    public Quote(String text, String author){
        this.text = text;
        this.author = author;
    }


    public Integer getId(){
        return this.id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Set<Tag> getTags() {
        return tags;
    }
}
