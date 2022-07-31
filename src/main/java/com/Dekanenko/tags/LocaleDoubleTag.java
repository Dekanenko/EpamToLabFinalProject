package com.Dekanenko.tags;

import java.io.IOException;

import javax.servlet.jsp.tagext.TagSupport;

public class LocaleDoubleTag extends TagSupport {

    private String locale;
    private double number;

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    @Override
    public int doStartTag() {
        String num = number+"";
        if(locale.equals("en") || locale.equals("")){
            num = num.replace(',', '.');
        }else if (locale.equals("uk")){
            num = num.replace('.', ',');
        }
        try {
            pageContext.getOut().println(num);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
