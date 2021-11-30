import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UserModel } from 'src/app/common/user-model';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit, OnDestroy {
  form!: FormGroup;
  isLoggedIn = false;
  errorMessage = [''];
  submitted = false;
  authServicerSub: Subscription = new Subscription;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: UserService,
    private tokenStorage: TokenStorageService,
    private route: ActivatedRoute,
  ) { }

  ngOnInit() {

    this.isLoggedIn = !!this.tokenStorage.getToken();

    this.form = this.formBuilder.group({
      email: [
        null,
        Validators.compose([
          Validators.required,
          Validators.email,
        ]),
      ]
    });

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

    this.authServicerSub = this.authService.findByEmail(this.form.controls.email.value).subscribe(
      (data: UserModel) => {
        this.tokenStorage.saveToken(data.email);
        this.tokenStorage.saveUser(data);

        this.isLoggedIn = true;
        this.reloadPage();
      },
      (error) => {
        alert(error.error.message);
      }
    );
  }

  reloadPage() {
    window.location.reload();
  }

  redirectToHome() {
    this.router.navigateByUrl('/home');
  }

  ngOnDestroy(): void {
    if (this.authServicerSub) {
      this.authServicerSub.unsubscribe();
    }
  }
}
