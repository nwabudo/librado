import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpRequestInterceptorMock } from 'src/app/interceptors/http-request-interceptor-mock';
import { borrowListResponse } from 'src/app/services/book.service.spec';

import { BorrowedListComponent } from './borrowed-list.component';

describe('BorrowedListComponent', () => {
  let component: BorrowedListComponent;
  let fixture: ComponentFixture<BorrowedListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BorrowedListComponent ],
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
    fixture = TestBed.createComponent(BorrowedListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should populate the users borrowed books after Angular calls ngOnInit', () => {
    component.ngOnInit();
    expect(component.books).toEqual(borrowListResponse.data);
  });
  
  it('should check that the subscription is closed after ngOnDestroy', () => {
    component.ngOnDestroy();
    expect(component.bookServiceSub.closed).toEqual(true);
  });
  
});
