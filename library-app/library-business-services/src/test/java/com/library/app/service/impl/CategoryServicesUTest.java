package com.library.app.service.impl;

import static com.library.app.common.entities.CategoryDtoForDaoTest.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.library.app.dao.CategoryDao;
import com.library.app.dto.CategoryDTO;
import com.library.app.exceptions.*;
import com.library.app.service.CategoryService;

public class CategoryServicesUTest {
	private CategoryService categoryServices;
	private CategoryDao categoryDao;
	private Validator validator;

	@Before
	public void initTestCase() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();

		categoryDao = mock(CategoryDao.class);

		categoryServices = new CategoryServiceImpl();
		((CategoryServiceImpl) categoryServices).validator = validator;
		((CategoryServiceImpl) categoryServices).categoryDao = categoryDao;
	}

	@Test
	public void addCategoryWithNullName() {
		addCategoryWithInvalidName(null);
	}

	@Test
	public void addCategoryWithShortName() {
		addCategoryWithInvalidName("A");
	}

	@Test
	public void addCategoryWithLongName() {
		addCategoryWithInvalidName("This is a long name that will cause an exception to be thrown");
	}

	@Test(expected = CategoryAlreadyExistsException.class)
	public void addCategoryWithExistentName() {
		when(categoryDao.alreadyExists(java())).thenReturn(true);

		categoryServices.add(java());
	}

	@Test
	public void addValidCategory() {
		when(categoryDao.alreadyExists(java())).thenReturn(false);
		when(categoryDao.add(java())).thenReturn(categoryWithId(java(), 1L));

		final CategoryDTO categoryAdded = categoryServices.add(java());
		assertThat(categoryAdded.getId(), is(equalTo(1L)));
	}

	@Test
	public void updateWithNullName() {
		updateCategoryWithInvalidName(null);
	}

	@Test
	public void updateCategoryWithShortName() {
		updateCategoryWithInvalidName("A");
	}

	@Test
	public void updateCategoryWithLongName() {
		updateCategoryWithInvalidName("This is a long name that will cause an exception to be thrown");
	}

	@Test(expected = CategoryAlreadyExistsException.class)
	public void updateCategoryWithExistentName() {
		when(categoryDao.alreadyExists(categoryWithId(java(), 1L))).thenReturn(true);

		categoryServices.update(categoryWithId(java(), 1L));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void updateCategoryNotFound() {
		when(categoryDao.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false);
		when(categoryDao.existsById(1L)).thenReturn(false);

		categoryServices.update(categoryWithId(java(), 1L));
	}

	@Test
	public void updateValidCategory() {
		when(categoryDao.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false);
		when(categoryDao.existsById(1L)).thenReturn(true);

		categoryServices.update(categoryWithId(java(), 1L));

		verify(categoryDao).update(categoryWithId(java(), 1L));
	}

	@Test
	public void findCategoryById() {
		when(categoryDao.findById(1L)).thenReturn(categoryWithId(java(), 1L));

		final CategoryDTO category = categoryServices.findById(1L);
		assertThat(category, is(notNullValue()));
		assertThat(category.getId(), is(equalTo(1L)));
		assertThat(category.getName(), is(equalTo(java().getName())));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void findCategoryByIdNotFound() {
		when(categoryDao.findById(1L)).thenReturn(null);

		categoryServices.findById(1L);
	}

	@Test
	public void findAllNoCategories() {
		when(categoryDao.findAll("name")).thenReturn(new ArrayList<>());

		final List<CategoryDTO> categories = categoryServices.findAll();
		assertThat(categories.isEmpty(), is(equalTo(true)));
	}

	@Test
	public void findAllCategories() {
		when(categoryDao.findAll("name"))
				.thenReturn(Arrays.asList(categoryWithId(java(), 1L), categoryWithId(networks(), 2L)));

		final List<CategoryDTO> categories = categoryServices.findAll();
		assertThat(categories.size(), is(equalTo(2)));
		assertThat(categories.get(0).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(networks().getName())));
	}

	private void addCategoryWithInvalidName(final String name) {
		try {
			categoryServices.add(new CategoryDTO(name));
			fail("An error should have been thrown");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}

	private void updateCategoryWithInvalidName(final String name) {
		try {
			categoryServices.update(new CategoryDTO(name));
			fail("An error should have been thrown");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}

}
