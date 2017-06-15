package com.example.s525339.partygaurd_androidnachos;

        /**
 + * Created by s525140 on 9/13/2016.
         This is the Issues class.It holds the issue along with the location name.
 + */
        public class Issues {
        String issueName;
        public Issues(String issueName){
                this.issueName=issueName;
            }

                public String getIssueName(){
                return issueName;
            }
        public void setIssueName(String locationName){
                this.issueName=locationName;
            }
    }