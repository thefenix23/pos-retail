import { Service } from '@angular/core';
import jsPDF from 'jspdf';
import JsBarcode from 'jsbarcode';
import { SaleResponse } from '../models/sale.model';
import { CartItem } from '../models/cart-item.model';

@Service()
export class TicketService {
  // ── Datos genéricos (módulo de caja/turno en fases finales) ──
  private readonly STORE_NAME = 'POS RETAIL';
  private readonly STORE_BRANCH = 'Sucursal Centro';
  private readonly TERMINAL = '02';
  private readonly CASHIER = 'Cajero Mostrador';
  private readonly SUC = '0604';
  private readonly TER = '10';
  private readonly TRA = '76';

  // ── Layout en milímetros ──
  private readonly WIDTH = 80;
  private readonly LEFT = 4;            // margen izquierdo
  private readonly RIGHT = 76;          // borde derecho (donde terminan los montos)
  private readonly LINE = 4.2;

  // ── Columnas X fijas para la tabla de productos (en mm) ──
  private readonly COL_CANT = 4;        // cantidad (izquierda)
  private readonly COL_ART = 12;        // artículo (izquierda)
  private readonly COL_PRECIO = 58;     // precio (derecha)
  private readonly COL_TOTAL = 76;      // total (derecha)

  // ── Columnas X para la sección de totales ──
  private readonly COL_LABEL = 48;      // etiqueta (derecha, termina antes del $)
  private readonly COL_DOLLAR = 51;     // símbolo $ (izquierda)
  private readonly COL_AMOUNT = 74;     // monto (derecha)

  generate(sale: SaleResponse, items: CartItem[], paymentMethodName: string, cashReceived: number | null): void {
    const height = 130 + items.length * this.LINE + (cashReceived !== null ? this.LINE * 2 : 0);

    const doc = new jsPDF({ unit: 'mm', format: [this.WIDTH, height] });
    doc.setFont('courier', 'normal');
    const center = this.WIDTH / 2;
    let y = 8;

    // ── Encabezado ──
    doc.setFontSize(11);
    doc.setFont('courier', 'bold');
    doc.text(this.STORE_NAME, center, y, { align: 'center' });
    y += this.LINE + 1;
    doc.setFontSize(7);
    doc.setFont('courier', 'normal');
    doc.text(this.STORE_BRANCH, center, y, { align: 'center' });
    y += this.LINE;
    doc.text('Comprobante de venta', center, y, { align: 'center' });
    y += this.LINE + 1;

    // ── Datos de operación ──
    doc.setFontSize(8);
    this.dashLine(doc, y); y += this.LINE;
    doc.text(`Folio: ${sale.id}`, this.LEFT, y); y += this.LINE;
    doc.text(`Fecha: ${new Date().toLocaleString('es-MX')}`, this.LEFT, y); y += this.LINE;
    doc.text(`Caja: ${this.TERMINAL}  Atendio: ${this.CASHIER}`, this.LEFT, y); y += this.LINE;
    this.dashLine(doc, y); y += this.LINE + 0.5;

    // ── Cabecera de productos (cada columna en su X) ──
    doc.setFont('courier', 'bold');
    doc.text('CANT', this.COL_CANT, y);
    doc.text('ARTICULO', this.COL_ART, y);
    doc.text('PRECIO', this.COL_PRECIO, y, { align: 'right' });
    doc.text('TOTAL', this.COL_TOTAL, y, { align: 'right' });
    y += this.LINE;
    doc.setFont('courier', 'normal');

    // ── Productos ──
    let totalArticulos = 0;
    for (const item of items) {
      totalArticulos += item.quantity;
      const precioUnit = item.subtotal / item.quantity;
      doc.text(item.quantity.toString(), this.COL_CANT, y);
      doc.text(this.truncate(item.product.name, 22), this.COL_ART, y);
      doc.text(precioUnit.toFixed(2), this.COL_PRECIO, y, { align: 'right' });
      doc.text(item.subtotal.toFixed(2), this.COL_TOTAL, y, { align: 'right' });
      y += this.LINE;
    }

    this.dashLine(doc, y); y += this.LINE + 0.5;

    // ── Total ──
    doc.setFont('courier', 'bold');
    this.totalLine(doc, 'TOTAL M.N.', sale.total.toFixed(2), y);
    y += this.LINE + 1;

    // ── Pago y cambio ──
    doc.setFont('courier', 'normal');
    if (cashReceived !== null) {
      this.totalLine(doc, paymentMethodName, cashReceived.toFixed(2), y);
      y += this.LINE;
      const cambio = cashReceived - sale.total;
      doc.setFont('courier', 'bold');
      this.totalLine(doc, 'CAMBIO', cambio.toFixed(2), y);
      y += this.LINE;
      doc.setFont('courier', 'normal');
    } else {
      this.totalLine(doc, paymentMethodName, sale.total.toFixed(2), y);
      y += this.LINE;
    }
    y += 0.5;

    // ── Total con letra ──
    doc.setFontSize(7);
    const enLetra = this.numeroALetras(sale.total);
    for (const linea of this.wrap(`***** ${enLetra}`, 42)) {
      doc.text(linea, this.LEFT, y);
      y += this.LINE;
    }
    doc.setFontSize(8);
    doc.text(`Total de articulos vendidos = ${totalArticulos}`, this.LEFT, y);
    y += this.LINE + 1;

    // ── Pie ──
    this.dashLine(doc, y); y += this.LINE + 1;
    doc.setFontSize(7);
    doc.text(`LE ATENDIO: ${this.CASHIER.toUpperCase()}`, center, y, { align: 'center' });
    y += this.LINE;
    const f = new Date();
    const fechaCorta = `${f.toLocaleDateString('es-MX')} ${f.toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit' })}`;
    doc.text(`${fechaCorta} SUC.${this.SUC} TER.${this.TER} TRA.${this.TRA}`, center, y, { align: 'center' });
    y += this.LINE;
    const folioLargo = this.folioLargo(sale.id);
    doc.setFont('courier', 'bold');
    doc.text(`***FOLIO:${folioLargo}***`, center, y, { align: 'center' });
    y += this.LINE + 2;

    // ── Código de barras ──
    const barcode = this.barcodeDataUrl(folioLargo.replace(/ /g, ''));
    if (barcode) {
      doc.addImage(barcode, 'PNG', this.LEFT + 8, y, this.WIDTH - (this.LEFT + 8) * 2, 12);
      y += 14;
    }
    doc.setFont('courier', 'normal');
    doc.setFontSize(7);
    doc.text('Gracias por su compra', center, y, { align: 'center' });

    doc.save(`ticket-${sale.id}.pdf`);
  }

  // ── Helpers ──

  // Línea de total: etiqueta (derecha) | $ (fijo) | monto (derecha al borde)
  private totalLine(doc: jsPDF, label: string, amount: string, y: number): void {
    doc.text(label, this.COL_LABEL, y, { align: 'right' });
    doc.text('$', this.COL_DOLLAR, y);
    doc.text(amount, this.COL_AMOUNT, y, { align: 'right' });
  }

  private dashLine(doc: jsPDF, y: number): void {
    doc.text('-'.repeat(42), this.LEFT, y);
  }

  private truncate(text: string, max: number): string {
    return text.length > max ? text.slice(0, max - 1) + '…' : text;
  }

  private wrap(text: string, max: number): string[] {
    const words = text.split(' ');
    const lines: string[] = [];
    let current = '';
    for (const w of words) {
      if ((current + ' ' + w).trim().length > max) {
        lines.push(current.trim());
        current = w;
      } else {
        current = (current + ' ' + w).trim();
      }
    }
    if (current) lines.push(current);
    return lines;
  }

  private folioLargo(id: number): string {
    const folio = id.toString().padStart(4, '0');
    return `${this.SUC} ${this.TER}${this.TER} ${this.SUC} ${this.TER}${this.TER} ${folio}`;
  }

  private barcodeDataUrl(value: string): string | null {
    try {
      const canvas = document.createElement('canvas');
      JsBarcode(canvas, value, {
        format: 'CODE128',
        displayValue: false,
        margin: 0,
        height: 40,
      });
      return canvas.toDataURL('image/png');
    } catch {
      return null;
    }
  }

  private numeroALetras(num: number): string {
    const entero = Math.floor(num);
    const centavos = Math.round((num - entero) * 100);
    const texto = this.enteroALetras(entero);
    const pesos = entero === 1 ? 'PESO' : 'PESOS';
    return `${texto} ${pesos} ${centavos.toString().padStart(2, '0')}/100 M.N.`.toUpperCase();
  }

  private enteroALetras(n: number): string {
    if (n === 0) return 'CERO';
    if (n === 100) return 'CIEN';

    const unidades = ['', 'UNO', 'DOS', 'TRES', 'CUATRO', 'CINCO', 'SEIS', 'SIETE', 'OCHO', 'NUEVE',
      'DIEZ', 'ONCE', 'DOCE', 'TRECE', 'CATORCE', 'QUINCE', 'DIECISEIS', 'DIECISIETE', 'DIECIOCHO', 'DIECINUEVE',
      'VEINTE'];
    const decenas = ['', '', 'VEINTI', 'TREINTA', 'CUARENTA', 'CINCUENTA', 'SESENTA', 'SETENTA', 'OCHENTA', 'NOVENTA'];
    const centenas = ['', 'CIENTO', 'DOSCIENTOS', 'TRESCIENTOS', 'CUATROCIENTOS', 'QUINIENTOS',
      'SEISCIENTOS', 'SETECIENTOS', 'OCHOCIENTOS', 'NOVECIENTOS'];

    let texto = '';

    if (n >= 1000) {
      const miles = Math.floor(n / 1000);
      texto += (miles === 1 ? 'MIL' : this.enteroALetras(miles) + ' MIL') + ' ';
      n %= 1000;
    }
    if (n >= 100) {
      texto += centenas[Math.floor(n / 100)] + ' ';
      n %= 100;
    }
    if (n > 20) {
      const d = Math.floor(n / 10);
      const u = n % 10;
      if (d === 2) {
        texto += 'VEINTI' + (u > 0 ? unidades[u] : '');
      } else {
        texto += decenas[d] + (u > 0 ? ' Y ' + unidades[u] : '');
      }
    } else if (n > 0) {
      texto += unidades[n];
    }

    return texto.trim();
  }
}
