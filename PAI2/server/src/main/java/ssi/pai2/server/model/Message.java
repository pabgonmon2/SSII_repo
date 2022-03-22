package ssi.pai2.server.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    private Long origen;

    @Column
    private Long destino;

    @Column 
    private Double cantidad;

    @Column 
    private String mac;

    @Column
    private String nonce;

    public Message(Map<String, String> params) {
        this.origen = Long.valueOf(params.get("origen"));
        this.destino = Long.valueOf(params.get("destino"));
        this.cantidad= Double.valueOf(params.get("cantidad"));
        this.nonce = params.get("nonce");
        this.mac =params.get("mac");
    }

    public Long getId() {
        return id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrigen() {
        return origen;
    }

    public void setOrigen(Long origen) {
        this.origen = origen;
    }

    public Long getDestino() {
        return destino;
    }

    public void setDestino(Long destino) {
        this.destino = destino;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isValid() {
        boolean b = false;
        b =  !this.cantidad.isNaN() && !this.nonce.isEmpty() && !this.nonce.isBlank() && this.nonce != null;
        b = b && !this.mac.isEmpty() && !this.mac.isBlank() && this.mac != null;
        b = b && this.destino.toString().length() == 8 && this.origen.toString().length() == 8 && !this.cantidad.isNaN();
        return b;
    }

    

}