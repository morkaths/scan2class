package com.morkath.scan2class.core;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseServiceImpl<E, D, ID> implements BaseService<D, ID> {
	
	protected JpaRepository<E, ID> repository;
	protected BaseMapper<E, D> mapper;
	
	public BaseServiceImpl(JpaRepository<E, ID> repository, BaseMapper<E, D> mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Autowired
	protected ObjectMapper objectMapper;

	@Override
	public List<D> findAll() {
		List<E> entities = repository.findAll();
		return entities.stream().map(mapper::toDto).toList();
	}

	@Override
	public Page<D> findAll(Pageable pageable) {
		Page<E> entityPage = repository.findAll(pageable);
		return entityPage.map(mapper::toDto);
	}

	@Override
	public D findById(ID id) {
		return repository.findById(id).map(mapper::toDto)
				.orElseThrow(() -> new IllegalArgumentException("Entity not found with id: " + id));
	}

	@Override
	public D save(D dto) {
		try {
			E entity = repository.save(mapper.toEntity(dto));
			return mapper.toDto(entity);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Data integrity violation: " + e.getMessage(), e);
		}
	}

	@Override
	public D update(ID id, Map<String, Object> fields) {
		E entity = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Entity not found with id: " + id));
		mapper.partialUpdateEntity(objectMapper, fields, entity);
		E saved = repository.save(entity);
		return mapper.toDto(saved);
	}

	@Override
	public boolean delete(ID id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}
		return false;
	}

}
