package com.suntelecoms.library_mifare.Util;

public class Constantes {


    /**
     * lecteur de carte constante
     */
    public static String _blankBlock = "00000000000000000000000000000000";

    public static final String NBR_BLOC_EXTRA_NAME = "nombreBloc";
    public static final int REDIRECT_ACTIVITY = 10;
    public static final int NBRE_BLOC_SHOWBOX_NTAG_216 = 225;
    public static final int NBRE_BLOC_SHOWBOX_MIFARE_1K = 64; //15 secteurs et 64 blocs
    public static final int ENCODAGE = 1;
    public static final int MISE_A_JOUR_CARTE = 51;
    public static final int LECTURE = 2;
    public static final int CONFIRMATION_LECTURE = 243;
    public static final int VERIF_PIN = 3;
    public static final int CONFIRMER_TRANSACTION = 4;
    public static final int AUTHENTIFICATION = 5;
    public static final int VERIF_PIN_SERVICE_CIBLE = 6;
    public static final int IDENTIFICATION_PIN = 12;
    public static final int CONTROLE = 7;
    public static final int RESULT_CODE_ECHEC_LECTURE = 10;
    /**
     * FIN lecteur de carte constante
     */

    /**
     * CODES
     */
    public static final String CODE_PROFIL_ETUDIANT = "ETU";
    public static final String CODE_PROFIL_PROFESSEUR = "PROF";

    /**
     * DEB Boolean string representation
     */
    public static final String FALSE = "false";
    public static final String TRUE = "true";
    /**
     * FIN Boolean string representation
     */




    /**
     * Message types
     */
    public class MessageType {
        public static final int SUCCESS = 1;
        public static final int INFO = 2;
        public static final int WARNING = 3;
        public static final int ERROR = 4;
    }


    public static final String RUNTIME_EXCEPTION_CODE = "RUN_TM_EXP";
    public static final String EXCEPTION_CODE = "EXP";
    public static final String WINDOWMANAGER_BADTOKENEXCEPTION = "WIN_BAD_EXP";
    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    public static final int NBR_CARACTERE_ESPECIAL = 2;
    public static final int NBR_DIGIT_ESPECIAL = 2;


    public static final String CYNOD_MESSAGE = "cynodMsg";
    public static String[] stateStrings = {"Unknown", "Absent",
            "Present", "Swallowed", "Powered", "Negotiable", "Specific"};

    /********************* A ENLEVER ************************/

    /**
     * Type events
     */
    public static final String EVENT_SCHOOL = "SCHOOL";
    /********************* A ENLEVER ************************/


    /** INIT KEYSTORE **/
    public static String initKeyA =  "313835334B52" ;//hexa -> 313835334B52, ascii ->  1853KR
    public static String bitAccess = "FF078069"; //FF078000
    public enum keyStore {
        secretKey,secretKeyA, secretKeyB, bitAccess
    }


    public static final String NFC_RESULT_OBJECT_NAME = "cynodMsg";


    public static final String MISE_A_JOUR = "MAJ";

    /**
     * Audit types
     */
    public static final String AUDIT_ADVERT = "ADVERT";
    public static final String AUDIT_SERVICE = "SERVICE";
    public static final String AUDIT_POST = "POST";

    /**
     * Type events
     */
    public static final String DB_DIR = "/db";
    public static final String CURRENT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_WITH_HYPHEN = "yyyy-MM-dd";
    public static final String DATE_FORMAT_FULL = "yyyy-MM-ddTHH:mm:ssZ";
    public static final String DATE_FORMAT_WITH_SLASH = "dd/MM/yyyy";
    public static final String TIME_FORMAT = "HH:mm";

    /**
     * Formats affichage
     */
    public static final String DISPLAY_ONE_COLUMN = "1C";
    public static final String DISPLAY_ONE_COLUMN_TWO_BLOCS = "1C2";
    public static final String DISPLAY_TWO_COLUMNS = "2C";
    /**
     * Intent Codes
     */
    public static final String INTENT_TOPUP_OPERATION_CODE = "topupOpe";
    /**
     * Op prefixes
     */
    public static final String FEEDBACK_PREFIX = "COLLECTE";
    public static final String AUDIT_PREFIX = "AUDIT";
    public static final String OPERATION_PREFIX = "OPE";
    public static final String TOPUP_PREFIX = "TOPUP";
    /**
     * PARAM COLLECTE
     */
    public static final String PARAM_COLLECTE_TEXTE = "TEXT";
    public static final String PARAM_COLLECTE_OPTION = "OPTION";
    public static final String PARAM_COLLECTE_NUMBER = "NUMBER";
    public static final String PARAM_COLLECTE_CHECK = "CHECK";
    public static final String TYPE_PUB_VIDEO = "video";
    public static final String TYPE_PUB_IMG = "image";
    /**
     * Services
     */
    public static final String MAIN_SERVICE_CODE = "MAIN";
    public static final String SERVICE_TYPE_LIBRE = "FREE";
    public static final String SERVICE_TYPE_CIBLE = "PRIVATE";
    public static final String SERVICE_TYPE_PAYANT = "P";
    /**
     * Status codes
     */
    public static final String STATUS_NEW = "NEW";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_VALIDATED = "OK";
    public static final String STATUS_ERROR = "ERROR";
    public static final String STATUS_FAILED = "NOK";
    /**
     * Operation Types
     */
    public static final String OPERATION_DEBIT = "DEBIT";
    public static final String OPERATION_CREDIT = "CREDIT";
    /**
     * Payment types
     */
    public static final String PAYMENT_CARD = "CARD";
    /**
     * Boolean string representation
     */


    public static final String NON = "non";
    public static final String OUI = "oui";

    /**
     * Time
     */
    public static final int DEFAULT_FLIP_INTERVAL = 60000;
    /**
     * Topup
     */
    public static final String TOPUP_WALLET = "WLT";
    public static final String TOPUP_AIRTIME = "ART";
    public static final String TOPUP_DEPOSIT = "DEP";
    public static final String TOPUP_VENTE_AIRTIME = "VENT_ART";

    /**
     * lecteur de carte constante
     */
    public static final int LECTURE_EMPLOI_TEMPS = 244;
    public static final int POINTAGE_CARTE = 250;
    public static final int POINTAGE_PARTICIPANT = 251;
    public static final int LECTURE_NOTES = 245;
    public static final int LECTURE_MA_CARTE = 246;
    public static final int LECTURE_DOCUMENT = 247;
    public static final int LECTURE_MISE_A_JOUR = 25;
    public static final int REDIRECT_ACTIVITY_PUBLICITE_INTERCALAIRE = 40;
    public static final int NBRE_BLOC_SHOWBOX = 225;
    /**
     * FIN lecteur de carte constante
     */

    public static final String CARTE_STATUT = "sn.sensoft.showbox.util.Constantes.CARTE_STATUT";
    public static final String STATUS = "sn.sensoft.showbox.util.Constantes.STATUS";
    public static final String ICONE_STATE = "sn.sensoft.showbox.util.Constantes.ICONE_STATE";
    public static final String MESSAGE = "sn.sensoft.showbox.util.Constantes.MESSAGE";
    public static final int DEBIT = -1;
    public static final int CREDIT = 1;
    /**
     * Profiles
     */
    public static final String PROFILE_TYPE_INTERACTIVE = "INTERACTIVE";
    public static final String PROFILE_TYPE_NON_INTERACTIVE = "SIMPLE";
    public static final String PROFILE_TYPE_SAD = "SUPERADMIN";

    /**
     * Jasper server report names
     */
    public enum JasperReport {
        CERTIFICAT_DE_SCOLARITE("certificat_de_scolarite"),
        RELEVE_DE_NOTE("releve_de_note"),
        RELEVE_COMPTE("releve_compte");

        private String name;

        JasperReport(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum ParamTerminalCode {
        terminalAfficheur("TA"),
        afficheServiceLibre("ASL"),
        resolution("RL"),
        imprimanteBluetooth("IB"),
        urlImpression("UI"),
        urlWebServerTopUp("UTP"),
        urlGfa("GFA"),
        lecteurCarteNFC("LCN"),
        lecteurCarteExterne("LCE"),
        lecteurBiometrique("BIO"),
        lecteurQRCode("QRC"),
        delaiInactivite("DI"),
        delaiMiseAJourPub("DP"),
        delaiMiseAJourPhoto("DIP"),
        delaiRemonteAudit("DRA"),
        delaiRemonteCarteEncode("DRC"),
        delaiMiseAjourDetailServiceLibre("DDSL"),
        delaiMiseAjourService("DMS"),
        delaiRemonteOperation("DRO"),
        delaiMiseAjourTypeFrais("DMTF"),
        delaiRemonteInfosPubCollected("DRI"),
        delaiMiseAjourParametre("DMP"),
        montantRechargeMin("RMI"),
        montantRechargeMax("RMX"),
        montantRetraitMin("RTMI"),
        montantAchatCreditMin("MACMI"),
        montantAchatCreditMax("MACMA"),
        devise("DEV"),
        delaiRemontePointage("DRP"),
        pageServiceFixe("PSF"),
        delaiBalayageService("DBS"),
        volumeAppelTerminal("VOL"),
        codeSecret("CODSCRT"),
        messageGfa("MSGGFA"),
        montantRetraitMax("RTMX"),
        layoutServiceHorizontal("LSH");
        String code;

        ParamTerminalCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }


    public class TagService {
        public static final String ACTUALITES = "AC";
        public static final String INFOS_UTILES = "IU";
        public static final String NOS_FILIERES = "NF";
        public static final String WALLET = "WLT";
        public static final String ORANGE_MONEY = "DEP_OM";
        public static final String ACHAT_CREDIT = "ART";
        public static final String ORANGE_SEDDO = "ART_SEDDO";
        public static final String TIGO_IZI = "ART_IZI";
        public static final String MA_CARTE = "MC";
        public static final String MISE_A_JOUR_CARTE = "MAJ";
        public static final String CERTIFICAT_SCOLARITE = "CS";
        public static final String IMP_CERTIFICAT_SCOLARITE = "IMP_CS";
        public static final String IMPRESSION_DOC = "IMP";
        public static final String IMP_MES_NOTES = "IMP_NOTES";
        public static final String MES_NOTES = "MN";
        public static final String EMPLOI_TEMPS = "ME";
        public static final String ENCODAGE = "ENC";
        public static final String ENCODAGE_LOT_CARTE = "ENCL";
        public static final String CONTROLE_CARTE = "CNT";
        public static final String RECHARGE_CARTE = "RC";
        public static final String RETRAIT_CARTE = "RT";
        public static final String POINTAGE = "PC";
        public static final String POINTAGE_PARTICIPANT = "PP";
        public static final String IMPRESSION_TICKET_CAISSE = "IMPTC";
        public static final String GET_SOLDE_COMPTE = "GETSLDCPT";
        public static final String GET_RELEVE_BANQUAIRE = "GETRLVBAQ";

    }

    public static final String INSERT = "INSERT";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
    public static final String NONE = "NONE";
    public static final String SEND_SOLDE_SMS = "sendSoldeSms";
    public static final String IMPRIMER_RELEVE_BANCAIRE = "imprimerReleveBancaire";

    public static final int DIALOG_CONSULTATION_SOLDE = 0;
    public static final int DIALOG_CONSULTATION_RLV_BANCAIRE = 1;
    public static final int DIALOG_CONSULTATION_TAB_AMT_PRET = 2;



    /** NFC **/
    public static final int FORMAT_TRAME_INCORRECT = 1;
    public static final int RESULT_CODE_ECHEC_ECRITURE = 9;

    public static final int RESULT_CODE_ECHEC_LECTURE_= 10;
    public static int ECHEC_PREPARATION_COMMAND_LECTURE = 1;

}
