package com.example.hrbs;

public class OverAllIssues {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    String name,rollNo,type,issue;
    public OverAllIssues(String name,String rollNo,String type,String issue)
    {
        this.name=name;
        this.rollNo=rollNo;
        this.type=type;
        this.issue=issue;

    }

}
