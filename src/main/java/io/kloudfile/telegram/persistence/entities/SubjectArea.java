/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.kloudfile.telegram.persistence.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author fr3d63
 */
@Entity
@Table(name = "subject_area")
@XmlRootElement
/*
@NamedQueries({
    @NamedQuery(name = "SubjectArea.findAll", query = "SELECT s FROM SubjectArea s"),
    @NamedQuery(name = "SubjectArea.findById", query = "SELECT s FROM SubjectArea s WHERE s.id = :id"),
    @NamedQuery(name = "SubjectArea.findByName", query = "SELECT s FROM SubjectArea s WHERE s.name = :name"),
    @NamedQuery(name = "SubjectArea.findByHostkey", query = "SELECT s FROM SubjectArea s WHERE s.hostkey = :hostkey")})
*/
public class SubjectArea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "hostkey")
    private String hostkey;
    @JoinTable(name = "user_has_subject_area", joinColumns = {
        @JoinColumn(name = "subject_area_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    @ManyToMany
    private List<User> userList;

    public SubjectArea() {
    }

    public SubjectArea(Integer id) {
        this.id = id;
    }

    public SubjectArea(Integer id, String name, String hostkey) {
        this.id = id;
        this.name = name;
        this.hostkey = hostkey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostkey() {
        return hostkey;
    }

    public void setHostkey(String hostkey) {
        this.hostkey = hostkey;
    }

    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubjectArea)) {
            return false;
        }
        SubjectArea other = (SubjectArea) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.SubjectArea[ id=" + id + " ]";
    }

    public void addUser(User user) {
        if (!userList.contains(user)) {
            this.userList.add(user);
            user.addSubjectArea(this);
        }
    }

    public void removeUser(User user) {
        if (userList.contains(user)) {
            userList.remove(user);
            user.removeSubjectArea(this);
        }
    }
}
