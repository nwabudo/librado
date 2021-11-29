package io.core.libra.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@JsonRootName(value = "book")
@Relation(collectionRelation = "books", itemRelation = "item")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDTO extends RepresentationModel<BookDTO> {
	
	private String bookTitle;
	private String authorName;
	private String bookImageUrl;
	@EqualsAndHashCode.Include
	private String bookISBNCode;
	private Integer quantity;
}