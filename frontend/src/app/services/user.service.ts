import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse } from '../common/api-response';
import { UserModel } from '../common/user-model';

const API_URL = environment.apiBaseUrl + 'auth';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) {}

  isEmailExisting(email: string): Observable<boolean> {
    const searchUrl = `${API_URL}/search?email=${email}`;
    
    return this.http.get<ApiResponse>(searchUrl)
    .pipe(map((response) => response.status));
  }

  findByEmail(email: string): Observable<UserModel> {
    const searchUrl = `${API_URL}/search?email=${email}`;
    
    return this.http.get<ApiResponse>(searchUrl)
    .pipe(map((response) => response.data));
  }

  createNewUser(userModel: UserModel): Observable<UserModel>{
    return this.http.post<ApiResponse>(
      API_URL + '/signup',
      {
        email: userModel.email,
        firstName: userModel.firstName,
        lastName: userModel.lastName
      },
      this.httpOptions
    ).pipe(map((response) => response.data));
  }
  
}
