import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { LoginModel, CreationModel } from '../common/login-model';
import { Observable } from 'rxjs';

const AUTH_API = environment.apiBaseUrl + 'auth/';
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  login(credentials: LoginModel): Observable<any> {
    return this.http.post(
      AUTH_API + 'login',
      {
        email: credentials.email,
      },
      httpOptions
    );
  }

  register(credentials: CreationModel): Observable<any> {
    return this.http.post(
      AUTH_API + 'signup',
      {
        firstName: credentials.firstName,
        lastName: credentials.lastName,
        email: credentials.email
      },
      httpOptions
    );
  }
}
