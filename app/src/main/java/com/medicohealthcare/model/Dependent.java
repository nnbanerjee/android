package com.medicohealthcare.model;

public class Dependent {

    private int id;
    private String name;
    private String relation;

    private int status;


    public Dependent()
    {

    }

    public Dependent(int id, String name, String relation, int status)
    {
        this.id = id;
        this.name = name;
        this.relation = relation;
        this.status = status;
    }

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
