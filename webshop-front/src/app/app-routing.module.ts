import { NgModule } from '@angular/core';
import { RegistrationComponent } from './components/registration/registration.component';
import { Routes, RouterModule } from '@angular/router';
import { EmailConfirmedComponent } from './components/email-confirmed/email-confirmed.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { NewPaperComponent } from './components/new-paper/new-paper.component';
import { UrednikRecenzentComponent } from './components/urednik-recenzent/urednik-recenzent.component';
import { ValidatedComponent } from './components/validated/validated.component';
import { AuthGuard } from './_guards';
import { AdminTasksComponent } from './components/admin-tasks/admin-tasks.component';
import { MyPapersComponent } from './components/my-papers/my-papers.component';

const routes: Routes = [
  { path : "", component : HomeComponent },
  { path : "login", component : LoginComponent },
  { path : "registration", component : RegistrationComponent },
  { path : "newPaper", component : NewPaperComponent, canActivate:[AuthGuard] },
  { path : "activate/:processId", component : EmailConfirmedComponent },
  { path : "validated", component : ValidatedComponent },
  { path : "newPaper/:processId", component : UrednikRecenzentComponent, canActivate:[AuthGuard] },
  { path : "tasks", component : AdminTasksComponent, canActivate:[AuthGuard] },
  { path : "myPapers", component : MyPapersComponent, canActivate:[AuthGuard] },
  { path : "correction/:processId", component : NewPaperComponent, canActivate:[AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
