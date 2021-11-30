import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ApiResponse } from 'src/app/common/api-response';
import { BookModel } from 'src/app/common/book-model';
import { BorrowModel } from 'src/app/common/borrow-model';
import { BookResponse, BookService } from 'src/app/services/book.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  books: BookModel[] = []
  isLoggedIn = false;
  currentPage = 1;
  bookServiceSub: Subscription = new Subscription;

  constructor(
    private bookService: BookService,
    private tokenStorageService: TokenStorageService,
    private router: Router
  ) {

  }

  ngOnInit(): void {
    this.listBooks();
    this.isLoggedIn = !!this.tokenStorageService.getToken();
  }

  listBooks() {
    this.bookServiceSub = this.bookService.fetchAllAvailableBooks(this.currentPage - 1)
      .subscribe(this.processPaginate());
  }

  processPaginate(): any {
    return (res: BookResponse) => {
      if (res.data && res.status) {
        this.books = res.data.content;
      }
    }
  }

  borrowAndReturnBook(book: BookModel, action: string) {

    if(!this.isLoggedIn){
      alert(`Kindly Login before continuing ${action} action`);
      return;
    }

    const userId: number = this.tokenStorageService.getUser().id;
    const borrowModel = <BorrowModel>({
      userId: userId,
      isbnCode: book.bookISBNCode
    });
    console.log(borrowModel);
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
