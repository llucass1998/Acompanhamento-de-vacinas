import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-status-chip',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="status-chip" [ngClass]="'status-' + status">
      {{ label }}
    </span>
  `,
  styles: [`
    .status-chip {
      display: inline-flex;
      align-items: center;
      padding: 4px 10px;
      border-radius: var(--radius-pill);
      font-size: 12px;
      font-weight: 600;
      line-height: 1;
      text-transform: uppercase;
      letter-spacing: 0.02em;
    }
    
    .status-taken {
      background: var(--app-success-soft);
      color: var(--app-success);
      border: 1px solid rgba(21, 128, 61, 0.2);
    }
    
    .status-pending {
      background: var(--app-warning-soft);
      color: var(--app-warning);
      border: 1px solid rgba(183, 121, 31, 0.2);
    }
    
    .status-late {
      background: var(--app-danger-soft);
      color: var(--app-danger);
      border: 1px solid rgba(194, 65, 58, 0.2);
    }
    
    .status-default {
      background: var(--app-surface-secondary);
      color: var(--app-text-secondary);
      border: 1px solid var(--app-border);
    }
  `]
})
export class StatusChipComponent {
  @Input() status: 'taken' | 'pending' | 'late' | 'default' = 'default';
  @Input() label: string = '';
}
