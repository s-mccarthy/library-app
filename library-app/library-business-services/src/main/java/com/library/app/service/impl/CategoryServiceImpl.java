package com.library.app.service.impl;

import java.util.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.library.app.dao.CategoryDao;
import com.library.app.dto.CategoryDTO;
import com.library.app.exceptions.*;
import com.library.app.service.CategoryService;

@Stateless
public class CategoryServiceImpl implements CategoryService {

	@Inject
	Validator validator;

	@Inject
	CategoryDao categoryDao;

	@Override
	public CategoryDTO add(final CategoryDTO category) {
		validateCategory(category);

		return categoryDao.add(category);
	}

	@Override
	public void update(final CategoryDTO category) {
		validateCategory(category);

		if (!categoryDao.existsById(category.getId())) {
			throw new CategoryNotFoundException();
		}

		categoryDao.update(category);
	}

	@Override
	public CategoryDTO findById(final Long id) throws CategoryNotFoundException {
		final CategoryDTO category = categoryDao.findById(id);
		if (category == null) {
			throw new CategoryNotFoundException();
		}
		return category;
	}

	@Override
	public List<CategoryDTO> findAll() {
		return categoryDao.findAll("name");
	}

	private void validateCategory(final CategoryDTO category) {
		validateCategoryFields(category);

		if (categoryDao.alreadyExists(category)) {
			throw new CategoryAlreadyExistsException();
		}
	}

	private void validateCategoryFields(final CategoryDTO category) {
		final Set<ConstraintViolation<CategoryDTO>> errors = validator.validate(category);
		final Iterator<ConstraintViolation<CategoryDTO>> itErrors = errors.iterator();
		if (itErrors.hasNext()) {
			final ConstraintViolation<CategoryDTO> violation = itErrors.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
		}
	}

}