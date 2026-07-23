import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  imports: [CommonModule, IonicModule],
  template: `
    <div class="empty-state">
      <div class="icon-wrapper">
        <ion-icon [name]="icon"></ion-icon>
      </div>
      <h2>{{ title }}</h2>
      <p class="text-secondary">{{ message }}</p>
      
      <ion-button 
        *ngIf="actionLabel" 
        class="main-button" 
        expand="block" 
        (click)="actionClick.emit()">
        {{ actionLabel }}
      </ion-button>
    </div>
  `,
  styles: [`
    .empty-state {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      text-align: center;
      padding: var(--space-12) var(--space-4);
      background: var(--app-surface);
      border: 1px dashed var(--app-border-strong);
      border-radius: var(--radius-lg);
      margin: var(--space-6) 0;
    }
    .icon-wrapper {
      width: 64px;
      height: 64px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--app-primary-soft);
      border-radius: var(--radius-pill);
      margin-bottom: var(--space-4);
      color: var(--app-primary);
    }
    ion-icon {
      font-size: 32px;
    }
    h2 {
      margin: 0 0 var(--space-2);
      font-size: 20px;
    }
    p {
      margin: 0 0 var(--space-6);
      max-width: 400px;
    }
    ion-button {
      min-width: 200px;
    }
  `]
})
export class EmptyStateComponent {
  @Input() icon: string = 'folder-open-outline';
  @Input() title: string = 'Nenhum item encontrado';
  @Input() message: string = 'Não há dados para exibir no momento.';
  @Input() actionLabel?: string;
  @Output() actionClick = new EventEmitter<void>();
}
