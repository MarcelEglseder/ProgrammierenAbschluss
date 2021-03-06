package de.gbsschulen;

import java.util.Objects;

public class Gegenstand {

    private String bezeichnung;
    private double preis;
    private int anzahl;

    public Gegenstand() {

    }

    public Gegenstand(String bezeichnung, double preis, int anzahl) {
        this.bezeichnung = bezeichnung;
        this.preis = preis;
        this.anzahl = anzahl;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gegenstand that = (Gegenstand) o;
        return Double.compare(that.preis, preis) == 0 &&
                anzahl == that.anzahl &&
                Objects.equals(bezeichnung, that.bezeichnung);
    }

    @Override
    public int hashCode() {

        return Objects.hash(bezeichnung, preis, anzahl);
    }

    @Override
    public String toString() {
        return bezeichnung;
    }
}
