package com.eventapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.eventapp.model.Convidado;
import com.eventapp.model.Event;

public interface ConvidadoRepository extends CrudRepository<Convidado, String> {

	Iterable<Convidado> findByEvento(Event event);

	Convidado findByRg(String rg);

}
