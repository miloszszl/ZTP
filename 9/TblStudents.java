/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author milosz
 */
@Entity
@Table(name = "TBL_STUDENTS")
@NamedQueries({
    @NamedQuery(name = "TblStudents.findAll",
            query = "SELECT t FROM TblStudents t")})
public class TblStudents implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 50)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Size(max = 50)
    @Column(name = "LASTNAME")
    private String lastname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblStudents")
    private Collection<TblStudentcourse> tblStudentcourseCollection;

    /**
     * Constructor
     */
    public TblStudents() {
    }

    /**
     * Constructor with parameter
     *
     * @param id integer id
     */
    public TblStudents(Integer id) {
        this.id = id;
    }

    /**
     * Getter for id
     *
     * @return integer id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id integer id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for firstName
     *
     * @return String firstName
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Setter for firstName
     *
     * @param firstname String firstName
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Getter for lastName
     *
     * @return lastName String lastName
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Setter for lastName
     *
     * @param lastname String lastName
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Getter for tblStudentcourseCollection
     *
     * @return Collection tblStudentcourseCollection
     */
    public Collection<TblStudentcourse> getTblStudentcourseCollection() {
        return tblStudentcourseCollection;
    }

    /**
     * Setter for tblStudentcourseCollection
     *
     * @param tblStudentcourseCollection Collection
     */
    public void setTblStudentcourseCollection(Collection<TblStudentcourse> tblStudentcourseCollection) {
        this.tblStudentcourseCollection = tblStudentcourseCollection;
    }

    /**
     * Generates hash code using id
     *
     * @return integer hash code for this object
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compares two objects
     *
     * @param object object for comparison
     * @return true if objects are equal otherwise false
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TblStudents)) {
            return false;
        }
        TblStudents other = (TblStudents) object;
        return !((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * Generates string that represents object
     *
     * @return string representing object
     */
    @Override
    public String toString() {
        return "entity2.TblStudents[ id=" + id + " ]";
    }

}
