package com.arkadi.books.repository;



import com.arkadi.books.model.Book;
import com.arkadi.books.utility.NumberGenerator;
import com.arkadi.books.utility.TextUtil;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

import static javax.transaction.Transactional.TxType.*;


@Transactional(SUPPORTS)
public class BookRepository {

    @PersistenceContext(unitName = "bookStorePU")
    private EntityManager em;

    @Inject
    private TextUtil textUtil;

    @Inject
    private NumberGenerator generator;

    public Book find(@NotNull Long id) {
        return em.find(Book.class, id);
    }

    @Transactional(REQUIRED)
    public Book create(@NotNull Book book) {
        book.setTitle(textUtil.sanitize(book.getTitle()));
        book.setIsbn(generator.generateNumber());
        em.persist(book);
        return book;
    }

    @Transactional(REQUIRED)
    public void delete(@NotNull  Long id) {
        em.remove(em.getReference(Book.class, id));
    }

    @Transactional
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b ORDER BY b.title DESC", Book.class);
        return query.getResultList();
    }

    @Transactional
    public Long countAll() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(b) FROM Book b", Long.class);
        return query.getSingleResult();
    }


}
