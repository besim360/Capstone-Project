package com.capgroup.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.ToString;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Entity
@Getter
@Setter
@ToString
@Transactional
//@Indexed(index = "SubjectIndex")
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
