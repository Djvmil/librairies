package com.suntelecoms.authenticate.activity

import android.Manifest
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.graphics.drawable.AnimatedVectorDrawable
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.suntelecoms.authenticate.R
import com.suntelecoms.authenticate.fingerprint.FingerPrintListener
import com.suntelecoms.authenticate.fingerprint.FingerprintHandler
import com.suntelecoms.authenticate.pinlockview.IndicatorType
import com.suntelecoms.authenticate.pinlockview.OnAuthListener
import com.suntelecoms.authenticate.pinlockview.PinLockListener
import com.suntelecoms.authenticate.util.Animate.animate
import com.suntelecoms.authenticate.util.Utils.decrypt
import com.suntelecoms.authenticate.util.Utils.encrypt
import kotlinx.android.synthetic.main.activity_enter_code.*
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
class AuthenticateActivity : AppCompatActivity() {
    private var mTextTitle: TextView? = null
    private var mTextAttempts: TextView? = null
    private var mTextFingerText: TextView? = null
    private var mImageViewFingerView: AppCompatImageView? = null
    private var mCipher: Cipher? = null
    private var mKeyStore: KeyStore? = null
    private var mKeyGenerator: KeyGenerator? = null
    private var mCryptoObject: FingerprintManager.CryptoObject? = null
    private var mFingerprintManager: FingerprintManager? = null
    private var mKeyguardManager: KeyguardManager? = null
    private var mSetPin = false
    private var mFirstPin = ""

    //    private int mTryCount = 0;
    private var showFingerprint: AnimatedVectorDrawable? = null
    private var fingerprintToTick: AnimatedVectorDrawable? = null
    private var fingerprintToCross: AnimatedVectorDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_enter_code)
        mTextAttempts = findViewById(R.id.attempts)
        mTextTitle = findViewById(R.id.title)
        mImageViewFingerView = findViewById(R.id.fingerView)
        mTextFingerText = findViewById(R.id.fingerText)
        PREFERENCES = getString(R.string.key_app_sharpref)

        mTextTitle!!.text = if (mTitle != null) mTitle else getString(R.string.pinlock_title)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showFingerprint = getDrawable(R.drawable.show_fingerprint) as AnimatedVectorDrawable?
            fingerprintToTick = getDrawable(R.drawable.fingerprint_to_tick) as AnimatedVectorDrawable?
            fingerprintToCross = getDrawable(R.drawable.fingerprint_to_cross) as AnimatedVectorDrawable?
        }

        imgMenuBackImageView.visibility =  if (goneBtnBack) View.GONE else View.VISIBLE

        imgMenuBackImageView.setOnClickListener {
            onBackPressed()
        }

        logo.setImageResource(icon)

        if (!useFingerPrint){
            mImageViewFingerView!!.visibility = View.GONE
            mTextFingerText!!.visibility = View.GONE
        }

        mSetPin = intent.getBooleanExtra(EXTRA_SET_PIN, false)
        if (mSetPin) {
            changeLayoutForSetPin()
        } else {
            val pin = pinFromSharedPreferences
            if (pin == "") {
                changeLayoutForSetPin()
                mSetPin = true
            } else {
                if (useFingerPrint) checkForFingerPrint()
            }
        }

        val pinLockListener: PinLockListener = object : PinLockListener {
            override fun onComplete(pin: String?) {
                if (mSetPin) setPin(pin!!)

                else checkPin(pin)

            }

            override fun onEmpty() {
                Log.d(TAG, getString(R.string.pin_empty))
            }

            override fun onPinChange(pinLength: Int, intermediatePin: String?) {
                Log.d(TAG, "Pin changed, new length $pinLength with intermediate pin $intermediatePin")
            }
        }

        pinlockView.attachIndicatorDots(indicator_dots)
        pinlockView.setPinLockListener(pinLockListener)
        pinlockView.pinLength = intent.getIntExtra(EXTRA_PIN_LENGTH, 4)
        if (shuffle) pinlockView.enableLayoutShuffling()
        indicator_dots.indicatorType = IndicatorType.FILL_WITH_ANIMATION
        checkForFont()
    }

    private fun checkForFont() {
        val intent = intent
        if (intent.hasExtra(EXTRA_FONT_TEXT)) {
            val font = intent.getStringExtra(EXTRA_FONT_TEXT)
            setTextFont(font)
        }
        if (intent.hasExtra(EXTRA_FONT_NUM)) {
            val font = intent.getStringExtra(EXTRA_FONT_NUM)
            setNumFont(font)
        }
    }

    private fun setTextFont(font: String?) {
        try {
            val typeface = Typeface.createFromAsset(assets, font)
            mTextTitle!!.typeface = typeface
            mTextAttempts!!.typeface = typeface
            mTextFingerText!!.typeface = typeface
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setNumFont(font: String?) {
        try {
            val typeface = Typeface.createFromAsset(assets, font)
            pinlockView!!.setTypeFace(typeface)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Create the generateKey method that we’ll use to gain access to the Android keystore and generate the encryption key//
    @Throws(FingerprintException::class)
    private fun generateKey() {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            mKeyStore = KeyStore.getInstance("AndroidKeyStore")

            //Generate the key//
            mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

            //Initialize an empty KeyStore//
            mKeyStore?.load(null)

            //Initialize the KeyGenerator//
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mKeyGenerator?.init(KeyGenParameterSpec.Builder(FINGER_PRINT_KEY,
                        KeyProperties.PURPOSE_ENCRYPT or
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC) //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build())
            }

            //Generate the key//
            mKeyGenerator?.generateKey()
        } catch (exc: KeyStoreException) {
            throw FingerprintException(exc)
        } catch (exc: NoSuchAlgorithmException) {
            throw FingerprintException(exc)
        } catch (exc: NoSuchProviderException) {
            throw FingerprintException(exc)
        } catch (exc: InvalidAlgorithmParameterException) {
            throw FingerprintException(exc)
        } catch (exc: CertificateException) {
            throw FingerprintException(exc)
        } catch (exc: IOException) {
            throw FingerprintException(exc)
        }
    }

    //Create a new method that we’ll use to initialize our mCipher//
    fun initCipher(): Boolean {
        try {
            //Obtain a mCipher instance and configure it with the properties required for fingerprint authentication//
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCipher = Cipher.getInstance(
                        KeyProperties.KEY_ALGORITHM_AES + "/"
                                + KeyProperties.BLOCK_MODE_CBC + "/"
                                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "Failed to get Cipher")
            return false
        } catch (e: NoSuchPaddingException) {
            Log.e(TAG, "Failed to get Cipher")
            return false
        }
        return try {
            mKeyStore!!.load(null)
            val key = mKeyStore!!.getKey(FINGER_PRINT_KEY,
                    null) as SecretKey
            mCipher!!.init(Cipher.ENCRYPT_MODE, key)
            //Return true if the mCipher has been initialized successfully//
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to init Cipher")
            false
        }
    }

    private fun writePinToSharedPreferences(pin: String) {
        val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PIN, encrypt(pin)).apply()
    }

    private val pinFromSharedPreferences: String?
        private get() {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            return decrypt(prefs.getString(KEY_PIN, "")!!)
        }

    private fun setPin(pin: String) {
        if (mFirstPin == "") {
            mFirstPin = pin
            mTextTitle!!.text = getString(R.string.pinlock_secondPin)
            pinlockView!!.resetPinLockView()
        } else {
            if (pin == mFirstPin) {
                writePinToSharedPreferences(pin)
                setResult(Activity.RESULT_OK)
                if(onAuthListener != null)
                    onAuthListener?.onSuccess(pin, authWithFinger = false, success = true)
                finish()
            } else {
                shake()
                mTextTitle!!.text = getString(R.string.pinlock_tryagain)
                pinlockView!!.resetPinLockView()
                mFirstPin = ""
                if(onAuthListener != null)
                    onAuthListener?.onError(getString(R.string.pinlock_tryagain))
            }
        }
    }

    private fun checkPin(pin: String?) {
        if (pin!! == pinFromSharedPreferences) {
            setResult(Activity.RESULT_OK)
            if(onAuthListener != null)
                onAuthListener?.onSuccess(pin, authWithFinger = false, success = true)
            finish()
        } else {
            Log.d(TAG, "checkPin: wrong pin $closeAfterAttempts" )
            shake()

            if(onAuthListener != null)
                onAuthListener?.onError(getString(R.string.pinlock_wrongpin))
//            mTryCount++;
            mTextAttempts!!.text = getString(R.string.pinlock_wrongpin)
            if (closeAfterAttempts){
                setResult(Activity.RESULT_OK)
                onAuthListener?.onSuccess(pin, authWithFinger = false, success = true)
                finish()
            }
            pinlockView!!.resetPinLockView()

//            if (mTryCount == 1) {
//                mTextAttempts.setText(getString(R.string.pinlock_firsttry));
//                mPinLockView.resetPinLockView();
//            } else if (mTryCount == 2) {
//                mTextAttempts.setText(getString(R.string.pinlock_secondtry));
//                mPinLockView.resetPinLockView();
//            } else if (mTryCount > 2) {
//                setResult(RESULT_TOO_MANY_TRIES);
//                finish();
//            }
        }
    }

    private fun shake() {
        val objectAnimator: ObjectAnimator = ObjectAnimator.ofFloat(pinlockView, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f).setDuration(1000)
        objectAnimator.start()
    }

    private fun changeLayoutForSetPin() {
        mImageViewFingerView!!.visibility = View.GONE
        mTextFingerText!!.visibility = View.GONE
        mTextAttempts!!.visibility = View.GONE
        mTextTitle!!.text = getString(R.string.pinlock_settitle)
    }

    private fun checkForFingerPrint() {
        val fingerPrintListener: FingerPrintListener = object : FingerPrintListener {
            override fun onSuccess() {
                setResult(Activity.RESULT_OK)

                if(onAuthListener != null)
                    onAuthListener?.onSuccess("", authWithFinger = true, success = true)
                animate(mImageViewFingerView!!, fingerprintToTick!!)
                val handler = Handler()
                handler.postDelayed({ finish() }, 750)
            }

            override fun onFailed() {

                if(onAuthListener != null){
                    onAuthListener?.onSuccess("", authWithFinger = true, success = false)
                    onAuthListener?.onError("")
                }
                animate(mImageViewFingerView!!, fingerprintToCross!!)
                val handler = Handler()
                handler.postDelayed({ animate(mImageViewFingerView!!, showFingerprint!!) }, 750)
                if (closeAfterAttempts) finish()
            }

            override fun onError(errorString: CharSequence?) {

                if(onAuthListener != null){
                    onAuthListener?.onSuccess("", authWithFinger = true, success = false)
                    onAuthListener?.onError("$errorString")
                }

                Toast.makeText(this@AuthenticateActivity, errorString, Toast.LENGTH_SHORT).show()
                if (closeAfterAttempts) finish()
            }

            override fun onHelp(helpString: CharSequence?) {

                if(onAuthListener != null){
                    onAuthListener?.onSuccess("", authWithFinger = true, success = false)
                    onAuthListener?.onError("$helpString")
                } 
                Toast.makeText(this@AuthenticateActivity, helpString, Toast.LENGTH_SHORT).show()
            }
        }

        // If you’ve set your app’s minSdkVersion to anything lower than 23, then you’ll need to verify that the device is running Marshmallow
        // or higher before executing any fingerprint-related code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getSystemService(Context.FINGERPRINT_SERVICE) != null) {
            val fingerprintManager = getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            if (fingerprintManager.isHardwareDetected) {
                //Get an instance of KeyguardManager and FingerprintManager//
                mKeyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                mFingerprintManager = getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager

                //Check whether the user has granted your app the USE_FINGERPRINT permission//
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)
                        != PackageManager.PERMISSION_GRANTED) {
                    // If your app doesn't have this permission, then display the following text//
//                Toast.makeText(EnterPinActivity.this, "Please enable the fingerprint permission", Toast.LENGTH_LONG).show();
                    mImageViewFingerView!!.visibility = View.GONE
                    mTextFingerText!!.visibility = View.GONE
                    return
                }

                //Check that the user has registered at least one fingerprint//
                if (!mFingerprintManager!!.hasEnrolledFingerprints()) {
                    // If the user hasn’t configured any fingerprints, then display the following message//
//                Toast.makeText(EnterPinActivity.this,
//                        "No fingerprint configured. Please register at least one fingerprint in your device's Settings",
//                        Toast.LENGTH_LONG).show();
                    mImageViewFingerView!!.visibility = View.GONE
                    mTextFingerText!!.visibility = View.GONE
                    return
                }

                //Check that the auth is secured//
                if (!mKeyguardManager!!.isKeyguardSecure) {
                    // If the user hasn’t secured their auth with a PIN password or pattern, then display the following text//
//                Toast.makeText(EnterPinActivity.this, "Please enable auth security in your device's Settings", Toast.LENGTH_LONG).show();
                    mImageViewFingerView!!.visibility = View.GONE
                    mTextFingerText!!.visibility = View.GONE
                    return
                } else {
                    try {
                        generateKey()
                        if (initCipher()) {
                            //If the mCipher is initialized successfully, then create a CryptoObject instance//
                            mCryptoObject = FingerprintManager.CryptoObject(mCipher!!)

                            // Here, I’m referencing the FingerprintHandler class that we’ll create in the next section. This class will be responsible
                            // for starting the authentication process (via the startAuth method) and processing the authentication process events//
                            val helper = FingerprintHandler(this)
                            helper.startAuth(mFingerprintManager!!, mCryptoObject)
                            helper.setFingerPrintListener(fingerPrintListener)
                        }
                    } catch (e: FingerprintException) {
                        Log.wtf(TAG, "Failed to generate key for fingerprint.", e)
                    }
                }
            } else {
                mImageViewFingerView!!.visibility = View.GONE
                mTextFingerText!!.visibility = View.GONE
            }
        } else {
            mImageViewFingerView!!.visibility = View.GONE
            mTextFingerText!!.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_BACK_PRESSED)
        super.onBackPressed()
    }

    private inner class FingerprintException(e: Exception?) : Exception(e)

    companion object {
        const val TAG = "EnterCodeActivity"
        const val RESULT_BACK_PRESSED = Activity.RESULT_FIRST_USER

        //    public static final int RESULT_TOO_MANY_TRIES = RESULT_FIRST_USER + 1;
        var mTitle: String? = null
        const val EXTRA_SET_PIN = "set_pin"
        const val EXTRA_FONT_TEXT = "textFont"
        const val EXTRA_FONT_NUM = "numFont"
        const val EXTRA_PIN_LENGTH = "pinLength"
        private const val FINGER_PRINT_KEY = "FingerPrintKey"
        private var PREFERENCES = "com.suntelecoms.auth"
        const val KEY_PIN = "pin_key"
        var shuffle = true
        var goneBtnBack = true
        var useFingerPrint = true
        var closeAfterAttempts = false
        var onAuthListener: OnAuthListener? = null
        var icon: Int = R.drawable.circlebutton_notpressed


        @JvmStatic
        fun getIntent(context: Context?, setPin: Boolean, fontText: String? = null, fontNum: String? = null, pinLength: Int? = 4): Intent {
            val intent = Intent(context, AuthenticateActivity::class.java)
            intent.putExtra(EXTRA_SET_PIN, setPin)
            PREFERENCES = context?.getString(R.string.key_app_sharpref)!!

            if (fontText != null)
                intent.putExtra(EXTRA_FONT_TEXT, fontText)

            if (fontNum != null)
                intent.putExtra(EXTRA_FONT_NUM, fontNum)

            intent.putExtra(EXTRA_PIN_LENGTH, pinLength)

            return intent
        }


        @JvmStatic
        fun getIntent(context: Context?, setPin: Boolean, fontText: String? = null, fontNum: String? = null,
                      onAuthListener: OnAuthListener? = null, shuffle: Boolean, @DrawableRes icon: Int,
                      goneBtnBack: Boolean, useFingerPrint: Boolean, mTitle: String? = null, pinLength: Int? = 4): Intent {
            val intent = Intent(context, AuthenticateActivity::class.java)
            intent.putExtra(EXTRA_SET_PIN, setPin)
            PREFERENCES = context?.getString(R.string.key_app_sharpref)!!

            if (fontText != null)
                intent.putExtra(EXTRA_FONT_TEXT, fontText)

            if (fontNum != null)
                intent.putExtra(EXTRA_FONT_NUM, fontNum)


            intent.putExtra(EXTRA_PIN_LENGTH, pinLength)

            this.onAuthListener = onAuthListener
            this.shuffle = shuffle
            this.icon = icon
            this.goneBtnBack = goneBtnBack
            this.useFingerPrint = useFingerPrint
            this.mTitle = mTitle

            return intent
        }


         fun changePinToSharedPreferences(context: Context?, pin: String) {
            val prefs = context?.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
             prefs?.edit()?.putString(KEY_PIN, encrypt(pin))?.apply()

             Log.d(TAG, "changePinToSharedPreferences: $pin")

        }



        fun checkFingerPrint(activity: Activity): Result? {
            var result: Result? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.getSystemService(Context.FINGERPRINT_SERVICE) != null) {
                val fingerprintManager = activity.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
                val keyguardManager = activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

                result = if (!fingerprintManager.isHardwareDetected)
                    Result(false, "Scanner d'empreintes digitales non détecté dans l'appareil.\n")
                else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
                    Result(false, "Autorisation d'utiliser le scanner d'empreintes digitales non accordée\n")
                else if (!keyguardManager.isKeyguardSecure)
                    Result(false, "Ajouter un verrou à votre téléphone dans les paramètres.\n")
                else if (!fingerprintManager.hasEnrolledFingerprints())
                    Result(false, "Vous devez ajouter au moins 1 empreinte digitale pour utiliser cette fonction.\n")
                else
                    Result(true, "Placez votre doigt sur le scanner pour accéder à l'application.\n")

            } else Result(false, "Empreinte digitale non disponible pour cette version android (V-${Build.VERSION.SDK_INT}")

            return result
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        onAuthListener = null

    }

    class Result (var isStatus: Boolean = false, var message: String? = null)
}