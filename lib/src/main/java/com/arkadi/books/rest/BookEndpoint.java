package com.arkadi.books.rest;


import com.arkadi.books.model.Book;
import com.arkadi.books.repository.BookRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;


//TODO response headers only for dev purposes. security issue. remove  for production
@OpenAPIDefinition(
        info = @Info(
                title = "Bookstore APIs",
                description = "Bookstore APIs exposed from a Java EE back-end to an Angular front-end",
                version = "V1.0.0",
                contact = @Contact(
                        name = "Arkadi Daschkewitsch",
                        email = "arkadi.daschkewitsch@gmail.com",
                        url = "https://my.instagram"),
                license = @License(
                        name = "arkadi",
                        url = "arkadi.com")
        ),
        servers = {
                @Server(url = "https://localhost:8080", description = "book api"),
                @Server(url = "https://localhost.test:8080", description = "book api for testing")
        },
        tags = {
                @Tag(name = "Book", description = "Tag grouping stuff together")
        },
        externalDocs = @ExternalDocumentation(
                description = "xwiki link",
                url = "https//:arkadi.pim.wiki.de"))
@Path("/books")
@Provider
public class BookEndpoint {

    @Inject
    private BookRepository bookRepository;

    @POST
    @Consumes(APPLICATION_JSON)
    @Operation(description = "Creates a book given a JSon Book representation", summary = "create a book", tags = "Book",
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, required = true, name = "book", schema = @Schema(implementation = Book.class))},
            responses = {
                    @ApiResponse(responseCode = "201", description = "The book is created"),
                    @ApiResponse(responseCode = "415", description = "Format is not JSon")
            })
    public Response createBook(@NotNull Book book, @Context UriInfo uriInfo) {
        book = bookRepository.create(book);
        URI createdURI = uriInfo.getBaseUriBuilder().path(book.getId().toString()).build();
        return Response.created(createdURI).build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    @Operation(summary = "Deletes a book given an id", description = "deletes a book given an id, this operation cant ak more time then expected",
            parameters = {
                    @Parameter(ref = "parameters.json#/id")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Book has been deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input. Id cannot be lower than 1"),
                    @ApiResponse(responseCode = "500", description = "Book not found")

            })
    public Response deleteBook(@PathParam("id") @Min(1) @Max(10) Long id) {
        bookRepository.delete(id);
        return Response.noContent().allow("DELETE").build();
    }

    @GET
    @Path("/{id : \\d+}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "returns a book", description = "Returns a book given an id",
            parameters = {
                    @Parameter(ref = "parameters.json#/id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book found", content = @Content(
                            mediaType = APPLICATION_JSON, schema = @Schema(implementation = Book.class))),
                    @ApiResponse(responseCode = "404", description = "Book not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input. Id cannot be lower than 1")})
    public Response getBook(@PathParam("id") @Min(1) Long id) {
        Book book = bookRepository.find(id);

        if (book == null)
            return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").build();

        return Response.ok(book).header("Access-Control-Allow-Origin", "*").build();
    }


    @GET
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Returns all the books", description = "return all books in a json format without author personal data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Books found", content = @Content(
                            mediaType = APPLICATION_JSON,
                            array = @ArraySchema(schema = @Schema(implementation = Book.class)))),
                    @ApiResponse(responseCode = "204", description = "No books found")})
    public Response getBooks() {
        List<Book> books = bookRepository.findAll();

        if (books.size() == 0)
            return Response.status(Response.Status.NO_CONTENT).build();

        return Response.ok(books).build();
    }

    @GET
    @Path("/count")
    @Produces(TEXT_PLAIN)
    @Operation(summary = "Returns the number of books",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Number of books found"),
                    @ApiResponse(responseCode = "204", description = "No books found")})
    public Response countBooks() {
        Long nbOfBooks = bookRepository.countAll();

        if (nbOfBooks == 0)
            return Response.status(Response.Status.NO_CONTENT).build();

        return Response.ok(nbOfBooks).build();
    }
}

