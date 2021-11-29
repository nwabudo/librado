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
  roles: string[] = [];
  returnUrl: string = "";
  submitted = false;

  authServicerSub: Subscription = new Subscription;
  routeService: any;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.isLoggedIn = this.tokenStorage.isLoggedIn();
    if (this.isLoggedIn) {
      this.roles = this.tokenStorage.getUser().roles;
    }
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
    if (this.form!.invalid) {
      return;
    }

    console.log(this.form);

    this.authServicerSub = this.authService.login(this.form.value).subscribe(
      (data) => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        this.returnUrl = this.routeService.getPreviousUrl() || '';
        this.reloadPage();
      },
      (error) => {
        this.isLoginFailed = true;
      }
    );
  }

  reloadPage() {
    window.location.reload();
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
