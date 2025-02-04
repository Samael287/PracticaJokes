import { Component, OnInit } from '@angular/core';
import { FlagsService } from '../../services/flags.service';
import { Router, RouterModule } from '@angular/router'; 
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Flags } from '../../models/flags.model';

@Component({
  selector: 'app-flags',
  standalone: true,
  imports: [RouterModule, FormsModule, CommonModule],
  templateUrl: './flags.component.html',
  styleUrl: './flags.component.css'
})
export class FlagsComponent implements OnInit {
  flags: any[] = [];
  newFlag: any = { flag: '' };
  selectedFlag: Flags | null = null;
  paginatedFlags: any[] = [];
  searchTerm: string = '';
  currentPage: number = 1;
  pageSize: number = 6;
  totalPages: number = 1;

  constructor(private flagsService: FlagsService) {}

  ngOnInit(): void {
    this.loadFlags();
  }

  loadFlags(): void {
    this.flagsService.getFlags().subscribe(data => {
      this.flags = data;
      this.applyFilters();
    });
  }

  addFlag(): void {
    this.flagsService.createFlag(this.newFlag).subscribe(() => {
      this.loadFlags();
      this.newFlag = { flag: '' };
    });
  }

  deleteFlag(id: number | undefined): void {
    if (id === undefined) {
      console.error('Flag ID is undefined. Cannot delete.');
      return;
    }
  
    this.flagsService.deleteFlag(id).subscribe(() => {
      this.loadFlags();
    });
  }
  
  onSearch(): void {
    this.applyFilters();
  }

  applyFilters(): void {
    const filteredFlags = this.flags.filter(flag =>
      flag.flag.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
    this.totalPages = Math.ceil(filteredFlags.length / this.pageSize);
    this.paginatedFlags = filteredFlags.slice(
      (this.currentPage - 1) * this.pageSize,
      this.currentPage * this.pageSize
    );
  }

  selectFlag(flag: Flags): void {
    this.selectedFlag = flag;
    console.log('selectedFlag:', this.selectedFlag);
    if (!this.selectedFlag.id) {
      console.error('Selected flag has no ID:', this.selectedFlag);
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.applyFilters();
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.applyFilters();
    }
  }
}
