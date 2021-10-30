/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omarmuy.videogames;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 *  The VideoGame class that will contain values for title/price/esrb rating and overrided methods toString, equals and HashCode. 
 * @author Omar Muy
 */
public class VideoGame {
    @SerializedName("title")
    private String title;
    @SerializedName("price")
    private double price;
    @SerializedName("esrb")
    private String esrb; 

    /**
     * Constructor for the video game that calls on the title/price/esrb 
     * @param title title of vg
     * @param price price of vg
     * @param esrb rating of vg
     */
    public VideoGame(String title, double price, String esrb) {
        this.title = title;
        this.price = price;
        this.esrb = esrb;
    }

    /**
     * setter method that sets the title
     * @param title sets title of vg
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * setter method that sets the price 
     * @param price sets price of vg
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * setter method that sets the esrb
     * @param esrb sets ersb of vg
     */
    public void setEsrb(String esrb) {
        this.esrb = esrb;
    }

    /**
     * getter method that retrieves the title
     * @return gets the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * getter method that retrieves the price
     * @return gets the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Getter method that retrieves the esrb
     * @return gets the rating
     */ 
    public String getEsrb() {
        return esrb;
    }
    /**
     * HashCode override based on Title (String HashCode value)
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.title);
        return hash;
    }
    /**
     * Equals method that will return true if the title is equal.
     * @param obj
     * @return  compares video game titles to each other
     */
    @Override
    public boolean equals(Object obj) {
        VideoGame vg = (VideoGame) obj;
        return vg.getTitle().equalsIgnoreCase(this.title);
       /** 
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VideoGame other = (VideoGame) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        return true;
        */
    }
    /**
     *  toString override method that returns all values concatenated with each other. 
     * @return Videogames concat to eachother
     */
    @Override
    public String toString() {
        return "VideoGame{" + "title=" + title + ", price=" + price + ", esrb=" + esrb + '}';
    }
}
