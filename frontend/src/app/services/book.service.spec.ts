import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BookResponse, BookService } from './book.service';
import { environment } from 'src/environments/environment';
import { BorrowModel } from '../common/borrow-model';
import { ApiResponse } from '../common/api-response';
import { BookModel } from '../common/book-model';

export const API_URL = environment.apiBaseUrl + 'books';
export const PAGE_SIZE = 6;
export const mockBookResponse: BookResponse = {
  message: 'Operation was a Success',
  status: true,
  data: [
    new BookModel(),
    new BookModel(),
    new BookModel(),
    new BookModel(),
    new BookModel(),
    new BookModel(),
  ]
};

export const borrowListResponse: BookResponse = {
  message: 'Operation was a Success',
  status: true,
  data: [
    new BookModel(),
    new BookModel()
  ]
}

export const apiResponse: ApiResponse = {
  status: true,
  message: 'Operation was a Success'
}

describe('BookService', () => {
  let service: BookService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        BookService
    ]
    });
    service = TestBed.inject(BookService);
    httpTestingController = TestBed.get(HttpTestingController);

  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('fetchAllAvailableBooks should provide data and have 6 elements in the data', () => {
    let page = 0;
    const url = `${API_URL}?page=${page}&size=${PAGE_SIZE}`;

    service.fetchAllAvailableBooks(page).subscribe((bookResponse: BookResponse) => {
      expect(bookResponse.data).not.toBeNull();
      expect(bookResponse.data.length).toEqual(PAGE_SIZE);
    });

    const req = httpTestingController.expectOne(url);
    req.flush(mockBookResponse);
  });

  it('borrowAndReturnBook should provide data on borrow action', () => {
    let action = 'borrow';
    let borrowModel: BorrowModel = {
      isbnCode: 'ISBN3438092JO',
      userId: 1
    }
    const url = `${API_URL}/${action}`;

    service.borrowAndReturnBook(borrowModel, action).subscribe((apiResponse: ApiResponse) => {
      expect(apiResponse.data).not.toBeNull();
      expect(apiResponse.data).not.toBeNull();
      expect(apiResponse.status).not.toBeNull();
    });

    const req = httpTestingController.expectOne(url);
    req.flush(apiResponse);
  });

  it('borrowAndReturnBook should provide data on return action', () => {
    let action = 'return';
    let borrowModel: BorrowModel = {
      isbnCode: 'ISBN3438092JO',
      userId: 1
    }
    const url = `${API_URL}/${action}`;

    service.borrowAndReturnBook(borrowModel, action).subscribe((apiResponse: ApiResponse) => {
      expect(apiResponse.data).not.toBeNull();
      expect(apiResponse.data).not.toBeNull();
      expect(apiResponse.status).not.toBeNull();
    });

    const req = httpTestingController.expectOne(url);
    req.flush(apiResponse);
  });

  it('fetchAllBorrowedBooks should provide data', () => {
    let userId = 1;

    const url = `${API_URL}/borrowed?userId=${userId}`;

    service.fetchAllBorrowedBooks(userId).subscribe((bookResponse: BookResponse) => {
      expect(bookResponse.data).not.toBeNull();
      expect(bookResponse.data.length).toEqual(2);
      expect(bookResponse.status).not.toBeNull();
    });

    const req = httpTestingController.expectOne(url);
    req.flush(borrowListResponse);
  });

});
