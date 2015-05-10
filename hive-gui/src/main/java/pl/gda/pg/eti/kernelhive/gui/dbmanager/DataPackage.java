/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.dbmanager;

/**
 *
 * @author mike
 */
public class DataPackage {

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    private byte[] data;
    private long id;
    private String description;
    
    @Override
    public String toString() {
        return id + " " + description;
    }
}
