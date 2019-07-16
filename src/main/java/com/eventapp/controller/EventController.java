package com.eventapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventapp.model.Convidado;
import com.eventapp.model.Event;
import com.eventapp.repository.ConvidadoRepository;
import com.eventapp.repository.EventoRepository;

@Controller
public class EventController {
	
	@Autowired
	private EventoRepository event;
	
	@Autowired
	private ConvidadoRepository convidado;
	
	@RequestMapping(value="/cadastraevento", method=RequestMethod.GET)
	public String form() {
		return "evento/formEvent";
	}
	
	@RequestMapping(value="/cadastraevento", method=RequestMethod.POST)
	public String form(@Valid Event evento, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()){
			attributes.addFlashAttribute("mensagem", "Os campos não podem ser vazios!");
			return "redirect:/cadastraevento";
		}		
		event.save(evento);
		attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
		return "redirect:/cadastraevento";
	}	
	
	//@RequestMapping("/")
	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Event> event = this.event.findAll();
		mv.addObject("eventos", event);
		return mv;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable("id") long id) {
		Event event = this.event.findById(id);
		ModelAndView mv = new ModelAndView("evento/detalhesevento");
		mv.addObject("evento", event);
		
		Iterable<Convidado> convidados = this.convidado.findByEvento(event);
		mv.addObject("convidados", convidados);
		return mv;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.POST)
	public String detalhesEventoPost(@PathVariable("id") long id, @Valid Convidado convidado,  BindingResult result, RedirectAttributes attributes ) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Os campos não podem ser vazios");
			return "redirect:/{id}";
		}
		Event event = this.event.findById(id);
		convidado.setEvento(event);
		this.convidado.save(convidado);
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		return "redirect:/{id}";
	}
	
	@RequestMapping("/deletarEvento")
	public String deleteEvento(long id) {
		Event evento = this.event.findById(id);
		Iterable<Convidado> convidados = this.convidado.findByEvento(evento);
		this.convidado.deleteAll(convidados);
		event.delete(evento);
		return "redirect:/eventos";
	}
	
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(String rg){
		Convidado convidado = this.convidado.findByRg(rg);
		this.convidado.delete(convidado);
		
		Event evento = convidado.getEvento();
		//String codigoLong = "" + evento.getId();
		//String codigo = "" + codigoLong;
		return "redirect:/" + evento.getId();
	}
}