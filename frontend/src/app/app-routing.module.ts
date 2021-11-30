import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BorrowedListComponent } from './components/borrowed-list/borrowed-list.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { RegisterComponent } from './components/register/register.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  {
    path: 'home', component: HomeComponent
  },
  {
    path: 'borrow-list', component: BorrowedListComponent
  },
  {
    path: 'login', component: LoginComponent
  },
  {
    path: 'signup', component: RegisterComponent
  },
  {
    path: '**', component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
