package com.medicohealthcare.model;

import java.util.List;

public interface PersonProfile
{


    /**
     *
     * @return
     * The person
     */
    public Person getPerson();

    /**
     *
     * @param person
     * The person
     */
    public void setPerson(Person person);

    /**
     *
     * @return
     * The dependents
     */
    public List<Dependent> getDependents();

    /**
     *
     * @param dependents
     * The dependents
     */
    public void setDependents(List<Dependent> dependents);

    /**
     *
     * @return
     * The delegates
     */
    public List<Delegation> getDelegates();

    /**
     *
     * @param delegates
     * The delegates
     */
    public void setDelegates(List<Delegation> delegates);

    public boolean isDependentProfile();

    public void setDependentProfile(boolean dependentProfile);

}