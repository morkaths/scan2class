package com.morkath.scan2class.core;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<T, ID> {
	List<T> getAll();
	Page<T> getAll(Pageable pageable);
	T getById(ID id);
	List<T> getByIds(List<ID> ids);
	T save(T dto);
	T update(ID id, Map<String, Object> fields);
	boolean delete(ID id);
}
