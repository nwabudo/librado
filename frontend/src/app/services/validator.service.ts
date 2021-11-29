import { Injectable } from '@angular/core';
import { UserService } from './user.service';
import { ValidationErrors, ValidatorFn, AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ValidatorService {

  constructor(private userService: UserService) {}

  emailValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return this.validateEmail(control.value).pipe(
        map(res => {
          // if res is true, email exists, return true
          return res ? { emailExists: true } : null;
          // NB: Return null if there is no error
        })
      );
    };
  
  }

  validateEmail(userNameEmail: string): Observable<boolean> {
      return this.userService.isEmailExisting(userNameEmail);
  }
}
