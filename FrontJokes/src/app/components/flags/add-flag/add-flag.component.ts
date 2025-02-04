import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FlagsService } from '../../../services/flags.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-flag',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './add-flag.component.html',
  styleUrls: ['./add-flag.component.css']
})
export class AddFlagComponent {
  newFlag = { flag: '' }; // Usar directamente un objeto para ngModel

  constructor(
    private flagsService: FlagsService,
    private router: Router
  ) {}

  addFlag(): void {
    if (this.newFlag.flag) {
      this.flagsService.createFlag(this.newFlag).subscribe({
        next: () => {
          alert('Flag added successfully!');
          this.router.navigate(['/flags']);
        },
        error: (err) => {
          console.error('Error adding flag:', err);
          alert('An error occurred while adding the flag.');
        }
      });
    } else {
      alert('Please fill in the flag name.');
    }
  }
}
