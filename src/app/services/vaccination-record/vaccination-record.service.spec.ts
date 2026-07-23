import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { VaccinationRecordService } from './vaccination-record.service';
import { environment } from '../../../environments/environment';

describe('VaccinationRecordService E2E Baseline Contract', () => {
  let service: VaccinationRecordService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiUrl}/children`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [VaccinationRecordService]
    });
    service = TestBed.inject(VaccinationRecordService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensure no pending requests
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('[Baseline E2E] should match backend contract for getChildSummary', () => {
    const childId = 'child-123';

    // O mock representa o novo formato acordado (DTO do Backend / Interface do Frontend)
    const mockSummaryResponse = {
      total: 10,
      taken: 4,
      pending: 4,
      overdue: 2,
      completionPercentage: 40
    };

    service.getChildSummary(childId).subscribe((summary) => {
      expect(summary.total).toBe(10);
      expect(summary.taken).toBe(4);
      expect(summary.pending).toBe(4);
      expect(summary.overdue).toBe(2);
      expect(summary.completionPercentage).toBe(40);
    });

    const req = httpMock.expectOne(`${baseUrl}/${childId}/vaccination-summary`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSummaryResponse);
  });

  it('[Baseline E2E] should match backend contract for getChildSchedule', () => {
    const childId = 'child-123';

    const mockScheduleResponse = [
      {
        id: 'sched-1',
        childId: childId,
        vaccineName: 'BCG',
        vaccineDose: {
          id: 'dose-1',
          vaccineId: 'vac-1',
          doseName: 'Dose Única',
          recommendedAgeMonths: 0,
          description: 'Previne formas graves de tuberculose',
          code: 'BCG_DOSE_1'
        },
        expectedDate: '2025-01-01',
        status: 'TAKEN'
      }
    ];

    service.getChildSchedule(childId).subscribe((schedule) => {
      expect(schedule.length).toBe(1);
      expect(schedule[0].vaccineName).toBe('BCG');
      expect(schedule[0].vaccineDose.doseName).toBe('Dose Única');
      expect(schedule[0].status).toBe('TAKEN');
    });

    const req = httpMock.expectOne(`${baseUrl}/${childId}/vaccination-schedule`);
    expect(req.request.method).toBe('GET');
    req.flush(mockScheduleResponse);
  });
});
