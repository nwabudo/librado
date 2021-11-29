import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root',
})
export class TokenStorageService {

  userSubject: BehaviorSubject<any>;
  user: Observable<any>;

  loggedIn = new BehaviorSubject<boolean>(false);
  token: string = "";

  constructor(private router: Router) {

    this.userSubject = new BehaviorSubject<any>(
      this.getUser()
    );
    this.user = this.userSubject.asObservable();

    const userData = this.userSubject.value;
    if (userData) {
      this.token = userData.email;
      this.loggedIn.next(true);
    }
  }

  signOut() {
    localStorage.removeItem(USER_KEY);
    this.userSubject.next(null);

    localStorage.removeItem(TOKEN_KEY);
    this.loggedIn.next(false);

    this.router.navigate(['/account/login']);
  }

  public saveToken(token: string) {
    window.localStorage.removeItem(TOKEN_KEY);
    window.localStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string {
    return window.localStorage.getItem(TOKEN_KEY) !;
  }

  public saveUser(user: any) {
    window.localStorage.removeItem(USER_KEY);
    window.localStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser() {
    return JSON.parse(localStorage.getItem(USER_KEY) !);
  }

  public isLoggedIn(): boolean{
    return this.loggedIn.value;
  }
}
