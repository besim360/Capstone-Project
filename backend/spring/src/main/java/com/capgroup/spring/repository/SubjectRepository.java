package com.capgroup.spring.repository;


import com.capgroup.spring.model.Subject;
import org.springframework.stereotype.Repository;

/**
 * Subject repository that contains subjects intended to be mapped to articles,
 * extends SearchRepository rather than SimpleJpaRepository due to the @EnableJpaRepositories base class
 * annotation in {@link com.capgroup.spring.Application}, may be possible to reconfigure to
 * no longer include unused search methods
 */
@Repository
public interface SubjectRepository extends SearchRepository<Subject, String> {
}
