package com.medicohealthcare.model;

/**
 * Created by Narendra on 03-02-2016.
 */
public class AssistantId
{
    public AssistantId(Integer assistantId)
    {
        this.assistantId = assistantId;
    }

    public Integer getDoctorId() {
        return assistantId;
    }

    public void setAssistantId(Integer assistantId) {
        this.assistantId = assistantId;
    }

    private Integer assistantId;
}
