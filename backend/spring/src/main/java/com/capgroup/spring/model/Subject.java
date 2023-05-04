package com.capgroup.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.ToString;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Subject entity that will be embedded into articles for categorization purposes
 */

@Entity
@Getter
@Setter
@ToString
@Transactional
@Table(name = "subjects")
public class Subject {


    @Id
    @Column(name = "subjectCode")
    private String subjectCode;


    @FullTextField(searchable = Searchable.YES, projectable = Projectable.YES)
    @Column(name="generalTopic")
    private String generalTopic;

    @FullTextField
    @Column(name = "topics", columnDefinition = "VARCHAR(2048)")
    // NOTE: Topics could be stored in the future as a list of strings rather than a single string so that the
    // formatted output can demarcate different topics, if so, the way topics are parsed must be changed in both
    // DataInput's getSubjects() method and any methods that manipulate subjects in ArticleController
    private String topics;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY,
    cascade = {
            CascadeType.MERGE,
            CascadeType.REMOVE
    })
    @JsonIgnore
    private List<Article> articles = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(subjectCode, subject.subjectCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectCode);
    }
}
