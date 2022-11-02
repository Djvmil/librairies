package com.djamil.suntelecom.utilities

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.EditText
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.text.NumberFormat
import java.util.*

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 4/15/20
 */
object UtilsFunction {
    private const val TAG = "FonctionUtils"
    fun getDecimalFormattedString(value: String): String {
        val lst = StringTokenizer(value, ".")
        var str1 = value
        var str2 = ""
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken()
            str2 = lst.nextToken()
        }
        var str3 = ""
        var i = 0
        var j = -1 + str1.length
        if (str1[-1 + str1.length] == '.') {
            j--
            str3 = "."
        }
        var k = j
        while (true) {
            if (k < 0) {
                if (str2.length > 0) str3 = "$str3.$str2"
                return str3
            }
            if (i == 3) {
                str3 = ",$str3"
                i = 0
            }
            str3 = str1[k].toString() + str3
            i++
            k--
        }
    }

    fun getNumberFloatFormat(`val`: Float?): String {
        val numberFormat = NumberFormat.getInstance(Locale.FRENCH)
        return numberFormat.format(`val`)
    }

    @JvmStatic
    fun md5Hash(s: String): String {
        try {
            // Create MD5 Hash
            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (b in messageDigest) hexString.append(Integer.toHexString(0xFF and b.toInt()))
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * Downloads push notification image before displaying it in
     * the notification tray
     *
     * @param strURL : URL of the notification Image
     * @return : BitMap representation of notification Image
     */
    fun getBitmapFromURL(strURL: String?): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Playing notification sound
     */
    fun playNotificationSound(ctx: Context) {
        try {
            val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + ctx.packageName + "/raw/notification")
            val r = RingtoneManager.getRingtone(ctx, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Convert an array of bytes into a string of hex values.
     *
     * @param bytes
     * Bytes to convert.
     * @return The bytes in hex string format.
     */
    fun byte2HexString(bytes: ByteArray?): String {
        var ret = ""
        if (bytes != null) {
            for (b in bytes) {
                ret += String.format("%02X", b.toInt() and 0xFF)
            }
        }
        return ret
    }

    fun hexString2Ascii(hexString: String): String {
        val s = System.getProperty("line.separator")
        var ascii = ""
        val hex = hexStringToByteArray(hexString)
        for (i in hex.indices) {
            if (hex[i] < 0x20.toByte() || hex[i] == 0x7F.toByte()) {
                hex[i] = 0x2E.toByte()
            }
        }
        // Hex to ASCII.
        try {
            ascii = TextUtils.concat(ascii, " ", Charsets.US_ASCII.toString(), s) as String
            // Log.d("ascii", String.valueOf(ascii.length()));
            // Log.d("ascii trim length",
            // String.valueOf(ascii.trim().length()));
        } catch (e: UnsupportedEncodingException) {
            Log.e(TAG, "Error while encoding to ASCII", e)
        }
        return ascii.trim { it <= ' ' }
    }

    /**
     * Convert a string of hex data into a byte array. Original author is: Dave
     * L. (http://stackoverflow.com/a/140861).
     *
     * @param s
     * The hex string to convert
     * @return An array of bytes with the values of the string.
     */
    fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        try {
            var i = 0
            while (i < len) {

                /*
                 * Log.i("hexStringToByteArray",
                 * "s.charAt("+i+") >> "+String.valueOf(s.charAt(i))+", \n" +
                 * "s.charAt("+(i+1)+") >> "+
                 * String.valueOf(s.charAt(i+1))+", \n" +
                 * "Character.digit "+i+" >> "+(byte)
                 * (Character.digit(s.charAt(i), 16) << 4)+", \n" +
                 * "Character.digit "+(i+1)+" >> "+(byte)
                 * (Character.digit(s.charAt(i+1), 16)));
                 */data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character
                        .digit(s[i + 1], 16)).toByte()
                i += 2
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "Argument(s) for hexStringToByteArray(String s)"
                    + "was not a hex string")
        }
        return data
    }

    /**
     * Cette méthode permet de faire un test Luhn sur les numéros de compte
     *
     * @author mthiam
     * @param str
     * le numéro de compte à vérifier
     * @return boolean
     */
    fun validateAccount(str: String): Boolean {
        return try {
            val longueur = str.length
            val reverse = StringBuilder().append(str).reverse()
                    .toString()
            var sum = 0
            for (i in 0 until longueur) {
                var currVal = ("" + reverse[i]).toInt()
                if (i % 2 == 1) {
                    currVal *= 2
                    if (currVal > 9) {
                        currVal -= 9
                    }
                }
                sum += currVal
            }
            sum % 10 == 0
        } catch (e: Exception) {
            false
        }
    }



    fun getUUID(): String? {
        // Creating a random UUID (Universally unique identifier).
        val uuid = UUID.randomUUID()
        val randomUUIDString = uuid.toString()
        val randomUUIDuniqueString = RandomUtil.unique()
        Log.d(DateUtils.TAG, "Random unique UUID String = $randomUUIDString")

        return randomUUIDuniqueString
    }

    internal object RandomUtil {
        // Maxim: Copied from UUID implementation :)
        @Volatile
        private var numberGenerator: SecureRandom? = null
        private val MSB = (-0x800000000000000L)
        fun unique(): String {
            var ng = numberGenerator
            if (ng == null) {
                ng = SecureRandom()
                numberGenerator = ng
            }
            return java.lang.Long.toHexString(MSB or ng.nextLong()) + java.lang.Long.toHexString(
                    MSB or ng.nextLong()
            )
        }
    }

    fun getVersionNameApp(appInstance: Application): String {
        try {
            return appInstance.packageManager.getPackageInfo(appInstance.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getVersionCodeApp(appInstance: Application): Int {
        try {
            return appInstance.packageManager.getPackageInfo(appInstance.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    fun validateInputs(listEditText : List<EditText>): Boolean {
        listEditText.forEach {
            val editText = it
            if (editText.text.isNullOrEmpty()) {
                editText.error = "Champs obligatoire"
                editText.requestFocus()
                return false
            }
        }

        return true
    }

    fun convertUriToBitmap(contentResolver: ContentResolver, targetUri: Uri): Bitmap? {
        return try {
            BitmapFactory.decodeStream(contentResolver.openInputStream(targetUri))
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            null
        }
    }

    fun convertUriToString(contentResolver: ContentResolver, targetUri: Uri): String? {
        val bitmap: Bitmap
        return try {
            bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(targetUri))
            convertBitmapToString(bitmap)
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            null
        }
    }

    //Utils for images
    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false
        )
    }

    //Decode from base64 to Bitmap
    fun decodeImageBase64(base64Img: String): Bitmap {
        val decodedString: ByteArray = Base64.decode(base64Img, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    //method to convert the selected image to base64 encoded string
    fun convertBitmapToString(bitmap: Bitmap): String? {

        var encodedImage = ""
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        try {
            encodedImage =  Base64.encodeToString(
                    byteArrayOutputStream.toByteArray(),
                    Base64.NO_WRAP
            )

            /*URLEncoder.encode(Base64.encodeToString(
                byteArrayOutputStream.toByteArray(),
                Base64.DEFAULT
            ), StandardCharsets.ISO_8859_1.name())*/

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return encodedImage
    }

    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(
                    -1f,
                    1f
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(
                    180f
            )
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(
                    90f
            )
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(
                    -90f
            )
            else -> return bitmap
        }
        return try {
            val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            bmRotated
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            null
        }
    }



    fun validateAccountNumber(str: String): Boolean {
        return try {
            val longueur = str.length
            val reverse = java.lang.StringBuilder().append(str).reverse()
                    .toString()
            var sum = 0
            for (i in 0 until longueur) {
                var currVal = (Constants.EMPTY_VALUE + reverse[i]).toInt()
                if (i % 2 == 1) {
                    currVal *= 2
                    if (currVal > 9) {
                        currVal -= 9
                    }
                }
                sum += currVal
            }
            sum % 10 == 0
        } catch (e: Exception) {
            false
        }
    }


    //Methode pour récupérer maintenant l'imei ou l'identifiant unique
    @SuppressLint("HardwareIds")
    @kotlin.jvm.Throws(SecurityException::class, NullPointerException::class)
    fun getIMEINumber(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imei: String
        imei =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) //this change is for Android 10 as per security concern it will not provide the imei number.
                    Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID
                    ) else {
                    if (tm?.deviceId != null && tm.deviceId != "000000000000000") tm.deviceId else Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID
                    )
                }
        Log.d(DateUtils.TAG, "getIMEINumber: $imei")
        return imei
    }

    fun getPhoneName(): String? {
        return String.format("%s %s", Build.MANUFACTURER, Build.MODEL)
    }

    fun md5(s: String): String? {
        try {
            // Create MD5 Hash
            val digest =
                    MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (b in messageDigest) hexString.append(
                    Integer.toHexString(
                            0xFF and b.toInt()
                    )
            )
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return Constants.EMPTY_VALUE
    }


    fun getAppLable(context: Context): String? {
        val packageManager = context.packageManager
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = packageManager.getApplicationInfo(context.applicationInfo.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return (if (applicationInfo != null) packageManager.getApplicationLabel(applicationInfo) else "Unknown") as String
    }

}