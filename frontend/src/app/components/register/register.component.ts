import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import {
  FormGroup,
  FormBuilder,
  FormArray,
  Validators
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit, OnDestroy {
  registrationFormGroup!: FormGroup;
  creationModel: any = {};
  isSuccessful = false;
  isSignUpFailed = false;

  authServicerSub!: Subscription;

  constructor(
    private authService: UserService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.registrationFormGroup = this.formBuilder.group(
      {
        firstName: [null, Validators.compose([Validators.required])],
        lastName: [null, Validators.compose([Validators.required])],
        email: [
          '',
          {
            validators: Validators.compose([
              Validators.required,
              Validators.email,
            ]),
            updateOn: 'blur'
          }
        ]
      }
    );
  }

  get registerFormControl() {
    return this.registrationFormGroup.controls;
  }

  onSubmit() {
    // this.submitted = true;
    this.creationModel = this.registrationFormGroup.value;

    this.authServicerSub = this.authService
      .createNewUser(this.creationModel)
      .pipe(first())
      .subscribe(
        () => {
          this.isSuccessful = true;
          this.isSignUpFailed = false;
          this.router.navigate(['../login'], { relativeTo: this.route });
        },
        (error) => {
          alert(error.error.message);
          this.isSignUpFailed = true;
        }
      );
  }
  
  ngOnDestroy(): void {
    if(this.authServicerSub){
      this.authServicerSub.unsubscribe();
    }
  }
}
