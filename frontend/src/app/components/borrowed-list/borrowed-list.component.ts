import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { BookModel } from 'src/app/common/book-model';
import { BookResponse, BookService } from 'src/app/services/book.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-borrowed-list',
  templateUrl: './borrowed-list.component.html',
  styleUrls: ['./borrowed-list.component.scss']
})
export class BorrowedListComponent implements OnInit {

  books: BookModel[] = [];
  bookServiceSub: Subscription = new Subscription;
  
  constructor(
    private tokenStorageService: TokenStorageService,
    private bookService: BookService) { }

  ngOnInit(): void {
    this.fetchBorrowedBook();
  }

  fetchBorrowedBook() {
    const userId: number = this.tokenStorageService.getUser().id;
    this.bookServiceSub = this.bookService.fetchAllBorrowedBooks(userId)
      .subscribe(this.processPaginate());
  }

  processPaginate(): any{
    return (res: BookResponse) => {
      if (res.data && res.status) {
        this.books = res.data.content;
      }
    }
  }

}
