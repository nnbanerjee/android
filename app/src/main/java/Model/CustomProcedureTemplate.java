
package Model;

import java.util.ArrayList;
import java.util.List;

public class CustomProcedureTemplate {

    private String templateId;
    private String personId;
    private String categoryId;
    private String parentId;
    private String templateName;
    private String templateSubName;
    private String icon;
    private List<TemplateField> templateFields = new ArrayList<TemplateField>();

    /**
     *
     * @return
     * The templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     *
     * @param templateId
     * The templateId
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     *
     * @return
     * The personId
     */
    public String getPersonId() {
        return personId;
    }

    /**
     *
     * @param personId
     * The personId
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     *
     * @return
     * The categoryId
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     *
     * @param categoryId
     * The categoryId
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     *
     * @return
     * The parentId
     */
    public String getParentId() {
        return parentId;
    }

    /**
     *
     * @param parentId
     * The parentId
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     *
     * @return
     * The templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     *
     * @param templateName
     * The templateName
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     *
     * @return
     * The templateSubName
     */
    public String getTemplateSubName() {
        return templateSubName;
    }

    /**
     *
     * @param templateSubName
     * The templateSubName
     */
    public void setTemplateSubName(String templateSubName) {
        this.templateSubName = templateSubName;
    }

    /**
     *
     * @return
     * The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     *
     * @param icon
     * The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     *
     * @return
     * The templateFields
     */
    public List<TemplateField> getTemplateFields() {
        return templateFields;
    }

    /**
     *
     * @param templateFields
     * The templateFields
     */
    public void setTemplateFields(List<TemplateField> templateFields) {
        this.templateFields = templateFields;
    }

}