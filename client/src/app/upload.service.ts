import { Injectable, inject } from "@angular/core";
import { BundleId, DateTitleId, RetrievedData, UploadData } from "./models";
import { Observable, lastValueFrom, map } from "rxjs";
import { HttpClient } from "@angular/common/http";

@Injectable()
export class UploadService {

  http = inject(HttpClient)

  upload(data: UploadData, f: File) : Promise<BundleId> {  

    const formData = new FormData()
    formData.set('name', data.name)
    formData.set('title', data.title)
    formData.set('comment', data.comment || " ") 

    formData.set('zipFile', f)

    return lastValueFrom(this.http.post<BundleId>('http://localhost:8080/upload', formData))
  }

  retrieve(bundleId: String): Observable<RetrievedData> {

    return this.http.get<RetrievedData>(`http://localhost:8080/bundle/${bundleId}`)
  }

  retrieveAll(): Observable<DateTitleId[]> {
    // return this.http.get<DateTitleId[]>('http://localhost:8080/bundles')
    
    return this.http.get<string[]>('http://localhost:8080/bundles').pipe(
      map((response: string[]) => {
        // Remove backslashes from each string in the response array
        const sanitizedResponse = response.map((item) => item.replace(/\\/g, ''));
        // Parse the sanitized JSON strings into DateTitleId objects
        return sanitizedResponse.map((item) => JSON.parse(item) as DateTitleId);
      })
    );
  }
 

}