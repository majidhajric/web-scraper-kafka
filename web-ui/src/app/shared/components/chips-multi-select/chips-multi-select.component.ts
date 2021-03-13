import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {MatChip} from '@angular/material/chips';

@Component({
  selector: 'app-chips-multi-select',
  templateUrl: './chips-multi-select.component.html',
  styleUrls: ['./chips-multi-select.component.scss']
})
export class ChipsMultiSelectComponent implements OnInit {

  @Input() title: string;

  @Input() options: string[] = [];

  @Input() values: string[] = [];

  @Output() selectedItemsEvent = new EventEmitter<string[]>();

  selectedChips: MatChip[] = [];

  constructor() { }

  ngOnInit(): void {
  }

  toggleSelection(chip: MatChip) {
    chip.toggleSelected();
    if (chip.selected) {
      this.values.push(chip.value);
      this.selectedChips.push(chip);
    } else {
      const i = this.values.lastIndexOf(chip.value);
      this.values.splice(i, 1);
    }
    this.selectedItemsEvent.emit(this.values);
  }

  resetValues() {
    this.selectedChips.forEach(chip => {
      chip.selected = false;
    });
    this.values = [];
    this.selectedItemsEvent.emit(this.values);
  }
}
