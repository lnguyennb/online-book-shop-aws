package com.kas.online_book_shop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kas.online_book_shop.exception.ISBNDuplicateException;
import com.kas.online_book_shop.exception.ResourceNotFoundException;
import com.kas.online_book_shop.model.Book;
import com.kas.online_book_shop.model.BookCategory;
import com.kas.online_book_shop.model.BookCollection;
import com.kas.online_book_shop.model.OrderState;
import com.kas.online_book_shop.repository.BookRepository;
import com.kas.online_book_shop.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    @Override
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book saveBook(Book book) {
        if (bookRepository.existsByISBN(book.getISBN())) {
            throw new ISBNDuplicateException("Mã ISBN không thể bị trùng.");
        }
        return bookRepository.save(book);
    }

    @Override
    public void DeleteBook(Long id) {
        var existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách để xóa"));
        existingBook.getOrderDetails().forEach((x) -> {
            if (x.getOrder().getOrderState() == OrderState.ORDER) 
                x.setBook(null); 
        });
        bookRepository.deleteById(id);
    }

    @Override
    public Book updateBook(Book book) {
        bookRepository.findById(book.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách để câp nhật"));
        return bookRepository.save(book);
    }

    @Override
    public Page<Book> getBookByCategoriesAndPriceRange(List<BookCategory> categories, int min, int max,
            Pageable pageable) {
        return bookRepository.findByCategoryInAndPriceBetween(categories, min, max, pageable);

    }

    @Override
    public Page<Book> getBookByCollectionAndPriceRanges(BookCollection collection, int min, int max,
            Pageable pageable) {
        return bookRepository.findByCollectionsAndPriceBetween(collection, min, max, pageable);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
