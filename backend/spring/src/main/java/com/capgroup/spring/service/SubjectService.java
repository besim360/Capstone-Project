package com.capgroup.spring.service;

import com.capgroup.spring.model.Subject;
import com.capgroup.spring.repository.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * The business layer that handles all requests regarding the Subject entity,
 * note that subjects are IndexEmbedded in articles and general searches do not
 * need to involve the subject service
 */
@Service
@Slf4j
public class SubjectService {
    @Autowired
    private  SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }
    @Transactional(readOnly = false)
    public void deleteSubject(String subjectCode) {subjectRepository.deleteById(subjectCode);}

    @Transactional(readOnly = false)
    public void addSubject(String subjectCode, String generalTopic, String topics){
        Subject subject = new Subject();
        subject.setSubjectCode(subjectCode);
        subject.setGeneralTopic(generalTopic);
        subject.setTopics(topics);
        subjectRepository.save(subject);
    }

    @Transactional(readOnly = false)
    public void updateSubject(String subjectCode, String generalTopic, String topics){
        var subject = subjectRepository.getReferenceById(subjectCode);
            subject.setSubjectCode(subjectCode);
        if(generalTopic != null){
            subject.setGeneralTopic(generalTopic);
        }
        if(topics != null){
            subject.setTopics(topics);
        }
    }

    @Transactional(readOnly = true)
    public List<Subject> getAllSubjects(){
        List<Subject> test = new ArrayList<>();
        try {
            test = subjectRepository.findAll();
        }
        catch (NullPointerException e){
            log.info("ERROR: {}", e.getMessage());
        }
        return test;
    }

    @Transactional(readOnly = true)
    public List<Subject> getSubjects(List<String> subjectCodes){
        List<Subject> subjList = new ArrayList<>();
        for(var code : subjectCodes){
            try{
                subjList.add(subjectRepository.getReferenceById(code));
            }
            catch (EntityNotFoundException e){
                log.info(e.getMessage());
            }
        }
        return subjList;
    }
}
