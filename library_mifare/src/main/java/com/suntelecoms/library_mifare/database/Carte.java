package com.suntelecoms.library_mifare.database;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.sensoftsarl.cynod.CynodField;
import com.sensoftsarl.cynod.CynodMsg;
import com.sensoftsarl.cynod.FieldFormat;
import com.sensoftsarl.cynod.FieldType;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

import com.suntelecoms.library_mifare.Util.Constantes;
import com.suntelecoms.library_mifare.Util.ConversionUtil;
import com.suntelecoms.library_mifare.Util.SecurityUtil;

public class Carte {

    private static final String LOG_TAG = Carte.class.getSimpleName();

    private static final long serialVersionUID = -1720905402701388278L;
    private Long id;
    private Long version = 0L;

    @NotNull
    private String cardProfile;

    @NotNull
    private String ccnumber;

    @NotNull
    private Date expiry;

    @NotNull
    private String pin;

    private String pinDecrypted;

    @NotNull
    private String matricule;

    private String matriculeEncodeur;

    @NotNull
    private String name;

    @NotNull
    private Double balance;

    private String photo;

    private String email;

    private String telephone;

    private String academicYear;

    private boolean student = false;

    private String faculty;

    private String niveau;

    private String classe;

    private Date lastInscription;

    private Date nextPaiement;



    private String feeList;

    private Double due = 0.0;

    private Date dueDate;

    private Date dateEncodage;

    private boolean withScholarship = false;
    private Date scholarshipExpiry;

    private String enterprise;
    private String enterpriseLogo;
    private String jobTitle;

    private boolean synced = false;
    private Date syncDate;
    private boolean encoded = false;

    @Keep
    public Carte(CynodMsg cynodMsg) throws Exception {
        matricule = cynodMsg.getCardClienId();
        if (matricule != null) {
            matricule = matricule.trim().replaceAll("/", ".");
        }
        ccnumber = cynodMsg.getCardNumber();
        name = cynodMsg.getCardClienName();
        if (name != null) {
            name = name.trim().replaceAll("/", ".");
        }
        email = cynodMsg.getEmail();
        if (email != null) {
            email = email.trim().replaceAll("/", ".");
        }
        telephone = cynodMsg.getNumeroTelephone();
        if (telephone != null) {
            telephone = telephone.trim();
        }
        try {
            version = Long.parseLong(cynodMsg.getCardVersion());
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Card: cannot convert version to long >>> ", ex);
        }

        pin = cynodMsg.getCardPinSha();
        pinDecrypted = SecurityUtil.decrypt(cynodMsg.getCardPinSha());
        String soldeCrypted = SecurityUtil.decrypt(cynodMsg.getSoldeSH());
        if (soldeCrypted != null) {
            try {
                balance = Double.valueOf(soldeCrypted);
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Card: Unable to convert balance to big decimal >>> ", ex);
            }
        }
        cardProfile = cynodMsg.getCardProfile();
        if (cardProfile != null) {
            cardProfile = cardProfile.trim().replaceAll("/", ".");
        }
        faculty = cynodMsg.getCodeFilier();
        if (faculty != null) {
            faculty = faculty.trim().replaceAll("/", ".");
        }
        academicYear = cynodMsg.getAnneeScolaire();
        niveau = cynodMsg.getNiveau();
        if (niveau != null) {
            niveau = niveau.trim().replaceAll("/", ".");
        }
        classe = cynodMsg.getClasse();
        if (classe != null) {
            classe = classe.trim().replaceAll("/", ".");
        }
        lastInscription = cynodMsg.getDateLastInscription();
        try {
            due = cynodMsg.getMontantAPayer().doubleValue();
            Log.w(LOG_TAG, "Carte: DUE SET TO " + due);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Carte: ", e);
        }
        withScholarship = cynodMsg.getIsBoursier() != null && "1".equals(cynodMsg.getIsBoursier());
        student = cynodMsg.isEtudiant();
        expiry = cynodMsg.getDateExpirationCarte();
        if (student) {
            feeList = cynodMsg.getListFraisImpayes();
        } else if (Constantes.CODE_PROFIL_PROFESSEUR.equals(cardProfile)) {
            feeList = cynodMsg.getListeCoursProf();
        }
        if (feeList != null) {
            feeList = feeList.trim().replaceAll("/", ".");
        }

        nextPaiement = cynodMsg.getDateProchainPaiement();
        scholarshipExpiry = cynodMsg.getDateEcheanceBourse();
        jobTitle = cynodMsg.getFonction();
    }

    public Carte(Long id, Long version, @NotNull String cardProfile, @NotNull String ccnumber, @NotNull Date expiry, @NotNull String pin, @NotNull String matricule, String matriculeEncodeur,
                 @NotNull String name, String photo, String email, String telephone, String academicYear, boolean student, String faculty, String niveau, String classe, Date lastInscription,
                 Date nextPaiement, @NotNull Double balance, String feeList, Double due, Date dueDate, Date dateEncodage, boolean withScholarship, Date scholarshipExpiry, String enterprise,
                 String jobTitle, boolean synced, Date syncDate, boolean encoded) {
        this.id = id;
        this.version = version;
        this.cardProfile = cardProfile;
        this.ccnumber = ccnumber;
        this.expiry = expiry;
        this.pin = pin;
        this.matricule = matricule;
        this.matriculeEncodeur = matriculeEncodeur;
        this.name = name;
        this.photo = photo;
        this.email = email;
        this.telephone = telephone;
        this.academicYear = academicYear;
        this.student = student;
        this.faculty = faculty;
        this.niveau = niveau;
        this.classe = classe;
        this.lastInscription = lastInscription;
        this.nextPaiement = nextPaiement;
        this.balance = balance;
        this.feeList = feeList;
        this.due = due;
        this.dueDate = dueDate;
        this.dateEncodage = dateEncodage;
        this.withScholarship = withScholarship;
        this.scholarshipExpiry = scholarshipExpiry;
        this.enterprise = enterprise;
        this.jobTitle = jobTitle;
        this.synced = synced;
        this.syncDate = syncDate;
        this.encoded = encoded;
    }

    public Carte() {
    }

    @Keep
    public String getUriPP() {
        return "";//ShowboxApplication.getEventDir() + FileSystemUtil.PP_DIR + matricule + ".png";
    }

    @Keep
    public CynodMsg toCynodMsg() throws Exception {
        CynodMsg cynodMsg = new CynodMsg();

        cynodMsg.setAt(FieldFormat.FieldDescAndSize._25_CARD_PROFILE.ordinal(), new CynodField(FieldType.STRING, cardProfile));
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._02_CARD_NUMBER.ordinal(), new CynodField(FieldType.STRING, ccnumber));
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._38_SH_DATE_EXPIRATION_CARTE.ordinal(), new CynodField(FieldType.DATE, expiry));
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._08_PIN_SHA.ordinal(), new CynodField(FieldType.STRING, SecurityUtil.encrypt(pin)));
        String encryptedSolde = SecurityUtil.encrypt(String.valueOf(balance));
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._47_SOLDE_SHA.ordinal(), new CynodField(FieldType.STRING, encryptedSolde));
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._46_SH_CARD_VERSION.ordinal(), new CynodField(FieldType.STRING, version.toString()));

        cynodMsg.setAt(FieldFormat.FieldDescAndSize._01_CLIENT_ID.ordinal(), new CynodField(FieldType.STRING, matricule));
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._03_CLIENT_NAME.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(name)));
        if (! TextUtils.isEmpty(email)) {
            cynodMsg.setAt(FieldFormat.FieldDescAndSize._40_SH_EMAIL.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(email)));
        }
        if (! TextUtils.isEmpty(telephone)) {
            cynodMsg.setAt(FieldFormat.FieldDescAndSize._41_SH_NUMERO_TEL.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(telephone)));
        }
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._34_SH_MONTANT_A_PAYER.ordinal(), new CynodField(FieldType.DECIMAL, new BigDecimal(due == null ? 0.0 : due)));
        Log.i(LOG_TAG, "toCynodMsg: MONTANT A PAYER >>> " + cynodMsg.getMontantAPayer());

        /*if (ShowboxApplication.getEvenement().getType().equals(Constantes.EVENT_SCHOOL)) { //SCHOOL EVENT
            if (!TextUtils.isEmpty(academicYear)) {
                cynodMsg.setAt(FieldFormat.FieldDescAndSize._29_SH_ANNEE_SCOLAIRE.ordinal(), new CynodField(FieldType.STRING, academicYear));
            }
            cynodMsg.setAt(FieldFormat.FieldDescAndSize._43_SH_IS_ETUDIANT.ordinal(), new CynodField(FieldType.STRING, student ? "1" : "0"));

            if (student) {
                if (!TextUtils.isEmpty(faculty)) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._28_SH_CODE_FILIERE.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(faculty)));
                }
                if (!TextUtils.isEmpty(niveau)) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._30_SH_NIVEAU.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(niveau)));
                }
                if (!TextUtils.isEmpty(classe)) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._31_SH_CLASSE.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(classe)));
                }
                if (this.lastInscription != null) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._32_SH_DATE_LAST_INSCRIPTION.ordinal(), new CynodField(FieldType.DATE, this.lastInscription));
                }
                if (this.nextPaiement != null) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._33_SH_DATE_PROCHAIN_PAIEMENT.ordinal(), new CynodField(FieldType.DATE, this.nextPaiement));
                }
                cynodMsg.setAt(FieldFormat.FieldDescAndSize._36_SH_IS_BOURSIER.ordinal(), new CynodField(FieldType.STRING, withScholarship ? "1" : "0"));
                if (scholarshipExpiry != null) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._37_SH_DATE_ECHEANCE_BOURSE.ordinal(), new CynodField(FieldType.DATE, scholarshipExpiry));
                }
                if (!TextUtils.isEmpty(feeList)) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._39_SH_LISTE_FRAIS_IMPAYES.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(feeList)));
                }
            } else {
                if (!TextUtils.isEmpty(feeList)) {
                    cynodMsg.setListeCoursProf(ConversionUtil.replaceSpecialCharacters(feeList));
                }
            }
        }*/

        if (jobTitle != null) {
            cynodMsg.setAt(FieldFormat.FieldDescAndSize._42_FONCTION.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(jobTitle)));
        }

        cynodMsg.generateBitMap();
        cynodMsg.fillDataInBlock1K(cynodMsg.dumpMsg());

        Log.i(LOG_TAG, "toCynodMsg: CARD CONVERTED TO CYNOD MSG >>> \n" + cynodMsg.logMsg());

        return cynodMsg;
    }

/*    @Keep
    public CynodMsg toCynodMsg() throws Exception {
        CynodMsg cynodMsg = new CynodMsg();
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._25_CARD_PROFILE.ordinal(), new CynodField(FieldType.STRING, cardProfile));//Common.byte2HexString(cardProfile.getBytes())
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._02_CARD_NUMBER.ordinal(), new CynodField(FieldType.STRING, ccnumber )); //Common.byte2HexString(ccnumber.getBytes())
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._38_SH_DATE_EXPIRATION_CARTE.ordinal(), new CynodField(FieldType.DATE, expiry));
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._08_PIN_SHA.ordinal(), new CynodField(FieldType.STRING, pin)); //Common.byte2HexString(pin.getBytes())
        *//** TODO ENCRYPT SOLDE **//*
        String encryptedSolde = SecurityUtil.encrypt(String.valueOf(balance));
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._47_SOLDE_SHA.ordinal(), new CynodField(FieldType.STRING, balance));
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._46_SH_CARD_VERSION.ordinal(), new CynodField(FieldType.STRING, version)); //Common.byte2HexString(version.toString().getBytes())

        cynodMsg.setAt(FieldFormat.FieldDescAndSize._01_CLIENT_ID.ordinal(), new CynodField(FieldType.STRING, matricule)); //Common.byte2HexString(matricule.getBytes())
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._03_CLIENT_NAME.ordinal(), new CynodField(FieldType.STRING,ConversionUtil.replaceSpecialCharacters(name) )); //Common.byte2HexString(ConversionUtil.replaceSpecialCharacters(name).getBytes())
        if (!TextUtils.isEmpty(email)) {
            cynodMsg.setAt(FieldFormat.FieldDescAndSize._40_SH_EMAIL.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(email))); //Common.byte2HexString(ConversionUtil.replaceSpecialCharacters(email).getBytes())
        }
        if (!TextUtils.isEmpty(telephone)) {
            cynodMsg.setAt(FieldFormat.FieldDescAndSize._41_SH_NUMERO_TEL.ordinal(), new CynodField(FieldType.STRING,ConversionUtil.replaceSpecialCharacters(telephone) )); //Common.byte2HexString(ConversionUtil.replaceSpecialCharacters(telephone).getBytes())
        }
        cynodMsg.setAt(FieldFormat.FieldDescAndSize._34_SH_MONTANT_A_PAYER.ordinal(), new CynodField(FieldType.DECIMAL, new BigDecimal(due == null ? 0.0 : due)));
        Log.i(LOG_TAG, "toCynodMsg: MONTANT A PAYER >>> " + cynodMsg.getMontantAPayer());

*//*        if (ShowboxApplication.getEvenement().getType().equals(Constantes.EVENT_SCHOOL)) { //SCHOOL EVENT
            if (!TextUtils.isEmpty(academicYear)) {
                cynodMsg.setAt(FieldFormat.FieldDescAndSize._29_SH_ANNEE_SCOLAIRE.ordinal(), new CynodField(FieldType.STRING, academicYear));
            }
            cynodMsg.setAt(FieldFormat.FieldDescAndSize._43_SH_IS_ETUDIANT.ordinal(), new CynodField(FieldType.STRING, student ? "1" : "0"));

            if (student) {
                if (!TextUtils.isEmpty(faculty)) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._28_SH_CODE_FILIERE.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(faculty)));
                }
                if (!TextUtils.isEmpty(niveau)) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._30_SH_NIVEAU.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(niveau)));
                }
                if (!TextUtils.isEmpty(classe)) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._31_SH_CLASSE.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(classe)));
                }
                if (this.lastInscription != null) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._32_SH_DATE_LAST_INSCRIPTION.ordinal(), new CynodField(FieldType.DATE, this.lastInscription));
                }
                if (this.nextPaiement != null) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._33_SH_DATE_PROCHAIN_PAIEMENT.ordinal(), new CynodField(FieldType.DATE, this.nextPaiement));
                }
                cynodMsg.setAt(FieldFormat.FieldDescAndSize._36_SH_IS_BOURSIER.ordinal(), new CynodField(FieldType.STRING, withScholarship ? "1" : "0"));
                if (scholarshipExpiry != null) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._37_SH_DATE_ECHEANCE_BOURSE.ordinal(), new CynodField(FieldType.DATE, scholarshipExpiry));
                }
                if (!TextUtils.isEmpty(feeList)) {
                    cynodMsg.setAt(FieldFormat.FieldDescAndSize._39_SH_LISTE_FRAIS_IMPAYES.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(feeList)));
                }
            } else {
                if (!TextUtils.isEmpty(feeList)) {
                    cynodMsg.setListeCoursProf(ConversionUtil.replaceSpecialCharacters(feeList));
                }
            }
        }*//*

        if (jobTitle != null) {
            cynodMsg.setAt(FieldFormat.FieldDescAndSize._42_FONCTION.ordinal(), new CynodField(FieldType.STRING, ConversionUtil.replaceSpecialCharacters(jobTitle)));
        }

        cynodMsg.generateBitMap();
        cynodMsg.fillDataInBlockNtag216(cynodMsg.dumpMsg());

        Log.i(LOG_TAG, "toCynodMsg: CARD CONVERTED TO CYNOD MSG >>> \n" + cynodMsg.logMsg());

        return cynodMsg;
    }*/

/*    @Keep
    public ArrayList<Frais> getListFrais() {
        try {
            Log.d("DEBUG FRAIS", "FRAIS >> " + feeList);
            ArrayList<Frais> fraisArrayList = new ArrayList<>();
            if (feeList != null) {
                String[] listeFraisStr = feeList.trim().split("\\|");
                for (String fraisStr : listeFraisStr) {
                    Log.d("DEBUG FRAIS", "FRAIS >> " + fraisStr);
                    if (!TextUtils.isEmpty(fraisStr)) {
                        String[] strFrais = fraisStr.trim().split(":");
                        if (strFrais.length == 3) {
                            Frais frais = new Frais();
                            frais.setId(Integer.parseInt(strFrais[0]));
                            frais.setMontantFrais(new BigDecimal(strFrais[1]));
                            frais.setEcheance(strFrais[2]);
                            fraisArrayList.add(frais);
                        }
                    }
                }

                return fraisArrayList;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "getListFrais: ", e);
        }
        return null;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCardProfile() {
        return cardProfile;
    }

    public void setCardProfile(String cardProfile) {
        this.cardProfile = cardProfile;
    }

    public String getCcnumber() {
        return ccnumber;
    }

    public void setCcnumber(String ccnumber) {
        this.ccnumber = ccnumber;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPinDecrypted() {
        try {
            return SecurityUtil.decrypt(pin);
        } catch (Exception e) {
            Log.e(LOG_TAG, "getPinDecrypted: ", e);
            return null;
        }
    }

    public void setPinDecrypted(String pinDecrypted) {
        this.pinDecrypted = pinDecrypted;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public boolean isStudent() {
        return student;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Date getLastInscription() {
        return lastInscription;
    }

    public void setLastInscription(Date lastInscription) {
        this.lastInscription = lastInscription;
    }

    public Date getNextPaiement() {
        return nextPaiement;
    }

    public void setNextPaiement(Date nextPaiement) {
        this.nextPaiement = nextPaiement;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getFeeList() {
        return feeList;
    }

    public void setFeeList(String feeList) {
        this.feeList = feeList;
    }

    public Double getDue() {
        return due;
    }

    public void setDue(Double due) {
        this.due = due;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isWithScholarship() {
        return withScholarship;
    }

    public Date getScholarshipExpiry() {
        return scholarshipExpiry;
    }

    public void setScholarshipExpiry(Date scholarshipExpiry) {
        this.scholarshipExpiry = scholarshipExpiry;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getEnterpriseLogo() {
        return enterpriseLogo;
    }

    public void setEnterpriseLogo(String enterpriseLogo) {
        this.enterpriseLogo = enterpriseLogo;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public boolean isSynced() {
        return synced;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append("\n");

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                //requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                Log.e(LOG_TAG, "toString: ", ex);
            }
            result.append("\n");
        }
        result.append("}");

        return result.toString();
    }

    public boolean getStudent() {
        return this.student;
    }

    public void setStudent(boolean student) {
        this.student = student;
    }

    public boolean getWithScholarship() {
        return this.withScholarship;
    }

    public void setWithScholarship(boolean withScholarship) {
        this.withScholarship = withScholarship;
    }

    public boolean getSynced() {
        return this.synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getMatriculeEncodeur() {
        return this.matriculeEncodeur;
    }

    public void setMatriculeEncodeur(String matriculeEncodeur) {
        this.matriculeEncodeur = matriculeEncodeur;
    }

    public Date getDateEncodage() {
        return this.dateEncodage;
    }

    public void setDateEncodage(Date dateEncodage) {
        this.dateEncodage = dateEncodage;
    }

    public boolean getEncoded() {
        return this.encoded;
    }

    public void setEncoded(boolean encoded) {
        this.encoded = encoded;
    }

    public Date getSyncDate() {
        return this.syncDate;
    }

    public void setSyncDate(Date syncDate) {
        this.syncDate = syncDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Carte other = (Carte) obj;
        if (!ccnumber.equalsIgnoreCase(other.ccnumber))
            return false;
        return true;
    }


    //@Override
    public int compareTo(@NonNull Carte o) {
        if (!this.ccnumber.equalsIgnoreCase(o.ccnumber)) {
            return 0;
        } else if (this.ccnumber.equalsIgnoreCase(o.ccnumber)) {
            return 1;
        } else {
            return -1;
        }
    }
}
