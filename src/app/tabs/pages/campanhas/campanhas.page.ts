import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
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

