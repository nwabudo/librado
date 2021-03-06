import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ApiResponse } from '../common/api-response';
import { BookModel } from '../common/book-model';
import { BorrowModel } from '../common/borrow-model';

const API_URL = environment.apiBaseUrl + 'books';
const PAGE_SIZE = 6;
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};


@Injectable({
  providedIn: 'root'
})
export class BookService {

  constructor(
    private http: HttpClient) { }

  fetchAllAvailableBooks(page: number): Observable<BookResponse> {
    const url = `${API_URL}?page=${page}&size=${PAGE_SIZE}`;
    return this.http.get<BookResponse>(url);
  }

  borrowAndReturnBook(borrowModel: BorrowModel, action: string): Observable<ApiResponse> {
    const url = `${API_URL}/${action}`;
    return this.http.put<ApiResponse>(
      url,
      {
        isbnCode: borrowModel.isbnCode,
        userId: borrowModel.userId,
      },
      httpOptions
    );
  }

  fetchAllBorrowedBooks(userId: number): Observable<BookResponse> {
    const url = `${API_URL}/borrowed?userId=${userId}`;
    return this.http.get<BookResponse>(url);
  }

}

export interface BookResponse {
  message: string;
  status: boolean;
  data: BookModel[]
}