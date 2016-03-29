package com.library.app.common.entities;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;

import com.library.app.dto.AuthorDTO;

@Ignore
public class AuthorDtoForDaoTest {

	public static AuthorDTO robertMartin() {
		return new AuthorDTO("Robert Martin");
	}

	public static AuthorDTO jamesGosling() {
		return new AuthorDTO("James Gosling");
	}

	public static AuthorDTO martinFowler() {
		return new AuthorDTO("Martin Fowler");
	}

	public static AuthorDTO erichGamma() {
		return new AuthorDTO("Erich Gamma");
	}

	public static AuthorDTO richardHelm() {
		return new AuthorDTO("Richard Helm");
	}

	public static AuthorDTO ralphJohnson() {
		return new AuthorDTO("Ralph Johnson");
	}

	public static AuthorDTO johnVlissides() {
		return new AuthorDTO("John Vlissides");
	}

	public static AuthorDTO kentBeck() {
		return new AuthorDTO("Kent Beck");
	}

	public static AuthorDTO johnBrant() {
		return new AuthorDTO("John Brant");
	}

	public static AuthorDTO williamOpdyke() {
		return new AuthorDTO("William Opdyke");
	}

	public static AuthorDTO donRoberts() {
		return new AuthorDTO("Don Roberts");
	}

	public static AuthorDTO joshuaBloch() {
		return new AuthorDTO("Joshua Bloch");
	}

	public static List<AuthorDTO> allAuthors() {
		return Arrays.asList(robertMartin(), jamesGosling(), martinFowler(), erichGamma(), richardHelm(),
				ralphJohnson(), johnVlissides(), kentBeck(), johnBrant(), williamOpdyke(), donRoberts(), joshuaBloch());
	}

	public static AuthorDTO authorWithId(final AuthorDTO author, final Long id) {
		author.setId(id);
		return author;
	}

}