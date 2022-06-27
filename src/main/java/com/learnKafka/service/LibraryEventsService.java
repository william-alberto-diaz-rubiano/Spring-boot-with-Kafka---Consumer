package com.learnKafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnKafka.entity.LibraryEvent;
import com.learnKafka.repository.LibraryEventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LibraryEventsService {

    @Autowired
    LibraryEventsRepository libraryEventsRepository;

    @Autowired
    ObjectMapper objectMapper;

    public void processLibraryEvent(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {

        LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
        log.info("LibraryEvent: {} ", libraryEvent);

        switch (libraryEvent.getLibraryEventType()){
            case NEW:
                //save operation
                save(libraryEvent);
                break;
            case UPDATE:
                // update operation
                validate(libraryEvent);
                update(libraryEvent);
                break;
            default:
                log.info("Invalid Libarary Event Type");
        }

    }

    private void validate(LibraryEvent libraryEvent) {
        if(libraryEvent.getLibraryEventId() == null){
            throw new IllegalArgumentException("Library Event Id is missing");
        }
        Optional<LibraryEvent> libraryEventOptional = libraryEventsRepository.findById(libraryEvent.getLibraryEventId());
        if(libraryEventOptional.isEmpty()){
            throw new IllegalArgumentException("Not a valid Library Event");
        }
        log.info("Validation is successfully for the library event : {} ", libraryEvent);
    }

    private void save(LibraryEvent libraryEvent) {
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventsRepository.save(libraryEvent);
        log.info("Successfully persisted the library event {}.",libraryEvent);
    }


    private void update(LibraryEvent libraryEvent) {
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventsRepository.save(libraryEvent);
        log.info("Successfully Update the library event {}.",libraryEvent);
    }


}
