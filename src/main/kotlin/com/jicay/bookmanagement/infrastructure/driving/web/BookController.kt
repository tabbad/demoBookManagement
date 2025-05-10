package com.jicay.bookmanagement.infrastructure.driving.web

import com.jicay.bookmanagement.domain.usecase.BookUseCase
import com.jicay.bookmanagement.infrastructure.driving.web.dto.BookDTO
import com.jicay.bookmanagement.infrastructure.driving.web.dto.toDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PatchMapping 
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.ExceptionHandler

@RestController
@RequestMapping("/books")
class BookController(
    private val bookUseCase: BookUseCase
) {
    @CrossOrigin
    @GetMapping
    fun getAllBooks(): List<BookDTO> {
        return bookUseCase.getAllBooks()
            .map { it.toDto() }
    }

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@RequestBody bookDTO: BookDTO) {
        bookUseCase.addBook(bookDTO.toDomain())
    }

    @CrossOrigin
    @PatchMapping 
    @ResponseStatus(HttpStatus.OK)
    fun reserveBook(@RequestBody bookDTO: BookDTO) {
        bookUseCase.reserveBook(bookDTO.toDomain())
    }

    @ExceptionHandler(IllegalStateException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalStateException(e: IllegalStateException) {
        // Optionnel : log ou retourner un message
    }
}