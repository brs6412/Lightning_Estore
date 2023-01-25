package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Represents a Jersey entity
 * 
 * @author Team Lightning
 */
public class Jersey {
    // Package private for tests
    static final String STRING_FORMAT = "Jersey [id=%d, number=%d, name=%s, price=%,.2f, size=%s, color=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("number") private int number;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private double price;
    @JsonProperty("size") private Size size;
    @JsonProperty("color") private Color color;
    @JsonPropertyOrder("quantity") private int quantity = 1;

    public enum Size {
        SMALL("Small"), MEDIUM("Medium"), LARGE("Large");
        private String name;

        private Size(final String name){this.name = name;}

        @Override
        public String toString() {return name;}
    }

    public enum Color {
        BLUE("Blue"), WHITE("White"), BLACK("Black");

        private String name;

        private Color(final String name){this.name = name;}

        @Override
        public String toString() {return name;}
    }

    /**
     * Create a jersey with the given number and name
     * @param number The number of the jersey
     * @param name The name of the jersey
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provnumbered in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Jersey(
        @JsonProperty("id") int id, 
        @JsonProperty("number") int number, 
        @JsonProperty("name") String name, 
        @JsonProperty("price") double price, 
        @JsonProperty("size") Size size, 
        @JsonProperty("color") Color color,
        @JsonProperty("quantity") int quantity        
    ) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.price = price;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
    }

    /**
     * Retrieves the id of the jersey
     * @return The id of the jersey
     */
    public int getId() {return id;}

    /**
     * Retrieves the number of the jersey
     * @return The number of the jersey
     */
    public int getNumber() {return number;}

    /**
     * Sets the name of the jersey - necessary for JSON object to Java object deserialization
     * @param name The name of the jersey
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the jersey
     * @return The name of the jersey
     */
    public String getName() {return name;}

    /**
     * Retrieves the price of the jersey
     * @return The price of the jersey
     */
    public double getPrice() {return price;}

    /**
     * Retrieves the size of the jersey
     * @return The size of the jersey
     */
    public Size getSize() {return size;}

    /**
     * Retrieves the color of the jersey
     * @return The color of the jersey
     */
    public Color getColor() {return color;}

    /**
     * Retrieves the quantity of this jersey available in inventory
     * @return Quantity of jersey available
     */
    public int getQuantity() {return quantity;}

    /**
     * Sets the quantity of this jersey available in inventory
     * @param quantity New quantity
     */
    public void setQuantity(int quantity) {
        if(quantity >= 0){
            this.quantity = quantity;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,number,name,price,size,color);
    }
    
    /**
     * A Jersey equals another if all attributes are the same except ID because
     * those are always unique.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Jersey)) {
            return false;
        }
        Jersey j = (Jersey)obj;
        
        return (
            j.number == number &&
            j.name.equals(name) &&
            j.price == price &&
            j.size == size &&
            j.color == color
        );

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + number
                + name.hashCode()
                + Double.valueOf(price).hashCode()
                + size.hashCode()
                + color.hashCode();
        return result;
    }

}
