package com.risknarrative.springexercise.companysearch.domain.entity;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "OFFICER_")

@NamedQueries({
@NamedQuery(
        name = "Officer.findByCompanyNumber",
        query = "SELECT o FROM Officer o WHERE o.company.company_number = :company_number"
),
@NamedQuery(
        name = "Officer.findByCriteria", query = "SELECT o FROM Officer o WHERE " +
        "o.company.company_number = :company_number AND o.name = :name AND o.officer_role = :officer_role"
)})
public class Officer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    
    @Column(name = "NAME_", length = 100)
    private String name;
    
    @Column(name = "OFFICER_ROLE")
    private String officer_role;
    
    @Column(name = "APPOINTED_ON")
    @Temporal(TemporalType.DATE)
    private Date appointed_on;

    @Column(name = "RESIGNED_ON")
    @Temporal(TemporalType.DATE)
    private Date resigned_on;
    
    @JoinColumn(name = "ADDRESS", referencedColumnName = "ID")
    @ManyToOne
    private Address address;
    
    @JoinColumn(name = "COMPANY", referencedColumnName = "COMPANY_NUMBER")
    @ManyToOne
    private Company company;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Officer)) {
            return false;
        }
        Officer other = (Officer) object;
        return !((this.id == null && other.id != null) 
                || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Officer: name=" + name
                + ", officer_role=" + officer_role 
                + ", appointed_on=" + appointed_on
                + ", resigned_on=" + resigned_on
                + ", address=" + address
                + ", company=" + company;
    }
    
}
