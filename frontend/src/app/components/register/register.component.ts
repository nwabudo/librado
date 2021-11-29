import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { ValidatorService } from 'src/app/services/validator.service';
import {
  FormGroup,
  FormBuilder,
  FormArray,
  Validators
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { Subscription } from 'rxjs';

// const emailRegex = '^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit, OnDestroy {
  form: any = {};
  registrationFormGroup!: FormGroup;
  addresses: FormArray = this.formBuilder.array([]);
  creationModel: any = {};
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = [''];

  authServicerSub!: Subscription;

  constructor(
    private authService: AuthService,
    private validatorService: ValidatorService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
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
            asyncValidators: [this.validatorService.emailValidator()],
            updateOn: 'blur'
          }
        ]
      }
    );
  }

  get registerFormControl() {
    return this.registrationFormGroup.controls;
  }

  /* Error handling */
  public handleError = (controlName: string, errorName: string) => {
    return this.registerFormControl[controlName].hasError(errorName);
  };

  onSubmit() {
    // this.submitted = true;
    this.creationModel = this.registrationFormGroup.value;
    console.log(this.creationModel);

    this.authServicerSub = this.authService
      .register(this.creationModel)
      .pipe(first())
      .subscribe(
        (data) => {
          console.log(data);
          this.isSuccessful = true;
          this.isSignUpFailed = false;
          this.router.navigate(['../login'], { relativeTo: this.route });
        },
        (error) => {
          this.errorMessage = error.error.message;
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
