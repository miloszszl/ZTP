/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author milosz
 */
@Entity
@Table(name = "TBL_STUDENTCOURSE")
@NamedQueries({
    @NamedQuery(name = "TblStudentcourse.findAll",
            query = "SELECT t FROM TblStudentcourse t")})
public class TblStudentcourse implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblStudentcoursePK tblStudentcoursePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MARK")
    private int mark;
    @JoinColumn(name = "COURSEID", referencedColumnName = "ID",
            insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TblCourses tblCourses;
    @JoinColumn(name = "STUDENTID", referencedColumnName = "ID",
            insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TblStudents tblStudents;

    /**
     * Constructor
     */
    public TblStudentcourse() {
    }

    /**
     * Constructor with parameter
     *
     * @param tblStudentcoursePK TblStudentcoursePK object
     */
    public TblStudentcourse(TblStudentcoursePK tblStudentcoursePK) {
        this.tblStudentcoursePK = tblStudentcoursePK;
    }

    /**
     * Constructor with two parameters
     *
     * @param tblStudentcoursePK TblStudentcoursePK object
     * @param mark integer mark
     */
    public TblStudentcourse(TblStudentcoursePK tblStudentcoursePK, int mark) {
        this.tblStudentcoursePK = tblStudentcoursePK;
        this.mark = mark;
    }

    /**
     * Constructor with two parameters
     *
     * @param studentid integer student id
     * @param courseid integer course id
     */
    public TblStudentcourse(int studentid, int courseid) {
        this.tblStudentcoursePK = new TblStudentcoursePK(studentid, courseid);
    }

    /**
     * Getter for tblStudentcoursePK
     *
     * @return TblStudentcoursePK tblStudentcoursePK
     */
    public TblStudentcoursePK getTblStudentcoursePK() {
        return tblStudentcoursePK;
    }

    /**
     * Setter for tblStudentcoursePK
     *
     * @param tblStudentcoursePK TblStudentcoursePK object
     */
    public void setTblStudentcoursePK(TblStudentcoursePK tblStudentcoursePK) {
        this.tblStudentcoursePK = tblStudentcoursePK;
    }

    /**
     * Getter for mark
     *
     * @return integer mark
     */
    public int getMark() {
        return mark;
    }

    /**
     * Setter for mark
     *
     * @param mark integer mark
     */
    public void setMark(int mark) {
        this.mark = mark;
    }

    /**
     * Getter for tblCourses
     *
     * @return TblCourses tblCourses object
     */
    public TblCourses getTblCourses() {
        return tblCourses;
    }

    /**
     * Setter for tblCourses
     *
     * @param tblCourses TblCourses object
     */
    public void setTblCourses(TblCourses tblCourses) {
        this.tblCourses = tblCourses;
    }

    /**
     * Getter for tblStudents
     *
     * @return tblStudents TblStudents object
     */
    public TblStudents getTblStudents() {
        return tblStudents;
    }

    /**
     * Setter for tblStudents
     *
     * @param tblStudents TblStudents tblStudents
     */
    public void setTblStudents(TblStudents tblStudents) {
        this.tblStudents = tblStudents;
    }

    /**
     * Generates hash code using tblStudentcoursePK
     *
     * @return integer hash code for this object
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblStudentcoursePK != null
                ? tblStudentcoursePK.hashCode() : 0);
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
        if (!(object instanceof TblStudentcourse)) {
            return false;
        }
        TblStudentcourse other = (TblStudentcourse) object;
        return !((this.tblStudentcoursePK == null
                && other.tblStudentcoursePK != null)
                || (this.tblStudentcoursePK != null
                && !this.tblStudentcoursePK.equals(other.tblStudentcoursePK)));
    }

    /**
     * Generates string that represents object
     *
     * @return string representing object
     */
    @Override
    public String toString() {
        return "entity2.TblStudentcourse[ tblStudentcoursePK="
                + tblStudentcoursePK + " ]";
    }

}
