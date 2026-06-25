import { Component, inject, OnInit, signal } from '@angular/core';
import { ProductSearch } from './features/sale/components/product-search/product-search';
import { CaptureInput } from './features/sale/components/capture-input/capture-input';
import { RouterOutlet } from '@angular/router';
import {Sidebar} from "./shared/layout/sidebar/sidebar";
import {Topbar} from "./shared/layout/topbar/topbar";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {}
