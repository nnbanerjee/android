package Model;

/**
 * Created by Narendra on 09-02-2016.
 */
 

    public class ResponsegetVerificationCodeForNewRegistration {

        private String smsSent;
        private String emailSent;

        /**
         *
         * @return
         * The smsSent
         */
        public String getSmsSent() {
            return smsSent;
        }

        /**
         *
         * @param smsSent
         * The smsSent
         */
        public void setSmsSent(String smsSent) {
            this.smsSent = smsSent;
        }

        /**
         *
         * @return
         * The emailSent
         */
        public String getEmailSent() {
            return emailSent;
        }

        /**
         *
         * @param emailSent
         * The emailSent
         */
        public void setEmailSent(String emailSent) {
            this.emailSent = emailSent;
        }

    }
 
