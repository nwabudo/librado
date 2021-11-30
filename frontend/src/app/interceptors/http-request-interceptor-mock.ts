import { Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';

import { mockBookResponse, borrowListResponse, apiResponse, API_URL, PAGE_SIZE } from '../services/book.service.spec';

@Injectable()
export class HttpRequestInterceptorMock implements HttpInterceptor {
    getBorrowedBookUrl = `${API_URL}/borrowed?userId=1`;
    borrowActionUrl = `${API_URL}/borrow`;
    returnActionUrl = `${API_URL}/return`;
    getAllAvailableBooksUrl = `${API_URL}?page=0&size=${PAGE_SIZE}`;

    constructor(private injector: Injector) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
   
        if (request.url && request.url.indexOf(this.getBorrowedBookUrl) > -1) {
            return of(new HttpResponse({ status: 200, body: borrowListResponse }));
        }

        if (request.url && request.url.indexOf(this.borrowActionUrl) > -1) {
            return of(new HttpResponse({ status: 200, body: apiResponse }));
        }

        if (request.url && request.url.indexOf(this.returnActionUrl) > -1) {
            return of(new HttpResponse({ status: 200, body: apiResponse }));
        }

        if (request.url && request.url.indexOf(this.getAllAvailableBooksUrl) > -1) {
            return of(new HttpResponse({ status: 200, body: mockBookResponse }));
        }

        return next.handle(request);
    }
}