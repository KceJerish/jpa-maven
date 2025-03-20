package com.example.jpamaven;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employeeId")
    private String employeeId;

    @Column(name = "employeeName")
    private String employeeName;

    @Column(name = "age")
    private int age;

    @Column(name = "birthday")
    private Date birthday;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public Employee() {}

    public Employee(String employeeId, String employeeName, int age, Date birthday) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.age = age;
        this.birthday = birthday;
    }

    public Employee(String employeeName, int age, Date birthday) {
        this.employeeName = employeeName;
        this.age = age;
        this.birthday = birthday;
    }

    public void addAddress(Address address) {
        addresses.add(address);
        address.setEmployee(this);
    }
}
