package com.medico.model;

/**
 * Created by Narendra on 27-01-2017.
 */

public class ProfileId
{
    Integer profileId;
    Integer personId;

    public ProfileId(Integer profileId)
    {
        this.profileId = profileId;
        this.personId = profileId;
    }

    public Integer getProfileId()
    {
        return profileId;
    }

    public void setProfileId(Integer profileId)
    {
        this.personId = profileId;
        this.profileId = profileId;
    }

    public Integer getPersonId()
    {
        return personId;
    }
}
