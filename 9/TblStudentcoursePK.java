/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author milosz
 */
@Embeddable
public class TblStudentcoursePK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STUDENTID")
    private int studentid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COURSEID")
    private int courseid;

    /**
     * Constructor
     */
    public TblStudentcoursePK() {
    }

    /**
     * Constructor with parameters
     *
     * @param studentid student id
     * @param courseid student id
     */
    public TblStudentcoursePK(int studentid, int courseid) {
        this.studentid = studentid;
        this.courseid = courseid;
    }

    /**
     * Getter for studentId
     *
     * @return integer student id
     */
    public int getStudentid() {
        return studentid;
    }

    /**
     * Setter for studentId
     *
     * @param studentid integer student id
     */
    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    /**
     * Getter for courseId
     *
     * @return integer course id
     */
    public int getCourseid() {
        return courseid;
    }

    /**
     * Setter for courseId
     *
     * @param courseid integer course id
     */
    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    /**
     * Generates hash code using studentId and courseId
     *
     * @return integer hash code for this object
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += studentid;
        hash += courseid;
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
        if (!(object instanceof TblStudentcoursePK)) {
            return false;
        }
        TblStudentcoursePK other = (TblStudentcoursePK) object;
        if (this.studentid != other.studentid) {
            return false;
        }
        return this.courseid == other.courseid;
    }

    /**
     * Generates string that represents object
     *
     * @return string representing object
     */
    @Override
    public String toString() {
        return "entity2.TblStudentcoursePK[ studentid="
                + studentid + ", courseid=" + courseid + " ]";
    }

}
