package com.hnsh.dialogue.bean;

import android.graphics.Color;

import com.hnsh.dialogue.utils.EmptyUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author lingu
 * @create 2019/12/25 11:24
 * @Describe
 */
public class TemplateInfo implements Serializable {

    public List<ThirdApkInfo> apkList;
    public ThemeInfo theme;




    public static class ThemeInfo implements Serializable {
// "themeSubtitle": "测试",
//         "themeTitle": "测试",
//         "topEndColor": "#121222",
//         "topStartColor": "#121211"

        private String themeTitle;
        private String themeSubtitle;
        public String topStartColor;
        public String topEndColor;
        private String sideStartColor;
        private String sideEndColor;
        private String bottomStartColor;
        private String bottomEndColor;
        private String logoUrl;
        private String bottomUrl;




        public int[] getSideColors() {
            return new int[]{Color.parseColor(getSideStartColor()), Color.parseColor(getSideEndColor())};
        }
        public int[] getBottomColors() {
            return new int[]{Color.parseColor(getBottomStartColor()), Color.parseColor(getBottomEndColor())};
        }
        public int[] getTopColors() {
            return new int[]{Color.parseColor(getTopStartColor()), Color.parseColor(getTopEndColor())};
        }


        public String getThemeTitle() {
            return themeTitle;
        }

        public void setThemeTitle(String themeTitle) {
            this.themeTitle = themeTitle;
        }

        public String getThemeSubtitle() {
            return themeSubtitle;
        }

        public void setThemeSubtitle(String themeSubtitle) {
            this.themeSubtitle = themeSubtitle;
        }

        public String getTopStartColor() {
            return EmptyUtils.isEmpty(topStartColor)? "#1698c7" : topStartColor;
        }

        public int getTopStartColorInt() {
            return Color.parseColor(getTopStartColor());
        }


        public void setTopStartColor(String topStartColor) {
            this.topStartColor = topStartColor;
        }

        public String getTopEndColor() {
            return EmptyUtils.isEmpty(topEndColor)? getTopStartColor() : topEndColor;
        }

        public void setTopEndColor(String topEndColor) {
            this.topEndColor = topEndColor;
        }

        public String getSideStartColor() {
            return EmptyUtils.isEmpty(sideStartColor)? "#1698c7" : sideStartColor;
        }


        public int getSideStartColorInt() {
            return Color.parseColor(getSideStartColor());
        }

        public int getSideEndColorInt() {
            return Color.parseColor(getSideEndColor());
        }

        public void setSideStartColor(String sideStartColor) {
            this.sideStartColor = sideStartColor;
        }




        public String getSideEndColor() {
            return EmptyUtils.isEmpty(sideEndColor)? getSideStartColor() : sideEndColor;
        }

        public void setSideEndColor(String sideEndColor) {
            this.sideEndColor = sideEndColor;
        }

        public String getBottomStartColor() {
            return EmptyUtils.isEmpty(bottomStartColor)? "#0e89c6" : bottomStartColor;
        }
        public int getBottomStartColorInt() {
            return Color.parseColor(getBottomStartColor());
        }


        public void setBottomStartColor(String bottomStartColor) {
            this.bottomStartColor = bottomStartColor;
        }


        public String getBottomEndColor() {
            return EmptyUtils.isEmpty(bottomEndColor)? getBottomStartColor() : bottomEndColor;
        }

        public int getBottomEndColorInt() {
            return Color.parseColor(getBottomEndColor());
        }

        public void setBottomEndColor(String bottomEndColor) {
            this.bottomEndColor = bottomEndColor;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getBottomUrl() {
            return bottomUrl;
        }

        public void setBottomUrl(String bottomUrl) {
            this.bottomUrl = bottomUrl;
        }

        @Override
        public String toString() {
            return "ThemeInfo{" +
                    "themeTitle='" + getThemeTitle() + '\'' +
                    ", themeSubtitle='" +  getThemeSubtitle()+ '\'' +
                    ", topStartColor='" + getTopStartColor() + '\'' +
                    ", topEndColor='" + getTopEndColor() + '\'' +
                    ", sideStartColor='" + getSideStartColor() + '\'' +
                    ", sideEndColor='" + getSideEndColor() + '\'' +
                    ", bottomStartColor='" + getBottomStartColor() + '\'' +
                    ", bottomEndColor='" + getBottomEndColor() + '\'' +
                    ", logoUrl='" + getLogoUrl() + '\'' +
                    ", bottomUrl='" + getBottomUrl() + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "TemplateInfo{" +
                "apkList=" + apkList +
                ", theme=" + theme +
                '}';
    }
}
