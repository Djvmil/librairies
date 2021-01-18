package com.suntelecoms.library_mifare.Util;

import android.util.Log;

import com.sensoftsarl.cynod.CynodMsg;

import com.suntelecoms.library_mifare.Activities.ReadAllSectors;
import com.suntelecoms.library_mifare.BuildConfig;

public class GenerateMsgTemplateTest {

    static String LOG_TAG = ReadAllSectors.class.getSimpleName();

    public static void trameEtudiant(){
       try{
           String trameStringValue = "f080004febd3000020180481        713218454903854427Abdoul Aziz Jonathan TRAORExlME8GiIdn0=                                                    ETU  BEI            Rentrée A 2019-Licence 1/SemesGroupe 4 S1A 19-20            1909261909010018000000050082403411:30000/0:|12:150000/0:01/09/201920straore300@gmail/com002267143950012  yQjc2XU3HpQ=                                                    ";
           Log.d(LOG_TAG, "-- DEB trameEtudiant() trameStringValue = ("+trameStringValue+") --");

           CynodMsg cynodMsg = new CynodMsg();
           cynodMsg.parseMsg(trameStringValue);
           cynodMsg.fillDataInBlock1K(trameStringValue);

           if(BuildConfig.DEBUG){
               Log.i(LOG_TAG,"cynodMsg log => "+cynodMsg.logMsg());
           }
           Log.i(LOG_TAG,"logAsciiDataInBlock1K => "+cynodMsg.logAsciiDataInBlock1K());
           Log.d(LOG_TAG, "-- END trameEtudiant() --");
       }catch (Exception ex){
           Log.e(LOG_TAG, "Exception : "+ex.getLocalizedMessage());
           ex.printStackTrace();
       }
    }



}
