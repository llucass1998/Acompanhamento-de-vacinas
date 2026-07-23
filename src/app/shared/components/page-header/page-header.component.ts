import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-page-header',
  standalone: true,
  imports: [CommonModule, IonicModule],
  template: `
    <ion-header class="ion-no-border">
      <ion-toolbar>
        <div class="header-content">
          <div class="title-group">
            <h1 class="page-title">{{ title }}</h1>
            <p *ngIf="subtitle" class="page-subtitle text-secondary">{{ subtitle }}</p>
          </div>
          <div class="actions-group">
            <ng-content select="[actions]"></ng-content>
          </div>
        </div>
      </ion-toolbar>
    </ion-header>
  `,
  styles: [`
    ion-toolbar {
      --background: transparent;
      --padding-top: var(--space-4);
      --padding-bottom: var(--space-4);
      --padding-start: var(--space-4);
      --padding-end: var(--space-4);
      
      @media (min-width: 720px) {
        --padding-start: var(--space-6);
        --padding-end: var(--space-6);
        --padding-top: var(--space-6);
      }
    }
    
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      gap: var(--space-4);
    }
    
    .title-group {
      flex: 1;
    }
    
    .page-title {
      margin: 0 0 var(--space-1);
      font-size: clamp(24px, 4vw, 32px);
      font-weight: 800;
      letter-spacing: -0.02em;
      color: var(--app-text-primary);
    }
    
    .page-subtitle {
      margin: 0;
      font-size: 16px;
    }
    
    .actions-group {
      display: flex;
      align-items: center;
      gap: var(--space-2);
    }
  `]
})
export class PageHeaderComponent {
  @Input() title: string = '';
  @Input() subtitle?: string;
}
