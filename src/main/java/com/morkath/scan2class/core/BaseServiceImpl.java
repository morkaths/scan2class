package com.morkath.scan2class.core;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

	protected final JpaRepository<T, ID> repository;

	public BaseServiceImpl(JpaRepository<T, ID> repository) {
		this.repository = repository;
	}

	@Override
	public List<T> getAll() {
		return repository.findAll();
	}

	@Override
	public Page<T> getAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public T getById(ID id) {
		return repository.findById(id).orElse(null);
	}
	
	@Override
	public List<T> getByIds(List<ID> ids) {
		return repository.findAllById(ids);
	}

	@Override
	public T save(T object) {
		return repository.save(object);
	}

	@Override
	public T update(ID id, Map<String, Object> fields) {
		Optional<T> opt = repository.findById(id);
		T entity = opt.orElseThrow(() -> new IllegalArgumentException("Entity not found with id: " + id));

		for (Map.Entry<String, Object> entry : fields.entrySet()) {
			String name = entry.getKey();
			Object value = entry.getValue();
			try {
				Field field = findField(entity.getClass(), name);
				if (field == null)
					continue;
				boolean accessible = field.canAccess(entity);
				field.setAccessible(true);
				field.set(entity, value);
				field.setAccessible(accessible);
			} catch (IllegalAccessException ex) {
				throw new DataIntegrityViolationException("Failed to set field: " + name, ex);
			}
		}

		T saved = repository.save(entity);
		return saved;
	}

	@Override
	public boolean delete(ID id) {
		if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
	}
	
	private Field findField(Class<?> clazz, String name) {
		Class<?> current = clazz;
		while (current != null && current != Object.class) {
			try {
				return current.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				current = current.getSuperclass();
			}
		}
		return null;
	}

}
