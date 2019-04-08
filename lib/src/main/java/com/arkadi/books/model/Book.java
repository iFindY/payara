package com.arkadi.books.model;


import io.swagger.v3.oas.annotations.media.Schema;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Schema(description = "Book resource representation")
public class Book {

    @Id
    @GeneratedValue
    @Schema(description = "Identifier")
    private Long id;

    @Column(length = 200)
    @NotNull
    @Size(min = 1, max = 200)
    @Schema(description = "Title of the book")
    private String title;

    @Column(length = 10000)
    @Size(min = 1, max = 10000)
    @Schema(description = "Summary describing the book")
    private String description;

    @Column(name = "unit_cost")
    @Min(1)
    @Schema(description = "Unit cost")
    private Float unitCost;

    @Column(length = 50)
    @NotNull
    @Size(min = 1, max = 50)
    @Schema(description = "ISBN number")
    private String isbn;

    @Column(name = "publication_date")
    @Temporal(TemporalType.DATE)
    @Past
    @Schema(description = "Date in which the book has been published")
    private Date publicationDate;

    @Column(name = "nb_of_pages")
    @Schema(description = "Number of pages",maximum = "5000",pattern = "\\d+")
    private Integer nbOfPages;

    @Column(name = "image_url")
    @Schema(description = "URL of the image cover")
    private String imageURL;

    @Enumerated
    @Schema(description = "Language in which the book has been written")
    private Language language;

    public Book() {
    }

    public Book(String isbn, String title, Float unitCost, Integer nbOfPages, Language language, Date publicationDate, String imageURL, String description) {
        this.isbn = isbn;
        this.title = title;
        this.unitCost = unitCost;
        this.nbOfPages = nbOfPages;
        this.language = language;
        this.publicationDate = publicationDate;
        this.imageURL = imageURL;
        this.description = description;
    }

    // ======================================
    // =        Getters and Setters         =
    // ======================================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Float unitCost) {
        this.unitCost = unitCost;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Integer getNbOfPages() {
        return nbOfPages;
    }

    public void setNbOfPages(Integer nbOfPages) {
        this.nbOfPages = nbOfPages;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imagURL) {
        this.imageURL = imagURL;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", unitCost=" + unitCost +
                ", isbn='" + isbn + '\'' +
                ", publicationDate=" + publicationDate +
                ", language=" + language +
                '}';
    }
}
