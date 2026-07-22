import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonContent,
  IonHeader,
  IonTitle,
  IonToolbar,
  IonCard,
  IonCardHeader,
  IonCardTitle,
  IonCardContent,
  IonButton,
  IonIcon,
  IonItem,
  IonLabel,
  IonList,
  IonFab,
  IonFabButton,
  IonModal,
  IonInput,
  IonDatetime,
  IonSelect,
  IonSelectOption,
  IonButtons,
  IonBadge,
  IonTextarea,
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  addOutline,
  personCircleOutline,
  calendarOutline,
  checkmarkCircleOutline,
  alertCircleOutline,
} from 'ionicons/icons';

import { Crianca, ChildCreateRequest } from '../../../models/crianca.model';
import { ResumoVacinal } from '../../../models/vacina.model';
import { ChildService } from '../../../services/child/child.service';
import { VaccinationRecordService } from '../../../services/vaccination-record/vaccination-record.service';
import { forkJoin, catchError, of } from 'rxjs';

@Component({
  selector: 'app-crianca',
  templateUrl: './crianca.page.html',
  styleUrls: ['./crianca.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonContent,
    IonHeader,
    IonTitle,
    IonToolbar,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardContent,
    IonButton,
    IonIcon,
    IonItem,
    IonLabel,
    IonList,
    IonFab,
    IonFabButton,
    IonModal,
    IonInput,
    IonDatetime,
    IonSelect,
    IonSelectOption,
    IonButtons,
    IonBadge,
    IonTextarea,
  ],
})
export class CriancaPage implements OnInit {
  private childService = inject(ChildService);
  private recordService = inject(VaccinationRecordService);

  criancas: Crianca[] = [];
  criancaSelecionadaId: string | null = null;
  resumosVacinais: Record<string, ResumoVacinal> = {};

  isModalOpen = false;
  novaCrianca: ChildCreateRequest = {
    name: '',
    birthDate: '',
    gender: 'M',
    notes: '',
  };

  constructor() {
    addIcons({
      addOutline,
      personCircleOutline,
      calendarOutline,
      checkmarkCircleOutline,
      alertCircleOutline,
    });
  }

  ngOnInit() {
    this.carregarDados();
    this.childService.selectedChild$.subscribe(id => {
      this.criancaSelecionadaId = id;
    });
  }

  ionViewWillEnter() {
    this.carregarDados();
  }

  carregarDados() {
    this.childService.getChildren().subscribe(response => {
      this.criancas = response.content || [];
      
      // Auto select if first time and no child selected
      if (this.criancas.length > 0 && !this.criancaSelecionadaId) {
        this.selecionarCrianca(this.criancas[0].id);
      }
      
      // Carregar resumos
      this.criancas.forEach(c => {
        this.recordService.getChildSummary(c.id).pipe(
          catchError(() => of({ total: 0, tomadas: 0, pendentes: 0, atrasadas: 0 }))
        ).subscribe(summary => {
          this.resumosVacinais[c.id] = summary;
        });
      });
    });
  }

  selecionarCrianca(id: string) {
    this.childService.selectChild(id);
  }

  setOpen(isOpen: boolean) {
    this.isModalOpen = isOpen;
    if (!isOpen) {
      this.resetarFormulario();
    }
  }

  salvarCrianca() {
    if (!this.novaCrianca.name || !this.novaCrianca.birthDate) {
      return; // Basic validation
    }

    // Format date correctly if needed (ensure YYYY-MM-DD)
    const formattedDate = this.novaCrianca.birthDate.split('T')[0];
    const payload = { ...this.novaCrianca, birthDate: formattedDate };

    this.childService.createChild(payload).subscribe({
      next: (criancaSalva) => {
        this.selecionarCrianca(criancaSalva.id);
        this.setOpen(false);
        this.carregarDados();
      },
      error: (err) => {
        console.error('Erro ao salvar criança', err);
      }
    });
  }

  resetarFormulario() {
    this.novaCrianca = {
      name: '',
      birthDate: '',
      gender: 'M',
      notes: '',
    };
  }

  calcularIdade(dataNascimento: string): string {
    const hoje = new Date();
    const nascimento = new Date(dataNascimento);
    let idade = hoje.getFullYear() - nascimento.getFullYear();
    const m = hoje.getMonth() - nascimento.getMonth();

    if (m < 0 || (m === 0 && hoje.getDate() < nascimento.getDate())) {
      idade--;
    }

    if (idade === 0) {
      let meses = m;
      if (meses < 0) {
        meses += 12;
      }
      return `${meses} meses`;
    }

    return `${idade} anos`;
  }
}
