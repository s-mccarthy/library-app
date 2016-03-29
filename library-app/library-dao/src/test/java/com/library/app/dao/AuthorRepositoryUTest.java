package com.library.app.dao;

import static com.library.app.common.entities.AuthorDtoForDaoTest.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.persistence.*;

import org.junit.*;

import com.library.app.common.filtering.*;
import com.library.app.common.filtering.PaginationData.OrderMode;
import com.library.app.dao.impl.AuthorDaoImpl;
import com.library.app.dto.AuthorDTO;
import com.library.app.utils.DBCommandTransactionalExecutor;

public class AuthorRepositoryUTest {
	private EntityManagerFactory emf;
	private EntityManager em;
	private DBCommandTransactionalExecutor dbCommandExecutor;
	private AuthorDaoImpl authorDao;

	@Before
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();

		authorDao = new AuthorDaoImpl();
		authorDao.em = em;

		dbCommandExecutor = new DBCommandTransactionalExecutor(em);
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

	@Test
	public void addAuthorAndFindIt() {
		final Long authorAddedId = dbCommandExecutor.executeCommand(() -> {
			return authorDao.add(robertMartin()).getId();
		});
		assertThat(authorAddedId, is(notNullValue()));

		final AuthorDTO author = authorDao.findById(authorAddedId);
		assertThat(author, is(notNullValue()));
		assertThat(author.getName(), is(equalTo(robertMartin().getName())));
	}

	@Test
	public void findAuthorByIdNotFound() {
		final AuthorDTO author = authorDao.findById(999L);
		assertThat(author, is(nullValue()));
	}

	@Test
	public void updateAuthor() {
		final Long authorAddedId = dbCommandExecutor.executeCommand(() -> {
			return authorDao.add(robertMartin()).getId();
		});
		assertThat(authorAddedId, is(notNullValue()));

		final AuthorDTO author = authorDao.findById(authorAddedId);
		assertThat(author.getName(), is(equalTo(robertMartin().getName())));

		author.setName("Uncle Bob");
		dbCommandExecutor.executeCommand(() -> {
			authorDao.update(author);
			return null;
		});

		final AuthorDTO authorAfterUpdate = authorDao.findById(authorAddedId);
		assertThat(authorAfterUpdate.getName(), is(equalTo("Uncle Bob")));
	}

	@Test
	public void existsById() {
		final Long authorAddedId = dbCommandExecutor.executeCommand(() -> {
			return authorDao.add(robertMartin()).getId();
		});

		assertThat(authorDao.existsById(authorAddedId), is(equalTo(true)));
		assertThat(authorDao.existsById(999l), is(equalTo(false)));
	}

	@Test
	public void findByFilterNoFilter() {
		loadDataForFindByFilter();

		final PaginatedData<AuthorDTO> result = authorDao.findByFilter(new AuthorFilter());
		assertThat(result.getNumberOfRows(), is(equalTo(4)));
		assertThat(result.getRows().size(), is(equalTo(4)));
		assertThat(result.getRow(0).getName(), is(equalTo(erichGamma().getName())));
		assertThat(result.getRow(1).getName(), is(equalTo(jamesGosling().getName())));
		assertThat(result.getRow(2).getName(), is(equalTo(martinFowler().getName())));
		assertThat(result.getRow(3).getName(), is(equalTo(robertMartin().getName())));
	}

	@Test
	public void findByFilterFilteringByNameAndPaginatingAndOrderingDescending() {
		loadDataForFindByFilter();

		final AuthorFilter authorFilter = new AuthorFilter();
		authorFilter.setName("o");
		authorFilter.setPaginationData(new PaginationData(0, 2, "name", OrderMode.DESCENDING));

		PaginatedData<AuthorDTO> result = authorDao.findByFilter(authorFilter);
		assertThat(result.getNumberOfRows(), is(equalTo(3)));
		assertThat(result.getRows().size(), is(equalTo(2)));
		assertThat(result.getRow(0).getName(), is(equalTo(robertMartin().getName())));
		assertThat(result.getRow(1).getName(), is(equalTo(martinFowler().getName())));

		authorFilter.setPaginationData(new PaginationData(2, 2, "name", OrderMode.DESCENDING));
		result = authorDao.findByFilter(authorFilter);

		assertThat(result.getNumberOfRows(), is(equalTo(3)));
		assertThat(result.getRows().size(), is(equalTo(1)));
		assertThat(result.getRow(0).getName(), is(equalTo(jamesGosling().getName())));

	}

	private void loadDataForFindByFilter() {
		dbCommandExecutor.executeCommand(() -> {
			authorDao.add(robertMartin());
			authorDao.add(jamesGosling());
			authorDao.add(martinFowler());
			authorDao.add(erichGamma());

			return null;
		});
	}

}