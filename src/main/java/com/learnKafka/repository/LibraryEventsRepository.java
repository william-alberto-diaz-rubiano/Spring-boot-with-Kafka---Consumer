package com.learnKafka.repository;

import com.learnKafka.entity.LibraryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryEventsRepository extends JpaRepository<LibraryEvent,Integer> {

}
