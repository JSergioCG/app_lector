package com.example.appservicesce;

import java.util.List;

public class Shoes {
    protected int idAlmacen;
    protected String Almacen;
    protected int idTalla;
    protected String Talla;
    protected int idColor;
    protected String Color;

    protected String lotCodigoBarras;

    public Shoes(int idAlmacen, String almacen, int idTalla, String talla, int idColor, String color, String lotCodigoBarras){
        this.idAlmacen = idAlmacen;
        this.Almacen = almacen;
        this.idTalla = idTalla;
        this.Talla = talla;
        this.idColor = idColor;
        this.Color = color;
        this.lotCodigoBarras = lotCodigoBarras;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getAlmacen() {
        return Almacen;
    }

    public void setAlmacen(String almacen) {
        Almacen = almacen;
    }

    public int getIdTalla() {
        return idTalla;
    }

    public void setIdTalla(int idTalla) {
        this.idTalla = idTalla;
    }

    public String getTalla() {
        return Talla;
    }

    public void setTalla(String talla) {
        Talla = talla;
    }

    public int getIdColor() {
        return idColor;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getLotCodigoBarras() {
        return lotCodigoBarras;
    }

    public void setLotCodigoBarras(String lotCodigoBarras) {
        this.lotCodigoBarras = lotCodigoBarras;
    }

    public Shoes findShoes(List<Shoes> listShoes, Shoes shoe){
        for (Shoes shoes : listShoes) {
            if (shoes.getIdAlmacen() == shoe.getIdAlmacen() && shoes.getIdTalla() == shoe.getIdTalla() && shoes.getIdColor() == shoe.getIdColor()) {
                return shoes;
            }
        }
        return null;
    }
}
