import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  IonContent,
  IonHeader,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import { Campanha } from '../../../models/vacina.model';
import { VacinaService } from '../../../services/vacina.service';

@Component({
  selector: 'app-campanhas',
  templateUrl: './campanhas.page.html',
  styleUrls: ['./campanhas.page.scss'],
  standalone: true,
  imports: [CommonModule, IonContent, IonHeader, IonTitle, IonToolbar],
})
export class CampanhasPage implements OnInit {
  campanhas: Campanha[] = [];

  constructor(private vacinaService: VacinaService) {}

  ngOnInit() {
    this.carregarDados();
  }

  ionViewWillEnter() {
    this.carregarDados();
  }

  carregarDados() {
    this.campanhas = this.vacinaService.getCampanhas();
  }

  get campanhasAtivas(): Campanha[] {
    return this.campanhas.filter(campanha => campanha.ativa);
  }
}

