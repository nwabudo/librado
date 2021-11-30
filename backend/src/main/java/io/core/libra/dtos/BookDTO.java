package io.core.libra.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class BookDTO {
	
	private String bookTitle;
	private String authorName;
	private String bookImageUrl;
	@EqualsAndHashCode.Include
	private String bookISBNCode;
	private Integer quantity;
}