package com.risknarrative.springexercise.companysearch.domain.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
@Table(name = "COMPANY_")
@NamedQueries({
      @NamedQuery(name = "Company.findByCompanyNumber", query = "SELECT c FROM Company c WHERE c.company_number = :company_number")
    , @NamedQuery(name = "Company.findByTitle", query = "SELECT c FROM Company c WHERE UPPER(c.title) = UPPER(:title)")})
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "COMPANY_NUMBER", nullable = false, length = 100)
    private String company_number;
    
    @Column(name = "COMPANY_TYPE", length = 100)
    private String company_type;

    @Column(length = 100)
    private String title;
    
    @Column(name = "COMPANY_STATUS", length = 100)
    private String company_status;
    
    @Column(name = "DATE_OF_CREATION")
    @Temporal(TemporalType.DATE)
    private Date date_of_creation;
    
    @JoinColumn(name = "ADDRESS", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Address address;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<Officer> officers;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (company_number != null ? company_number.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        return !((this.company_number == null && other.company_number != null) 
                || (this.company_number != null && !this.company_number.equals(other.company_number)));
    }

    @Override
    public String toString() {
        return "Company: company_number=" + company_number 
                + ", company_type=" + company_type 
                + ", title=" + title 
                + ", company_status=" + company_status 
                + ", date_of_creation=" + date_of_creation;
    }

    
}
