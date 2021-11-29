import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse } from '../common/api-response';

const API_URL = environment.apiBaseUrl + 'auth';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {}

  isEmailExisting(email: string): Observable<boolean> {
    const searchUrl = `${API_URL}/search?email=${email}`;
    
    return this.http.get<ApiResponse>(searchUrl)
    .pipe(map((response) => response.status));
  }

}
