package com.eventapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.eventapp.model.Event;

public interface EventoRepository extends CrudRepository<Event, String> {
	Event findById(long id);
}
