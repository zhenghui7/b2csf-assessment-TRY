import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import { Routes, RouterModule } from "@angular/router";
import { AppComponent } from "./app.component";
import { View1Component } from "./components/view1.component";
import { UploadService } from "./upload.service";
import { View2Component } from './components/view2.component';
import { View0Component } from './components/view0.component';


const appRoutes: Routes = [
  { path: "", component: View0Component },
  { path: "view1", component: View1Component },
  { path: "view2/:bundleId", component: View2Component },
  { path: '**', redirectTo: '/', pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent, View1Component, View2Component, View0Component
  ],
  imports: [
    BrowserModule, FormsModule, ReactiveFormsModule, 
    HttpClientModule, RouterModule.forRoot(appRoutes, {useHash: true ,  bindToComponentInputs: true})
  ],
  providers: [UploadService],
  bootstrap: [AppComponent]
})
export class AppModule { }
