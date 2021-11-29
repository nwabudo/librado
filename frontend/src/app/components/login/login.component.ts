import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit, OnDestroy {
  form!: FormGroup;
  isLoggedIn = false;
  isLoginFailed = false;
  returnUrl: string = "";
  errorMessage = [''];
  submitted = false;
  authServicerSub: Subscription = new Subscription;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.isLoggedIn = this.tokenStorage.isLoggedIn();

    this.form = this.formBuilder.group({
      email:  [
        null,
        Validators.compose([
          Validators.required,
          Validators.email,
        ]),
      ]
    });

    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '';
    if (this.returnUrl.endsWith('login')) {
      this.returnUrl = '';
    }

    if (this.isLoggedIn) {
      this.redirectToHome();
    }
  }

  get f() {
    return this.form.controls;
  }

  onSubmit() {
    this.submitted = true;
    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }

    console.log(this.form);

    this.authServicerSub = this.authService.login(this.form.value).subscribe(
      (data) => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
      },
      (error) => {
        this.errorMessage = error.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  redirectToHome() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;

    this.router.navigateByUrl(this.router.url).then(() => {
      this.router.navigated = true;
      this.router.navigate([this.returnUrl]);
    });
  }

  ngOnDestroy(): void {
    if (this.authServicerSub) {
      this.authServicerSub.unsubscribe();
    }
  }
}
