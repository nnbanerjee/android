
package com.medicohealthcare.model;

import java.util.List;

public class CustomProcedureTemplate1
{
    public Integer templateId;
    public Integer personId;
    public Integer categoryId;
    public Integer parentId;
    public String templateName;
    public String templateSubName;
    public String icon;
    public List<TemplateField> templateFields;

    public boolean isChanged = true;

    public CustomProcedureTemplate1 () {

    }
    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateSubName() {
        return templateSubName;
    }

    public void setTemplateSubName(String templateSubName) {
        this.templateSubName = templateSubName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<TemplateField> getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(List<TemplateField> templateFields) {
        this.templateFields = templateFields;
    }




    public static class TemplateField
    {
        public Integer idTemplateField;
        public Integer fieldId;
        public String fieldName;
        public String templateFieldcol;
        public String value;
        public Integer templateId;
        public void setField(String fieldValue)
        {
            this.value = fieldValue;
        }

    }
    public void setField(int fieldId, String fieldValue)
    {
        for(TemplateField field1:this.templateFields)
        {
            if(field1.fieldId.intValue()==fieldId)
            {
                field1.setField(fieldValue);
                break;
            }
        }
    }
    public TemplateField getField(int field)
    {
        for(TemplateField field1:this.templateFields)
        {
            if(field1.fieldId.intValue()==field)
                return field1;
        }
        return null;
    }


    public boolean isChanged()
    {
        return isChanged;
    }
    public void setChanged(boolean changed)
    {
        isChanged = changed;
    }
    public boolean canBeSaved()
    {
        boolean canBeSaved = true;

        if(templateId == null || templateId.intValue() >= 0 == false)
            canBeSaved = false;
        else if(personId == null || personId.intValue() > 0 == false)
            canBeSaved = false;
        else if(categoryId == null || categoryId.intValue() > 0 == false)
            canBeSaved = false;
        else if(templateName == null || templateName.trim().length() > 0 == false )
            canBeSaved = false;
        else if(templateSubName == null || templateSubName.trim().length() > 0 == false )
            canBeSaved = false;

        return canBeSaved;
    }
}