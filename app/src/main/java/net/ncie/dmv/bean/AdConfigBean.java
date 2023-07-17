package net.ncie.dmv.bean;

public class AdConfigBean {

    /**
     * Fire_Testing_Ad_Interval : 5
     * show_ad_configure : {"Fire_All_Ad_Show_Max":"30","Fire_Interstitial_Show_Max":"10","Fire_Native_main_Show_Max":"10","Fire_Native_node_Show_Max":"10","Fire_Native_result_Show_Max":"10","Fire_Open_Ad_Show_Max":"10"}
     * click_ad_configure : {"Fire_All_Clicks_Max":"999","Fire_Interstitial_Clicks_Max":"999","Fire_Native_main_Clicks_Max":"999","nameFire_Native_node_Clicks_Max":"999","Fire_Native_result_Clicks_Max":"999","Fire_Open_Clicks_Max":"999"}
     */

    private String Fire_Testing_Ad_Interval;
    private ShowAdConfigureBean show_ad_configure;
    private ClickAdConfigureBean click_ad_configure;

    public String getFire_Testing_Ad_Interval() {
        return Fire_Testing_Ad_Interval;
    }

    public void setFire_Testing_Ad_Interval(String Fire_Testing_Ad_Interval) {
        this.Fire_Testing_Ad_Interval = Fire_Testing_Ad_Interval;
    }

    public ShowAdConfigureBean getShow_ad_configure() {
        return show_ad_configure;
    }

    public void setShow_ad_configure(ShowAdConfigureBean show_ad_configure) {
        this.show_ad_configure = show_ad_configure;
    }

    public ClickAdConfigureBean getClick_ad_configure() {
        return click_ad_configure;
    }

    public void setClick_ad_configure(ClickAdConfigureBean click_ad_configure) {
        this.click_ad_configure = click_ad_configure;
    }

    public static class ShowAdConfigureBean {
        /**
         * Fire_All_Ad_Show_Max : 30
         * Fire_Interstitial_Show_Max : 10
         * Fire_Native_main_Show_Max : 10
         * Fire_Native_node_Show_Max : 10
         * Fire_Native_result_Show_Max : 10
         * Fire_Open_Ad_Show_Max : 10
         */

        private String Fire_All_Ad_Show_Max;
        private String Fire_Interstitial_Show_Max;
        private String Fire_Native_main_Show_Max;
        private String Fire_Native_node_Show_Max;
        private String Fire_Native_result_Show_Max;
        private String Fire_Open_Ad_Show_Max;

        public String getFire_All_Ad_Show_Max() {
            return Fire_All_Ad_Show_Max;
        }

        public void setFire_All_Ad_Show_Max(String Fire_All_Ad_Show_Max) {
            this.Fire_All_Ad_Show_Max = Fire_All_Ad_Show_Max;
        }

        public String getFire_Interstitial_Show_Max() {
            return Fire_Interstitial_Show_Max;
        }

        public void setFire_Interstitial_Show_Max(String Fire_Interstitial_Show_Max) {
            this.Fire_Interstitial_Show_Max = Fire_Interstitial_Show_Max;
        }

        public String getFire_Native_main_Show_Max() {
            return Fire_Native_main_Show_Max;
        }

        public void setFire_Native_main_Show_Max(String Fire_Native_main_Show_Max) {
            this.Fire_Native_main_Show_Max = Fire_Native_main_Show_Max;
        }

        public String getFire_Native_node_Show_Max() {
            return Fire_Native_node_Show_Max;
        }

        public void setFire_Native_node_Show_Max(String Fire_Native_node_Show_Max) {
            this.Fire_Native_node_Show_Max = Fire_Native_node_Show_Max;
        }

        public String getFire_Native_result_Show_Max() {
            return Fire_Native_result_Show_Max;
        }

        public void setFire_Native_result_Show_Max(String Fire_Native_result_Show_Max) {
            this.Fire_Native_result_Show_Max = Fire_Native_result_Show_Max;
        }

        public String getFire_Open_Ad_Show_Max() {
            return Fire_Open_Ad_Show_Max;
        }

        public void setFire_Open_Ad_Show_Max(String Fire_Open_Ad_Show_Max) {
            this.Fire_Open_Ad_Show_Max = Fire_Open_Ad_Show_Max;
        }
    }

    public static class ClickAdConfigureBean {
        /**
         * Fire_All_Clicks_Max : 999
         * Fire_Interstitial_Clicks_Max : 999
         * Fire_Native_main_Clicks_Max : 999
         * nameFire_Native_node_Clicks_Max : 999
         * Fire_Native_result_Clicks_Max : 999
         * Fire_Open_Clicks_Max : 999
         */

        private String Fire_All_Clicks_Max;
        private String Fire_Interstitial_Clicks_Max;
        private String Fire_Native_main_Clicks_Max;
        private String nameFire_Native_node_Clicks_Max;
        private String Fire_Native_result_Clicks_Max;
        private String Fire_Open_Clicks_Max;

        public String getFire_All_Clicks_Max() {
            return Fire_All_Clicks_Max;
        }

        public void setFire_All_Clicks_Max(String Fire_All_Clicks_Max) {
            this.Fire_All_Clicks_Max = Fire_All_Clicks_Max;
        }

        public String getFire_Interstitial_Clicks_Max() {
            return Fire_Interstitial_Clicks_Max;
        }

        public void setFire_Interstitial_Clicks_Max(String Fire_Interstitial_Clicks_Max) {
            this.Fire_Interstitial_Clicks_Max = Fire_Interstitial_Clicks_Max;
        }

        public String getFire_Native_main_Clicks_Max() {
            return Fire_Native_main_Clicks_Max;
        }

        public void setFire_Native_main_Clicks_Max(String Fire_Native_main_Clicks_Max) {
            this.Fire_Native_main_Clicks_Max = Fire_Native_main_Clicks_Max;
        }

        public String getNameFire_Native_node_Clicks_Max() {
            return nameFire_Native_node_Clicks_Max;
        }

        public void setNameFire_Native_node_Clicks_Max(String nameFire_Native_node_Clicks_Max) {
            this.nameFire_Native_node_Clicks_Max = nameFire_Native_node_Clicks_Max;
        }

        public String getFire_Native_result_Clicks_Max() {
            return Fire_Native_result_Clicks_Max;
        }

        public void setFire_Native_result_Clicks_Max(String Fire_Native_result_Clicks_Max) {
            this.Fire_Native_result_Clicks_Max = Fire_Native_result_Clicks_Max;
        }

        public String getFire_Open_Clicks_Max() {
            return Fire_Open_Clicks_Max;
        }

        public void setFire_Open_Clicks_Max(String Fire_Open_Clicks_Max) {
            this.Fire_Open_Clicks_Max = Fire_Open_Clicks_Max;
        }
    }
}
