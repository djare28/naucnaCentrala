import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { Router, ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import { NotifierService } from 'angular-notifier';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-new-paper',
  templateUrl: './new-paper.component.html',
  styleUrls: ['./new-paper.component.scss']
})
export class NewPaperComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];

  private formModel = [
    {
      fieldId:"naziv",
      fieldValue:""
    },
    {
      fieldId:"clanarina",
      fieldValue:""
    },
    {
      fieldId:"komeSeNaplacuje",
      fieldValue:null
    }
  ]

  private naucneOblasti = [
    {
      fieldId:"naucnaOblast1",
      fieldValue:null
    }
  ];

  private processId = "";

  private naciniPlacanja = [
    {
      fieldId:"nacinPlacanja1",
      fieldValue:null
    }
  ]

  private oblasti=["Naučna oblast 1"];
  private taskId = "";
  private listaOblasti=[];
  private listaPlacanja=[];
  private enumValues = [];
  private placanja=["Način plaćanja 1"];
  private hidden=false;
  
  private readonly notifier: NotifierService;

  constructor(private endpoints:EndpointsService, 
    private activatedRoute: ActivatedRoute,
    private router: Router,
    notifierService: NotifierService, 
    private authenticationService:AuthenticationService) { 
    this.notifier = notifierService;

    this.processId = this.activatedRoute.snapshot.paramMap.get('processId');
    if(this.processId==null){
      let x = endpoints.startNewPaperProcess(this.authenticationService.currentUserValue.username);
      
      x.subscribe(
        res => {
          console.log(res);
          //this.categories = res;
          this.taskId = res.taskId;
          this.processId = res.processInstanceId;
          this.formFieldsDto = res;
          this.formFields = res.formFields;
          this.formFields.forEach( (field) =>{
            
            if( field.type.name=='enum'){
              this.enumValues = Object.keys(field.type.values);
            }
          });
        },
        err => {
          console.log("Error occured");
        }
      );
      
    }else{
      this.getCorrectionData(this.processId);
    }
    
    this.getOblasti();
    this.getPlacanja();
  

  }

  ngOnInit() {

  }

  getCorrectionData(id){
    this.endpoints.getCorrectionData(id).subscribe(
      res => {
        this.formFields = res.formFields;
        this.formModel[0].fieldValue=res.naziv;
        this.formModel[1].fieldValue=res.clanarina;
        this.formModel[2].fieldValue=res.komeSeNaplacuje;
        
        let id = {
          fieldId:"id",
          fieldValue:res.id
        }
        this.formModel.push(id);
        
        let i = 0;
        for(let no of res.naucneOblasti){
          if(i==0){
            this.naucneOblasti[0].fieldValue=no;
          }else{
            let body = {
              fieldValue:no,
              fieldId:"naucnaOblast"+i
            }
            this.oblasti.push("Naučna oblast "+i);
            this.naucneOblasti.push(body);
          }

          i = i+1;
        }

        let j = 0;
        for(let np of res.naciniPlacanja){
          if(j==0){
            this.naciniPlacanja[0].fieldValue=np;
          }else{
            let body = {
              fieldValue:np,
              fieldId:"nacinPlacanja"+j
            }
            this.placanja.push("Način plaćanja "+j);
            this.naciniPlacanja.push(body);
          }

          j = j+1;
        }
      },
      err => {
        console.log(err); 
      }
    )
  }


  getOblasti(){
    this.endpoints.getOblasti().subscribe(
      res => {
          this.listaOblasti=res;
      },
      err => {
        alert("An error has occured while getting science fields");
        console.log(err); 
      }
    )
  }

  getPlacanja(){
    this.endpoints.getPlacanja().subscribe(
      res => {
          this.listaPlacanja=res;
      },
      err => {
        alert("An error has occured while getting payments");
        console.log(err); 
      }
    )
  }

  addOblast(){
    var index = this.naucneOblasti.length+1;

    var oblast = {
      fieldValue:null,
      fieldId:"naucnaOblast"+index
    }

    this.naucneOblasti.push(oblast);
    this.oblasti.push("Naučna oblast "+index);
  }

  addPlacanje(){
    var index = this.naciniPlacanja.length+1;

    var placanje = {
      fieldValue:null,
      fieldId:"nacinPlacanja"+index
    }

    this.naciniPlacanja.push(placanje);
    this.placanja.push("Način plaćanja "+index);
  }

  onSubmit(newPaperForm:NgForm){
    this.hidden=true;
    for(let o of this.naucneOblasti){
      this.formModel.push(o);
    }
    for(let p of this.naciniPlacanja){
      this.formModel.push(p);
    }
    if(newPaperForm.valid===true){
      this.endpoints.submitNewPaper(this.formModel,this.processId).subscribe(
        res => {
            console.log(res);
            this.endpoints.getValidation(res).subscribe(
              res => {
                if(res==true){
                  this.notifier.notify("success", "Novi časopis je validan. Unesite urednike i recenzente.");
                  this.router.navigate(['/newPaper/'+this.processId]);
                }else{
                  this.notifier.notify("error", "Novi časopis nije validan.");
                  this.hidden=false
                }
              },
              err => {
                alert("An error has occured while getting validation");
                console.log(err); 
                this.hidden=false;
              }
            )
        },
        err => {
          alert("An error has occured while registrating");
          console.log(err); 
          this.hidden=false;
        }
      )
    }
  }

}
