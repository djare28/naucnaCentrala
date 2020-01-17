import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const BASE_URL = "http://localhost:8080/webshop";

@Injectable({
  providedIn: 'root'
})
export class EndpointsService {

  constructor(private http: HttpClient) { }

  startProcess(): Observable<any>{
    return this.http.get(`${BASE_URL}/registration`);
  }

  submitRegistration(formModel,taskId): Observable<any>{
    return this.http.post(`${BASE_URL}/registration/${taskId}`, formModel,{
      headers: new HttpHeaders({
        'responseType': 'text',
        'Content-Type': 'application/json'
      })
    });
  }

  getValidation(processId): Observable<any>{
    return this.http.get(`${BASE_URL}/registration/validation/${processId}`);
  }

  activateAccount(processId): Observable<any>{
    return this.http.post(`${BASE_URL}/registration/confirmation/${processId}`,null,{
      headers: new HttpHeaders({
        'responseType': 'text',
        'Content-Type': 'application/json'
      })
    });
  }

  startNewPaperProcess(username): Observable<any>{
    return this.http.get(`${BASE_URL}/newPaper/${username}`);
  }

  submitNewPaper(formModel,processId): Observable<any>{
    return this.http.post(`${BASE_URL}/newPaper/${processId}`, formModel,{
      headers: new HttpHeaders({
        'responseType': 'text',
        'Content-Type': 'application/json'
      })
    });
  }

  submitUredniciRecenzenti(body,processId): Observable<any>{
    return this.http.post(`${BASE_URL}/newPaper/uredniciRecenzenti/${processId}`, body,{
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    });
  }


  getUrednikeRecenzente(processId): Observable<any>{
    return this.http.get(`${BASE_URL}/uredniciRecenzenti/${processId}`);
  }

  getPlacanja(): Observable<any>{
    return this.http.get(`${BASE_URL}/payments`);
  }

  getOblasti(): Observable<any>{
    return this.http.get(`${BASE_URL}/sciencefields`);
  }

  getCorrectionData(processId): Observable<any>{
    return this.http.get(`${BASE_URL}/correction/${processId}`);
  }

  potvrdaRecenzenta(dto): Observable<any>{
    return this.http.post(`${BASE_URL}/potvrdaRecenzenta`,dto,{
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    });
  }

  aktivacijaCaspisa(body): Observable<any>{
    return this.http.post(`${BASE_URL}/potvrdaCasopisa`,body,{
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    });
  }

  getMyPapers(username): Observable<any>{
    return this.http.get(`${BASE_URL}/myPapers/${username}`);
  }

  getTasks(username): Observable<any>{
    return this.http.get(`${BASE_URL}/tasks/${username}`);
  }


}
