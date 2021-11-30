import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ApiResponse } from 'src/app/common/api-response';
import { BookModel } from 'src/app/common/book-model';
import { BorrowModel } from 'src/app/common/borrow-model';
import { BookResponse, BookService } from 'src/app/services/book.service';
import { environment } from 'src/environments/environment';

const USER_ID = environment.userId;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  books: BookModel[] = []
  currentPage = 1;
  bookServiceSub: Subscription = new Subscription;

  constructor(
    private bookService: BookService
  ) {}

  ngOnInit(): void {
    this.listBooks();
  }

  listBooks() {
    this.bookServiceSub = this.bookService.fetchAllAvailableBooks(this.currentPage - 1)
      .subscribe(this.processPaginate());
  }

  processPaginate(): any {
    return (res: BookResponse) => {
      if (res.data && res.status) {
        this.books = res.data;
      }
    }
  }

  borrowAndReturnBook(book: BookModel, action: string) {

    const borrowModel = <BorrowModel>({
      userId: USER_ID,
      isbnCode: book.bookISBNCode
    });
    this.bookServiceSub = this.bookService.borrowAndReturnBook(borrowModel, action)
      .subscribe(
        this.extractMessage(),
        (error) => {
          alert(error.error.message);
        }
      );
  }

  extractMessage() {
    return (res: ApiResponse) => {
      if (res.status) {
        alert(res.message);
        return;
      }
      alert(res.message);
    }
  }

  ngOnDestroy(): void {
    if (this.bookServiceSub) {
      this.bookServiceSub.unsubscribe();
    }
  }

}
