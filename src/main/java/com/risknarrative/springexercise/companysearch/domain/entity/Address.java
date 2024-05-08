package com.risknarrative.springexercise.companysearch.domain.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ADDRESS_")
@NamedQuery(
        name = "Address.findByCriteria",
        query = "SELECT a FROM Address a WHERE a.locality = :locality AND a.postal_code = :postal_code AND " +
                "a.premises = :premises AND a.address_line_1 = :address_line_1 AND a.country = :country"
)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    
    @Column(length = 100)
    private String locality;

    @Column(name = "POSTAL_CODE", length = 100)
    private String postal_code;
    
    @Column(length = 100)
    private String premises;
    
    @Column(name = "ADDRESS_LINE_1", length = 100)
    private String address_line_1;
    
    @Column(length = 100)
    private String country;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Address)) {
            return false;
        }
        
        Address other = (Address) object;
        return !((this.id == null && other.id != null) 
                || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Address: locality=" + locality 
                + ", postalCode=" + postal_code 
                + ", premises=" + premises 
                + ", address_line_1=" + address_line_1 
                + ", country=" + country;
    }

    
}
