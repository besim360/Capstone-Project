package com.capgroup.spring.repository;


import com.capgroup.spring.model.Subject;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends SearchRepository<Subject, String> {
}
