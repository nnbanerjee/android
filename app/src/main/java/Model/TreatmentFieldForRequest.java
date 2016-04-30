package Model;

/**
 * Created by Narendra on 21-04-2016.
 */
public class TreatmentFieldForRequest {



        private String fieldId;
        private String fieldName;
        private String value;

        public boolean isUpdated() {
            return updated;
        }

        public void setUpdated(boolean updated) {
            this.updated = updated;
        }

        private boolean updated;

        public String getTreatmentAttributeId() {
            return treatmentAttributeId;
        }

        public void setTreatmentAttributeId(String treatmentAttributeId) {
            this.treatmentAttributeId = treatmentAttributeId;
        }

        private String treatmentAttributeId;
        private String treatmentId;
        public TreatmentFieldForRequest(){}

        public TreatmentFieldForRequest(String fieldId, String fieldName, String value, String treatmentId) {
            this.fieldId = fieldId;
            this.fieldName = fieldName;
            this.value = value;
            this.treatmentId = treatmentId;
        }



        /**
         *
         * @return
         * The fieldId
         */
        public String getFieldId() {
            return fieldId;
        }

        /**
         *
         * @param fieldId
         * The fieldId
         */
        public void setFieldId(String fieldId) {
            this.fieldId = fieldId;
        }

        /**
         *
         * @return
         * The fieldName
         */
        public String getFieldName() {
            return fieldName;
        }

        /**
         *
         * @param fieldName
         * The fieldName
         */
        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        /**
         *
         * @return
         * The value
         */
        public String getValue() {
            return value;
        }

        /**
         *
         * @param value
         * The value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         *
         * @return
         * The treatmentId
         */
        public String getTreatmentId() {
            return treatmentId;
        }

        /**
         *
         * @param treatmentId
         * The treatmentId
         */
        public void setTreatmentId(String treatmentId) {
            this.treatmentId = treatmentId;
        }



}
