package com.morkath.scan2class.core;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public abstract class BaseMapper<E, D> {

	@Autowired
	protected ObjectMapper objectMapper;

	protected abstract E createEntityInstance();

	protected abstract D createDtoInstance();

	public E toEntity(D dto) {
		if (dto == null)
			return null;
		E entity = createEntityInstance();
		autoMapFields(dto, entity);
		return entity;
	}

	public D toDto(E entity) {
		if (entity == null)
			return null;
		D dto = createDtoInstance();
		autoMapFields(entity, dto);
		return dto;
	}

	protected void partial(Map<String, Object> fields, E entity) {
		Set<String> validFields = Arrays.stream(entity.getClass().getDeclaredFields()).map(Field::getName)
				.collect(Collectors.toSet());
		String invalidKeys = fields.keySet().stream().filter(key -> !validFields.contains(key))
				.collect(Collectors.joining(", "));
		if (!invalidKeys.isEmpty()) {
			throw new IllegalArgumentException("Invalid field(s): " + invalidKeys);
		}
		try {
			this.objectMapper.updateValue(entity, fields);
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException("Invalid update data: " + e.getMessage(), e);
		}
	}

	protected void autoMapFields(Object source, Object target) {
		for (Field sourceField : source.getClass().getDeclaredFields()) {
			try {
				Field targetField = target.getClass().getDeclaredField(sourceField.getName());
				sourceField.setAccessible(true);
				targetField.setAccessible(true);
				targetField.set(target, sourceField.get(source));
			} catch (NoSuchFieldException e) {
				System.err.println("Field " + sourceField.getName() + " does not exist in target class.");
			} catch (Exception e) {
				System.err.println("Error copying field " + sourceField.getName() + ": " + e.getMessage());
			}
		}
	}
}
