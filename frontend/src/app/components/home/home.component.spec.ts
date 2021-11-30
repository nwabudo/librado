import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BookModel } from 'src/app/common/book-model';
import { BorrowModel } from 'src/app/common/borrow-model';
import { HttpRequestInterceptorMock } from 'src/app/interceptors/http-request-interceptor-mock';
import { mockBookResponse } from 'src/app/services/book.service.spec';

import { HomeComponent } from './home.component';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomeComponent ],
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useClass: HttpRequestInterceptorMock,
          multi: true
        }
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  
  it('should populate the books after Angular calls ngOnInit', () => {
    component.ngOnInit();
    expect(component.books).toEqual(mockBookResponse.data);
  });
  
  it('should check that the subscription is closed after ngOnDestroy', () => {
    component.ngOnDestroy();
    expect(component.bookServiceSub.closed).toEqual(true);
  });

});
