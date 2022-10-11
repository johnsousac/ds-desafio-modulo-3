package com.devsuperior.bds04.services;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.services.exceptions.DatabaseException;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class CityService {

	@Autowired
	private CityRepository repository;
	
	@Transactional(readOnly = true)
	public List<CityDTO> findAll() {
		List<City> list = repository.findAll();
		return list.stream()
				.sorted(Comparator.comparing(City::getName))
				.map(CityDTO::new)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CityDTO findById(Long id) {
		Optional<City> obj = repository.findById(id);
		City entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new CityDTO(entity);
	}

	@Transactional
	public CityDTO insert(CityDTO dto) {
		City entity = new City();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CityDTO(entity);
	}

	@Transactional
	public CityDTO update(Long id, CityDTO dto) {
		try {
			City entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CityDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}		
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
}
