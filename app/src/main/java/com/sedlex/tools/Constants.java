package com.sedlex.tools;

public class Constants {

    public static final String URL_LAWS = "http://sedlex.jbcid.me/laws?page=";
    public static final String URL_LAW_DETAILS = "http://sedlex.jbcid.me/laws/";
    public static final String URL_GET_DEBATES = "/debates";

    public static final String URL_LAWS_DEBUG = "http://api.letsbeehive.tk/static";

    public static final int GetProgressFromMapping(String step){

        if(step.equals("conseil-des-ministres_projet") || step.equals("conseil-ministres_ordonnance")){
            return 0;
        }
        else if(step.equals("depot-au-parlement_projet") || step.equals("depot-au-parlement_proposition")){
               return 0;
        }
        else if(step.equals("examen_projet") || step.equals("examen_proposition")){
                return 1;
        }
        else if (step.equals("promulgation_proposition") || step.equals("promulgation-signature_ordonnance") || step.equals("promulgation_projet") || step.equals("decret-application_projet") || step.equals("decret-application_proposition") || step.equals("evaluation_projet") || step.equals("loi-ratification_ordonnance") || step.equals("evaluation_proposition")){
            return 2;
        }
        else{
            return 0;
        }
    }
}
