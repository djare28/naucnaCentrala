import { Component, OnInit } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { NgForm } from '@angular/forms';
import { elementStyleProp } from '@angular/core/src/render3';

@Component({
  selector: 'app-urednik-recenzent',
  templateUrl: './urednik-recenzent.component.html',
  styleUrls: ['./urednik-recenzent.component.scss']
})
export class UrednikRecenzentComponent implements OnInit {

  private readonly notifier: NotifierService;

  private body = {
    "recenzenti":[null,null],
    "urednici":[]
  }
    
  private hidden=false;
  
  private urednici = [];
  private uredniciList=[];

  private recenzenti = ["",""];
  private recenzentiList=[];

  private processId="";

  constructor(
    private activatedRoute: ActivatedRoute,
    private endpoints:EndpointsService,
    private router: Router,
    notifierService: NotifierService,
    private authenticationService:AuthenticationService
    ) {
    this.notifier = notifierService;
   }

  ngOnInit() {

    this.processId = this.activatedRoute.snapshot.paramMap.get('processId');
    this.getUrednikeRecenzente(this.processId);
  }

  getUrednikeRecenzente(processId){
    this.endpoints.getUrednikeRecenzente(processId).subscribe(
      res => {
          console.log(res);
          
          this.recenzentiList=res.recenzenti;
          this.uredniciList=res.urednici;
      },
      err => {
        console.log(err); 
      }
    )
  }

  addUrednik(){
    this.body.urednici.push(null);
    this.urednici.push("");
  }

  addRecenzent(){
    this.recenzenti.push(""); 
    this.body.recenzenti.push(null);
  }

  onSubmit(newPaperForm:NgForm){
    this.hidden=true;

    if(newPaperForm.valid===true){
      this.endpoints.submitUredniciRecenzenti(this.body,this.processId).subscribe(
        res => {
          if(res == true){
            this.notifier.notify("success", "Novi časopis je poslat administratoru na proveru.");
            this.router.navigate(['']);
          }else if(res == false){
            this.notifier.notify("error", "Unos recenzenata i urednika nije validan.");
          }
        },
        err => {
          console.log(err); 
          this.notifier.notify("error", "Unos recenzenata i urednika nije validan.");
          this.hidden=false;
        }
      )
    }
  }
}
