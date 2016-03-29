package com.library.app.dao;

import static com.library.app.common.entities.CategoryDtoForDaoTest.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.*;

import org.junit.*;

import com.library.app.dao.impl.CategoryDaoImpl;
import com.library.app.dto.CategoryDTO;
import com.library.app.utils.DBCommandTransactionalExecutor;

public class CategoryDaoUTest {
	private EntityManagerFactory emf;
	private EntityManager em;
	private CategoryDaoImpl categoryDao;
	private DBCommandTransactionalExecutor dBCommandTransactionalExecutor;

	@Before
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();

		categoryDao = new CategoryDaoImpl();
		categoryDao.em = em;

		dBCommandTransactionalExecutor = new DBCommandTransactionalExecutor(em);
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

	@Test
	public void addCategoryAndFindIt() {
		final Long categoryAddedId = dBCommandTransactionalExecutor.executeCommand(() -> {
			return categoryDao.add(java()).getId();
		});

		assertThat(categoryAddedId, is(notNullValue()));

		final CategoryDTO category = categoryDao.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));
	}

	@Test
	public void findCategoryByIdNotFound() {
		final CategoryDTO category = categoryDao.findById(999L);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void findCategoryByIdWithNullId() {
		final CategoryDTO category = categoryDao.findById(null);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void updateCategory() {
		final Long categoryAddedId = dBCommandTransactionalExecutor.executeCommand(() -> {
			return categoryDao.add(java()).getId();
		});

		final CategoryDTO categoryAfterAdd = categoryDao.findById(categoryAddedId);
		assertThat(categoryAfterAdd.getName(), is(equalTo(java().getName())));

		categoryAfterAdd.setName(cleanCode().getName());
		dBCommandTransactionalExecutor.executeCommand(() -> {
			categoryDao.update(categoryAfterAdd);
			return null;
		});

		final CategoryDTO categoryAfterUpdate = categoryDao.findById(categoryAddedId);
		assertThat(categoryAfterUpdate.getName(), is(equalTo(cleanCode().getName())));
	}

	@Test
	public void findAllCategories() {
		dBCommandTransactionalExecutor.executeCommand(() -> {
			allCategories().forEach(categoryDao::add);
			return null;
		});

		final List<CategoryDTO> categories = categoryDao.findAll("name");
		assertThat(categories.size(), is(equalTo(4)));
		assertThat(categories.get(0).getName(), is(equalTo(architecture().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(cleanCode().getName())));
		assertThat(categories.get(2).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(3).getName(), is(equalTo(networks().getName())));
	}

	@Test
	public void alreadyExistsForAdd() {
		dBCommandTransactionalExecutor.executeCommand(() -> {
			categoryDao.add(java());
			return null;
		});

		assertThat(categoryDao.alreadyExists(java()), is(equalTo(true)));
		assertThat(categoryDao.alreadyExists(cleanCode()), is(equalTo(false)));
	}

	@Test
	public void alreadyExistsCategoryWithId() {
		final CategoryDTO java = dBCommandTransactionalExecutor.executeCommand(() -> {
			categoryDao.add(cleanCode());
			return categoryDao.add(java());
		});

		assertThat(categoryDao.alreadyExists(java), is(equalTo(false)));

		java.setName(cleanCode().getName());
		assertThat(categoryDao.alreadyExists(java), is(equalTo(true)));

		java.setName(networks().getName());
		assertThat(categoryDao.alreadyExists(java), is(equalTo(false)));
	}

	@Test
	public void existsById() {
		final Long categoryAddedId = dBCommandTransactionalExecutor.executeCommand(() -> {
			return categoryDao.add(java()).getId();
		});

		assertThat(categoryDao.existsById(categoryAddedId), is(equalTo(true)));
		assertThat(categoryDao.existsById(999L), is(equalTo(false)));
	}

}