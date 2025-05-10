package com.jicay.bookmanagement.domain.usecase

import com.jicay.bookmanagement.domain.model.Book
import com.jicay.bookmanagement.domain.port.BookPort
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class BookUseCaseTest : FunSpec({

    val bookPort = mockk<BookPort>()
    val bookUseCase = BookUseCase(bookPort)

    test("get all books should returns all books sorted by name") {
        every { bookPort.getAllBooks() } returns listOf(
            Book("Les Misérables", "Victor Hugo", false),
            Book("Hamlet", "William Shakespeare", false)
        )

        val res = bookUseCase.getAllBooks()

        res.shouldContainExactly(
            Book("Hamlet", "William Shakespeare", false),
            Book("Les Misérables", "Victor Hugo", false)
        )
    }

    test("add book") {
        justRun { bookPort.createBook(any()) }

        val book = Book("Les Misérables", "Victor Hugo", false)

        bookUseCase.addBook(book)

        verify(exactly = 1) { bookPort.createBook(book) }
    }

    test("reserve book") {
        val book = Book("Les Misérables", "Victor Hugo", false)
        
        every { bookPort.reserveBook(any()) } answers {
            val bookToReserve = firstArg<Book>()
            bookToReserve.reserved = true
        }

        bookUseCase.reserveBook(book)

        verify(exactly = 1) { bookPort.reserveBook(book) }
        book.reserved shouldBe true
    }

    test("should throw exception when trying to reserve an already reserved book") {
        val book = Book("Les Misérables", "Victor Hugo", true)
        
        shouldThrow<IllegalStateException> {
            bookUseCase.reserveBook(book)
        }
    }

})