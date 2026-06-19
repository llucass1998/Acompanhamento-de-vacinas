import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { Campanha } from '../../../models/vacina.model';
import { VacinaService } from '../../../services/vacina.service';

@Component({
  selector: 'app-campanhas',
  templateUrl: './campanhas.page.html',
  styleUrls: ['./campanhas.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule],
})
export class CampanhasPage {
  campanhas: Campanha[] = [];

  constructor(private vacinaService: VacinaService) {}

  ionViewWillEnter() {
    this.campanhas = this.vacinaService.getCampanhas();
  }
}