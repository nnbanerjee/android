package com.medicohealthcare.model;

public class Dependent {

    private int id;
    private String name;
    private String relation;

    private int status;

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The relation
     */
    public String getRelation() {
        return relation;
    }

    /**
     *
     * @param relation
     * The relation
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
