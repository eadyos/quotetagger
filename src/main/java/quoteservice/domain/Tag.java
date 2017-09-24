package quoteservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag",
        indexes = {@Index(name = "name_index",  columnList="name", unique = true)})
public class Tag {

    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "The database generated ID")
    private Integer id;

    @ApiModelProperty(notes = "Brief tag name", example = "motivational")
    private String name;
    @ApiModelProperty(notes = "Detailed description about this tag",
            example = "Quotes that inspire or motivate")
    private String description;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    private Set<Quote> quotes = new HashSet<>();

    Tag(){}//Jpa only

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Quote> getQuotes() {
        return quotes;
    }
}
