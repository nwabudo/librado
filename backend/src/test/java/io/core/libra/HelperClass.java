package io.core.libra;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.core.libra.dtos.BookDTO;
import io.core.libra.entity.Book;

public final class HelperClass {

    private HelperClass() {
    }

    /**
     * convert object to json string.
     * @param obj object
     * @return String
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BookDTO formBookDTO(String isbnCode){
        return new BookDTO("Amazing Stories",
                "Emmanuel Nwabudo", "",
                isbnCode, 20);
    }

    public static Book formBook(String isbnCode, int quantity){
        return new Book("Amazing Stories",
                "Emmanuel Nwabudo", "",
                isbnCode, quantity);
    }

}