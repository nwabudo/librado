import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ApiResponse } from '../common/api-response';
import { BookModel } from '../common/book-model';
import { BorrowModel } from '../common/borrow-model';

const API_URL = environment.apiBaseUrl + 'book';
const PAGE_SIZE = 6;
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};


@Injectable({
  providedIn: 'root'
})
export class BookService {

  constructor(
    private http: HttpClient){}

  fetchAllAvailableBooks(page: number): Observable<BookResponse>{
    const url = `${API_URL}?page=${page}&size=${PAGE_SIZE}`;
    return this.http.get<BookResponse>(url);
  }

  borrowAndReturnBook(borrowModel: BorrowModel, action: string): Observable<ApiResponse> {
    const url = `${API_URL}/${action}`;
    return this.common(url, borrowModel);
  }

  fetchAllBorrowedBooks(userId: number): Observable<BookResponse>{
    const url = `${API_URL}/borrow/${userId}`;
    return this.http.get<BookResponse>(url);
  }

  common(url: string, borrowModel: BorrowModel): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(
      url,
      {
        isbnCode: borrowModel.isbnCode,
        userId: borrowModel.userId,
      },
      httpOptions
    );
  }

}
  
export interface BookResponse {
  message: string;
  status: boolean;
  data:{
    content: BookModel[];
    page: {
      // Current Size per Page
      size: number;
      totalElements: number;
      totalPages: number;
      number: number;
    };
  }
  
}