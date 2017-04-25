package com.che58.ljb.rxjava.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class Student {
    public String name;
    public int age;
    public List<Course> course;

    public Student(String _name, List<Course> list) {
        name = _name;
        course = list;
    }
}
