package io.core.libra.dtos.assembler;

import io.core.libra.controller.LibraryController;
import io.core.libra.dtos.BookDTO;
import io.core.libra.entity.Book;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookAssembler extends RepresentationModelAssemblerSupport<Book, BookDTO> {

	public BookAssembler() {
		super(LibraryController.class, BookDTO.class);
	}

	@Override
	public BookDTO toModel(Book entity) {
		if(entity == null) return null;

		BookDTO model = instantiateModel(entity);
		model.setBookImageUrl(entity.getBookImageUrl());
		model.setAuthorName(entity.getAuthorName());
		model.setBookTitle(entity.getBookTitle());
		model.setBookISBNCode(entity.getBookISBNCode());
		model.setQuantity(entity.getQuantity());

		model.add(linkTo(methodOn(LibraryController.class).getByBookISBNCode(entity.getBookISBNCode())).withSelfRel());
		return model;
	}

	@Override
	public CollectionModel<BookDTO> toCollectionModel(Iterable<? extends Book> entities) {
		CollectionModel<BookDTO> bookModels = super.toCollectionModel(entities);
		bookModels.add(linkTo(methodOn(LibraryController.class).getAllBooks(0, 20)).withSelfRel());
		return bookModels;
	}

	private List<BookDTO> toBookDTO(List<Book> bookList) {
		if (bookList == null || bookList.isEmpty())
			return Collections.emptyList();

		return bookList.stream()
				.map(book -> BookDTO.builder()
						.bookISBNCode(book.getBookISBNCode())
						.bookTitle(book.getBookTitle())
						.bookImageUrl(book.getBookImageUrl())
						.authorName(book.getAuthorName())
						.quantity(book.getQuantity())
						.build()
						.add(linkTo(
							methodOn(LibraryController.class)
									.getByBookISBNCode(book.getBookISBNCode()))
							.withSelfRel()))
				.collect(Collectors.toList());
	}

}
