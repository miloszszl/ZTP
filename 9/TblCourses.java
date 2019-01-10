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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author milosz
 */
@Entity
@Table(name = "TBL_COURSES")
@NamedQueries({
    @NamedQuery(name = "TblCourses.findAll",
            query = "SELECT t FROM TblCourses t")})
public class TblCourses implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "COURSENAME")
    private String coursename;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblCourses")
    private Collection<TblStudentcourse> tblStudentcourseCollection;

    /**
     * Constructor
     */
    public TblCourses() {
    }

    /**
     * Constructor with parameter
     *
     * @param id integer id
     */
    public TblCourses(Integer id) {
        this.id = id;
    }

    /**
     * Constructor with two parameters
     *
     * @param id integer id
     * @param coursename String course name
     */
    public TblCourses(Integer id, String coursename) {
        this.id = id;
        this.coursename = coursename;
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
     * Getter for courseName
     *
     * @return String courseName
     */
    public String getCoursename() {
        return coursename;
    }

    /**
     * Setter for courseName
     *
     * @param coursename String courseName
     */
    public void setCoursename(String coursename) {
        this.coursename = coursename;
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
    public void setTblStudentcourseCollection(
            Collection<TblStudentcourse> tblStudentcourseCollection) {
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
        if (!(object instanceof TblCourses)) {
            return false;
        }
        TblCourses other = (TblCourses) object;
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
        return "entity2.TblCourses[ id=" + id + " ]";
    }

}
